package de.ba.bub.studisu.common.model.facetten;

import java.io.Serializable;
import java.util.Comparator;

import de.ba.bub.studisu.common.model.facetten.Facette.Auswahl;

/**
 * Created by SiegelP003 on 14.06.2016.
 */
public final class AuswahlComparator implements Comparator<Auswahl>, Serializable {

	private static final long serialVersionUID = 8261171883112797325L;

	@Override
    public int compare(Auswahl a1, Auswahl a2) {
		int result, leftValue, rightValue; 
		
		FacettenOption left = a1.getFacettenOption();
		FacettenOption right = a2.getFacettenOption();
		
		if (left != null) {
			leftValue = left.getDisplayOrder();
		} else {
			leftValue = Integer.MIN_VALUE;
		}
		
		if (right != null) {
			rightValue = right.getDisplayOrder();
		} else {
			rightValue = Integer.MIN_VALUE;
		}
		
		result = Integer.compare(leftValue, rightValue);
		
		return result;
    }
}
