package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator fuer Studienangebote.
 * 
 * Der Vergleich erfolgt primaer uber den in den Angeboten enthaltenen Abstand
 * (zum naechstgelegenen vom User gewaehlten Ort) und sekundaer uber die ID des
 * Studienangebotes.
 * 
 * Auf diese Weise ist "Pinning" der Reihenfolge auch dann gegeben, wenn mehrere
 * Studienangebote die gleichen Mindestabstaende haben.
 * 
 * @author schneidek084
 */
public class StudienangebotComparator implements Comparator<Studienangebot>, Serializable {
	
	/**
	 * Dummy-ID zur Beglueckung von Sonar.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Studienangebot a, Studienangebot b) {
		int comparison = Double.compare(a.getAbstand(), b.getAbstand());
		if (0 != comparison) {
			return comparison;
		}
		return a.getId().compareTo(b.getId()); // Abstand gleich, Sortierung
												// anhand Veranstaltungs-ID
	}

}
