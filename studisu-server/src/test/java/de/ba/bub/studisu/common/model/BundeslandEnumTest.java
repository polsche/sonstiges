package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BundeslandEnumTest {

	@Test
	public void testValue() {
		assertEquals("Baden-Württemberg", BundeslandEnum.BADEN_WUERTTEMBERG.value());
		assertEquals("Bayern", BundeslandEnum.BAYERN.value());
		assertEquals("Berlin", BundeslandEnum.BERLIN.value());
		assertEquals("Brandenburg", BundeslandEnum.BRANDENBURG.value());
		assertEquals("Bremen", BundeslandEnum.BREMEN.value());
		assertEquals("Hamburg", BundeslandEnum.HAMBURG.value());
		assertEquals("Hessen", BundeslandEnum.HESSEN.value());
		assertEquals("Mecklenburg-Vorpommern", BundeslandEnum.MECKLENBURG_VORPOMMERN.value());
		assertEquals("Niedersachsen", BundeslandEnum.NIEDERSACHSEN.value());
		assertEquals("Nordrhein-Westfalen", BundeslandEnum.NORDRHEIN_WESTFALEN.value());
		assertEquals("Rheinland-Pfalz", BundeslandEnum.RHEINLAND_PFALZ.value());
		assertEquals("Saarland", BundeslandEnum.SAARLAND.value());
		assertEquals("Sachsen", BundeslandEnum.SACHSEN.value());
		assertEquals("Sachsen-Anhalt", BundeslandEnum.SACHSEN_ANHALT.value());
		assertEquals("Schleswig-Holstein", BundeslandEnum.SCHLESWIG_HOLSTEIN.value());
		assertEquals("Thüringen", BundeslandEnum.THUERINGEN.value());
	}

	@Test
	public void testFromValue() {
		for (BundeslandEnum bl : BundeslandEnum.values()) {
			assertEquals(bl, BundeslandEnum.fromValue(bl.value()));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromValueFail() {
		BundeslandEnum.fromValue("bayern"); // Methode ist case-sensitiv!
	}

}
