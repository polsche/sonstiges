package de.ba.bub.studisu.common.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.exception.ErrorData;
import de.ba.bub.studisu.common.exception.ResponseDataException;

/**
 * Basis Controller mit gemeinsamer Logik (insb. bzgl. Exception-Handling) für
 * alle konkreten Controller.
 * <p>
 * Keine abstrakte Klasse, da keine abstrakten Methoden enhalten.
 *
 * @author StraubP, KunzmannC
 */
public class StudisuController {

    // Konstanten fuer alle URL Parameter des Backends
    public static final String URL_PARAM_STUDIENFAECHER = "sfa";
    public static final String URL_PARAM_STUDIENFELDER = "sfe";
    public static final String URL_PARAM_STUDIENFORM = "sfo";
    public static final String URL_PARAM_ORT = "ort";
    public static final String URL_PARAM_STUDIENTYP = "st";
    public static final String URL_PARAM_FITFUERSTUDIUM = "ffst";
    public static final String URL_PARAM_HOCHSCHULART = "hsa";
    public static final String URL_PARAM_UMKREIS = "uk";
    public static final String URL_PARAM_PAGE = "pg";
    public static final String URL_PARAM_RELOAD = "reload";
    public static final String URL_PARAM_REGION = "re";

    public static final String URL_PARAM_STUDIENANGEBOT_ID = "stangid";

    public static final String URL_PARAM_ORTSUCHE = "ortsuche";
    public static final String URL_PARAM_ORTSUCHE_ZU_LANG = "ortsuche-zu-lang";

    public static final String URL_PARAM_STUDIENFACH = "sfa";
    public static final String URL_PARAM_STUDIENFACH_IDS = "sfa-ids";
    public static final String URL_PARAM_STUDIENFACH_SUCHWORT = "sfa-sw";
    public static final String URL_PARAM_STUDIENFACH_SUCHWORT_ZU_LANG = "sfa-sw-zu-lang";

    public static final String URL_PARAM_SIGNET_BANID = "banid";

