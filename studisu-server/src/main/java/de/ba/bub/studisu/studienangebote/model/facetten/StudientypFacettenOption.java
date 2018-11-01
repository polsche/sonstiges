package de.ba.bub.studisu.studienangebote.model.facetten;

import java.io.Serializable;
import java.util.List;

import de.ba.bub.studisu.common.SystematikConstants;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.common.model.facetten.FacettenOptionImpl;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik;

/**
 * Klasse fuer Facetten-Auspraegungen von Studientyp
 *
 * Die Daten erlauben die Angabe der textlichen ID für die Kursnet-Suche, der DB-ID aus Kursnet (gleichzeitig für unsere
 * Frontend-API verwendet), der Sortierreihenfolge der Einträge (aufsteigend!) und der textlichen Bezeichnung für die
 * Darstellung von Labels im Frontend.
 *
 * @author OettlJ
 *
 */
public final class StudientypFacettenOption extends FacettenOptionImpl implements Serializable, FacettenOption {

	private static final long serialVersionUID = -6341679642271112919L;

	/**
	 * StudientypFacettenOption für grundständige Studiengänge.
	 */
	public static final StudientypFacettenOption GRUNDSTAENDIG = new StudientypFacettenOption(
			SystematikConstants.CODENR_STUDIUM_GRUNDSTAENDIG, 0, 0, "Studiengang grundständig");

	/**
	 * StudientypFacettenOption für weiterführende Studiengänge.
	 */
	public static final StudientypFacettenOption WEITERFUEHREND = new StudientypFacettenOption(
			SystematikConstants.CODENR_STUDIUM_WEITERFUEHREND, 1, 1, "Studiengang weiterführend");

	/**
	 * StudientypFacettenOption für sonstige Studiengänge (derzeit nicht verwendet, zeigt ggf. Fehler in den Daten auf).
	 */
	public static final StudientypFacettenOption SONSTIGE = new StudientypFacettenOption("ST", 2, 2,
			"Sonstiger Studiengang");

	/**
	 * Liste aller verfügbaren StudientypFacettenOptionen.
	 */
	static final StudientypFacettenOption[] ALL_OPTIONS = { GRUNDSTAENDIG, WEITERFUEHREND, SONSTIGE };

	/**
	 * C-tor
	 *
	 * @param name
	 *            Der Name der StudientypFacettenOption.
	 * @param id
	 *            Die ID der StudientypFacettenOption.
	 * @param displayOrder
	 *            Sortierreihenfolge für die StudientypFacettenOption, Anzeige aufsteigend nach Wert.
	 * @param label
	 *            Das anzuzeigende Label (Bezeichnung) für die StudientypFacettenOption.
	 */
	private StudientypFacettenOption(String name, int id, int displayOrder, String label) {
		super(name, id, displayOrder, label, true);
	}
	
	/**
	 * Accessor für die StudientypFacettenOption auf Basis der angegebenen ID.
	 *
	 * @param id
	 *            Die ID der gesuchten StudientypFacettenOption.
	 * @return Die zur ID passende StudientypFacettenOption.
	 */
	public static StudientypFacettenOption forId(int id) {
		for (final StudientypFacettenOption option : ALL_OPTIONS) {
			if (option.getId() == id) {
				return option;
			}
		}
		return SONSTIGE;
	}

	/**
	 * Accessor für die StudientypFacettenOption auf Basis der angegebenen Liste mit Systematiken.
	 *
	 * Liefert den Studientyp zur ersten gefundenen H-Systematik innerhalb der uebergebenen Systematik-Liste, da es
	 * fachlich nicht vorgesehen ist, dass ein Angebot sowohl HA- als auch HC-Systematiken zugeordnet hat.
	 *
	 * @param systematiken
	 *            Die Liste der Systematiken in denen nach einem passenden Studientyp gesucht wird.
	 * @return Die zur ersten passenden Systematik gefundene StudientypFacettenOption.
	 */
	public static StudientypFacettenOption forSystematiken(List<Systematik> systematiken) {
		for (final Systematik systematik : systematiken) {
			if (systematik.getDkzCodenr().startsWith(GRUNDSTAENDIG.getName())) {
				return GRUNDSTAENDIG;
			}
			if (systematik.getDkzCodenr().startsWith(WEITERFUEHREND.getName())) {
				return WEITERFUEHREND;
			}
		}

		/**
		 * STUDISU-48: Falls keine HA/HC-Systematik vom WS kommt (was nach jetzigem Stand (Mai17) _fachlich_ nicht
		 * möglich/vorgesehen ist), wird diese unter dem Pseudo-Studiengang "Sonstiger Studiengang" geführt.
		 *
		 * Vorheriges Verhalten: Werfen einer Exception.
		 */
		return SONSTIGE;
	}

}
