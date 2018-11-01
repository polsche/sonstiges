package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


import de.ba.bub.studisu.test.TestUtils;

public class PseudoUUIDTest {

	@Test
	public void testCreateSize0() {
		int size = 0;
		String ret = PseudoUUID.create(size);
		assertTrue("Aufruf testCreateSize mit Wert 0 erwartet Leerstring", ret.isEmpty());
	}
	
	@Test
	public void testCreateSize100() {
		int size = 100;
		String ret = PseudoUUID.create(size);
		String strPattern = "[a-zA-Z0-9]{" + size + "}";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(ret);
		assertTrue("Aufruf testCreateSize mit Wert 100 erwartet nur Buchstaben klein/gross a-z und Zahlen", matcher.matches());
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException,
        InvocationTargetException {
    new TestUtils().callUtilityClassConstructor(PseudoUUID.class);
}
}
