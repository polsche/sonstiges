package de.ba.bub.studisu.ort.model;

/**
 * Simple ort such anfrage TO Created by loutzenhj on 29.03.2017.
 */
public class OrtsucheAnfrage {

	private int validationResult;

	/**
	 * Ort search string, for example 'Ber' or 'Frankf'
	 */
	private String suchString;

	/**
	 * C-tor with such string
	 *
	 * @param such
	 *            such string
	 */
	public OrtsucheAnfrage(String such) {
		this.suchString = such;
		OrtsucheValidator validator = new OrtsucheValidator(such);
		validationResult = validator.getResult();
	}

	/**
	 * Validation result
	 *
	 * @return validation result
	 */
	public int getValidationResult() {
		return validationResult;
	}

	public String getSuchString() {
		return suchString;
	}
}
