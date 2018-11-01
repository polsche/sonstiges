package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class ErrorDataTest {

	

	@Test
	public void testGetZusatzinfos() {
		ErrorData instance = new ErrorData();
		assertNull("in Klasse ErrorData immer null erwartet, in abgeleiteten Klassen zu ueberschreiben", instance.getZusatzinfos());
	}

	@Test
	public void testGetLogref() {
		ErrorData instance = new ErrorData();
		String ret = instance.getLogref();
		int size = 5;
		String strPattern = "[a-zA-Z0-9]{" + size + "}";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(ret);
		assertTrue("fuenfstellige Logreferenz aus 5 Zeichen (Buchstaben klein/gross a-z und Zahlen) erwartet", matcher.matches());
	}

	@Test
	public void testGetMessageNull() {
		ErrorData instance = new ErrorData();
		assertNull("erwartet von getMessage null, falls keine Meldung gesetzt wurde", instance.getMessage());
	}

	@Test
	public void testSetAndGetMessage() {
		String message = "wichtige info";
		ErrorData instance = new ErrorData();
		instance.setMessage(message);
		String ret = instance.getMessage();
		assertEquals("erwartet bei getMessage vorher mit setMessage gesetzten Text", message, ret);
	}

	@Test
	public void testGetDetailMessageNull() {
		ErrorData instance = new ErrorData();
		assertNull("erwartet von getDetailMessage null, falls keine Detailmeldung gesetzt wurde", instance.getDetailMessage());
	}
	
	@Test
	public void testSetAndGetDetailMessage() {
		String message = "wichtige detailinfo";
		ErrorData instance = new ErrorData();
		instance.setDetailMessage(message);
		String ret = instance.getDetailMessage();
		assertEquals("erwartet bei getDetailMessage vorher mit setDetailMessage gesetzten Text", message, ret);
	}

	@Test
	public void testGetHttpStatusNull() {
		ErrorData instance = new ErrorData();
		assertNull("erwartet von getHttpStatus null, falls keine Status gesetzt wurde", instance.getHttpStatus());
	}
	
	@Test
	public void testGetAndSetHttpStatus() {
		HttpStatus status = HttpStatus.BAD_GATEWAY;
		ErrorData instance = new ErrorData();
		instance.setHttpStatus(status);
		HttpStatus ret = instance.getHttpStatus();
		assertEquals("erwartet bei getHttpStatus vorher mit setHttpStatus gesetzten Status", status.name(), ret.name());
	}

}
