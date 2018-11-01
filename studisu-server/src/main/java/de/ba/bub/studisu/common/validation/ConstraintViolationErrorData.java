package de.ba.bub.studisu.common.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;

import de.ba.bub.studisu.common.exception.ErrorData;

/**
 * errordata erweiterung fuer ConstraintViolations
 */
public class ConstraintViolationErrorData extends ErrorData {
    private final List<Violation> violations = new ArrayList<>();

    /**
     * @param violations
     */
    public ConstraintViolationErrorData(final Set<? extends ConstraintViolation<?>> violations) {
        if (violations != null && !violations.isEmpty()) {
            /*this.violations.addAll(
                    violations.stream()
                    .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(toList()));*/
            //Java 7 backport
            for (ConstraintViolation<?> violation : violations) {
            	Violation newViolation = new Violation(violation.getPropertyPath().toString(), violation.getMessage());
            	this.violations.add(newViolation);
            }

        }

        setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    /**
     * violations getter
     * @return
     */
    public List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    @Override
    public String getZusatzinfos() {
        return "Violations: " + violations.toString();
    }
}
