package de.ba.bub.studisu.common.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Fehlerdaten zur Konventionskonformen Ausgabe im Log und Rueckgabe im JSON Body 
 */
public class ErrorData {
    private String logref;
    private String message;
    private String detailMessage;
    private HttpStatus httpStatus;

    /**
     * Constructor, der eine Logreferenz (Pseude-UUID aus 5 Zeichen (Buchstaben a-z klein/gross und Ziffern) erzeugt
     */
    public ErrorData() {
        this.logref = createLogref();
    }

    /**
     * create log ref unique id used for logging
     * @return erzeugt Logreferenz (Pseude-UUID aus 5 Zeichen (Buchstaben a-z klein/gross und Ziffern)
     */
    private static String createLogref() {
        return PseudoUUID.create(5);
    }

    /**
     * Von Subclasses zu implementieren, um in einer HTML-Response für die zusätzlichen Attribute Output zu erzeugen.
     * (Das Mustache-Template kann per {{zusatzInfos}} diesen String mit hineinrendern.)
     * @return String-Repräsentation etwaiger Zusatzattribute
     */
    @JsonIgnore
    public String getZusatzinfos() {
        return null;
    }

    /**
     * gibt die bei Konstruktoraufruf generierte Logreferenz zurueck
     * @return generierte Logreferenz
     */
    public String getLogref() {
        return logref;
    }


    /**
     * gibt die gesetzte Meldung zurueck
     * @return gesetzte Meldung
     */
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * gibt die gesetzte Detailmeldung zurueck
     * @return gesetzte Detailmeldung
     */
    public String getDetailMessage() {
        return detailMessage;
    }
    
    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    /**
     * gibt den gesetzten Http-Status zurueck
     * @return gesetzter Http-Status
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    
}
