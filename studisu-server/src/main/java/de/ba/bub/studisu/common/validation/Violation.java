package de.ba.bub.studisu.common.validation;

/**
 * Klasse fuer eine Validierungsverletzung bei Ueberpruefung eines Bean-Attributs
 */
public class Violation {
	
    private final  String path;
    private final String message;

    /**
     * Constructor, der Pfad und Meldungstext erwartet
     * @param Pfad der Beanklassen-Property, bei der eine Validierungsverletzung auftritt
     * @param message Meldung zur Validierungsverletzung
     */
    public Violation(String path, String message) {
        this.path = path;
        this.message = message;
    }

    /**
     * gibt den Pfad der Beanklassen-Property zurueck, bei der eine Validierungsverletzung auftritt
     * @return Pfad der Beanklassen-Property, bei der eine Validierungsverletzung auftritt
     */
    public String getPath() {
        return path;
    }

    /**
     * gibt Meldung zur Validierungsverletzung zurueck
     * @return Meldung zur Validierungsverletzung
     */
    public String getMessage() {
        return message;
    }

    /**
     * String-Repraesentation, die die aktuelle Klasseninstanz beschreibt
     */
    @Override
    public String toString() {
        return "{" +
                "path='" + path + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}