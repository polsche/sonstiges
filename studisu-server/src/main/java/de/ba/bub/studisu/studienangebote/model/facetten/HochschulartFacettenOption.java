package de.ba.bub.studisu.studienangebote.model.facetten;

import java.io.Serializable;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.common.model.facetten.FacettenOptionImpl;

/**
 * Gibt alle möglichen Facetten-Optionen für die Hochschularten an.
 *
 * Die Daten erlauben die Angabe der textlichen ID für die Kursnet-Suche, der DB-ID aus Kursnet (gleichzeitig für unsere
 * Frontend-API verwendet), der Sortierreihenfolge der Einträge (aufsteigend!) und der textlichen Bezeichnung für die
 * Darstellung von Labels im Frontend.
 *
 * @author loutzenhj on 10.04.2017.
 *
 *         TODO eventually implement innerclass enum for JSON transfer
 */
public final class HochschulartFacettenOption extends FacettenOptionImpl implements Serializable, FacettenOption {

	private static final long serialVersionUID = 6773385175506942621L;

	/**
	 * HochschulartFacettenOption für Universitäten.
	 */
	public static final HochschulartFacettenOption UNIVERSITAET = new HochschulartFacettenOption("universitaet", 108, 1,
			"Universität");

	/**
	 * HochschulartFacettenOption für Fachhochschulen.
	 */
	public static final HochschulartFacettenOption FACHHOCHSCHULE = new HochschulartFacettenOption(
			"fachhochschuleHochschuleFuerAngewandteWissenschaften", 106, 2,
			"Fachhochschule/Hochschule für angewandte Wissenschaften");

	/**
	 * HochschulartFacettenOption für Berufsakademien.
	 */
	public static final HochschulartFacettenOption BERUFSAKADEMIE = new HochschulartFacettenOption(
			"berufsakademieDualeHochschule", 101, 3, "Berufsakademie/Duale Hochschule");

	/**
	 * HochschulartFacettenOption für Kunst- und Musikhochschulen.
	 */
	public static final HochschulartFacettenOption KUNST_UND_MUSIKHOCHSCHULE = new HochschulartFacettenOption(
			"kunst_UndMusikhochschule", 107, 4, "Kunst- und Musikhochschule");

	/**
	 * HochschulartFacettenOption für Verwaltungshochschule.
	 */
	public static final HochschulartFacettenOption VERWALTUNGSHOCHSCHULE = new HochschulartFacettenOption(
			"verwaltungshochschule", 111, 5, "Verwaltungshochschule");

	/**
	 * HochschulartFacettenOption für Kirchliche Hochschule.
	 */
	public static final HochschulartFacettenOption KIRCHLICHEHOCHSCHULE = new HochschulartFacettenOption(
			"kirchlichehochschule", 112, 6, "Kirchliche Hochschule");

	/**
	 * HochschulartFacettenOption für vergleichbare Private Hochschule.
	 */
	public static final HochschulartFacettenOption PRIVATEHOCHSCHULE = new HochschulartFacettenOption(
			"privatehochschule", 113, 7, "Private Hochschule");

	/**
	 * HochschulartFacettenOption für Hochschule eigenen Typs.
	 */
	public static final HochschulartFacettenOption HOCHSCHULEEIGENENTYPS = new HochschulartFacettenOption(
			"hochschuleEigenenTyps", 114, 8, "Hochschule eigenen Typs");

	/**
	 * HochschulartFacettenOption für Einrichtungen der beruflichen Weiterbildung.
	 */
	public static final HochschulartFacettenOption EINRICHTUNG_DER_BERUFLICHEN_WEITERBILDUNG = new HochschulartFacettenOption(
			"einrichtungDerBeruflichenWeiterbildung", 105, 9, "Einrichtung der beruflichen Weiterbildung");

	/**
	 * HochschulartFacettenOption für Berufsbildende Schulen/Einrichtungen.
	 */
	public static final HochschulartFacettenOption BERUFSBILDENDE_SCHULE_EINRICHTUNG = new HochschulartFacettenOption(
			"berufsbildendeSchule_Einrichtung", 102, 10, "Berufsbildende Schule/Einrichtung");

	/**
	 * HochschulartFacettenOption für Berufsbildungswerke.
	 */
	public static final HochschulartFacettenOption BERUFSBILDUNGSWERK = new HochschulartFacettenOption(
			"berufsbildungswerk", 103, 11, "Berufsbildungswerk");

