package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hauptunterrichtssprache;

public class UnterrichtsspracheMapperTest {

	@Test
	public void testMapAll() {
		UnterrichtsspracheMapper mapper = new UnterrichtsspracheMapper();
		for (Hauptunterrichtssprache value : Hauptunterrichtssprache.values()) {
			assertNotNull("Alle Unterrichtssprachen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		UnterrichtsspracheMapper mapper = new UnterrichtsspracheMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		UnterrichtsspracheMapper mapper = new UnterrichtsspracheMapper();
		assertEquals("Andere", mapper.map(Hauptunterrichtssprache.ANDERE));
		assertEquals("Chinesisch", mapper.map(Hauptunterrichtssprache.CHINESISCH));
		assertEquals("Dänisch", mapper.map(Hauptunterrichtssprache.DAENISCH));
		assertEquals("Deutsch", mapper.map(Hauptunterrichtssprache.DEUTSCH));
		assertEquals("Englisch", mapper.map(Hauptunterrichtssprache.ENGLISCH));
		assertEquals("Französisch", mapper.map(Hauptunterrichtssprache.FRANZOESISCH));
		assertEquals("Italienisch", mapper.map(Hauptunterrichtssprache.ITALIENISCH));
		assertEquals("keine Angabe", mapper.map(Hauptunterrichtssprache.KEINE_ANGABE));
		assertEquals("Niederländisch", mapper.map(Hauptunterrichtssprache.NIEDERLAENDISCH));
		assertEquals("Polnisch", mapper.map(Hauptunterrichtssprache.POLNISCH));
		assertEquals("Schwedisch", mapper.map(Hauptunterrichtssprache.SCHWEDISCH));
		assertEquals("Slowakisch", mapper.map(Hauptunterrichtssprache.SLOWAKISCH));
		assertEquals("Spanisch", mapper.map(Hauptunterrichtssprache.SPANISCH));
		assertEquals("Tschechisch", mapper.map(Hauptunterrichtssprache.TSCHECHISCH));
	}

}
