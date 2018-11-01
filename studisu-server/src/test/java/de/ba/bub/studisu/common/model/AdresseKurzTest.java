package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AdresseKurzTest {

	@Test
	public void testAdresseKurz() {
		
		AdresseKurz cut = new AdresseKurz();
		
		assertNull(cut.getBundesland());
		assertNull(cut.getName());
		assertNull(cut.getOrt());
		assertNull(cut.getPostleitzahl());
		assertNull(cut.getStrasse());
		
		String bundesland = "bundesland";
		String name = "name";
		String ort = "ort";
		String postleitzahl = "postleitzahl";
		String strasse = "strasse";

		cut.setBundesland(bundesland);
		cut.setName(name);
		cut.setOrt(ort);
		cut.setPostleitzahl(postleitzahl);
		cut.setStrasse(strasse);
		
		assertEquals(bundesland, cut.getBundesland());
		assertEquals(name, cut.getName());	
		assertEquals(ort, cut.getOrt());	
		assertEquals(postleitzahl, cut.getPostleitzahl());	
		assertEquals(strasse, cut.getStrasse());	
	}

}
