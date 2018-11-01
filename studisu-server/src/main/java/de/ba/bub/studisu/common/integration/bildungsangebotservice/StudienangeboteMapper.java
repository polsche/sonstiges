package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.ba.bub.studisu.common.model.BundeslandEnum;
import de.ba.bub.studisu.common.model.ExternalLink;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.ort.model.GeoKoordinaten;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.util.GeoAbstandUtil;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Schulart;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Adresse;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsangebot;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Ort;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Studienveranstaltung;

/**
 * Map veranstaltungen (ruckgabe TO vom WebService) auf Studienangeboten.
 * <p>
 * Created by loutzenhj on 06.04.2017.
 */
@Component
public final class StudienangeboteMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudienangeboteMapper.class);

	private StudienangeboteMapper() {
	}

	/**
	 * maximal zulässige Zeichenanzahl für Kursinhalte (ohne Tags)
	 */
	private final static int MAX_CHARS_INHALT = 400;

	/**
	 * maximal zulässige Zeilenzahl ("br", "h4, "li" und "p"-Elemente) für Kursinhalte.
	 */
	private final static int MAX_LINES_INHALT = 15;

	/**
	 * map to studienangebot liste
	 *
	 * @param veranstaltungen
	 *            Liste der zu mappenden Veranstaltungen.
	 * @param abfrageKoordinaten
	 *            Geokoordinate des Abfrageorts für Abstandsberechnung.
	 * @param maxAbstand
	 *            Maximal erlaubter Abstand des Angebotsortes von den übergebenen Ortskoordinaten.
	 * @return liste von studienangeboten
	 */
	public static List<Studienangebot> map(Collection<Studienveranstaltung> veranstaltungen, GeoKoordinaten abfrageKoordinaten,
			int maxAbstand) {
		List<Studienangebot> studienangebote = new ArrayList<Studienangebot>();

		for (Studienveranstaltung v : veranstaltungen) {
			// Keine sinnvollen Daten vorhanden? => Angebot ignorieren!
			if (null == v || null == v.getBildungsangebot() || null == v.getBildungsangebot().getBildungsanbieter()) {
				continue;
			}

			Studienangebot sa = new Studienangebot();
			sa.setId(String.valueOf(v.getId()));

			mapAdressangabenUndBerechneAbstand(sa, v.getAdresse(), abfrageKoordinaten);

			// Nur Angebot innerhalb des berechneten Umkreises werden weiter bearbeitet + übernommen
			if (sa.getAbstand() <= maxAbstand) {
				mapBildungsangebot(sa, v.getBildungsangebot());
				mapHochschulart(sa, v.getSchulart());
				mapUnterrichtsform(sa, v.getUnterrichtsform());
				mapUnterrichtsbeginn(sa, v.getZeitBeginn());
				studienangebote.add(sa);
			}
		}

		return studienangebote;
	}

	/**
	 * Setzt die Daten aus der übergebenen Adresse im Studienangebot.
	 * 
	 * @param sa
	 *            Das zu ändernde Studienangebot.
	 * @param adresse
	 *            Die zu mappende Adressangabe.
	 * @param abfrageKoordinaten
	 *            Geokoordinate des Abfrageorts für Abstandsberechnung.
	 */
	private static void mapAdressangabenUndBerechneAbstand(Studienangebot sa, Adresse adresse,
			GeoKoordinaten abfrageKoordinaten) {
		if (null == adresse) {
			return;
		}

		mapOrtUndAbstand(sa, adresse.getOrt(), abfrageKoordinaten);
		sa.setStudienort(getStudienortFromAdresse(adresse));
	}

	/**
	 * Mapping für Ortsangabe und setzen der Region.
	 * 
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param ort
	 *            Der als Datenquelle dienende Ort
	 * @param abfrageKoordinaten
	 *            Geokoordinate des Abfrageorts für Abstandsberechnung.
	 */
	private static void mapOrtUndAbstand(Studienangebot sa, Ort ort, GeoKoordinaten abfrageKoordinaten) {
		if (null == ort) {
			return;
		}

		Double laengengrad = ort.getLaengengrad();
		Double breitengrad = ort.getBreitengrad();
		GeoKoordinaten vKoordinaten = new GeoKoordinaten(laengengrad, breitengrad);

		sa.setKoordinaten(vKoordinaten);
		sa.setAbstand(GeoAbstandUtil.berechneEntfernung(abfrageKoordinaten, vKoordinaten));

		if (null != ort.getBundesland()) {
			String bundesland = ort.getBundesland().value();
			sa.setRegion(RegionenFacettenOption.forRegionServiceName(bundesland));
		} else if (null != ort.getStaat()) {
			String staat = ort.getStaat().value();
			sa.setRegion(RegionenFacettenOption.forRegionServiceName(staat));
		} else {
			sa.setRegion(RegionenFacettenOption.KEINE_ZUORDNUNG_MOEGLICH);
		}
	}

	/**
	 * Liefert den Studienort auf Basis der übergebenen Adresse.
	 *
	 * @param adresse
	 *            Die Adresse, aus der der Studienort ermittelt werden soll.
	 * @return Der ermittelte Studienort.
	 */
	private static AdresseKurz getStudienortFromAdresse(Adresse adresse) {
		AdresseKurz studienort = new AdresseKurz();
		studienort.setStrasse(adresse.getStrasse());

		if (null != adresse.getOrt()) {
			Ort ort = adresse.getOrt();
			studienort.setOrt(ort.getOrtsname());
			studienort.setPostleitzahl(ort.getPostleitzahl());
			if (null != ort.getBundesland()) {
				studienort.setBundesland(BundeslandEnum.valueOf(ort.getBundesland().name()).value());
			}
		}

		return studienort;
	}

	/**
	 * Mapping für Hochschulart durchführen.
	 * 
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param hsa
	 *            Die als Datenquelle dienende Hochschulart
	 */
	private static void mapHochschulart(Studienangebot sa, Schulart hsa) {
		if (null != hsa) {
			sa.setHochschulart(HochschulartFacettenOption.forName(hsa.value()));
		} else {
			sa.setHochschulart(HochschulartFacettenOption.KEINE_ZUORDNUNG_MOEGLICH);
		}
	}

	/**
	 * Mapping für Studienform durchführen.
	 * 
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param uf
	 *            Die als Datenquelle dienende Unterrichtsform.
	 */
	private static void mapUnterrichtsform(Studienangebot sa, Unterrichtsform uf) {
		if (null != uf) {
			sa.setStudienform(StudienformFacettenOption.forName(uf.value()));
		} else {
			sa.setStudienform(StudienformFacettenOption.SONSTIGE);
		}
	}

	/**
	 * Mapping für Unterrichtsbeginn durchführen.
	 * 
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param cal
	 *            Die als Datenquelle dienende Kalenderangabe.
	 */
	private static void mapUnterrichtsbeginn(Studienangebot sa, XMLGregorianCalendar cal) {
		if (null != cal) {
			sa.setStudiBeginn(cal.toString());
		}
	}

	/**
	 * Mapping für die Daten aus dem Bildungsangebot.
	 * 
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param ba
	 *            Das als Datenquelle dienende Bildungsangebot
	 */
	private static void mapBildungsangebot(Studienangebot sa, Bildungsangebot ba) {

		sa.setBildungsanbieterName(ba.getBildungsanbieter().getName());
		sa.setBildungsanbieterId(ba.getBildungsanbieter().getId());
		sa.setBildungsanbieterHasSignet(ba.getBildungsanbieter().isHasSignet());

		sa.setStudiBezeichnung(ba.getTitel());

		// Inhaltsstring, bei dem die leider oft auftretenden "Bandwurmtexte",
		// bei denen hinter einem Komma kein Leerzeichen kommt, durch Einfügen
		// von grammatikalisch korrekten Leerzeichen "umbrechbar" werden
		// (NB: Komma vor Ziffern wird _nicht_ angefasst).
		String fixedInhalt = "";
		if (ba.getInhalte() != null) {
			fixedInhalt = ba.getInhalte().replaceAll(",([a-zA-ZöäüÖÄÜ])", ", $1");
		}

		// kürze Inhalte bei Bedarf (Tags werden nicht mitgezählt, müssen
		// aber ordnungsgemäß geschlossen werden)
		sa.setStudiInhalt((new ContentClipHelper()).clipContent(fixedInhalt, MAX_CHARS_INHALT, MAX_LINES_INHALT));

		// OSA + StudiCheck-Links
		mapLinks(sa, ba);

		// Systematiken mappen ebenfalls mappen
		mapSystematiken(sa, ba.getSystematik());
	}

	/**
	 * Mapping für StudiCheck- und OSA-Url.
	 *
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param ba
	 *            Das als Datenquelle dienende Bildungsangebot
	 */
	private static void mapLinks(Studienangebot sa, Bildungsangebot ba) {

		String studicheckurl = ba.getStudicheckUrl();
		String osaUrl = ba.getOsaUrl();

		if (!StringUtils.isEmpty(studicheckurl)) {
			ExternalLink studicheckLink = null;
			try {
				// TODO: Auslagern in MessageBundle; hat aber auf anhieb
				// nicht funktioniert, message.properties nicht gefunden
				studicheckLink = new ExternalLink("studicheck", studicheckurl,
						"Für diesen Studiengang sind zugeschnittene Studicheck-Tests verfügbar.");
			} catch (URISyntaxException e) {
				LOGGER.error("Studicheck url fehlerhaft", e);
			}
			sa.addExternalLink(studicheckLink);
			sa.addFitFuerStudiumFacettenOption(FitFuerStudiumFacettenOption.STUDICHECK);
		}

		if (!StringUtils.isEmpty(osaUrl)) {
			ExternalLink osaLink = null;
			try {
				// TODO: Auslagern in MessageBundle; hat aber auf anhieb
				// nicht funktioniert, message.properties nicht gefunden
				osaLink = new ExternalLink("osa", osaUrl,
						"Für diesen Studiengang ist ein webbasierter Selbsttest verfügbar.");
			} catch (URISyntaxException e) {
				LOGGER.error("osa url fehlerhaft", e);
			}
			sa.addExternalLink(osaLink);
			sa.addFitFuerStudiumFacettenOption(FitFuerStudiumFacettenOption.OSA);
		}

		if (StringUtils.isEmpty(studicheckurl) && StringUtils.isEmpty(osaUrl)) {
			sa.addFitFuerStudiumFacettenOption(FitFuerStudiumFacettenOption.KEINES);
		}
	}

	/**
	 * Mapping für Systematiken durchführen.
	 * 
	 * @param sa
	 *            Das zu füllende Studienangebot
	 * @param systematiken
	 *            Die als Datenquelle dienende Liste mit Systematiken
	 */
	private static void mapSystematiken(Studienangebot sa, List<Systematik> systematiken) {
		sa.setStudientyp(StudientypFacettenOption.forSystematiken(systematiken));

		List<String> studienfaecher = new ArrayList<String>();
		for (Systematik systematik : systematiken) {
			studienfaecher.add(systematik.getDkzBezeichnung());
		}
		sa.setStudienfaecher(studienfaecher);
	}
}
