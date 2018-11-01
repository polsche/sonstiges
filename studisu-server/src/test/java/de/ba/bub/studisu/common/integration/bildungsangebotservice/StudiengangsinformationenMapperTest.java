package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.model.Studiengangsinformationen;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlussgrad;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlusstyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bildungsart;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hauptunterrichtssprache;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hochschultyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Lehramtstyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Studienmodell;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StudiengangsinformationenMapperTest {

	private StudiengangsinformationenMapper cut;

	@Mock
	private BildungsartMapper bildungsartMapper;
	
	@Mock
	private AbschlusstypMapper abschlusstypMapper;
	
	@Mock
	private UnterrichtsformMapper unterrichtsformMapper;
	
	@Mock
	private HochschultypMapper hochschultypMapper;
	
	@Mock
	private AbschlussgradMapper abschlussgradMapper;
	
	@Mock
	private LehramtstypMapper lehramtstypMapper;
	
	@Mock
	private UnterrichtsspracheMapper unterrichtsspracheMapper;
	
	@Mock
	private StudienmodellMapper studienmodellMapper;
	
	private Studienveranstaltung studienveranstaltung;

	private Veranstaltung veranstaltung;

	private Bildungsanbieter bildungsanbieter;

	@Before
	public void setUp() throws Exception {
		cut = new StudiengangsinformationenMapper();
		cut.setAbschlussgradMapper(abschlussgradMapper);
		cut.setAbschlusstypMapper(abschlusstypMapper);
		cut.setBildungsartMapper(bildungsartMapper);
		cut.setHochschultypMapper(hochschultypMapper);
		cut.setLehramtstypMapper(lehramtstypMapper);
		cut.setStudienmodellMapper(studienmodellMapper);
		cut.setUnterrichtsformMapper(unterrichtsformMapper);
		cut.setUnterrichtsspracheMapper(unterrichtsspracheMapper);

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

		when(bildungsartMapper.map(null)).thenReturn("bildungsartMapperCalledCorrectly");
		when(abschlusstypMapper.map(null)).thenReturn("abschlusstypMapperCalledCorrectly");
		when(unterrichtsformMapper.map(null)).thenReturn("unterrichtsformMapperCalledCorrectly");
		when(hochschultypMapper.map(null)).thenReturn("hochschultypMapperCalledCorrectly");
		when(abschlussgradMapper.map(null)).thenReturn("abschlussgradMapperCalledCorrectly");
		when(lehramtstypMapper.map(null)).thenReturn("lehramtstypMapperCalledCorrectly");
		when(unterrichtsspracheMapper.map(null)).thenReturn("unterrichtsspracheMapperCalledCorrectly");

		Studiengangsinformationen studiengangsinformationen = cut.map(studienveranstaltung, veranstaltung);

		assertEquals("abschlussgradMapperCalledCorrectly", studiengangsinformationen.getAbschlussgrad());
		assertNull(studiengangsinformationen.getAbschlussgradIntern());
		assertEquals("abschlusstypMapperCalledCorrectly", studiengangsinformationen.getAbschlusstyp());
		assertEquals("bildungsartMapperCalledCorrectly", studiengangsinformationen.getBildungsart());
		assertEquals(0, studiengangsinformationen.getDualeStudienmodelle().size());
		assertNull(studiengangsinformationen.getInternationalerDoppelabschluss());
		assertNull(studiengangsinformationen.getLehramtsbefaehigung());
		assertEquals("lehramtstypMapperCalledCorrectly", studiengangsinformationen.getLehramtstyp());
		assertNull(studiengangsinformationen.getRegelstudienzeit());
		assertEquals("hochschultypMapperCalledCorrectly", studiengangsinformationen.getSchulart());
		assertEquals(0, studiengangsinformationen.getStudienfaecher().size());
		assertEquals("unterrichtsformMapperCalledCorrectly", studiengangsinformationen.getStudienform());
		assertEquals("unterrichtsspracheMapperCalledCorrectly", studiengangsinformationen.getUnterrichtssprache());
	}

	@Test
	public void testMapWithValues() {

		MapperTestUtils.fillWithValues(studienveranstaltung);
		MapperTestUtils.fillWithValues(veranstaltung);

		when(bildungsartMapper.map(Bildungsart.STUDIENANGEBOT_WEITERFUEHREND)).thenReturn("bildungsartMapperCalledCorrectly");
		when(abschlusstypMapper.map(Abschlusstyp.KONSEKUTIV)).thenReturn("abschlusstypMapperCalledCorrectly");
		when(unterrichtsformMapper.map(Unterrichtsform.VOLLZEIT)).thenReturn("unterrichtsformMapperCalledCorrectly");
		when(hochschultypMapper.map(Hochschultyp.UNIVERSITAET)).thenReturn("hochschultypMapperCalledCorrectly");
		when(abschlussgradMapper.map(Abschlussgrad.DIPLOM)).thenReturn("abschlussgradMapperCalledCorrectly");
		when(lehramtstypMapper.map(Lehramtstyp.LEHRAEMTER_DER_GRUNDSCHULE_BZW_PRIMARSTUFE)).thenReturn("lehramtstypMapperCalledCorrectly");
		when(unterrichtsspracheMapper.map(Hauptunterrichtssprache.ENGLISCH)).thenReturn("unterrichtsspracheMapperCalledCorrectly");
		when(studienmodellMapper.map(Studienmodell.BERUFSBEGLEITEND)).thenReturn("studienmodellMapperCalledCorrectly1");
		when(studienmodellMapper.map(Studienmodell.DUALES_STUDIUM)).thenReturn("studienmodellMapperCalledCorrectly2");
		
		Studiengangsinformationen studiengangsinformationen = cut.map(studienveranstaltung, veranstaltung);
		
		assertEquals("abschlussgradMapperCalledCorrectly", studiengangsinformationen.getAbschlussgrad());
		assertEquals("Masterdiplom", studiengangsinformationen.getAbschlussgradIntern());
		assertEquals("abschlusstypMapperCalledCorrectly", studiengangsinformationen.getAbschlusstyp());
		assertEquals("bildungsartMapperCalledCorrectly", studiengangsinformationen.getBildungsart());
		assertEquals(2, studiengangsinformationen.getDualeStudienmodelle().size());
		assertEquals("studienmodellMapperCalledCorrectly1", studiengangsinformationen.getDualeStudienmodelle().get(0));
		assertEquals("studienmodellMapperCalledCorrectly2", studiengangsinformationen.getDualeStudienmodelle().get(1));
		assertFalse(studiengangsinformationen.getInternationalerDoppelabschluss());
		assertTrue(studiengangsinformationen.getLehramtsbefaehigung());
		assertEquals("lehramtstypMapperCalledCorrectly", studiengangsinformationen.getLehramtstyp());
		assertEquals("12 Trimester", studiengangsinformationen.getRegelstudienzeit());
		assertEquals("hochschultypMapperCalledCorrectly", studiengangsinformationen.getSchulart());
		assertEquals(2, studiengangsinformationen.getStudienfaecher().size());
		assertEquals("dkzBezeichnung1", studiengangsinformationen.getStudienfaecher().get(0));
		assertEquals("dkzBezeichnung2", studiengangsinformationen.getStudienfaecher().get(1));
		assertEquals("unterrichtsformMapperCalledCorrectly", studiengangsinformationen.getStudienform());
		assertEquals("unterrichtsspracheMapperCalledCorrectly", studiengangsinformationen.getUnterrichtssprache());
	}

}
