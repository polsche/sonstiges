package de.ba.bub.studisu.studienangebote.model.requestparams;

import java.util.Objects;

import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.ort.model.GeoKoordinaten;

/**
 * Ein Ort in der Suchanfrage kann bereits Geokoordinaten enthalten.
 */
public final class AnfrageOrt {

	private static final String CACHE_KEY_SEPARATOR = "_";
	
	private String ortsname;

	private GeoKoordinaten koordinaten;

	/**
	 * Construct an anfrageort from a string having structure "Berlin_58.1234_10.3333"
	 *
	 * @param paramString
	 */
	public AnfrageOrt(String paramString) {
		if (StringUtils.isEmpty(paramString)) {
			throw new IllegalArgumentException();
		}
		String[] arr = paramString.split("_");
		if (arr.length != 3) {
			throw new EingabeValidierungException("invalid ort string");
		}
		ortsname = arr[0];
		try {
			Double lgrad = Double.parseDouble(arr[1]);
			Double bgrad = Double.parseDouble(arr[2]);
			koordinaten = new GeoKoordinaten(lgrad, bgrad);
		} catch (NumberFormatException nfe) {
			throw new EingabeValidierungException(nfe.getMessage());
		}
	}

	/**
	 * getter fuer ortsname
	 * @return
	 */
	public String getOrtsname() {
		return ortsname;
	}

	/**
	 * getter fuer koordinaten
	 * @return
	 */
	public GeoKoordinaten getKoordinaten() {
		return koordinaten;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AnfrageOrt that = (AnfrageOrt) o;
		return Objects.equals(ortsname, that.ortsname) && Objects.equals(koordinaten, that.koordinaten);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ortsname, koordinaten);
	}
	
	/**
	 * return cache key used eg by eh cache
	 * this should correspond to equals or hashcode of AnfrageOrt 
	 * 
	 * @return identifying string representation for cache key
	 */
	public String getCacheKey() {
		//vorlage aus urlparam: Bremen_8.8064_53.0739
		//                      ortsname
		//                             longitude
		//                                    latitude
		StringBuilder ckbuilder = new StringBuilder(String.valueOf(getOrtsname())); // Bremen
		GeoKoordinaten coords = getKoordinaten();
		if (coords != null) {
			// longitude / laengengrad
			// _8.8064
			ckbuilder.append(CACHE_KEY_SEPARATOR + String.valueOf(coords.getLaengengrad()));
			//         latitude / breitengrad
			ckbuilder.append(CACHE_KEY_SEPARATOR + String.valueOf(coords.getBreitengrad()));
		}
		return ckbuilder.toString();
	}
}
