package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.ba.bub.studisu.common.model.Dauer;
import de.ba.bub.studisu.common.model.Kontakt;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.common.model.Studiengangsinformationen;
import de.ba.bub.studisu.common.model.Zugangsinformationen;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Eingabeverfahren;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StudienangebotInformationenMapperTest {

	private StudienangebotInformationenMapper cut;

	@Mock
	private DauerMapper dauerMapper = new DauerMapper();

	@Mock
	private ZugangsinformationenMapper zugangsinformationenMapper = new ZugangsinformationenMapper();

	@Mock
	private StudiengangsinformationenMapper studiengangsinformationenMapper = new StudiengangsinformationenMapper();

	@Mock
	private StudienanbieterMapper studienanbieterMapper = new StudienanbieterMapper();

	@Mock
	private StudienortMapper studienortMapper = new StudienortMapper();

	@Mock
	private KontaktMapper kontaktMapper = new KontaktMapper();

	@Mock
	private Dauer dauer;

	@Mock
	private Kontakt kontakt;

	@Mock
	private AdresseKurz studienanbieter;

	@Mock
	private Studiengangsinformationen studiengangsinformationen;

	@Mock
	private AdresseKurz studienort;

	@Mock
	private Zugangsinformationen zugangsinformationen;

	private Studienveranstaltung studienveranstaltung;

	private Veranstaltung veranstaltung;

	private Bildungsanbieter bildungsanbieter;

	@Before
	public void setUp() throws Exception {
		cut = new StudienangebotInformationenMapper();
		cut.setDauerMapper(dauerMapper);
		cut.setKontaktMapper(kontaktMapper);
		cut.setStudienanbieterMapper(studienanbieterMapper);
		cut.setStudiengangsinformationenMapper(studiengangsinformationenMapper);
		cut.setStudienortMapper(studienortMapper);
		cut.setZugangsinformationenMapper(zugangsinformationenMapper);

		studienveranstaltung = new Studienveranstaltung();
		veranstaltung = new Veranstaltung();
		studienveranstaltung.getVeranstaltung().add(veranstaltung);
		bildungsanbieter = new Bildungsanbieter();
		studienveranstaltung.setBildungsanbieter(bildungsanbieter);

		when(dauerMapper.map(veranstaltung)).thenReturn(new Dauer());
		when(kontaktMapper.map(bildungsanbieter)).thenReturn(new Kontakt());
		when(studienanbieterMapper.map(bildungsanbieter)).thenReturn(new AdresseKurz());
		when(studiengangsinformationenMapper.map(studienveranstaltung, veranstaltung))
				.thenReturn(new Studiengangsinformationen());
		when(studienortMapper.map(veranstaltung)).thenReturn(new AdresseKurz());
		when(zugangsinformationenMapper.map(studienveranstaltung, veranstaltung))
				.thenReturn(new Zugangsinformationen());
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMapWithNulls() {

		MapperTestUtils.fillWithNulls(studienveranstaltung);
		MapperTestUtils.fillWithNulls(veranstaltung);
		MapperTestUtils.fillWithNulls(bildungsanbieter);

		setupMocks();

		StudienangebotInformationen studienangebotInformationen = cut.map(studienveranstaltung, veranstaltung);

		assertNull(studienangebotInformationen.getAktualisierungsdatum());
		assertNull(studienangebotInformationen.getBezeichnung());
		assertFalse(studienangebotInformationen.getBildungsanbieterHasSignet());
		assertEquals(0, studienangebotInformationen.getBildungsanbieterId());
		assertEquals(0, studienangebotInformationen.getExternalLinks().size());
		assertEquals("0", studienangebotInformationen.getId());
		assertNull(studienangebotInformationen.getInhalt());
		assertFalse(studienangebotInformationen.getIsHrkDatensatz());
		assertNull(studienangebotInformationen.getKosten());
		assertEquals("", studienangebotInformationen.getStudienfaecherCsv());
		assertNull(studienangebotInformationen.getStudienschwerpunkte());
		assertNull(studienangebotInformationen.getStudiuminformationen());
		assertNull(studienangebotInformationen.getVeranstaltungZusatzlink());
		assertEquals(StudientypFacettenOption.SONSTIGE, studienangebotInformationen.getStudientyp());
		assertEquals(HochschulartFacettenOption.KEINE_ZUORDNUNG_MOEGLICH,
				studienangebotInformationen.getHochschulart());
		assertEquals(StudienformFacettenOption.SONSTIGE, studienangebotInformationen.getStudienform());

		verifyMocks(studienangebotInformationen);

	}

	@Test
	public void testMapWithValues() throws URISyntaxException {

		MapperTestUtils.fillWithValues(studienveranstaltung);
		MapperTestUtils.fillWithValues(veranstaltung);
		MapperTestUtils.fillWithValues(bildungsanbieter);

		setupMocks();

		StudienangebotInformationen studienangebotInformationen = cut.map(studienveranstaltung, veranstaltung);

		assertEquals("30.08.2018", studienangebotInformationen.getAktualisierungsdatum());
		assertEquals("titel", studienangebotInformationen.getBezeichnung());
		assertTrue(studienangebotInformationen.getBildungsanbieterHasSignet());
		assertEquals(543, studienangebotInformationen.getBildungsanbieterId());
		assertEquals(2, studienangebotInformationen.getExternalLinks().size());
		assertEquals(new URI("http://osa"), studienangebotInformationen.getExternalLinks().get(0).getLink());
		assertEquals(new URI("http://studicheckUrl"), studienangebotInformationen.getExternalLinks().get(1).getLink());
		assertEquals("Methode muss die ID der Veranstalung, nicht die des Studienangebots, liefern!", "22", studienangebotInformationen.getId());
		assertEquals("inhalte", studienangebotInformationen.getInhalt());
		assertFalse(studienangebotInformationen.getIsHrkDatensatz());
		assertEquals("bemerkungKosten", studienangebotInformationen.getKosten());
		assertEquals("34;52", studienangebotInformationen.getStudienfaecherCsv());
		assertEquals("studienschwerpunkte", studienangebotInformationen.getStudienschwerpunkte());
		assertEquals("studiuminformation", studienangebotInformationen.getStudiuminformationen());
		assertEquals("http://zusatzlink", studienangebotInformationen.getVeranstaltungZusatzlink());
		assertEquals(StudientypFacettenOption.GRUNDSTAENDIG, studienangebotInformationen.getStudientyp()); // wg HA_34
		assertEquals(HochschulartFacettenOption.UNIVERSITAET, studienangebotInformationen.getHochschulart());
		assertEquals(StudienformFacettenOption.VOLLZEIT, studienangebotInformationen.getStudienform());

		verifyMocks(studienangebotInformationen);
	}

	@Test
	public void testMapWithValuesHrk() {

		MapperTestUtils.fillWithValues(studienveranstaltung);
		MapperTestUtils.fillWithValues(veranstaltung);
		MapperTestUtils.fillWithValues(bildungsanbieter);

		bildungsanbieter.setEingabeverfahren(Eingabeverfahren.H_RK);

		setupMocks();

		StudienangebotInformationen studienangebotInformationen = cut.map(studienveranstaltung, veranstaltung);

		assertTrue(studienangebotInformationen.getIsHrkDatensatz());
	}

	private void setupMocks() {
		when(dauerMapper.map(veranstaltung)).thenReturn(dauer);
		when(kontaktMapper.map(bildungsanbieter)).thenReturn(kontakt);
		when(studienanbieterMapper.map(bildungsanbieter)).thenReturn(studienanbieter);
		when(studiengangsinformationenMapper.map(studienveranstaltung, veranstaltung))
				.thenReturn(studiengangsinformationen);
		when(studienortMapper.map(veranstaltung)).thenReturn(studienort);
		when(zugangsinformationenMapper.map(studienveranstaltung, veranstaltung)).thenReturn(zugangsinformationen);
	}

	private void verifyMocks(StudienangebotInformationen studienangebotInformationen) {
		assertEquals(dauer, studienangebotInformationen.getDauer());
		assertEquals(kontakt, studienangebotInformationen.getKontakt());
		assertEquals(studienanbieter, studienangebotInformationen.getBildungsanbieter());
		assertEquals(studiengangsinformationen, studienangebotInformationen.getStudiengangsinformationen());
		assertEquals(studienort, studienangebotInformationen.getStudienort());
		assertEquals(zugangsinformationen, studienangebotInformationen.getZugangsinformationen());
	}
}
