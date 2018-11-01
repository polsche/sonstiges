package de.ba.bub.studisu.common.model;

import java.io.Serializable;

public class OhneAbiZugangsbedingung implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bedingung;
	
	private String bemerkung;

	public String getBedingung() {
		return bedingung;
	}

	public void setBedingung(String bedingung) {
		this.bedingung = bedingung;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
}
