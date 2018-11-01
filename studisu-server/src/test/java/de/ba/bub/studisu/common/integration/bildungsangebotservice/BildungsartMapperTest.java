package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bildungsart;

public class BildungsartMapperTest {

	@Test
	public void testMapAll() {
		BildungsartMapper mapper = new BildungsartMapper();
		for (Bildungsart value : Bildungsart.values()) {
			assertNotNull("Alle Bildungsarten müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		BildungsartMapper mapper = new BildungsartMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		BildungsartMapper mapper = new BildungsartMapper();
		assertEquals("Aktivierung/Berufliche Eingliederung",
				mapper.map(Bildungsart.AKTIVIERUNG_BERUFLICHE_EINGLIEDERUNG));
		assertEquals("Allgemeinbildung", mapper.map(Bildungsart.ALLGEMEINBILDUNG));
		assertEquals("Berufliche Grundqualifikation", mapper.map(Bildungsart.BERUFLICHE_GRUNDQUALIFIKATION));
		assertEquals("Berufsausbildung", mapper.map(Bildungsart.BERUFSAUSBILDUNG));
		assertEquals("Fortbildung/Qualifizierung", mapper.map(Bildungsart.FORTBILDUNG_QUALIFIZIERUNG));
		assertEquals("Gesetzlich/gesetzesähnlich geregelte Fortbildung/Qualifizierung",
				mapper.map(Bildungsart.GESETZLICH_GESETZESAEHNLICH_GEREGELTE_FORTBILDUNG_QUALIFIZIERUNG));
		assertEquals("Integrationskurs", mapper.map(Bildungsart.INTEGRATIONSKURS));
		assertEquals("Keine Zuordnung möglich", mapper.map(Bildungsart.KEINE_ZUORDNUNG_MOEGLICH));
		assertEquals("Nachholen des Berufsabschlusses", mapper.map(Bildungsart.NACHHOLEN_DES_BERUFSABSCHLUSSES));
		assertEquals("Rehabilitation", mapper.map(Bildungsart.REHABILITATION));
		assertEquals("Studienangebot - grundständig", mapper.map(Bildungsart.STUDIENANGEBOT_GRUNDSTAENDIG));
		assertEquals("Studienangebot - weiterführend", mapper.map(Bildungsart.STUDIENANGEBOT_WEITERFUEHREND));
		assertEquals("Umschulung", mapper.map(Bildungsart.UMSCHULUNG));
	}
}
