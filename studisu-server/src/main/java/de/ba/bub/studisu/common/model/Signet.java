package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Model Signets
 * 
 * @author OettlJ
 *
 */
public class Signet implements Serializable {

	private static final long serialVersionUID = -7769802184193930378L;

	private byte[] daten;
	private String mimetype;
	/**
	 * @return the daten
	 */
	public byte[] getDaten() {
		return daten.clone();
	}
	/**
	 * @param daten the daten to set
	 */
	public void setDaten(byte[] daten) {
		this.daten = daten.clone();
	}
	/**
	 * @return the mimetype
	 */
	public String getMimetype() {
		return mimetype;
	}
	/**
	 * @param mimetype the mimetype to set
	 */
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

}
