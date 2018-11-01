package de.ba.bub.studisu.common.exception;

/**
 * Ausnhame die geworfen wird wenn beim Zugriff auf Content des WCC ein Fehler
 * auftritt der eingegrenzt aber nicht behandelt werden kann.
 */
public class HtmlContentClientException extends Exception {
  
	/**
	 * serializable stuff
	 */
	private static final long serialVersionUID = 4406072678964474037L;
	
	/**
	 * Grund des Fehlers
	 */
	public static enum Reason {
		/**
		 * technisches Problem
		 */
		TECHNICAL,
		/**
		 * fehlender Inhalt
		 */
		NO_CONTENT
	}
	
	/**
	 * Grund des Fehlers.
	 */
	private final Reason reason;

	/**
	 * Konstruktor, der Fehlergrund, Fehlermeldung und zugrundeliegenden Fehler erwartet
	 * @param reason Grund fuer das Auftreten des Fehlers
     * @param message Meldungstext zum aufgetretenen Fehler
     * @param cause zugrundeliegender Fehler
     */
    public HtmlContentClientException(Reason reason, String message, Exception cause) {
        super(message, cause);
        this.reason = reason;
    }
    
    /**
	 * Konstruktor, der Fehlergrund und Fehlermeldung erwartet
	 * @param reason Grund fuer das Auftreten des Fehlers
     * @param message Meldungstext zum aufgetretenen Fehler
     */
    public HtmlContentClientException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    } 
    
	/**
	 * Liefert den Grund der Exception.
	 * 
	 * @return Fehlergrund
	 */
	public Reason getReason() {
		return reason;
	}
    
}
