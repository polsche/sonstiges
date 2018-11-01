package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.model.Zugangsinformationen;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Akkreditierung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zugangsbedingung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungsmodus;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ZugangsinformationenMapperTest {

	private ZugangsinformationenMapper cut;

	@Mock
	private ZulassungsmodusMapper zulassungsmodusMapper;

	@Mock
	private ZugangsbedingungMapper zugangsbedingungMapper;

	@Mock
	private AkkreditierungsMapper akkreditierungsMapper;

	private Studienveranstaltung studienveranstaltung;

	private Veranstaltung veranstaltung;

	private Bildungsanbieter bildungsanbieter;

	@Before
	public void setUp() throws Exception {
		cut = new ZugangsinformationenMapper();
		cut.setAkkreditierungsMapper(akkreditierungsMapper);
		cut.setZugangsbedingungMapper(zugangsbedingungMapper);
		cut.setZulassungsmodusMapper(zulassungsmodusMapper);

		studienveranstaltung = new Studienveranstaltung();
		veranstaltung = new Veranstaltung();
		studienveranstaltung.getVeranstaltung().add(veranstaltung);
		bildungsanbieter = new Bildungsanbieter();
		studienveranstaltung.setBildungsanbieter(bildungsanbieter);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMapWithNulls() {

		MapperTestUtils.fillWithNulls(studienveranstaltung);
		MapperTestUtils.fillWithNulls(veranstaltung);

		when(akkreditierungsMapper.map(null)).thenReturn("akkreditierungsMapperCalledCorrectly");
		when(zulassungsmodusMapper.map(null)).thenReturn("zulassungsmodusMapperCalledCorrectly");

		Zugangsinformationen zugangsinformationen = cut.map(studienveranstaltung, veranstaltung);

		assertEquals("akkreditierungsMapperCalledCorrectly", zugangsinformationen.getAkkreditierung());
		assertNull(zugangsinformationen.getAkkreditierungBis());
		assertNull(zugangsinformationen.getAkkreditierungsbedingungen());
		assertNull(zugangsinformationen.getAkkreditierungVon());
		assertNull(zugangsinformationen.getOhneAbiMoeglich());
		assertEquals(0, zugangsinformationen.getOhneAbiZugangsbedingungen().size());
		assertNull(zugangsinformationen.getVoraussetzungen());
		assertNull(zugangsinformationen.getZulassungsmodusInfo());
		assertEquals("zulassungsmodusMapperCalledCorrectly", zugangsinformationen.getZulassungsmodus());

	}

	@Test
	public void testMapWithValues() {

		MapperTestUtils.fillWithValues(studienveranstaltung);
		MapperTestUtils.fillWithValues(veranstaltung);

		when(akkreditierungsMapper.map(Akkreditierung.REAKKREDITIERUNG))
				.thenReturn("akkreditierungsMapperCalledCorrectly");
		when(zulassungsmodusMapper.map(Zulassungsmodus.KEINE_ZULASSUNGSBESCHRAENKUNG))
				.thenReturn("zulassungsmodusMapperCalledCorrectly");
		when(zugangsbedingungMapper
				.map(Zugangsbedingung.MEHRJAEHRIGE_BERUFSAUSBILDUNG_UND_ODER_BERUFSERFAHRUNG_IN_EINEM_BERUFSFELD_MIT_FACHLICHER_NAEHE_ZUM_STUDIENFACH))
						.thenReturn("zugangsbedingungMapperCalledCorrectly1");
		when(zugangsbedingungMapper.map(Zugangsbedingung.MEISTERPRUEFUNG_ODER_GLEICHWERTIGE_AUFSTIEGSFORTBILDUNG))
				.thenReturn("zugangsbedingungMapperCalledCorrectly2");

		Zugangsinformationen zugangsinformationen = cut.map(studienveranstaltung, veranstaltung);

		assertEquals("akkreditierungsMapperCalledCorrectly", zugangsinformationen.getAkkreditierung());
		assertEquals("11.07.2035", zugangsinformationen.getAkkreditierungBis());
		assertEquals("AkkreditierungBedingung", zugangsinformationen.getAkkreditierungsbedingungen());
		assertEquals("01.03.2018", zugangsinformationen.getAkkreditierungVon());
		assertTrue(zugangsinformationen.getOhneAbiMoeglich());
		assertEquals(2, zugangsinformationen.getOhneAbiZugangsbedingungen().size());
		assertEquals("bemerkung133", zugangsinformationen.getOhneAbiZugangsbedingungen().get(0).getBemerkung());
		assertEquals("zugangsbedingungMapperCalledCorrectly1", zugangsinformationen.getOhneAbiZugangsbedingungen().get(0).getBedingung());
		assertEquals("bemerkung233", zugangsinformationen.getOhneAbiZugangsbedingungen().get(1).getBemerkung());
		assertEquals("zugangsbedingungMapperCalledCorrectly2", zugangsinformationen.getOhneAbiZugangsbedingungen().get(1).getBedingung());
		assertEquals("zugang", zugangsinformationen.getVoraussetzungen());
		assertEquals("zulassungsmodusInfo", zugangsinformationen.getZulassungsmodusInfo());
		assertEquals("zulassungsmodusMapperCalledCorrectly", zugangsinformationen.getZulassungsmodus());
	}

}
