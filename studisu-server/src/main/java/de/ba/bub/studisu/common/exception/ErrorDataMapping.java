package de.ba.bub.studisu.common.exception;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.HttpStatus;

/**
 * Jobboerse / APOK.<p/>
 * Annotation fuer Fehlerinformationen zu einer Exception
 * @author WinzingeR
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorDataMapping {
    /**
     * gibt den http status code zurueck
     * @return http status code zum Fehler
     */
    HttpStatus status() default HttpStatus.BAD_REQUEST;

    /**
     * gibt reason phrase for the error zurueck
     * @return Begruendung zum Fehler
     */
    String reasonPhrase() default "Internal Server Error";

    /**
     * gibt message for the error zurueck
     * @return Meldung zum Fehler
     */
    String message() default "Interner Anwendungsfehler";
}
