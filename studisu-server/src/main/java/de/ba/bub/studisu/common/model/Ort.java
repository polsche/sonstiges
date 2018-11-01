package de.ba.bub.studisu.common.model;

import java.io.Serializable;

import org.springframework.util.StringUtils;

/**
 * Simple TO for studisu Ort. Created by loutzenhj on 29.03.2017.
 */
public class Ort implements Serializable, Comparable<Ort> {

	private static final long serialVersionUID = 3478546599717126794L;

	/**
	 * Der Ortsname.
	 */
	private final String name;

	/**
	 * Die Postleitzahl als String (z.B. 90584).
	 */
	private final String postleitzahl;

	/**
	 * Das Bundesland (wird derzeit i.d.R. nicht verwendet, weil oft nicht gesetzt).
	 */
	private final String bundesland;

	/**
	 * Der Breitengrad für den Ort.
	 */
	private final Double breitengrad;
	
	/**
	 * Der Längengrad für den Ort.
	 */
	private final Double laengengrad;

	/**
	 * Simple builder for convenience.
	 *
	 * @param name
	 *            Der Name des Ortes.
	 * @return Ort mit dem gegebenen Namen.
	 */
	public static Ort withName(String name) {
		return new Ort(name, null, null, null, null);
	}

	/**
	 * Simple builder for convenience.
	 *
	 * @param name
	 *            Der Name des Ortes.
	 * @param plz
	 *            Die Postleitzahl des Ortes.
	 * @return Ort mit Namen und PLZ.
	 */
	public static Ort withNameAndPlz(String name, String plz) {
		return new Ort(name, plz, null, null, null);
	}

	/**
	 * C-tor mit allen properties
	 *
	 * @param name
	 *            Der Name des Ortes.
	 * @param plz
	 *            Die Postleitzahl des Ortes.
	 * @param breitengrad
	 *            Der Breitengrad des Ortes.
	 * @param laengengrad
	 *            Der Längengrade des Ortes.
	 * @param bundesland
	 *            Der Name des Bundeslandes, in dem der Ort liegt.
	 */
	public Ort(String name, String plz, Double breitengrad, Double laengengrad, String bundesland) {
		this.name = name;
		this.postleitzahl = plz;
		this.breitengrad = breitengrad;
		this.laengengrad = laengengrad;
		this.bundesland = bundesland;
	}

	public String getName() {
		return name;
	}

	public String getPostleitzahl() {
		return postleitzahl;
	}

	public Double getBreitengrad() {
		return breitengrad;
	}

	public Double getLaengengrad() {
		return laengengrad;
	}

	public String getBundesland() {
		return bundesland;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Ort o) {
		if (o == null) {
			throw new IllegalArgumentException("compareTo shouldnt be called with NULL");
		}
		final String name2compare = o.getName();
		final String plz2compare = o.getPostleitzahl();
		if (!StringUtils.isEmpty(name2compare)) {
			if (!StringUtils.isEmpty(name)) {
				return name.compareTo(name2compare);
			}
		}
		if (!StringUtils.isEmpty(plz2compare)) {
			if (!StringUtils.isEmpty(postleitzahl)) {
				return postleitzahl.compareTo(plz2compare);
			}
		}
		// TODO maybe a stupid fallback here..
		return -1;
	}

	/**
	 * For the autocomplete behaviour we have to check wether the names are equal. Service is partially responding with
	 * multiple results of any location, only with differing zip codes
	 */
	@Override
	public boolean equals(Object o) {
		Ort compareOrt;
		if (o instanceof Ort) {
			compareOrt = (Ort) o;
			if (compareOrt.getName() != null) {
				return compareOrt.getName().equals(getName());
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// overriding toString() as Spring uses it on Debug-Logging on Entity
		final StringBuilder sb = new StringBuilder();
		final String separator = ":";
		sb.append(String.valueOf(name));
		sb.append(separator);
		sb.append(String.valueOf(postleitzahl));
		// skipping lesser relevant fields for readability

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getName() != null) {
			return getName().hashCode();
		} else {
			return 0;
		}
	}

}
