package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlussgrad;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlusstyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Akkreditierung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bildungsart;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bundesland;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Eingabeverfahren;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hauptunterrichtssprache;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hochschultyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Lehramtstyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.RegelstudienzeitEinheit;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Staat;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Studienmodell;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zugangsbedingung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungsmodus;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungssemester;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Adresse;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Ort;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Postfach;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Systematik;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.ZugangsbedingungStudierenOhneAbi;

public class MapperTestUtils {

	public static void fillWithNulls(Studienveranstaltung studienveranstaltung) {
		studienveranstaltung.setAbschlussgrad(null);
		studienveranstaltung.setAbschlussgradIntern(null);
		studienveranstaltung.setAbschlusstyp(null);
		studienveranstaltung.setAkkreditierung(null);
		studienveranstaltung.setAkkreditierungBedingung(null);
		studienveranstaltung.setAkkreditierungBis(null);
		studienveranstaltung.setAkkreditierungVon(null);
		studienveranstaltung.setBildungsart(null);
		studienveranstaltung.setHochschultyp(null);
		studienveranstaltung.setId(0);
		studienveranstaltung.setInhalte(null);
		studienveranstaltung.setInternationalerDoppelabschluss(null);
		studienveranstaltung.setLehramtsbefaehigung(null);
		studienveranstaltung.setLehramtstypen(null);
		studienveranstaltung.setOsaUrl(null);
		studienveranstaltung.setRegelstudienzeitEinheit(null);
		studienveranstaltung.setRegelstudienzeitWert(null);
		studienveranstaltung.setStudicheckUrl(null);
		studienveranstaltung.setStudienschwerpunkte(null);
		studienveranstaltung.setStudierenOhneAbi(null);
		studienveranstaltung.setStudiuminformation(null);
		studienveranstaltung.setTitel(null);
		studienveranstaltung.setZugang(null);
	}

