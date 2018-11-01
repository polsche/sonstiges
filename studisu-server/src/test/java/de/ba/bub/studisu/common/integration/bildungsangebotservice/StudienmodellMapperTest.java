package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Studienmodell;

public class StudienmodellMapperTest {

	@Test
	public void testMapAll() {
		StudienmodellMapper mapper = new StudienmodellMapper();
		for (Studienmodell value : Studienmodell.values()) {
			assertNotNull("Alle Studienmodelle müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		StudienmodellMapper mapper = new StudienmodellMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		StudienmodellMapper mapper = new StudienmodellMapper();
		assertEquals("ausbildungsbegleitend", mapper.map(Studienmodell.AUSBILDUNGSBEGLEITEND));
		assertEquals("ausbildungsintegrierend", mapper.map(Studienmodell.AUSBILDUNGSINTEGRIEREND));
		assertEquals("berufsbegleitend", mapper.map(Studienmodell.BERUFSBEGLEITEND));
		assertEquals("berufsintegrierend", mapper.map(Studienmodell.BERUFSINTEGRIEREND));
		assertEquals("Duales Studium", mapper.map(Studienmodell.DUALES_STUDIUM));
		assertEquals("keine Angabe", mapper.map(Studienmodell.KEINE_ANGABE));
		assertEquals("praxisintegrierend", mapper.map(Studienmodell.PRAXISINTEGRIEREND));
	}

}
