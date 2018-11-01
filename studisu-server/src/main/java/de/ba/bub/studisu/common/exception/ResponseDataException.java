package de.ba.bub.studisu.common.exception;

/**
 * Parent exception for all exceptions providing error data to response.
 * <p/>
 */
public class ResponseDataException extends RuntimeException {
	
	static final String NO_ERRORDATA = "No Errordata provided for ";

	/**
	 * serializable stuff
	 */
	private static final long serialVersionUID = -4374081495330954104L;

	/**
	 * Constructor, der Fehlermeldung erwartet
	 *
	 * @param message
	 *            Meldungstext zum aufgetretenen Fehler
	 */
	public ResponseDataException(String message) {
		super(message);
	}

	/**
	 * Constructor, der Fehlermeldung und zugrundeliegenden Fehler erwartet
	 * 
	 * @param message
	 *            Meldungstext zum aufgetretenen Fehler
	 * @param rootCause
	 *            zugrundeliegender Fehler
	 */
	public ResponseDataException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	/**
	 * Extract ErrorData from provided @ErrorDataMapping
	 * 
	 * @return Zusatzinformationen zum Fehler (Status, Meldung) aus der
	 *         Annotation @ErrorDataMapping der abgeleiteten Fehlerklasse
	 */
	public ErrorData getErrorData() {
		final ErrorData ed = new ErrorData();
		ed.setDetailMessage(this.getMessage());
		final ErrorDataMapping edm = this.getClass().getAnnotation(ErrorDataMapping.class);
		if (edm != null) {
			ed.setMessage(edm.message());
			ed.setHttpStatus(edm.status());
			return ed;
		}

		throw new IllegalArgumentException(NO_ERRORDATA + this.getClass().getName());
	}
}
