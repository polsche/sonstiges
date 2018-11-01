package de.ba.bub.studisu.studienfach.model;

import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;

/**
 * Einfaches TO fuer die Anfrage nach einem Studienfach.
 *
 * @author schneidek084 on 2017-06-01
 */
public class StudienfachAnfrage {

	private int validationResult;

	/**
	 * Suchstring fuer die Anfrage nach dem Studienfach, z.B. "Archi" oder
	 * "Mas".
	 */
	private String suchString;

	/**
	 * IDs von Studienfächern, die geladen werden sollen.
	 */
	private Studienfaecher studienfaecher;

	/**
	 * C-tor mit Suchstring
	 * @param suchString der Suchstring
	 * @param studienfaecher zu suchende Studienfaecher
	 */
	public StudienfachAnfrage(String suchString, Studienfaecher studienfaecher) {
		this.suchString = suchString;
		this.studienfaecher = studienfaecher;
		StudienfachSucheValidator validator = new StudienfachSucheValidator(suchString, studienfaecher);
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
	 * Suchstring fuer die Anfrage nach dem Studienfach, z.B. "Archi" oder
	 * "Mas".
	 * 
	 * @return 
	 */
	public String getSuchString() {
		return suchString;
	}

	/**
	 * IDs von Studienfächern, die geladen werden sollen.
	 * 
	 * @return Studienfaecher
	 */
	public Studienfaecher getStudienfaecher() {
		return studienfaecher;
	}
}
