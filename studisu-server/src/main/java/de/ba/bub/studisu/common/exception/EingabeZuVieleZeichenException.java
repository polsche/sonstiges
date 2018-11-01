package de.ba.bub.studisu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Validierungsfehler wegen Eingabe zu vieler Zeichen fuehrt zu BAD_REQUEST (400)
 */
@ErrorDataMapping(status = HttpStatus.BAD_REQUEST, message = "EINGABEPARAMETER_ZU_LANG")
public class EingabeZuVieleZeichenException extends ResponseDataException {
	
	static final String ERRORMESSAGE_START = "Zu viele Zeichen für Parameter '";
	static final String ERRORMESSAGE_END = "'.";

    /**
	 * serializable stuff
	 */
	private static final long serialVersionUID = 4687796886237924950L;

	/**
	 * Constructor, der Parametername als Uebergabeparameter erwartet
	 * @param parameter Name des Parameters, bei dem der Eingabevalidierungsfehler aufgetreten ist
	 */
	public EingabeZuVieleZeichenException(final String parameter) {
        super(ERRORMESSAGE_START + parameter + ERRORMESSAGE_END);
    }

	/**
	 * Constructor, der Parametername und zugrundeliegenden Fehler als Uebergabeparameter erwartet
	 * @param parameter Name des Parameters, bei dem der Eingabevalidierungsfehler aufgetreten ist
	 * @param rootCause zugrundeliegender Fehler
	 */
	public EingabeZuVieleZeichenException(final String parameter, Throwable rootCause) {
        super(ERRORMESSAGE_START + parameter + ERRORMESSAGE_END, rootCause);
    }

}
