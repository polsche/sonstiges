package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hochschultyp;

public class HochschultypMapperTest {

	@Test
	public void testMapAll() {
		HochschultypMapper mapper = new HochschultypMapper();
		for (Hochschultyp value : Hochschultyp.values()) {
			assertNotNull("Alle Hochschultypen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		HochschultypMapper mapper = new HochschultypMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		HochschultypMapper mapper = new HochschultypMapper();
		assertEquals("Berufsakademie / Duale Hochschule", mapper.map(Hochschultyp.BERUFSAKADEMIE_DUALE_HOCHSCHULE));
		assertEquals("Fachhochschule / Hochschule für angewandte Wissenschaften",
				mapper.map(Hochschultyp.FACHHOCHSCHULE_HOCHSCHULE_FUER_ANGEWANDTE_WISSENSCHAFTEN));
		assertEquals("Hochschule eigenen Typs", mapper.map(Hochschultyp.HOCHSCHULE_EIGENEN_TYPS));
		assertEquals("Kirchliche Hochschule", mapper.map(Hochschultyp.KIRCHLICHE_HOCHSCHULE));
		assertEquals("Kunst- und Musikhochschule", mapper.map(Hochschultyp.KUNST_UND_MUSIKHOCHSCHULE));
		assertEquals("Private Hochschule", mapper.map(Hochschultyp.PRIVATE_HOCHSCHULE));
		assertEquals("Universität", mapper.map(Hochschultyp.UNIVERSITAET));
		assertEquals("Verwaltungshochschule", mapper.map(Hochschultyp.VERWALTUNGSHOCHSCHULE));
	}

}
