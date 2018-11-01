package de.ba.bub.studisu.studienfach.model;

import java.io.Serializable;
import java.util.Comparator;

import de.ba.bub.studisu.common.model.Studienfach;

/**
 * Comparator zur Sortierung von Studienfach-Objekten
 * anhand der Stelle der Übereinstimmung mit der Nutzereingabe.
 * 
 * @author OettlJ
 *
 */
public class StudienfachSuchwortComparator implements Comparator<Studienfach>, Serializable  {
	
	private static final long serialVersionUID = 8841117064895785862L;

	/** Konstante für maximales Gewicht */
	private final static int MAX_WEIGHT = 999;
	
	/** Nutzereingabe - Suchwort */
	private String sw;

	/**
	 * Default Konstruktor mittels Nutzereingabe.
	 * @param sw die Nutzereingabe.
	 */
	public StudienfachSuchwortComparator(String sw) {
		this.sw = sw.toLowerCase();
	}

	/**
	 * Vergleicht zwei Studienfach-Objekte. Bei identischer Gewichtung
	 * anhand der Stelle der Übereinstimmung mit der Nutzereingabe
	 * erfolgt ein alphabetischer Vergleich.
	 * @param studf1 Studienfach 1.
	 * @param studf2 Studienfach 2.
	 * @return < 0 wenn Studienfach 1 vor Studienfach 2 kommt,
	 * 0 wenn Studienfach 1 und Studienfach 2 an selber Stelle kommen,
	 * > 0 wenn Studienfach 2 vor Studienfach 1 kommt.
	 */
	@Override
	public int compare(Studienfach studf1, Studienfach studf2) {
		int weight1 = getWeight(studf1);
		int weight2 = getWeight(studf2);
		
		int result;
		// je höher das Gewicht, umso weiter vorne soll das Studienfach in der Liste stehen
		if (weight1 > weight2) {
			result = -1;
		} else if (weight2 > weight1) {
			result = 1;
		} else {
			// bei gleicher Gewichtung -> alphabetischer Vergleich
			result = studf1.getName().compareTo(studf2.getName());
		}
		return result;
	}
	
	/**
	 * Ermittelt das Gewicht des übergebenen Studienfachs
	 * anhand der Stelle der Übereinstimmung mit der Nutzereingabe.
	 * Je höher das Gewicht, umso weiter vorne soll das
	 * Studienfach in der Liste stehen. 
	 * @param studf das zu gewichtende Studienfach.
	 * @return das Gewicht des übergebenen Studienfachs.
	 */
	private int getWeight(Studienfach studf) {
		int posInBez = studf.getName().toLowerCase().indexOf(this.sw);
		// bei keiner Übereinstimmung -> Gewicht = -1
		if (posInBez < 0) {
			return posInBez;
		}
		// bei Übereinstimmung -> Gewicht = 999 - Position Übereinstimmung
		return MAX_WEIGHT - posInBez;
	}
}
