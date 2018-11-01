package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungsmodus;

public class ZulassungsmodusMapperTest {

	@Test
	public void testMapAll() {
		ZulassungsmodusMapper mapper = new ZulassungsmodusMapper();
		for (Zulassungsmodus value : Zulassungsmodus.values()) {
			assertNotNull("Alle Zulassungsmodus müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		ZulassungsmodusMapper mapper = new ZulassungsmodusMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		ZulassungsmodusMapper mapper = new ZulassungsmodusMapper();
		assertEquals("Auswahlverfahren/Eignungsprüfung", mapper.map(Zulassungsmodus.AUSWAHLVERFAHREN_EIGNUNGSPRUEFUNG));
		assertEquals("Bundesweite Zulassungsbeschränkung",
				mapper.map(Zulassungsmodus.BUNDESWEITE_ZULASSUNGSBESCHRAENKUNG));
		assertEquals("Keine Zulassung von Studienanfängern",
				mapper.map(Zulassungsmodus.KEINE_ZULASSUNG_VON_STUDIENANFAENGERN));
		assertEquals("Keine Zulassungsbeschränkung", mapper.map(Zulassungsmodus.KEINE_ZULASSUNGSBESCHRAENKUNG));
		assertEquals("Örtliche Zulassungsbeschränkung", mapper.map(Zulassungsmodus.OERTLICHE_ZULASSUNGSBESCHRAENKUNG));
	}

}
