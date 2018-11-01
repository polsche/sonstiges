package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Model einer Adresse
 * nur mit Name, Straße, PLZ, Ort, Bundesland.
 *
 */
public class AdresseKurz implements Serializable {
	
	private static final long serialVersionUID = 4459923693653424846L;

	/**
     * Der Name des Adresse / auch Adressat (z.B. Valerien Ismael).
     */
    private String name;

    /**
     * Die Postleitzahl als String (z.B. 90584).
     */
    private String postleitzahl;

    /**
     * Die Bezeichnung des Bundeslandes (z.B. Sachsen-Anhalt).
     */
    private String bundesland;

    /**
     * Der Stra&szlig;enname inkl. Hausnummer als String.
     */
    private String strasse;

    /**
     * Der Ortsname der Adresse.
     */
    private String ort;
    
    /**
     * Setter f&uuml;r Name.
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter f&uuml;r den Namen der Adresse.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Setter f&uuml;r Postleitzahl.
     * @param postleitzahl String
     */
    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    /**
     * Getter f&uuml;r Postleitzahl.
     * @return String
     */
    public String getPostleitzahl() {
        return postleitzahl;
    }

    /**
     * Setter f&uuml;r Bundesland.
     * @param bundesland
     */
    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    /**
     * Getter f&uuml;r Bundesland.
     * @return String
     */
    public String getBundesland() {
        return bundesland;
    }

    /**
     * Setter f&uuml;r Strasse inkl. Hausnummer.
     * @param strasse
     */
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    /**
     * Getter f&uuml;r Strasse.
     * @return String
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Setter f&uuml;r den Ortsnamen.
     * @param ort String
     */
    public void setOrt(String ort) {
        this.ort = ort;
    }

    /**
     * Getter f&uuml;r den Ortsnamen.
     * @return String
     */
    public String getOrt() {
        return ort;
    }	
}
