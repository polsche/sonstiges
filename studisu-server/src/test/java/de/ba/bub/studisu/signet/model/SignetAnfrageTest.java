package de.ba.bub.studisu.signet.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class SignetAnfrageTest {

	private int banid = 26383;
	
	private SignetAnfrage signetAnfrage  = new SignetAnfrage(banid);
	
	@Test
	public void testGetValidationResult() {
		assertTrue("Nur Abdeckung", 1 == signetAnfrage.getValidationResult());
	}

	@Test
	public void testGetBanId() {
		assertTrue("Nur Abdeckung", banid == signetAnfrage.getBanId());
	}
	
	@Test
	public void testSetBanId() {
		signetAnfrage.setBanId(0);
		assertTrue("Nur Abdeckung", 0 == signetAnfrage.getBanId());
	}

}
