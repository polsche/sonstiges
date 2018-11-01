package de.ba.bub.studisu.common.validation;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.mockito.Mockito;

public class ValidationExceptionTest {

	private String message = "myMessage";

	@Test
	public void testValidationExceptionString() {
		ValidationException instance = new ValidationException(message);
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Meldung", message, instance.getMessage());
		assertTrue("pruefe cause null nach Aufruf Konstruktor mit Meldung", instance.getCause() == null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testValidationExceptionStringConstraintViolations() {
		Set violations = new HashSet();
		violations.add(Mockito.mock(ConstraintViolation.class));
		ValidationException instance = new ValidationException(message, violations);
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Meldung und Cause", message, instance.getMessage());
		
	}
}