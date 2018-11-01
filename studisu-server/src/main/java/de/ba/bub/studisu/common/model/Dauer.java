package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Model für Dauer und Termine.
 * @author OettlJ
 *
 */
public class Dauer implements Serializable {

	private static final long serialVersionUID = 4163459088415450968L;

	private String beginn;
	private String ende;
	private String zulassungssemester;
	private Boolean individuellerEinstieg;
	private String unterrichtszeiten;
	private String bemerkung;
	
	/**
	 * getter for bemerkung
	 * @return the bemerkung
	 */
	public String getBemerkung() {
		return bemerkung;
	}

	/**
	 * setter for bemerkung
	 * @param bemerkung the bemerkung to set
	 */
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	/**
	 * getter for unterrichtszeiten
	 * @return the unterrichtszeiten
	 */
	public String getUnterrichtszeiten() {
		return unterrichtszeiten;
	}

	/**
	 * setter for unterrichtszeiten
	 * @param unterrichtszeiten the unterrichtszeiten to set
	 */
	public void setUnterrichtszeiten(String unterrichtszeiten) {
		this.unterrichtszeiten = unterrichtszeiten;
	}

	/**
	 * getter for ende
	 * @return the ende
	 */
	public String getEnde() {
		return ende;
	}

	/**
	 * setter for ende
	 * @param ende the ende to set
	 */
	public void setEnde(String ende) {
		this.ende = ende;
	}	

	/**
	 * getter for beginn
	 * @return the beginn
	 */
	public String getBeginn() {
		return beginn;
	}

	/**
	 * setter for beginn
	 * @param beginn the beginn to set
	 */
	public void setBeginn(String beginn) {
		this.beginn = beginn;
	}

	public String getZulassungssemester() {
		return zulassungssemester;
	}

	public void setZulassungssemester(String zulassungssemester) {
		this.zulassungssemester = zulassungssemester;
	}

	public Boolean getIndividuellerEinstieg() {
		return individuellerEinstieg;
	}

	public void setIndividuellerEinstieg(Boolean individuellerEinstieg) {
		this.individuellerEinstieg = individuellerEinstieg;
	}
}
