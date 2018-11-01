package de.ba.bub.studisu.studienangebote.model.facetten;

import java.io.Serializable;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.common.model.facetten.FacettenOptionImpl;

/**
 * Gibt alle möglichen Facetten-Optionen für die Studienformen an.
 *
 * Die Daten erlauben die Angabe der textlichen ID für die Kursnet-Suche, der DB-ID aus Kursnet (gleichzeitig für unsere
 * Frontend-API verwendet), der Sortierreihenfolge der Einträge (aufsteigend!) und der textlichen Bezeichnung für die
 * Darstellung von Labels im Frontend.
 *
 * @author loutzenhj on 05.04.2017.
 */
public class StudienformFacettenOption extends FacettenOptionImpl implements Serializable, FacettenOption {

	private static final long serialVersionUID = 8732447732910157597L;

	/**
	 * StudienformFacettenOption für Einträge, bei denen der Studienverlauf auf Anfrage mitgeteilt wird.
	 */
	public final static StudienformFacettenOption AUF_ANFRAGE 
		= new StudienformFacettenOption("aufAnfrage", 0, 7, "Auf Anfrage");

	/**
	 * StudienformFacettenOption für Einträge, bei denen das Studium in Vollzeit stattfindet.
	 */
	public final static StudienformFacettenOption VOLLZEIT 
		= new StudienformFacettenOption("vollzeit", 1, 0, "Vollzeitstudium");

	/**
	 * StudienformFacettenOption für Einträge, bei denen das Studium in Teilzeit stattfindet.
	 */
	public final static StudienformFacettenOption TEILZEIT 
		= new StudienformFacettenOption("teilzeit", 2, 1, "Teilzeitstudium");

	/**
	 * StudienformFacettenOption für Einträge, bei denen das Studium als Wochenendveranstaltung(en) organisiert ist.
	 */
	public final static StudienformFacettenOption WOCHENENDVERANSTALTUNG 
		= new StudienformFacettenOption("wochenendveranstaltung", 3, 3, "Wochenendveranstaltung");

	/**
	 * StudienformFacettenOption für Einträge, bei denen ein Fernstudium stattfindet.
	 */
	public final static StudienformFacettenOption FERNUNTERRICHT_FERNSTUDIUM 
		= new StudienformFacettenOption("fernunterricht_Fernstudium", 4, 2, "Fernstudium");

	/**
	 * StudienformFacettenOption für Einträge, bei denen ein Selbststudium stattfindet.
	 */
	public final static StudienformFacettenOption SELBSTSTUDIUM_E_LEARNING_BLENDED_LEARNING 
		= new StudienformFacettenOption("selbststudium_E_learning_BlendedLearning", 5, 4, "Selbststudium");

	/**
	 * StudienformFacettenOption für Einträge, bei denen das Studium im Blockunterricht stattfindet.
	 */
	public final static StudienformFacettenOption BLOCKUNTERRICHT 
		= new StudienformFacettenOption("blockunterricht", 6, 5, "Blockstudium");

	/**
	 * StudienformFacettenOption für Einträge, bei denen Inhouse- und/oder Firmensemimare stattfinden.
	 */
	public final static StudienformFacettenOption INHOUSE_FIRMENSEMINAR 
		= new StudienformFacettenOption("inhouse__Firmenseminar", 7, 6, "Inhouse-/Firmenseminar");

	/**
	 * StudienformFacettenOption für Einträge, keine oder eine unbekannte Studienform haben.
	 */
	public final static StudienformFacettenOption SONSTIGE 
		= new StudienformFacettenOption("sonstige_studienform", 8, 8, "Sonstige Studienform");

	/**
	 * Liste aller Optionen von {@link StudienformFacettenOption}
	 */
	final static StudienformFacettenOption[] ALL_OPTIONS = { AUF_ANFRAGE, VOLLZEIT, TEILZEIT, WOCHENENDVERANSTALTUNG,
			FERNUNTERRICHT_FERNSTUDIUM, SELBSTSTUDIUM_E_LEARNING_BLENDED_LEARNING, BLOCKUNTERRICHT,
			INHOUSE_FIRMENSEMINAR, SONSTIGE };

	/**
	 * C-tor
	 *
	 * @param name
	 *            Der Name der StudienformFacettenOption.
	 * @param id
	 *            Die ID der StudienformFacettenOption.
	 * @param displayOrder
	 *            Sortierreihenfolge für die StudienformFacettenOption, Anzeige aufsteigend nach Wert.
	 * @param label
	 *            Das anzuzeigende Label (Bezeichnung) für die StudienformFacettenOption.
	 */
	private StudienformFacettenOption(String name, int id, int displayOrder, String label) {
		super(name, id, displayOrder, label, true);
	}

	/**
	 * Accessor für die StudienformFacettenOption auf Basis des angegebenen Namens.
	 *
	 * @param name
	 *            Der Name der gesuchten StudienformFacettenOption.
	 * @return Die zum Namen passende StudienformFacettenOption.
	 */
	public static StudienformFacettenOption forName(String name) {
		for (final StudienformFacettenOption option : ALL_OPTIONS) {
			if (option.getName().equals(name)) {
				return option;
			}
		}
		return SONSTIGE;
	}

	/**
	 * Accessor für die StudienformFacettenOption auf Basis der angegebenen ID.
	 *
	 * @param id
	 *            Die ID der gesuchten StudienformFacettenOption.
	 * @return Die zur ID passende StudienformFacettenOption.
	 */
	public static StudienformFacettenOption forId(int id) {
		for (final StudienformFacettenOption option : ALL_OPTIONS) {
			if (option.getId() == id) {
				return option;
			}
		}
		return SONSTIGE;
	}

}
