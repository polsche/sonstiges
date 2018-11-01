package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BeschreibungsZustandTest {

	@Test
	public void testIsValid() {
		assertTrue(BeschreibungsZustand.BESCHRIEBEN.isValid());
		assertFalse(BeschreibungsZustand.NICHT_BESCHRIEBEN.isValid());
		assertFalse(BeschreibungsZustand.ARCHIVIERT.isValid());
		assertFalse(BeschreibungsZustand.KEINE_RELEVANZ.isValid());
	}

	@Test
	public void testFromValue() {
		assertEquals(BeschreibungsZustand.BESCHRIEBEN, BeschreibungsZustand.fromValue("J"));
		assertEquals(BeschreibungsZustand.NICHT_BESCHRIEBEN, BeschreibungsZustand.fromValue("N"));
		assertEquals(BeschreibungsZustand.ARCHIVIERT, BeschreibungsZustand.fromValue("A"));
		assertEquals(BeschreibungsZustand.KEINE_RELEVANZ, BeschreibungsZustand.fromValue("-"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromValueFail() {
		BeschreibungsZustand.fromValue("j"); // die Werte sind Case-Sesitive, darum findendet j nichts! 
	
	}

}
