package de.ba.bub.studisu.ort.model;

import java.io.Serializable;

/**
 * Speichert die Geokoordinaten.
 * 
 * Die Klasse wird unter anderem zur Abstandsberechnung von Orten verwendet.
 */
public class GeoKoordinaten implements Serializable {

	private static final long serialVersionUID = 2045111142739366237L;

	private Double laengengrad;
	private Double breitengrad;

	/**
	 * C-tor.
	 * 
	 * Erzeugt eine neuen Koordinate, die nicht mehr geändert werden kann.
	 * 
	 * @param laengengrad
	 *            Längengrad für den Ort.
	 * @param breitengrad
	 *            Breitengrad für den Ort.
	 */
	public GeoKoordinaten(Double laengengrad, Double breitengrad) {
		this.laengengrad = null == laengengrad ? 0 : laengengrad;
		this.breitengrad = null == breitengrad ? 0 : breitengrad;
	}

	/**
	 * Getter für Längengrad.
	 * 
	 * @return
	 */
	public Double getLaengengrad() {
		return laengengrad;
	}

	/**
	 * Getter für Breitengrad.
	 * 
	 * @return
	 */
	public Double getBreitengrad() {
		return breitengrad;
	}
}
