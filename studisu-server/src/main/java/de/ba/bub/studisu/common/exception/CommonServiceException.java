package de.ba.bub.studisu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * response data exception implementation for studienangebot service exception
 * Created by loutzenhj on 06.04.2017.
 */
@ErrorDataMapping(status = HttpStatus.INTERNAL_SERVER_ERROR, message = "SERVICE_EXCEPTION")
public class CommonServiceException extends ResponseDataException {
    /**
	 * serializable stuff
	 */
	private static final long serialVersionUID = -7929189395422461103L;

	/**
	 * std constructor, der Fehlermeldung erwartet
	 * @param message Meldungstext zum aufgetretenen Fehler
	 */
	public CommonServiceException(String message) {
        super(message);
    }

    /**
     * std constructor, der Fehlermeldung und zugrundeliegenden Fehler erwartet
     * @param message Meldungstext zum aufgetretenen Fehler
     * @param rootCause zugrundeliegender Fehler
     */
    public CommonServiceException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
