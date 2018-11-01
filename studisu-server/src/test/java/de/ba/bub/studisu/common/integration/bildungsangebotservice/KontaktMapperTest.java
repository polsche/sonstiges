package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.model.Kontakt;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;

@RunWith(MockitoJUnitRunner.Silent.class)
public class KontaktMapperTest {

	private KontaktMapper cut;

	private Bildungsanbieter bildungsanbieter;

	@Before
	public void setUp() throws Exception {
		cut = new KontaktMapper();
		bildungsanbieter = new Bildungsanbieter();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMapWithNulls() {

		MapperTestUtils.fillWithNulls(bildungsanbieter);

		Kontakt kontakt = cut.map(bildungsanbieter);

		assertNull(kontakt.getEmail());
		assertNull(kontakt.getInternet());
		assertNull(kontakt.getTelefaxVorwahl());
		assertNull(kontakt.getTelefaxNummer());
		assertNull(kontakt.getTelefonVorwahl());
		assertNull(kontakt.getTelefonNummer());
	}

	@Test
	public void testMapWithValues() {

		MapperTestUtils.fillWithValues(bildungsanbieter);

		Kontakt kontakt = cut.map(bildungsanbieter);

		assertEquals("email-ba@example.com", kontakt.getEmail());
		assertEquals("http://ba.homepage", kontakt.getInternet());
		assertEquals("344", kontakt.getTelefaxVorwahl());
		assertEquals("546546343", kontakt.getTelefaxNummer());
		assertEquals("432", kontakt.getTelefonVorwahl());
		assertEquals("23464364", kontakt.getTelefonNummer());
	}

}