    /**
     * Simple-Class-Name der ClientAbortException.
     */
    private static final String CLIENT_ABORT_EXCEPTION = "ClientAbortException";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StudisuController.class);

    /**
     * Identifiziert, um welche Art des zu cachenden Content es sich handelt;
     * daraus resultiert die Caching-Dauer. Seltende Änderung am Content =
     * STATIC_CONTENT, z.B. Studienfeld-Informationen = 6h-Cache Häufige
     * Änderung am Content = DYNAMIC_CONTENT, z.B. Studienangebote = 6h-Cache
     */
    public static enum HTTP_CACHING_TYPE {
        STATIC_CONTENT, DYNAMIC_CONTENT
    }

    @Value("${studisu.caching.http.studienangebote}")
    private String studienangeboteCachingTime;

    @Value("${studisu.caching.http.static-content}")
    private String staticContentCachingTime;

    /**
     * StudienangebotServiceException implemented by superclass
     * ResponseDataException exception handler fired when required params are
     * missing
     *
     * @param e Die zu behandelnde Exception
     * @return Die aus der Exception generierte und an den Client zu sendende
     * Antwort.
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorData> behandleMissingParamFehler(final MissingServletRequestParameterException e) {
        return behandleValidierungsFehler(new EingabeValidierungException(e.getMessage()));
    }

    /**
     * StudienangebotServiceException implemented by superclass
     * ResponseDataException exception handler fired when required params are
     * missing
     *
     * @param e Die zu behandelnde Exception
     * @return Die aus der Exception generierte und an den Client zu sendende
     * Antwort.
     */
    @ExceptionHandler({ResponseDataException.class})
    public ResponseEntity<ErrorData> behandleFehler(final ResponseDataException e) {
        return behandleValidierungsFehler(e);
    }

    /**
     * Exception-Handler fuer Fehler aus der Datenkonvertierung von
     * Request-Parametern.
     *
     * @param e Die zu behandelnde Exception
     * @return Die aus der Exception generierte und an den Client zu sendende
     * Antwort.
     */
    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<ErrorData> behandleConversionFehler(final ConversionFailedException e) {
        final EingabeValidierungException eve = new EingabeValidierungException(e.getMessage());
        return behandleValidierungsFehler(eve);
    }

    /**
     * Exception-Handler fuer alle nicht anderweitig behandelten Fehler
     * ("catch-all").
     *
     * @param e Die zu behandelnde Exception
     * @return Die aus der Exception generierte und an den Client zu sendende
     * Antwort.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorData> behandleFehler(final Exception e) {
        final CommonServiceException sse = new CommonServiceException(e.getMessage(), e);
        return behandleValidierungsFehler(sse, true);
    }

    /**
     * Exception-Handler fuer Fehler in den Antwortdaten.
     *
     * @param e Die zu behandelnde Exception
     * @return Die aus der Exception generierte und an den Client zu sendende
     * Antwort.
     */
    ResponseEntity<ErrorData> behandleValidierungsFehler(final ResponseDataException e) {
        return behandleValidierungsFehler(e, false);
    }

    /**
     * Exception-Handler fuer Fehler in den Antwortdaten.
     *
     * @param e       Die zu behandelnde Exception
     * @param asError Flag, ob Fehler als ERROR statt WARN geloggt werden soll
     * @return Die aus der Exception generierte und an den Client zu sendende
     * Antwort.
     */
    ResponseEntity<ErrorData> behandleValidierungsFehler(final ResponseDataException e, boolean asError) {
        final ErrorData ed = e.getErrorData();
        if (isClientAbortException(e)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(this.erzeugeFehlertext(ed), e);
            } else {
                LOGGER.info(e.getMessage()); // in diesem Fall wollen wir keinen
                // Stacktrace
            }
        } else {
            if (asError) {
                LOGGER.error(this.erzeugeFehlertext(ed), e);
            } else {
                LOGGER.warn(this.erzeugeFehlertext(ed), e);
            }
        }

        /**
         * TODO CSP/MPL Die detaillierte Message gibt aktuell zu viel interne
         * Daten weiter. Beispiel: Failed to convert value of type
         * [java.lang.String] to required type
         * [de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher]...
         * Diese Daten sollten entsprechend nicht weitergegeben
         * werden.03.08.2017
         */
        return ResponseEntity.status(ed.getHttpStatus()).body(ed);
    }

    /**
     * erzeugt Fehlermeldungstext mit Logreferenz
     *
     * @param errorData Fehlerdatenobjekt mit Logreferenz
     * @return Fehlermeldungstext mit Logreferenz
     */
    String erzeugeFehlertext(ErrorData errorData) {
        String fehlertext = "[" + errorData.getLogref() + "] Fehler";
        return fehlertext;
    }

    /**
     * Prüft, ober der Fehler eine ClientAbortException ist.
     *
     * @param e Exception
     * @return true oder false
     */
    boolean isClientAbortException(final ResponseDataException e) {
        // Wir gehen von einer ClientAbortException aus, wenn die übergebene
        // Exception selbst oder ihre Cause eine ClientAbortException ist.
        // Leider können wir nicht per instanceof direkt auf die Klasse prüfen,
        // da die Klasse in keiner direkt vorhandenen Lib enthalten ist.
        if (CLIENT_ABORT_EXCEPTION.equals(e.getClass().getSimpleName())) {
            return true;
        }

        return (e.getCause() != null && CLIENT_ABORT_EXCEPTION.equals(e.getCause().getClass().getSimpleName()));
    }

    /**
     * Setzt die http-Caching-Header in die Response. Die Zeit ist abhängig von
     * dem übergebenen Parameter cachingType; werte sind momentan für statischen
     * Content 3h, dynamische (wie Studienangebote) 3h.
     *
     * @param response    mit caching-Headern gesetzte http-Response
     * @param cachingType dynamisch oder statisch; je nach Typ werden andere Werte für
     *                    die Caching-Dauer geschrieben
     */
    public void setHttpCacheHeader(HttpServletResponse response, HTTP_CACHING_TYPE cachingType) {

        String time = HTTP_CACHING_TYPE.STATIC_CONTENT.equals(cachingType) ? this.staticContentCachingTime
                : this.studienangeboteCachingTime;
        // public == jeder Cache kann cachen, d.h. Proxy-Server und
        // Benutzer-Browser
        // max-age == Sekunden, für die die Anfrage gespeichert werden soll
        response.setHeader("Cache-Control", "public,max-age=" + time);

    }
}
