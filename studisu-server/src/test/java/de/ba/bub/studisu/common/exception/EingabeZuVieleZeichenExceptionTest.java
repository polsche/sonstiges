package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class EingabeZuVieleZeichenExceptionTest {

	
	private String parameter = "meinFeld";
	private String causeMessage = "nix da";
	private Exception cause = new NullPointerException(causeMessage);

	@Test
	public void testEingabeZuVieleZeichenExceptionString() {
		EingabeZuVieleZeichenException instance = new EingabeZuVieleZeichenException(parameter);
		String expectedMessage = EingabeZuVieleZeichenException.ERRORMESSAGE_START + parameter
				+ EingabeZuVieleZeichenException.ERRORMESSAGE_END;
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Parameter", expectedMessage, instance.getMessage());
		assertTrue("pruefe cause null nach Aufruf Konstruktor mit Parameter", instance.getCause() == null);
	}

	@Test
	public void testEingabeZuVieleZeichenExceptionStringThrowable() {
		EingabeZuVieleZeichenException instance = new EingabeZuVieleZeichenException(parameter, cause);
		String expectedMessage = EingabeZuVieleZeichenException.ERRORMESSAGE_START + parameter
				+ EingabeZuVieleZeichenException.ERRORMESSAGE_END;
		assertEquals("pruefe getMessage nach Aufruf Konstruktor mit Parameter und Cause", expectedMessage,
				instance.getMessage());
		assertEquals("pruefe cause nach Aufruf Konstruktor mit Parameter und Cause", cause.getClass().getName(),
				instance.getCause().getClass().getName());
		assertEquals("pruefe cause.getMessage nach Aufruf Konstruktor mit Meldung und Cause", causeMessage,
				instance.getCause().getMessage());
	}

}
