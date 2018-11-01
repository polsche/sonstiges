package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class SuchortAbstandTest {

	@Test
	public void testSuchortAbstand() {
		
		String suchort = "suchort";
		Double abstand = 3d;
		
		SuchortAbstand cut = new SuchortAbstand(suchort, abstand);		
		
		assertEquals(suchort, cut.getSuchort());
		assertEquals(abstand, cut.getAbstand());
		
		String suchort2 = "suchort2";
		Double abstand2 = 5d;
		
		cut.setSuchort(suchort2);
		cut.setAbstand(abstand2);
		
		assertEquals(suchort2, cut.getSuchort());
		assertEquals(abstand2, cut.getAbstand());
	}

}
