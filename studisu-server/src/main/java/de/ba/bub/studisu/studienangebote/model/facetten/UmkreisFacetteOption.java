package de.ba.bub.studisu.studienangebote.model.facetten;

import java.io.Serializable;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.common.model.facetten.FacettenOptionImpl;

/**
 * Gibt alle möglichen Facetten-Optionen für die Umkreise an.
 *
 * Die Daten erlauben die Angabe des Names (wird zugleich als Radius des Umkreises interpretiert) und der
 * Sortierreihenfolge (aufsteigend interpretiert). Die ID und das Label werden nicht gesetzt und bei dieser
 * Facetten-Option auch nicht verwendet.
 */
public class UmkreisFacetteOption extends FacettenOptionImpl implements Serializable, FacettenOption {

	private static final long serialVersionUID = 1606462708284932306L;

	/**
	 * C-tor
	 *
	 * @param name
	 *            Der Name der UmkreisFacetteOption (zugleich Entfernung, oder "Bundesweit").
	 * @param displayOrder
	 *            Sortierreihenfolge für die HochschulartFacettenOption, Anzeige aufsteigend nach Wert.
	 */
	public UmkreisFacetteOption(String name, int displayOrder) {
		super(name, 1, displayOrder, name, true);
	}

	/**
	 * Liefert den Umkreis zur UmkreisFacetteOption in Kilometern.
	 *
	 * @return Umkreis in Kilometern.
	 */
	public int getUmkreisKm() {
		if (this.equals(UmkreisFacette.BUNDESWEIT)) {
			return 9999;
		} else {
			return Integer.parseInt(this.getName());
		}
	}

}
