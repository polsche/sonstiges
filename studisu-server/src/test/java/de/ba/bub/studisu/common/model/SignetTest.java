package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests für {@link Signet}.
 * 
 * Prueft das Signet Model. Getter/Setter
 * Bisher fuer die Abdeckung
 * 
 * @author CSP
 *
 */

public class SignetTest {

	private String mimetype = "text";
	
	private Signet signet  = new Signet();
	

	@Test
	public void testGetMimetype() {
		signet.setMimetype("text");
		assertTrue("Nur Abdeckung", mimetype.equals(signet.getMimetype()));
	}
	
	@Test
	public void testSetMimetype() {
		signet.setMimetype("css");
		assertTrue("Nur Abdeckung", "css".equals(signet.getMimetype()));
	}

}
