package de.ba.bub.studisu.signet.model;

/**
 * Validator für Signetabfrage
 *
 * Es muss eine BAN-ID angegeben werden
 */
public class SignetValidator {

	/**
	 * Ungültige Suchparameter.
	 */
	public static final int INVALID = 0;
	
	/**
	 * Gültige Ban-ID.
	 */
	public static final int VALID = 1;
	
	//Ergebnis der Validierung
	private int result = INVALID;

	/**
	 * C-tor with input
	 *
	 * @param banid
	 */
	public SignetValidator(Integer banid) {

		result = INVALID;
		
		if (banid != null) {
			result = VALID;
		}

	}

	/**
	 * Validation result 
	 *
	 * @return validation result
	 */
	public int getResult() {
		return result;
	}
}
