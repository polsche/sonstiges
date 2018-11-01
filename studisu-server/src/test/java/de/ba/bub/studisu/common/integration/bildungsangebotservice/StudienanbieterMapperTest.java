package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;

public class StudienanbieterMapperTest {

	private StudienanbieterMapper cut;

	private Bildungsanbieter bildungsanbieter;


	@Before
	public void setUp() throws Exception {
		cut = new StudienanbieterMapper();
		bildungsanbieter = new Bildungsanbieter();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMapWithNulls() {

		MapperTestUtils.fillWithNulls(bildungsanbieter);

		AdresseKurz studienanbieter = cut.map(bildungsanbieter);

		assertNull(studienanbieter.getName());
		assertNull(studienanbieter.getStrasse());
		assertNull(studienanbieter.getPostleitzahl());
		assertNull(studienanbieter.getOrt());
		assertNull(studienanbieter.getBundesland());

	}

	@Test
	public void testMapWithValues() {

		MapperTestUtils.fillWithValues(bildungsanbieter);

		AdresseKurz studienanbieter = cut.map(bildungsanbieter);

		assertEquals("name34", studienanbieter.getName());
		assertEquals("strasse2", studienanbieter.getStrasse());
		assertEquals("03333", studienanbieter.getPostleitzahl());
		assertEquals("ortsname3", studienanbieter.getOrt());
		assertNull("Bundesland wird z.Z. noch ignoriert", studienanbieter.getBundesland());
	}

}
