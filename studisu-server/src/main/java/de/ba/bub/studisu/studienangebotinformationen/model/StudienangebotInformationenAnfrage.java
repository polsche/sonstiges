package de.ba.bub.studisu.studienangebotinformationen.model;

/**
 * Model fuer die Anfrage von einem Studienangebot. Enthaelt eine StudienAngebotID
 * @author FuchsD013
 *
 */
public class StudienangebotInformationenAnfrage {
	
	private int studienangebotID;

	/**
	 * Ctor
	 * @param studienangebotID
	 */
	public StudienangebotInformationenAnfrage(int studienangebotID) {
		this.studienangebotID = studienangebotID;
	}

	/**
	 * Gibt die StudienangebotsID zurueck
	 * @return
	 */
	public int getStudienangebotID() {
		return studienangebotID;
	}
}
