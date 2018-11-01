package de.ba.bub.studisu.studienangebote.util;

import de.ba.bub.studisu.ort.model.GeoKoordinaten;

/**
 * Utility zur Berechnung von Abständen zwischen zwei Geo-Koordinaten.
 * 
 * Anmerkung: Ursprünglich war ein Caching der bereits berechneten Abstände
 * vorgesehen. Eine Zeitmessung hat aber ergeben, dass bei der Berechnung von
 * 1.000.000 gleichen Abständen die direkte Berechnung ca. 16x schneller (!)
 * war, als der Einsatz des Caches (~ 250ms vs. > 4.000ms).
 * 
 * Daher wird nicht mehr gecached!
 * 
 * @author schneidek084 on 2017-10-17
 */
public final class GeoAbstandUtil {

	/**
	 * Berechnet den Abstand (in Kilometern) zwischen zwei Geokoordinaten.
	 * 
	 * Diese Methode wurde aus BEN/BerufeNET übernommen und ursprünglich von
	 * Christian Kalb implementiert. Änderungen wären ggf. hier wie dort
	 * durchzuführen.
	 * 
	 * @param gk1
	 *            Die erste Koordinate.
	 * @param gk2
	 *            Die zweite Koordinate.
	 * @return Der Abstand zwischen beiden Koordinaten in Kilometern.
	 */
	public static Double berechneEntfernung(GeoKoordinaten gk1, GeoKoordinaten gk2) {
		int radius = 6371;
		double breite = Math.toRadians(gk1.getBreitengrad() - gk2.getBreitengrad());
		double laenge = Math.toRadians(gk1.getLaengengrad() - gk2.getLaengengrad());
		double teilergebnis1 = (Math.sin(breite / 2) * Math.sin(breite / 2))
				+ Math.cos(Math.toRadians(gk1.getBreitengrad())) * Math.cos(Math.toRadians(gk2.getBreitengrad()))
						* Math.sin(laenge / 2) * Math.sin(laenge / 2);
		double teilergebnis2 = 2 * Math.atan2(Math.sqrt(teilergebnis1), Math.sqrt(1 - teilergebnis1));

		return Math.abs(radius * teilergebnis2);
	}

	/**
	 * Privater Konstruktor verhindert Instantiierung.
	 */
	private GeoAbstandUtil() {
	}

}
