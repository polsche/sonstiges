package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommonServiceExceptionTest {
	
	private String message = "myMessage";
	private String causeMessage = "nix da";
	private Exception cause = new NullPointerException(causeMessage);

	@Test
	public void testCommonServiceExceptionString() {
		CommonServiceException instance = new CommonServiceException(message);
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Meldung", message, instance.getMessage());
		assertTrue("pruefe cause null nach Aufruf Konstruktor mit Meldung", instance.getCause() == null);
	}

	@Test
	public void testCommonServiceExceptionStringThrowable() {
		CommonServiceException instance = new CommonServiceException(message, cause);
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Meldung und Cause", message, instance.getMessage());
		assertEquals("pruefe cause nach Aufruf Konstruktor mit Meldung und Cause", cause.getClass().getName(), 
				instance.getCause().getClass().getName());
		assertEquals("pruefe cause.getMessage nach Aufruf Konstruktor mit Meldung und Cause", causeMessage, 
				instance.getCause().getMessage());
	}

}
