package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.ba.bub.studisu.common.model.BundeslandEnum;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Adresse;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Ort;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Postfach;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

/**
 * Mappt Bildungsangebotservice Veranstaltung auf AdresseKurz (den Studienort).
 *
 */
public class StudienortMapper {

	/**
	 * Erzeugt eine neue AdresseKurz zur Beschreibung des Studienortes. Und
	 * füllt die AdresseKurz mit Informationen zum Studienort, falls vorhanden.
	 * 
	 * @param veranstaltung
	 *            die Veranstaltungsdaten
	 * @return AdresseKurz mit evtl. vorhandenen Studienortinformationen
	 */
	public AdresseKurz map(Veranstaltung veranstaltung) {
		AdresseKurz studienort = new AdresseKurz();
		Adresse adresse = veranstaltung.getAdresse();
		
		if (adresse != null) {
			// Straßenadresse setzen (falls vorhanden), andernfalls Postfach
			// heranziehen (falls vorhanden)
			// Straßenadresse und/oder Postfach sollte immer vorhanden sein, da
			// der Service nur Angebote mit Koordinaten liefert
			if (isOrtGefuellt(adresse.getOrt())) {
				studienort.setStrasse(adresse.getStrasse());
				addVeranstaltungsortData(studienort, adresse.getOrt());

			} else if (adresse.getPostfach() != null && isOrtGefuellt(adresse.getPostfach().getOrt())) {
				Postfach postfach = adresse.getPostfach();
				studienort.setStrasse(postfach.getPostfach() == null ? null : String.valueOf(postfach.getPostfach()));
				addVeranstaltungsortData(studienort, postfach.getOrt());
			}
		}
		return studienort;
	}

	/**
	 * Gibt zurück, ob im übergebenen Ort zumindest Postleitzahl oder Ort
	 * befüllt sind.
	 * 
	 * @param ort
	 *            der zu prüfende Ort.
	 * @return true, wenn im übergebenen Ort Postleitzahl oder Ort befüllt sind,
	 *         andernfalls false.
	 */
	private static boolean isOrtGefuellt(Ort ort) {
		if (ort != null) {
			return ort.getPostleitzahl() != null || ort.getOrtsname() != null;
		}
		return false;
	}

	/**
	 * Ergänzt das übergebene AdresseKurz-Objekt um die Veranstaltungsortdaten
	 * aus dem übergebenen Ort-Objekt.
	 * 
	 * @param studienort
	 *            die zu ergänzende Adresse.
	 * @param ort
	 *            die Veranstaltungsortdaten.
	 */
	private static void addVeranstaltungsortData(AdresseKurz studienort, Ort ort) {
		studienort.setOrt(ort.getOrtsname());
		studienort.setPostleitzahl(ort.getPostleitzahl());
		if (ort.getBundesland() != null) {
			studienort.setBundesland(BundeslandEnum.valueOf(ort.getBundesland().toString()).value());
		}
	}
}
