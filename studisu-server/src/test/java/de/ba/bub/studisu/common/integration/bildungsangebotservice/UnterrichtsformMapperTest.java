package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;

public class UnterrichtsformMapperTest {

	@Test
	public void testMapAll() {
		UnterrichtsformMapper mapper = new UnterrichtsformMapper();
		for (Unterrichtsform value : Unterrichtsform.values()) {
			assertNotNull("Alle Unterrichtsformen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		UnterrichtsformMapper mapper = new UnterrichtsformMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		UnterrichtsformMapper mapper = new UnterrichtsformMapper();
		assertEquals("Auf Anfrage", mapper.map(Unterrichtsform.AUF_ANFRAGE));
		assertEquals("Blockunterricht", mapper.map(Unterrichtsform.BLOCKUNTERRICHT));
		assertEquals("Einzelmaßnahme (Einzelbetreuung)", mapper.map(Unterrichtsform.EINZELMASSNAHME_EINZELBETREUUNG));
		assertEquals("Fernunterricht/ Fernstudium", mapper.map(Unterrichtsform.FERNUNTERRICHT_FERNSTUDIUM));
		assertEquals("Gruppenmaßnahme", mapper.map(Unterrichtsform.GRUPPENMASSNAHME));
		assertEquals("Inhouse-/ Firmenseminar", mapper.map(Unterrichtsform.INHOUSE_FIRMENSEMINAR));
		assertEquals("Selbststudium/ E-learning/ Blended Learning",
				mapper.map(Unterrichtsform.SELBSTSTUDIUM_E_LEARNING_BLENDED_LEARNING));
		assertEquals("Teilzeit", mapper.map(Unterrichtsform.TEILZEIT));
		assertEquals("Vollzeit", mapper.map(Unterrichtsform.VOLLZEIT));
		assertEquals("Wochenendveranstaltung", mapper.map(Unterrichtsform.WOCHENENDVERANSTALTUNG));
	}

}
