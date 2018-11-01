package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

public class StudienortMapperTest {

	private StudienortMapper cut;

	private Veranstaltung veranstaltung;


	@Before
	public void setUp() throws Exception {
		cut = new StudienortMapper();
		veranstaltung = new Veranstaltung();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMapWithNulls() {

		MapperTestUtils.fillWithNulls(veranstaltung);

		AdresseKurz studienort = cut.map(veranstaltung);

		assertNull(studienort.getName());
		assertNull(studienort.getStrasse());
		assertNull(studienort.getPostleitzahl());
		assertNull(studienort.getOrt());
		assertNull(studienort.getBundesland());

	}

	@Test
	public void testMapWithValues() {

		MapperTestUtils.fillWithValues(veranstaltung);

		AdresseKurz studienort = cut.map(veranstaltung);

		assertNull("Wird z.Z. nicht gemappt", studienort.getName());
		assertEquals("strasse", studienort.getStrasse());
		assertEquals("01111", studienort.getPostleitzahl());
		assertEquals("ortsname", studienort.getOrt());
		assertEquals("Mecklenburg-Vorpommern", studienort.getBundesland());
	}
	
	@Test
	public void testMapWithValues2() {

		MapperTestUtils.fillWithValues(veranstaltung);
		
		// Hiermit muss dann das postfach verwendet werden
		veranstaltung.getAdresse().setOrt(null);

		AdresseKurz studienort = cut.map(veranstaltung);

		assertNull("Wird z.Z. nicht gemappt", studienort.getName());
		assertEquals("Bei Postfach-Adresse muss in Straße die Postfach-Nr.", "32311", studienort.getStrasse());
		assertEquals("82178", studienort.getPostleitzahl());
		assertEquals("ortsname2", studienort.getOrt());
		assertEquals("Bayern", studienort.getBundesland());
	}

}