	public static void fillWithValues(Studienveranstaltung studienveranstaltung) {

		// mocks
		XMLGregorianCalendar akkreditierungBis = mock(XMLGregorianCalendar.class);
		XMLGregorianCalendar akkreditierungVon = mock(XMLGregorianCalendar.class);

		// werte festlegen
		Abschlussgrad abschlussgrad = Abschlussgrad.DIPLOM;
		String abschlussgradIntern = "Masterdiplom";
		Abschlusstyp abschlusstyp = Abschlusstyp.KONSEKUTIV;
		Akkreditierung akkreditierung = Akkreditierung.REAKKREDITIERUNG;
		String akkreditierungBedingung = "AkkreditierungBedingung";
		when(akkreditierungVon.toGregorianCalendar()).thenReturn(cal(1, 3, 2018));
		when(akkreditierungBis.toGregorianCalendar()).thenReturn(cal(11, 7, 2035));
		Bildungsart bildungsart = Bildungsart.STUDIENANGEBOT_WEITERFUEHREND;
		Hochschultyp hochschultyp = Hochschultyp.UNIVERSITAET;
		int id = 123;
		String inhalte = "inhalte";
		Boolean internationalerDoppelabschluss = false;
		Boolean lehramtsbefaehigung = true;
		Lehramtstyp lehramtstyp = Lehramtstyp.LEHRAEMTER_DER_GRUNDSCHULE_BZW_PRIMARSTUFE;
		String osaUrl = "http://osa";
		RegelstudienzeitEinheit regelstudienzeitEinheit = RegelstudienzeitEinheit.TRIMESTER;
		Integer regelstudienzeitWert = 12;
		String studicheckUrl = "http://studicheckUrl";
		String studienschwerpunkte = "studienschwerpunkte";
		Boolean studierenOhneAbi = true;
		String studiuminformation = "studiuminformation";
		String titel = "titel";
		String zugang = "zugang";
		Studienmodell studienmodell1 = Studienmodell.BERUFSBEGLEITEND;
		Studienmodell studienmodell2 = Studienmodell.DUALES_STUDIUM;
		Systematik systematik1 = systematik(34, "HA_34", "dkzBezeichnung1");
		Systematik systematik2 = systematik(52, "HA_52", "dkzBezeichnung2");
		ZugangsbedingungStudierenOhneAbi zugangsbedingungStudierenOhneAbi1 = zugangsbedingungStudierenOhneAbi(
				Zugangsbedingung.MEHRJAEHRIGE_BERUFSAUSBILDUNG_UND_ODER_BERUFSERFAHRUNG_IN_EINEM_BERUFSFELD_MIT_FACHLICHER_NAEHE_ZUM_STUDIENFACH,
				"bemerkung133");
		ZugangsbedingungStudierenOhneAbi zugangsbedingungStudierenOhneAbi2 = zugangsbedingungStudierenOhneAbi(
				Zugangsbedingung.MEISTERPRUEFUNG_ODER_GLEICHWERTIGE_AUFSTIEGSFORTBILDUNG, "bemerkung233");

		// werte setzen
		studienveranstaltung.setAbschlussgrad(abschlussgrad);
		studienveranstaltung.setAbschlussgradIntern(abschlussgradIntern);
		studienveranstaltung.setAbschlusstyp(abschlusstyp);
		studienveranstaltung.setAkkreditierung(akkreditierung);
		studienveranstaltung.setAkkreditierungBedingung(akkreditierungBedingung);
		studienveranstaltung.setAkkreditierungBis(akkreditierungBis);
		studienveranstaltung.setAkkreditierungVon(akkreditierungVon);
		studienveranstaltung.setBildungsart(bildungsart);
		studienveranstaltung.setHochschultyp(hochschultyp);
		studienveranstaltung.setId(id);
		studienveranstaltung.setInhalte(inhalte);
		studienveranstaltung.setInternationalerDoppelabschluss(internationalerDoppelabschluss);
		studienveranstaltung.setLehramtsbefaehigung(lehramtsbefaehigung);
		studienveranstaltung.setLehramtstypen(lehramtstyp);
		studienveranstaltung.setOsaUrl(osaUrl);
		studienveranstaltung.setRegelstudienzeitEinheit(regelstudienzeitEinheit);
		studienveranstaltung.setRegelstudienzeitWert(regelstudienzeitWert);
		studienveranstaltung.setStudicheckUrl(studicheckUrl);
		studienveranstaltung.setStudienschwerpunkte(studienschwerpunkte);
		studienveranstaltung.setStudierenOhneAbi(studierenOhneAbi);
		studienveranstaltung.setStudiuminformation(studiuminformation);
		studienveranstaltung.setTitel(titel);
		studienveranstaltung.setZugang(zugang);
		studienveranstaltung.getStudienmodell().add(studienmodell1);
		studienveranstaltung.getStudienmodell().add(studienmodell2);
		studienveranstaltung.getSystematik().add(systematik1);
		studienveranstaltung.getSystematik().add(systematik2);
		studienveranstaltung.getZugangsbedingungStudierenOhneAbi().add(zugangsbedingungStudierenOhneAbi1);
		studienveranstaltung.getZugangsbedingungStudierenOhneAbi().add(zugangsbedingungStudierenOhneAbi2);
	}

	private static ZugangsbedingungStudierenOhneAbi zugangsbedingungStudierenOhneAbi(Zugangsbedingung zugangsbedingung,
			String bemerkung) {
		ZugangsbedingungStudierenOhneAbi ret = new ZugangsbedingungStudierenOhneAbi();
		ret.setBemerkungen(bemerkung);
		ret.setZugangsbedingung(zugangsbedingung);
		return ret;
	}

	private static Systematik systematik(Integer dkzId, String dkzCodeNr, String dkzBezeichnung) {
		Systematik systematik = new Systematik();
		systematik.setDkzBezeichnung(dkzBezeichnung);
		systematik.setDkzCodenr(dkzCodeNr);
		systematik.setDkzId(dkzId);
		return systematik;
	}

