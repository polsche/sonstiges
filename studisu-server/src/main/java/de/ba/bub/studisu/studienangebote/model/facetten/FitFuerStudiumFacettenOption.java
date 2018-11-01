package de.ba.bub.studisu.studienangebote.model.facetten;

import java.io.Serializable;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.common.model.facetten.FacettenOptionImpl;

/**
 * Created by loutzenhj on 18.09.2017.
 */
public class FitFuerStudiumFacettenOption extends FacettenOptionImpl implements Serializable, FacettenOption {

	private static final long serialVersionUID = -1754655242119977029L;

	public static final FitFuerStudiumFacettenOption OSA = new FitFuerStudiumFacettenOption("osa", 2, 2, "OSA", true);

	public static final FitFuerStudiumFacettenOption STUDICHECK = new FitFuerStudiumFacettenOption("studicheck", 1, 1,
			"Studicheck", true);

	// sonder option - keine ffs links vorhanden. Diese Option wird rausgeschmissen bevor json gerendert wird, also
	// erscheint an der OF nicht.
	// ist aber nötig, damit filter logik richtig greift.
	public static final FitFuerStudiumFacettenOption KEINES = new FitFuerStudiumFacettenOption("", -1, -1, "", false);

	static final FitFuerStudiumFacettenOption[] ALL_OPTIONS = { OSA, STUDICHECK, KEINES };

	/**
	 * private C-tor
	 *
	 * @param name
	 *            Der Name der Facetten-Option.
	 * @param id
	 *            Die ID der Facetten-Option.
	 * @param displayOrder
	 *            Sortierreihenfolge für die Facetten-Option, Anzeige aufsteigend nach Wert.
	 * @param label
	 *            Das anzuzeigende Label (Bezeichnung) für die Facetten-Option.
	 * @param show
	 *            Flag, das angibt, ob diese Facetten-Option in's JSON für die Oberfläche exportiert wird.
	 */
	private FitFuerStudiumFacettenOption(String name, int id, int displayOrder, String label, boolean show) {
		super(name, id, displayOrder, label, show);
	}

	/**
	 * accessor with id
	 * 
	 * @param id
	 *            id
	 * @return option for id
	 */
	public static FitFuerStudiumFacettenOption forId(Integer id) {
		for (FitFuerStudiumFacettenOption opt : ALL_OPTIONS) {
			if (opt.getId() == id) {
				return opt;
			}
		}
		return null;
	}
}
