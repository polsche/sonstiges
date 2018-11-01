package de.ba.bub.studisu.studienangebote.model;

import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;

/**
 * validator, der nach der spring validierung von z.b. required params
 * weiter logik pruefen kann.
 * Hier z.B. dass studienfeld oder studienfach gefuellt sein muss
 * falls beides leer ist, ist die Eingabe invalid.
 *
 * following the established pattern from ortsuche and studienfachsuche
 * @author KunzmannC
 *
 */
public class StudienangeboteValidator{

	/**
	 * Ungueltige Eingabe.
	 */
	public static final int INVALID = 0;

	/**
	 * Gueltige Eingabe.
	 */
	public static final int VALID = 1;

	/**
	 * Ergebnis der Validierung.
	 */
	private int result = INVALID;

	/**
	 * Konstante fuer Fehlermeldung
	 */
	public static final String MISSING_PARAM_MESSAGE_S_AND_S = "sfa or sfe has to be set";
	
	/**
	 * constructor evaluating the result
	 * 
	 * at least one has to be not empty to be valid
	 * 
	 * @param studienfelder
	 * @param studienfaecher
	 */
	public StudienangeboteValidator(Studienfelder studienfelder, Studienfaecher studienfaecher){
		boolean studienfelderValid = 
				studienfelder != null 
				&& studienfelder.getStudienfelderIds() != null
				&& !studienfelder.getStudienfelderIds().isEmpty();

		boolean studienfaecherValid = 
				studienfaecher != null
				&& studienfaecher.getStudienfaecherIds() != null
				&& !studienfaecher.getStudienfaecherIds().isEmpty();
		
		if (   studienfelderValid
			|| studienfaecherValid){
			this.result = VALID;
		}
	}

	/**
	 * Validation result INVALID or VALID
	 *
	 * @return validation result
	 */
	public int getResult() {
		return result;
	}
}