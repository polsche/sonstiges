package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Model eines Kontakts
 * 
 * @author OettlJ
 *
 */
public class Kontakt implements Serializable {

	private static final long serialVersionUID = -4720706550435667528L;

	private String telefonVorwahl;
	private String telefonNummer;
	private String telefaxVorwahl;
	private String telefaxNummer;
	private String internet;
	private String email;	

	/**
	 * getter for telefonVorwahl
	 * @return the telefonVorwahl
	 */
	public String getTelefonVorwahl() {
		return telefonVorwahl;
	}

	/**
	 * setter for telefonVorwahl
	 * @param telefonVorwahl the telefonVorwahl to set
	 */
	public void setTelefonVorwahl(String telefonVorwahl) {
		this.telefonVorwahl = telefonVorwahl;
	}

	/**
	 * getter for telefonNummer
	 * @return the telefonNummer
	 */
	public String getTelefonNummer() {
		return telefonNummer;
	}

	/**
	 * setter for telefonNummer
	 * @param telefonNummer the telefonNummer to set
	 */
	public void setTelefonNummer(String telefonNummer) {
		this.telefonNummer = telefonNummer;
	}

	/**
	 * getter for telefaxVorwahl
	 * @return the telefaxVorwahl
	 */
	public String getTelefaxVorwahl() {
		return telefaxVorwahl;
	}

	/**
	 * setter for telefaxVorwahl
	 * @param telefaxVorwahl the telefaxVorwahl to set
	 */
	public void setTelefaxVorwahl(String telefaxVorwahl) {
		this.telefaxVorwahl = telefaxVorwahl;
	}

	/**
	 * getter for telefaxNummer
	 * @return the telefaxNummer
	 */
	public String getTelefaxNummer() {
		return telefaxNummer;
	}

	/**
	 * setter for telefaxNummer
	 * @param telefaxNummer the telefaxNummer to set
	 */
	public void setTelefaxNummer(String telefaxNummer) {
		this.telefaxNummer = telefaxNummer;
	}
	
	/**
	 * getter for internet
	 * @return the internet
	 */
	public String getInternet() {
		return internet;
	}

	/**
	 * setter for internet
	 * @param internet the internet to set
	 */
	public void setInternet(String internet) {
		this.internet = internet;
	}

	/**
	 * getter for email
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * setter for email
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
