package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class ErrorDataMappingTest {
	
	@ErrorDataMapping
	private class MyClass {
		
	}
	

	@Test
	public void testStatus() {
		HttpStatus retStatus = MyClass.class.getAnnotation(ErrorDataMapping.class).status();
		assertEquals("erwarte Defaultwert fuer httpStatus", HttpStatus.BAD_REQUEST.name(), retStatus.name());
	}

	@Test
	public void testReasonPhrase() {
		String retReasonPhrase = MyClass.class.getAnnotation(ErrorDataMapping.class).reasonPhrase();
		assertEquals("erwarte Defaultwert fuer reasonPhrase", "Internal Server Error", retReasonPhrase);
	}

	@Test
	public void testMessage() {
		String retMessage = MyClass.class.getAnnotation(ErrorDataMapping.class).message();
		assertEquals("erwarte Defaultwert fuer message", "Interner Anwendungsfehler", retMessage);
	}

}
