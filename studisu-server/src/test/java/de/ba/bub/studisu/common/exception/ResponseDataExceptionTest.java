package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class ResponseDataExceptionTest {

	private String message = "myMessage";
	private String causeMessage = "nix da";
	private Exception cause = new NullPointerException(causeMessage);

	@Test
	public void testResponseDataExceptionString() {
		ResponseDataException instance = new ResponseDataException(message);
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Meldung", message, instance.getMessage());
		assertTrue("pruefe cause null nach Aufruf Konstruktor mit Meldung", instance.getCause() == null);
	}

	@Test
	public void testResponseDataExceptionStringThrowable() {
		ResponseDataException instance = new ResponseDataException(message, cause);
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Meldung und Cause", message, instance.getMessage());
		assertEquals("pruefe cause nach Aufruf Konstruktor mit Meldung und Cause", cause.getClass().getName(), 
				instance.getCause().getClass().getName());
		assertEquals("pruefe cause.getMessage nach Aufruf Konstruktor mit Meldung und Cause", causeMessage, 
				instance.getCause().getMessage());
	}

	@Test
	public void testGetErrorData() {
		CommonServiceException abgeleiteteInstanz = new CommonServiceException(message, cause);
		ErrorData errorData = abgeleiteteInstanz.getErrorData();
		assertEquals("erwartete Annotationsinformation fuer Status bei CommonServiceException", HttpStatus.INTERNAL_SERVER_ERROR,
				errorData.getHttpStatus());
		assertEquals("erwartete Annotationsinformation fuer Fehlermeldung bei CommonServiceException", "SERVICE_EXCEPTION",
				errorData.getMessage());
		assertEquals("erwartete Annotationsinformation fuer Fehlerdetailmeldung", message,
				errorData.getDetailMessage());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetErrorDataNull() {
		ResponseDataException instance = new ResponseDataException(message, cause);
		try {
			instance.getErrorData();
		} catch (IllegalArgumentException ex) {
			String expectedMessage = ResponseDataException.NO_ERRORDATA + ResponseDataException.class.getName();
			assertEquals("Fehlermeldung keine Annotationsinformationen vorliegend erwartet", expectedMessage, ex.getMessage());
			throw ex;
		}
	}
	
	
}
