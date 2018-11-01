package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Akkreditierung;

public class AkkreditierungsMapperTest {

	@Test
	public void testMapAll() {
		AkkreditierungsMapper mapper = new AkkreditierungsMapper();
		for (Akkreditierung value : Akkreditierung.values()) {
			assertNotNull("Alle Akkreditierungen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		AkkreditierungsMapper mapper = new AkkreditierungsMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		AkkreditierungsMapper mapper = new AkkreditierungsMapper();
		assertEquals("Erstakkreditierung", mapper.map(Akkreditierung.ERSTAKKREDITIERUNG));
		assertEquals("keine Angabe", mapper.map(Akkreditierung.KEINE_ANGABE));
		assertEquals("Reakkreditierung", mapper.map(Akkreditierung.REAKKREDITIERUNG));
	}

}
