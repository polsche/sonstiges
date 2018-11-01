package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlussgrad;

public class AbschlussgradMapperTest {

	@Test
	public void testMapAll() {
		AbschlussgradMapper mapper = new AbschlussgradMapper();
		for (Abschlussgrad value : Abschlussgrad.values()) {
			assertNotNull("Alle Abschlussgrade müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		AbschlussgradMapper mapper = new AbschlussgradMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		AbschlussgradMapper mapper = new AbschlussgradMapper();
		assertEquals("Abschlussprüfung", mapper.map(Abschlussgrad.ABSCHLUSSPRUEFUNG));
		assertEquals("Bachelor/Bakkalaureus", mapper.map(Abschlussgrad.BACHELOR_BAKKALAUREUS));
		assertEquals("Diplom", mapper.map(Abschlussgrad.DIPLOM));
		assertEquals("Diplom (FH)", mapper.map(Abschlussgrad.DIPLOM_FH));
		assertEquals("Fakultätsexamen", mapper.map(Abschlussgrad.FAKULTAETSEXAMEN));
		assertEquals("keine Angabe", mapper.map(Abschlussgrad.KEINE_ANGABE));
		assertEquals("Kirchlicher Abschluss", mapper.map(Abschlussgrad.KIRCHLICHER_ABSCHLUSS));
		assertEquals("Konzertexamen", mapper.map(Abschlussgrad.KONZERTEXAMEN));
		assertEquals("Lizentiatenprüfung", mapper.map(Abschlussgrad.LIZENTIATENPRUEFUNG));
		assertEquals("Magister", mapper.map(Abschlussgrad.MAGISTER));
		assertEquals("Magister der Theologie", mapper.map(Abschlussgrad.MAGISTER_DER_THEOLOGIE));
		assertEquals("Magister/Master", mapper.map(Abschlussgrad.MAGISTER_MASTER));
		assertEquals("Magister (Masterstudiengang)", mapper.map(Abschlussgrad.MAGISTER_MASTERSTUDIENGANG));
		assertEquals("Master", mapper.map(Abschlussgrad.MASTER));
		assertEquals("Promotion", mapper.map(Abschlussgrad.PROMOTION));
		assertEquals("Staatsexamen", mapper.map(Abschlussgrad.STAATSEXAMEN));
		assertEquals("Theologische Prüfung", mapper.map(Abschlussgrad.THEOLOGISCHE_PRUEFUNG));
	}

}
