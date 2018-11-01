package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlusstyp;

public class AbschlusstypMapperTest {

	@Test
	public void testMapAll() {
		AbschlusstypMapper mapper = new AbschlusstypMapper();
		for (Abschlusstyp value : Abschlusstyp.values()) {
			assertNotNull("Alle Abschlusstypen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		AbschlusstypMapper mapper = new AbschlusstypMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		AbschlusstypMapper mapper = new AbschlusstypMapper();
		assertEquals("keine Angabe", mapper.map(Abschlusstyp.KEINE_ANGABE));
		assertEquals("konsekutiv", mapper.map(Abschlusstyp.KONSEKUTIV));
		assertEquals("nicht konsekutiv", mapper.map(Abschlusstyp.NICHT_KONSEKUTIV));
		assertEquals("konsekutiv", mapper.map(Abschlusstyp.WEITERBILDEND));
	}

}