	private static GregorianCalendar cal(int date, int month, int year) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(year, month - 1, date);
		return (GregorianCalendar) cal;
	}

	public static void fillWithNulls(Veranstaltung veranstaltung) {
		veranstaltung.setAdresse(null);
		veranstaltung.setAktualisierungsdatum(null);
		veranstaltung.setBemerkungKosten(null);
		veranstaltung.setBemerkungZeit(null);
		veranstaltung.setHauptunterrichtssprache(null);
		veranstaltung.setId(0);
		veranstaltung.setIndividuellerEinstieg(null);
		veranstaltung.setUnterrichtsform(null);
		veranstaltung.setVorlesungszeiten(null);
		veranstaltung.setZeitBeginn(null);
		veranstaltung.setZeitEnde(null);
		veranstaltung.setZulassungsmodus(null);
		veranstaltung.setZulassungsmodusInfo(null);
		veranstaltung.setZulassungssemester(null);
		veranstaltung.setZusatzlink(null);
	}

	public static void fillWithValues(Veranstaltung veranstaltung) {

		// mocks
		XMLGregorianCalendar aktualisierungsdatum = mock(XMLGregorianCalendar.class);
		XMLGregorianCalendar zeitBeginn = mock(XMLGregorianCalendar.class);
		XMLGregorianCalendar zeitEnde = mock(XMLGregorianCalendar.class);

		// werte festlegen
		Adresse studienort = new Adresse();
		fillWithValues1(studienort);
		when(aktualisierungsdatum.toGregorianCalendar()).thenReturn(cal(30, 8, 2018));
		String bemerkungKosten = "bemerkungKosten";
		String bemerkungZeit = "bemerkungZeit";
		Hauptunterrichtssprache hauptunterrichtssprache = Hauptunterrichtssprache.ENGLISCH;
		int id = 22;
		Boolean individuellerEinstieg = false;
		Unterrichtsform unterrichtsform = Unterrichtsform.VOLLZEIT;
		String vorlesungszeiten = "vorlesungszeiten";
		when(zeitBeginn.toGregorianCalendar()).thenReturn(cal(1, 9, 2018));
		when(zeitEnde.toGregorianCalendar()).thenReturn(cal(10, 9, 2018));
		Zulassungsmodus zulassungsmodus = Zulassungsmodus.KEINE_ZULASSUNGSBESCHRAENKUNG;
		String zulassungsmodusInfo = "zulassungsmodusInfo";
		Zulassungssemester zulassungssemester = Zulassungssemester.HERBSTTRIMESTER;
		String zusatzlink = "http://zusatzlink";

		// werte setzen
		veranstaltung.setAdresse(studienort);
		veranstaltung.setAktualisierungsdatum(aktualisierungsdatum);
		veranstaltung.setBemerkungKosten(bemerkungKosten);
		veranstaltung.setBemerkungZeit(bemerkungZeit);
		veranstaltung.setHauptunterrichtssprache(hauptunterrichtssprache);
		veranstaltung.setId(id);
		veranstaltung.setIndividuellerEinstieg(individuellerEinstieg);
		veranstaltung.setUnterrichtsform(unterrichtsform);
		veranstaltung.setVorlesungszeiten(vorlesungszeiten);
		veranstaltung.setZeitBeginn(zeitBeginn);
		veranstaltung.setZeitEnde(zeitEnde);
		veranstaltung.setZulassungsmodus(zulassungsmodus);
		veranstaltung.setZulassungsmodusInfo(zulassungsmodusInfo);
		veranstaltung.setZulassungssemester(zulassungssemester);
		veranstaltung.setZusatzlink(zusatzlink);
		// einnbauen, falls benötigt:
		// veranstaltung.getVeranstaltungsablauf().add(varanstalungsablauf1);
		// veranstaltung.getVeranstaltungsablauf().add(varanstalungsablauf2);
	}

	private static void fillWithValues1(Adresse adresse) {
		
		// werte festlegen
		String bezeichnung = "bezeichnung";
		String hinweiseZurAdresse = "hinweiseZurAdresse";
		String informationenZuGebaeudeUndDienstleistungen = "informationenZuGebaeudeUndDienstleistungen";
		Ort ort = new Ort();
		ort.setBundesland(Bundesland.MECKLENBURG_VORPOMMERN);
		ort.setOrtsname("ortsname");
		ort.setPostleitzahl("01111");
		ort.setStaat(Staat.DEUTSCHLAND);
		Postfach postfach = new Postfach();
		Ort postfachOrt  = new Ort();
		postfachOrt.setBundesland(Bundesland.BAYERN);
		postfachOrt.setOrtsname("ortsname2");
		postfachOrt.setPostleitzahl("82178");
		postfachOrt.setStaat(Staat.FRANKREICH);
		postfach.setOrt(postfachOrt);
		postfach.setPostfach(32311);
		String strasse = "strasse";
		
		// werte setzen
		adresse.setBezeichnung(bezeichnung);
		adresse.setHinweiseZurAdresse(hinweiseZurAdresse);
		adresse.setInformationenZuGebaeudeUndDienstleistungen(informationenZuGebaeudeUndDienstleistungen);
		adresse.setOrt(ort);
		adresse.setPostfach(postfach);
		adresse.setStrasse(strasse);
	}
	
	private static void fillWithValues2(Adresse adresse) {
		
		// werte festlegen
		String bezeichnung = "bezeichnung2";
		String hinweiseZurAdresse = "hinweiseZurAdresse2";
		String informationenZuGebaeudeUndDienstleistungen = "informationenZuGebaeudeUndDienstleistungen2";
		Ort ort = new Ort();
		ort.setBundesland(Bundesland.BERLIN);
		ort.setOrtsname("ortsname3");
		ort.setPostleitzahl("03333");
		ort.setStaat(Staat.DAENEMARK);
		Postfach postfach = new Postfach();
		Ort postfachOrt  = new Ort();
		postfachOrt.setBundesland(Bundesland.HAMBURG);
		postfachOrt.setOrtsname("ortsname4");
		postfachOrt.setPostleitzahl("44444");
		postfachOrt.setStaat(Staat.DEUTSCHLAND);
		postfach.setOrt(postfachOrt);
		postfach.setPostfach(53232);
		String strasse = "strasse2";
		
		// werte setzen
		adresse.setBezeichnung(bezeichnung);
		adresse.setHinweiseZurAdresse(hinweiseZurAdresse);
		adresse.setInformationenZuGebaeudeUndDienstleistungen(informationenZuGebaeudeUndDienstleistungen);
		adresse.setOrt(ort);
		adresse.setPostfach(postfach);
		adresse.setStrasse(strasse);
	}

	public static void fillWithNulls(Bildungsanbieter bildungsanbieter) {
		bildungsanbieter.setAdresse(null);
		bildungsanbieter.setEingabeverfahren(null);
		bildungsanbieter.setEMail(null);
		bildungsanbieter.setFaxnummer(null);
		bildungsanbieter.setFaxvorwahl(null);
		bildungsanbieter.setHasSignet(false);
		bildungsanbieter.setHomepage(null);
		bildungsanbieter.setId(0);
		bildungsanbieter.setName(null);
		bildungsanbieter.setTelefonnummer(null);
		bildungsanbieter.setTelefonvorwahl(null);
		bildungsanbieter.setZusatzlink(null);
	}
	
	public static void fillWithValues(Bildungsanbieter bildungsanbieter) {
		
		Adresse adresse = new Adresse();
		fillWithValues2(adresse);
		Eingabeverfahren eingabeverfahren = Eingabeverfahren.ONLINE;
		String email = "email-ba@example.com";
		String faxnummer = "546546343";
		String faxvorwahl = "344";
		boolean hasSignet = true;
		String homepage = "http://ba.homepage";
		int id = 543;
		String name = "name34";
		String telefonnummer = "23464364";
		String telefonvorwahl = "432";
		String zusatzlink = "http://ba.zusatzlink";
		
		bildungsanbieter.setAdresse(adresse);
		bildungsanbieter.setEingabeverfahren(eingabeverfahren);
		bildungsanbieter.setEMail(email);
		bildungsanbieter.setFaxnummer(faxnummer);
		bildungsanbieter.setFaxvorwahl(faxvorwahl);
		bildungsanbieter.setHasSignet(hasSignet);
		bildungsanbieter.setHomepage(homepage);
		bildungsanbieter.setId(id);
		bildungsanbieter.setName(name);
		bildungsanbieter.setTelefonnummer(telefonnummer);
		bildungsanbieter.setTelefonvorwahl(telefonvorwahl);
		bildungsanbieter.setZusatzlink(zusatzlink);
	}
}