	/**
	 * HochschulartFacettenOption für Berufsförderungswerke.
	 */
	public static final HochschulartFacettenOption BERUFSFOERDERUNGSWERK = new HochschulartFacettenOption(
			"berufsfoerderungswerk", 104, 12, "Berufsförderungswerk");

	/**
	 * HochschulartFacettenOption für Einträge von Allgemeinbildenden Schulen/Einrichtungen.
	 */
	public static final HochschulartFacettenOption ALLGEMEINBILDENDE_SCHULE_EINRICHTUNG = new HochschulartFacettenOption(
			"allgemeinbildendeSchule_Einrichtung", 100, 13, "Allgemeinbildende Schule/Einrichtung");

	/**
	 * HochschulartFacettenOption für med.-berufl. Rehabilitationseinrichtungen.
	 */
	public static final HochschulartFacettenOption MED_BERUFL_REHABILITATIONSEINRICHTUNG = new HochschulartFacettenOption(
			"med__berufl_Rehabilitationseinrichtung", 110, 14, "med.-berufl. Rehabilitationseinrichtung");

	/**
	 * HochschulartFacettenOption für vergleichbare Rehabilitationseinrichtungen.
	 */
	public static final HochschulartFacettenOption VERGLEICHBARE_REHABILITATIONSEINRICHTUNG = new HochschulartFacettenOption(
			"vergleichbareRehabilitationseinrichtung", 109, 15, "vergleichbare Rehabilitationseinrichtung");

	/**
	 * HochschulartFacettenOption für Einträge, bei denen keinen Zuordnung möglich ist.
	 */
	public static final HochschulartFacettenOption KEINE_ZUORDNUNG_MOEGLICH = new HochschulartFacettenOption(
			"keineZuordnungMoeglich", 0, 16, "Keine Zuordnung möglich");

	/**
	 * Liste aller verfügbaren HochschulartFacettenOptionen.
	 */
	static final HochschulartFacettenOption[] ALL_OPTIONS = { UNIVERSITAET, FACHHOCHSCHULE, KUNST_UND_MUSIKHOCHSCHULE,
			BERUFSAKADEMIE, EINRICHTUNG_DER_BERUFLICHEN_WEITERBILDUNG, BERUFSBILDENDE_SCHULE_EINRICHTUNG,
			ALLGEMEINBILDENDE_SCHULE_EINRICHTUNG, BERUFSBILDUNGSWERK, BERUFSFOERDERUNGSWERK,
			MED_BERUFL_REHABILITATIONSEINRICHTUNG, VERGLEICHBARE_REHABILITATIONSEINRICHTUNG, VERWALTUNGSHOCHSCHULE,
			KIRCHLICHEHOCHSCHULE, PRIVATEHOCHSCHULE, HOCHSCHULEEIGENENTYPS, KEINE_ZUORDNUNG_MOEGLICH };

	/**
	 * Accessor für die HochschulartFacettenOption auf Basis des angegebenen Namens.
	 *
	 * @param name
	 *            Der Name der gesuchten HochschulartFacettenOption.
	 * @return Die zum Namen passende HochschulartFacettenOption.
	 */
	public static HochschulartFacettenOption forName(String name) {
		for (final HochschulartFacettenOption option : ALL_OPTIONS) {
			if (option.getName().equalsIgnoreCase(name)) {
				return option;
			}
		}
		return KEINE_ZUORDNUNG_MOEGLICH;
	}

	/**
	 * Accessor für die HochschulartFacettenOption auf Basis der angegebenen ID.
	 *
	 * @param id
	 *            Die ID der gesuchten HochschulartFacettenOption.
	 * @return Die zur ID passende HochschulartFacettenOption.
	 */
	public static HochschulartFacettenOption forId(int id) {
		for (final HochschulartFacettenOption option : ALL_OPTIONS) {
			if (option.getId() == id) {
				return option;
			}
		}
		return KEINE_ZUORDNUNG_MOEGLICH;
	}

	/**
	 * C-tor
	 *
	 * @param name
	 *            Der Name der HochschulartFacettenOption.
	 * @param id
	 *            Die ID der HochschulartFacettenOption.
	 * @param displayOrder
	 *            Sortierreihenfolge für die HochschulartFacettenOption, Anzeige aufsteigend nach Wert.
	 * @param label
	 *            Das anzuzeigende Label (Bezeichnung) für die HochschulartFacettenOption.
	 */
	private HochschulartFacettenOption(String name, int id, int displayOrder, String label) {
		super(name, id, displayOrder, label, true);
	}

}
