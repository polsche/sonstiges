package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Beschreibt den Abstand (eines Studienortes) von dem angegebenen Suchort.
 */
public class SuchortAbstand implements Serializable {

	private static final long serialVersionUID = 6066747189034269423L;

	String suchort;
	Double abstand;

	/**
	 * Erzeugt ein Objekt zur Beschreibung des Abstands zum Suchort.
	 * 
	 * @param suchort
	 *            Der Name des Suchortes, zu dem der Abstand definiert wird.
	 * @param abstand
	 *            Der definierte Abstand zum Suchort in Kilometern.
	 */
	public SuchortAbstand(String suchort, Double abstand) {
		this.suchort = suchort;
		this.abstand = abstand;
	}

	public String getSuchort() {
		return suchort;
	}

	public void setSuchort(String suchort) {
		this.suchort = suchort;
	}

	public Double getAbstand() {
		return abstand;
	}

	public void setAbstand(Double abstand) {
		this.abstand = abstand;
	}

}
