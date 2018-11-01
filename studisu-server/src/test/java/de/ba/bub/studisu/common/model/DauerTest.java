package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DauerTest {

	@Test
	public void testDauer() {
		
		Dauer cut = new Dauer();
		
		assertNull(cut.getBeginn());
		assertNull(cut.getEnde());
		assertNull(cut.getBemerkung());
		assertNull(cut.getUnterrichtszeiten());
		
		String beginn = "beginn";
		String ende = "ende";
		String bemerkung = "bemerkung";
		String unterrichtszeiten = "unterrichtszeiten";

		cut.setBeginn(beginn);
		cut.setEnde(ende);
		cut.setBemerkung(bemerkung);
		cut.setUnterrichtszeiten(unterrichtszeiten);
		
		assertEquals(beginn, cut.getBeginn());
		assertEquals(ende, cut.getEnde());
		assertEquals(bemerkung, cut.getBemerkung());
		assertEquals(unterrichtszeiten, cut.getUnterrichtszeiten());
	}

}
