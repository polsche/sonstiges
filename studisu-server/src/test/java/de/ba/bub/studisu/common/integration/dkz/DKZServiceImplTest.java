package de.ba.bub.studisu.common.integration.dkz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.common.model.Systematik;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.FehlerResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.FindSystematikRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.FindSystematikResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.SystematikPositionType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.BerufBasisType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.BerufskundlicheGruppe2014Type;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.BeschreibungszustandType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.DKZServiceFaultType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindAusbildungenBySuchwortRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindAusbildungenBySuchwortResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindBerufeFuerStudienbereichRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindBerufeFuerStudienbereichResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindStudienbereicheByObercodenrRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindStudienbereicheByObercodenrResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.GetBerufeByIDRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.GetBerufeByIDResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.SystematikBasisType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.ZustandType;
import de.baintern.dst.services.types.common.serviceinfo_v_2.ServiceStatus;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServiceFaultException;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServicePortType;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.FehlerResponseMessage;

/**
 * Testklasse fuer {@link DKZServiceImpl}
 * 
 * waehrend wir fuer Powermocks mockstatic, um den Logger zu mocken den PowerMockRunner brauchen,
 * veraendert dieser das Verhalten bei expected exceptions sowie Whitebox-Zugriffen negativ
 * deswegen wurden verschiedene Unittests auf zwei Klassen aufgeteilt mit den verschiedenen
 * 
 * @author KunzmannC
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class DKZServiceImplTest {

@Mock 
private de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZService dkzService;

	@Mock
	private DKZServicePortType dkzServicePort;

	@Mock
	private static Logger logger;

	private DKZServiceImpl instance;

	private final static String OBERCODENR1 = "HC 21";
	private final static String CODENR1 = "HC 2103";
	private final static String CODENR2 = "HC 2109";
	

	@BeforeClass
	public static void init() {
		//einschalten des loggings auf debug, damit auch diese branches durchlaufen werden
		//vgl. StudienangebotsucheControllerTest
	    mockStatic(LoggerFactory.class);
	    logger = mock(Logger.class);
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
	    when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);
	}
	
	@Before
	public void setUp() throws Exception {
		// DISCLAIMER:
		// spaghetticode inc, jedoch wurde hier nicht in zerschneiden investiert,
		// da es sich nur um JUnit-Setup handelt und manche responses fuer mehrere Tests genutzt werden,
		// v.a. wenn einmal der positivfall und dann noch X Exceptions getestet werden sollen...

		
		// ANTWORT1 des serviceports mocken
		FindStudienbereicheByObercodenrResponseType response = mock(FindStudienbereicheByObercodenrResponseType.class);
		//response erweitertn um mapping zu testen
		SystematikBasisType sys1 = mock(SystematikBasisType.class);
		when(sys1.getObercodenr()).thenReturn(OBERCODENR1);
		when(sys1.getCodenr()).thenReturn(CODENR1);

		SystematikBasisType sys2 = mock(SystematikBasisType.class);
		when(sys2.getObercodenr()).thenReturn(OBERCODENR1);
		when(sys2.getCodenr()).thenReturn(CODENR2);

		List<SystematikBasisType> studienbereich = new ArrayList<>();
		studienbereich.add(sys1);
		studienbereich.add(sys2);
		when(response.getStudienbereich()).thenReturn(studienbereich);

		when(dkzServicePort.findStudienbereicheByObercodenr(any(FindStudienbereicheByObercodenrRequestType.class))).thenReturn(response);


		// ANTWORT2 des serviceports mocken
		FindSystematikResponseType findSystematikResponse = mock(FindSystematikResponseType.class);
		//response erweitertn um mapping zu testen
		// List<SystematikPositionType> systematikPositionTypeListe = findSystematikResponseType.getSystematikPosition();
		List<SystematikPositionType> systematikPositionTypeListe = new ArrayList<>();
		SystematikPositionType sysPos1 = mock(SystematikPositionType.class);
		when(sysPos1.getZustand()).thenReturn("E");
		when(sysPos1.getBeschreibungsZustand()).thenReturn("J");
		when(sysPos1.getObercodenr()).thenReturn(OBERCODENR1);
		when(sysPos1.getCodenr()).thenReturn(CODENR1);

		SystematikPositionType sysPos2 = mock(SystematikPositionType.class);
		when(sysPos2.getZustand()).thenReturn("E");
		when(sysPos2.getBeschreibungsZustand()).thenReturn("J");
		when(sysPos2.getObercodenr()).thenReturn(OBERCODENR1);
		when(sysPos2.getCodenr()).thenReturn(CODENR2);

		systematikPositionTypeListe.add(sysPos1);
		systematikPositionTypeListe.add(sysPos2);
		when(findSystematikResponse.getSystematikPosition()).thenReturn(systematikPositionTypeListe);
		when(dkzServicePort.findSystematik(any(FindSystematikRequestType.class))).thenReturn(findSystematikResponse);
		
		//ANTWORT 3 des serviceports mocken
		//List<Systematik> findStudienfaecherFuerStudienfeld(Integer studfId)
		FindBerufeFuerStudienbereichResponseType response3 = mock(FindBerufeFuerStudienbereichResponseType.class);
		//response erweitertn um mapping zu testen
		//BerufBasisType beruf : response.getBeruf()
		List<BerufBasisType> berufe = new ArrayList<>();
		
		// mockito way
		BerufBasisType beruf1 = mock(BerufBasisType.class);
		when(beruf1.getZustand()).thenReturn(ZustandType.WERT_E_ENDPUNKT);
		when(beruf1.getBeschreibungszustand()).thenReturn(BeschreibungszustandType.WERT_J_BESCHRIEBEN);
		when(beruf1.getCodenr()).thenReturn(CODENR1);

		// constructor goes as well :)
		BerufBasisType beruf2 = new BerufBasisType();
		beruf2.setZustand(ZustandType.WERT_E_ENDPUNKT);
		beruf2.setBeschreibungszustand(BeschreibungszustandType.WERT_N_NICHT_BESCHRIEBEN);
		beruf2.setCodenr(CODENR1);
		
		berufe.add(beruf1);
		berufe.add(beruf2);
		when(response3.getBeruf()).thenReturn(berufe);
		when(dkzServicePort.findBerufeFuerStudienbereich(any(FindBerufeFuerStudienbereichRequestType.class))).thenReturn(response3);
		
		//ANTWORT 4
		//FindAusbildungenBySuchwortResponseType response4 = new FindAusbildungenBySuchwortResponseType();
		FindAusbildungenBySuchwortResponseType response4 = mock(FindAusbildungenBySuchwortResponseType.class);
		//response erweitertn um mapping zu testen
		List<BerufBasisType> studienfaecher = new ArrayList<>();
		studienfaecher.add(beruf1);
		studienfaecher.add(beruf2);
		BerufBasisType studienfachGrundst = new BerufBasisType();
		studienfachGrundst.setBerufskundlicheGruppe(BerufskundlicheGruppe2014Type.WERT_3120_A_GRUNDST_STUDIENFAECHER_GAENGE);
		studienfachGrundst.setKurzBezeichnungNeutral("studienfachGrundst");
		studienfachGrundst.setId(0);
		BerufBasisType studienfachWeiterf = new BerufBasisType();
		studienfachWeiterf.setBerufskundlicheGruppe(BerufskundlicheGruppe2014Type.WERT_3130_A_WEITERF_STUDIENFAECHER_GAENGE);
		studienfachWeiterf.setKurzBezeichnungNeutral("studienfachWeiterf");
		studienfachWeiterf.setId(1);
		studienfaecher.add(studienfachGrundst);
		studienfaecher.add(studienfachWeiterf);
		when(response4.getBeruf()).thenReturn(studienfaecher);
		when(dkzServicePort.findAusbildungenBySuchwort(any(FindAusbildungenBySuchwortRequestType.class))).thenReturn(response4);
		
		//ANTWORT 5 
		GetBerufeByIDResponseType response5 = mock(GetBerufeByIDResponseType.class);
		// nutze studienfaecher von anderem test
		when(response5.getBeruf()).thenReturn(studienfaecher);
		when(dkzServicePort.getBerufeByID(any(GetBerufeByIDRequestType.class))).thenReturn(response5);
		
		// INSTANZ AUFBAUEN UND SERVICEPORT MOCKEN
		instance = new DKZServiceImpl();
		Whitebox.setInternalState(instance, dkzServicePort, dkzServicePort);
	}

	// List<Systematik> findStudienfeldgruppeSystematiken(String obercodenr);
	@Test
	public void testfindStudienfeldgruppeSystematiken() {
		String obercodenr = "HC 21";
		List<Systematik> result = instance.findStudienfeldgruppeSystematiken(obercodenr);
		
		assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
		assertEquals("service beide systematiken des mocks liefern ", 2, result.size());
	}
	
	@Test(expected=CommonServiceException.class)
	public void testfindStudienfeldgruppeSystematikenDKZServiceFaultException() {
		String obercodenr = "HC 21";

		DKZServiceFaultType fault = mock (DKZServiceFaultType.class);
		DKZServiceFaultException exception = new DKZServiceFaultException("junit exception", fault);

		try {
			when(dkzServicePort.findStudienbereicheByObercodenr(any(FindStudienbereicheByObercodenrRequestType.class))).thenThrow(exception);
			instance.findStudienfeldgruppeSystematiken(obercodenr);
		} catch (CommonServiceException cse) {
			assertTrue("message sollte erhalten bleiben", cse.getMessage().contains("junit exception"));
			// fullfill expectation
			throw cse;
		} catch (Exception e) {
			fail("unexpected exception");
		}
	}

	@Test(expected=CommonServiceException.class)
	public void testfindStudienfeldgruppeSystematikenWebServiceException() {
		String obercodenr = "HC 21";
		
		WebServiceException exception = new WebServiceException("junit exception");

		try {
			when(dkzServicePort.findStudienbereicheByObercodenr(any(FindStudienbereicheByObercodenrRequestType.class))).thenThrow(exception);
			instance.findStudienfeldgruppeSystematiken(obercodenr);
		} catch (CommonServiceException cse) {
			assertTrue("message sollte erhalten bleiben",cse.getMessage().contains("junit exception"));
			// fullfill expectation
			throw cse;
		} catch (Exception e) {
			fail("unexpected exception");
		}
	}

	//@override ExternalServiceStatus getServiceStatus() throws CommonServiceException;
	@Test
	public void testgetServiceStatus() {
		try {
			de.baintern.dst.services.types.common.serviceinfo_v_2.ServiceStatus status = mock(ServiceStatus.class);
			when(status.getWsversion()).thenReturn("Version Junit 1.12.13");
			when(dkzServicePort.getServiceStatus()).thenReturn(status);
			
			ExternalServiceStatus extStatus = instance.getServiceStatus();
			assertEquals("service impl sollte verfuegbar liefern",
					ExternalServiceStatus.Status.VERFUEGBAR, extStatus.getServiceStatus());
			assertEquals("service impl name falsch",
					"DKZService", extStatus.getServiceName());
		} catch (FehlerResponseMessage e) {
			fail("service liefert exception statt status und version");
		}
	}

	// List<Systematik> findSystematik(int dkzId, List<String> codeNummern);
	@Test
	public void testfindSystematik() {
		int dkzId = 3626;
		List<String> codeNummern = new ArrayList<>();
		codeNummern.add("HC 2103");
		codeNummern.add("HC 2109");

		List<Systematik> result = instance.findSystematik(dkzId, codeNummern);
		
		assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
		assertEquals("service beide systematiken des mocks liefern ", 2, result.size());
	}
	
	@Test(expected=CommonServiceException.class)
	public void testfindSystematikFehlerResponseMessage() {
		int dkzId = 3626;
		List<String> codeNummern = new ArrayList<>();
		codeNummern.add("HC 2103");
		codeNummern.add("HC 2109");
		
		FehlerResponseType fault = mock(FehlerResponseType.class);
		FehlerResponseMessage exception = new FehlerResponseMessage("junit exception", fault);

		try {
			when(dkzServicePort.findSystematik(any(FindSystematikRequestType.class))).thenThrow(exception);
			instance.findSystematik(dkzId, codeNummern);
		} catch (CommonServiceException cse) {
			assertTrue("message sollte erhalten bleiben",cse.getMessage().contains("junit exception"));
			// fullfill expectation
			throw cse;
		} catch (Exception e) {
			fail("unexpected exception");
		}
	}

	@Test(expected=CommonServiceException.class)
	public void testfindSystematikWebServiceException() {
		int dkzId = 3626;
		List<String> codeNummern = new ArrayList<>();
		codeNummern.add("HC 2103");
		codeNummern.add("HC 2109");
		
		WebServiceException exception = new WebServiceException("junit exception");

		try {
			when(dkzServicePort.findSystematik(any(FindSystematikRequestType.class))).thenThrow(exception);
			instance.findSystematik(dkzId, codeNummern);
		} catch (CommonServiceException cse) {
			assertTrue("message sollte erhalten bleiben",cse.getMessage().contains("junit exception"));
			// fullfill expectation
			throw cse;
		} catch (Exception e) {
			fail("unexpected exception");
		}
	}

	//List<Systematik> findStudienfaecherFuerStudienfeld(Integer studfId);
	@Test
	public void testfindStudienfaecherFuerStudienfeld() {
		Integer studfId = Integer.valueOf(1111);
		
		List<Systematik> result = instance.findStudienfaecherFuerStudienfeld(studfId);

		StringBuilder expected = new StringBuilder("*** TIME-INFO *** - findStudienfaecherFuerStudienfeldAllgemein(");
		expected.append(studfId.toString());
		expected.append(", false");
		Mockito.verify(logger).debug(Mockito.startsWith(expected.toString()));//, Mockito.times(1)
		
		assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
		assertEquals("service beide systematiken des mocks liefern, auch die unbeschriebene", 
				2, result.size());
	}
	
	//List<Systematik> findBeschriebeneStudienfaecherFuerStudienfeld(Integer studfId);
	@Test
	public void testfindBeschriebeneStudienfaecherFuerStudienfeld() {
		Integer studfId = Integer.valueOf(1111);
		
		List<Systematik> result = instance.findBeschriebeneStudienfaecherFuerStudienfeld(studfId);
		assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
		assertEquals("service sollte nur die beschriebene systematiken des mocks liefern", 
				1, result.size());
	}
	
	@Test(expected=CommonServiceException.class)
	public void testfindBeschriebeneStudienfaecherFuerStudienfeldWebServiceException() {
		Integer studfId = Integer.valueOf(1111);
		
		WebServiceException exception = new WebServiceException("junit exception");

		try {
			when(dkzServicePort.findBerufeFuerStudienbereich(any(FindBerufeFuerStudienbereichRequestType.class))).thenThrow(exception);
			instance.findBeschriebeneStudienfaecherFuerStudienfeld(studfId);
		} catch (CommonServiceException cse) {
			assertTrue("message sollte erhalten bleiben",cse.getMessage().contains("junit exception"));
			// fullfill expectation
			throw cse;
		} catch (Exception e) {
			fail("unexpected exception");
		}
	}

	// public List<Studienfach> findStudienfachBySuchwort(String suchbefriff);
	@Test
	public void testfindStudienfachBySuchwort() {
		String suchbefriff = "baum";
		
		List<Studienfach> result = instance.findStudienfachBySuchwort(suchbefriff);
		
		assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
		assertEquals("service sollte zwei studienfaecher liefern", 
				2, result.size());
		
		assertEquals("kurzbezeichnung sollte zu name werden", 
				"studienfachGrundst", result.get(0).getName());
		assertEquals("kurzbezeichnung sollte zu name werden", 
				"studienfachWeiterf", result.get(1).getName());
	}
	
	// List<Studienfach> findStudienfachById(List<Integer> dkzIds);
	@Test
	public void testfindStudienfachById() {
		List<Integer> dkzIds = new ArrayList<>();
		
		List<Studienfach> result = instance.findStudienfachById(dkzIds);

		assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
		assertEquals("service sollte zwei studienfaecher liefern (und zwei berufe, weil die methode nicht filtert :)", 
				4, result.size());
		
		assertEquals("id sollte zu dkzid werden", 
				0, result.get(2).getDkzId());
		assertEquals("id sollte zu dkzid werden",  
				1, result.get(3).getDkzId());
		assertEquals("kurzbezeichnung sollte zu name werden", 
				"studienfachGrundst", result.get(2).getName());
		assertEquals("kurzbezeichnung sollte zu name werden", 
				"studienfachWeiterf", result.get(3).getName());
	}
}
