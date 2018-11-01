package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zugangsbedingung;

public class ZugangsbedingungMapperTest {

	@Test
	public void testMapAll() {
		ZugangsbedingungMapper mapper = new ZugangsbedingungMapper();
		for (Zugangsbedingung value : Zugangsbedingung.values()) {
			assertNotNull("Alle Zugangsbedingungen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		ZugangsbedingungMapper mapper = new ZugangsbedingungMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		ZugangsbedingungMapper mapper = new ZugangsbedingungMapper();
		assertEquals(
				"Mehrjährige Berufsausbildung und/oder Berufserfahrung in einem Berufsfeld mit fachlicher Nähe zum Studienfach",
				mapper.map(
						Zugangsbedingung.MEHRJAEHRIGE_BERUFSAUSBILDUNG_UND_ODER_BERUFSERFAHRUNG_IN_EINEM_BERUFSFELD_MIT_FACHLICHER_NAEHE_ZUM_STUDIENFACH));
		assertEquals(
				"Mehrjährige Berufsausbildung und/oder Berufserfahrung in einem Berufsfeld ohne fachliche Nähe zum Studienfach",
				mapper.map(
						Zugangsbedingung.MEHRJAEHRIGE_BERUFSAUSBILDUNG_UND_ODER_BERUFSERFAHRUNG_IN_EINEM_BERUFSFELD_OHNE_FACHLICHE_NAEHE_ZUM_STUDIENFACH));
		assertEquals("Meisterprüfung oder gleichwertige Aufstiegsfortbildung",
				mapper.map(Zugangsbedingung.MEISTERPRUEFUNG_ODER_GLEICHWERTIGE_AUFSTIEGSFORTBILDUNG));
	}

}
