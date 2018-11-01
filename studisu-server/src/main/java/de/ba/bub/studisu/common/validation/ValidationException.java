package de.ba.bub.studisu.common.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;

import de.ba.bub.studisu.common.exception.ErrorDataMapping;
import de.ba.bub.studisu.common.exception.ResponseDataException;

/**
 * Jobboerse / APOK
 * Created by WinzingeR on 17.11.2015.
 */
@ErrorDataMapping(status = HttpStatus.BAD_REQUEST, message = "EINGABEN_UNVOLLSTAENDIG_ODER_FEHLERHAFT")
public class ValidationException extends ResponseDataException {
    /**
	 * serializable stuff
	 */
	private static final long serialVersionUID = 3567685142895350359L;

	private final transient Set<ConstraintViolation<?>> violations = new HashSet<>();

    /**
     * simple constructor
     * @param message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * another nearly simple constructor
     * @param message
     * @param violations
     */
    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        this(message);
        if (violations != null) {
            this.violations.addAll(violations);
        }
    }
}
