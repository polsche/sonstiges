package de.ba.bub.studisu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Validierungsfehler wegen falscher Eingabe fuehrt zu BAD_REQUEST (400)
 */
@ErrorDataMapping(status = HttpStatus.BAD_REQUEST, message = "EINGABEPARAMETER_FEHLERHAFT")
public class EingabeValidierungException extends ResponseDataException {
	
	static final String ERRORMESSAGE_START = "Ungültiger Wert für Parameter '";
	static final String ERRORMESSAGE_END = "'.";
	

    /**
	 * serializable stuff
	 */
	private static final long serialVersionUID = 4687796886237924950L;

	/**
	 * Constructor, der Parametername als Uebergabeparameter erwartet
	 * @param parameter Name des Parameters, bei dem ein Validierungsfehler aufgetreten ist
	 */
	public EingabeValidierungException(final String parameter) {
        super(ERRORMESSAGE_START + parameter + ERRORMESSAGE_END);
    }

	/**
	 * Constructor, der Parametername und zugrundeliegenden Fehler als Uebergabeparameter erwartet
	 * @param parameter Name des Parameters, bei dem ein Validierungsfehler aufgetreten ist
	 * @param rootCause zugrundeliegender Fehler
	 */
	public EingabeValidierungException(final String parameter, Throwable rootCause) {
        super(ERRORMESSAGE_START + parameter + ERRORMESSAGE_END, rootCause);
    }

}
