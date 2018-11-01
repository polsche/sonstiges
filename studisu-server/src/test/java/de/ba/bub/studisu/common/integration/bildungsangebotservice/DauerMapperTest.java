package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.model.Dauer;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungssemester;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DauerMapperTest {

	private DauerMapper cut;

	@Mock
	private ZulassungssemesterMapper zulassungssemesterMapper;

	private Veranstaltung veranstaltung;


	@Before
	public void setUp() throws Exception {
		cut = new DauerMapper();
		cut.setZulassungssemesterMapper(zulassungssemesterMapper);
		veranstaltung = new Veranstaltung();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMapWithNulls() {

		MapperTestUtils.fillWithNulls(veranstaltung);

		when(zulassungssemesterMapper.map(null)).thenReturn("zulassungssemesterMapperCalledCorrectly");

		Dauer dauer = cut.map(veranstaltung);

		assertEquals("Auf Anfrage", dauer.getBeginn());
		assertNull(dauer.getBemerkung());
		assertEquals("Auf Anfrage", dauer.getEnde());
		assertNull(dauer.getIndividuellerEinstieg());
		assertNull(dauer.getUnterrichtszeiten());
		assertEquals("zulassungssemesterMapperCalledCorrectly", dauer.getZulassungssemester());

	}

	@Test
	public void testMapWithValues() {

		MapperTestUtils.fillWithValues(veranstaltung);

		when(zulassungssemesterMapper.map(Zulassungssemester.HERBSTTRIMESTER)).thenReturn("zulassungssemesterMapperCalledCorrectly");

		Dauer dauer = cut.map(veranstaltung);

		assertEquals("01.09.2018", dauer.getBeginn());
		assertEquals("bemerkungZeit", dauer.getBemerkung());
		assertEquals("10.09.2018", dauer.getEnde());
		assertFalse(dauer.getIndividuellerEinstieg());
		assertEquals("vorlesungszeiten", dauer.getUnterrichtszeiten());
		assertEquals("zulassungssemesterMapperCalledCorrectly", dauer.getZulassungssemester());
	}
}
