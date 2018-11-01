package de.ba.bub.studisu.signet.model;

/**
 * Einfaches TO fuer die Anfrage nach einem Signet
 *
 * @author pleinesm001
 */
public class SignetAnfrage {

	private int validationResult;

	/**
	 * Ban-Id des Bildungsanbieters
	 */
	private Integer banId;

	/**
	 * C-tor mit BAN_ID
	 * @param banId die Bildungsanbieter-ID
	 */
	public SignetAnfrage(Integer banId) {
		this.banId = banId;
		SignetValidator validator = new SignetValidator(banId);
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

	/**
	 * @return the banId
	 */
	public Integer getBanId() {
		return banId;
	}

	/**
	 * @param banId the banId to set
	 */
	public void setBanId(Integer banId) {
		this.banId = banId;
	}

}
