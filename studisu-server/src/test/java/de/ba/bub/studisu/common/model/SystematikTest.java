package de.ba.bub.studisu.common.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests für {@link Systematik}.
 * 
 * @author StraubP
 */
public class SystematikTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullCodeNummer() {
		// null wurde auch als illegal argument eingestuft, cku 29.12.17
		new Systematik(0, null, "HA", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyCodeNummer() {
		new Systematik(0, "", "HA", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongCodeNummer() {
		new Systematik(0, "foo", "HA", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
	}
	
	@Test
	public void testGetters() {
		Systematik systematik = new Systematik(1, "HA 12", "HA", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
		
		assertEquals(1, systematik.getId());
		assertEquals("HA 12", systematik.getCodenr());
		assertEquals("HA", systematik.getObercodenr());
		assertEquals("kurzBezeichnungNeutral", systematik.getKurzBezeichnungNeutral());
		assertEquals("bezeichnungNeutral", systematik.getBezeichnungNeutral());
		assertEquals(SystematikZustand.SYSTEMATIKPOSITION, systematik.getZustand());
	}
	
	@Test
	public void testZustandValues() {
		Systematik systematik = new Systematik(1, "HA 12", "HA", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
		
		assertEquals("S", systematik.getZustand().value());
	}
	
	@Test
	public void testFromValue() {
		SystematikZustand zustand = null;
		zustand = SystematikZustand.fromValue("S");
		assertEquals(zustand, SystematikZustand.SYSTEMATIKPOSITION);
	}
}
