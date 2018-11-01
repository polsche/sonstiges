package de.ba.bub.studisu.studienangebote.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.SuchortAbstand;
import de.ba.bub.studisu.studienangebote.command.StudienangebotsucheCommand;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StudienangebotsucheAnfrage.class, LoggerFactory.class})
public class StudienangebotsucheControllerTest {

	@Mock
	private static Logger logger;
	
	StudienangebotsucheCommand angebotsucheCommand;
	List<StudienangebotWrapperMitAbstand> angeboteWrapped;
	Studienangebot studienangebot;
	SuchortAbstand suchortAbstand;
	List<SuchortAbstand> abstaende;
	StudienangebotWrapperMitAbstand item;
	List<? extends StudienangebotFacette> facetten;
	long filteredOutErgebnisse;
	StudienangebotsucheErgebnis angebotsucheErgebnis;
	Integer anzahlSuchErgebnisse;
	StudienangebotsucheController instance;

	// suche parameter
	Studienfelder studienfelder;
	Studienfaecher studienfaecher;
	AnfrageOrte orte;
	StudienformFacette studienformFacette;
	HochschulartFacette hochschulartFacette;
	UmkreisFacette umkreisFacette;
	StudientypFacette studientypFacette;
	FitFuerStudiumFacette fitFuerStudiumFacette;
	RegionenFacette bundeslandFacette;
	Integer page;
	Integer reload;
	WebRequest request;
	HttpServletResponse response;
	
	@BeforeClass
	public static void init() {
		//einschalten des loggings auf debug, damit auch diese branches durchlaufen werden
		//vgl. StudienangebotsucheControllerTest
	    mockStatic(LoggerFactory.class);
	    logger = mock(Logger.class);
	    when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);
	}
	
	@Before
	public void setUp() {
		// the loggers behaviour gets reset from tearDown beside its counts, 
		// therefore we set it after the reset
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
	    
		// mock aufbauen fuer den command, damit dieser ein testbares ergebnis liefert
		angebotsucheCommand = mock(StudienangebotsucheCommand.class);

		angeboteWrapped = new ArrayList<>();

		studienangebot = new Studienangebot();
		studienangebot.setStudiInhalt("test inhalt");
		suchortAbstand = new SuchortAbstand("Paderborn", Double.valueOf(66));
		abstaende = new ArrayList<>();
		abstaende.add(suchortAbstand);
		item = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);
		angeboteWrapped.add(item);
		facetten = new ArrayList<>();
		filteredOutErgebnisse = 0;
		angebotsucheErgebnis = StudienangebotsucheErgebnis.withItems(angeboteWrapped, facetten, filteredOutErgebnisse);

		// mock des zugriffs auf dependency
		when(angebotsucheCommand.execute(Mockito.any(StudienangebotsucheAnfrage.class))).thenReturn(angebotsucheErgebnis);
		
		// instanzieren des system under test SUT, hier instance
		anzahlSuchErgebnisse = 12;
		instance =  new StudienangebotsucheController(angebotsucheCommand, anzahlSuchErgebnisse);
		
		// suche parameter
		studienfelder = mock(Studienfelder.class);
		studienfaecher = mock(Studienfaecher.class);
		orte = mock(AnfrageOrte.class);

		//studienformFacette = mock(StudienformFacette.class);
		studienformFacette = new StudienformFacette("1;2;4");

		hochschulartFacette = mock(HochschulartFacette.class);
		umkreisFacette = mock(UmkreisFacette.class);

		//studientypFacette = mock(StudientypFacette.class);
		studientypFacette = new StudientypFacette("0;1");
		
		fitFuerStudiumFacette = mock(FitFuerStudiumFacette.class);
		bundeslandFacette = mock(RegionenFacette.class);
		page = PowerMockito.mock(Integer.class);
		reload = PowerMockito.mock(Integer.class);
		request = mock(WebRequest.class);
		response = mock(HttpServletResponse.class);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}
	
	@Test
	public void testSucheAngeboteAngebotInhalt() {
		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);
	}

	@Test
	public void testSucheAngeboteAngebotOrtAbstand() {
		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielOrtAbstand = responseEntity.getBody().getItems().get(0).getAbstaende().get(0).getSuchort();
		
		assertEquals("suchortabstand sollte aus gemockten service durchschlagen", "Paderborn", beispielOrtAbstand);
	}

	@Test
	public void testSucheAngeboteAngebotPageNoReload() {
		//branch coverage erhoehung noch ohne funktionalen weitern test
		page=Integer.valueOf(1);

		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielOrtAbstand = responseEntity.getBody().getItems().get(0).getAbstaende().get(0).getSuchort();
		assertEquals("suchortabstand sollte aus gemockten service durchschlagen", "Paderborn", beispielOrtAbstand);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);
	}

	@Test
	public void testSucheAngeboteAngebotPageAndReload() {
		//branch coverage erhoehung noch ohne funktionalen weitern test
		page = Integer.valueOf(1);
		reload = Integer.valueOf(1);

		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielOrtAbstand = responseEntity.getBody().getItems().get(0).getAbstaende().get(0).getSuchort();
		assertEquals("suchortabstand sollte aus gemockten service durchschlagen", "Paderborn", beispielOrtAbstand);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);
		
		ArgumentCaptor<StudienangebotsucheAnfrage> argument = ArgumentCaptor.forClass(StudienangebotsucheAnfrage.class);
		// actually there were zero interactions with this mock
		Mockito.verify(angebotsucheCommand).execute(argument.capture());
		List<StudienangebotsucheAnfrage> allValues = argument.getAllValues();

		assertNotNull("command sollte executed worden sein", allValues);
		assertEquals("command sollte einmal executed worden sein", 1, allValues.size());

		StudienangebotsucheAnfrage anfrage = allValues.get(0);
		Paging paging = anfrage.getPaging();
		assertEquals("offset falsch berechnet  fuer page"+page+" , reload "+reload,
					 0, paging.getOffset());
		assertEquals("count falsch berechnet fuer page"+page+" , reload "+reload, 
					 20, paging.getCount());

		//super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.DYNAMIC_CONTENT)
		//responseEntity.getHeaders()
	}

	@Test
	public void testSucheAngeboteAngebotPageFuenf() {
		page = Integer.valueOf(5);
		// no reload

		instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		ArgumentCaptor<StudienangebotsucheAnfrage> argument = ArgumentCaptor.forClass(StudienangebotsucheAnfrage.class);
		Mockito.verify(angebotsucheCommand).execute(argument.capture());
		List<StudienangebotsucheAnfrage> allValues = argument.getAllValues();

		assertNotNull("command sollte executed worden sein", allValues);
		assertEquals("command sollte einmal executed worden sein", 1, allValues.size());

		StudienangebotsucheAnfrage anfrage = allValues.get(0);
		Paging paging = anfrage.getPaging();
		assertEquals("offset falsch berechnet  fuer page"+page+" , reload "+reload,
					 0, paging.getOffset());
		assertEquals("count falsch berechnet fuer page"+page+" , reload "+reload, 
					 Paging.COUNT_DEFAULT * page, paging.getCount());
	}
	
	@Test
	public void testSucheAngeboteAngebotPageFuenfAndReload() {
		page = Integer.valueOf(5);
		reload = Integer.valueOf(1);

		instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		ArgumentCaptor<StudienangebotsucheAnfrage> argument = ArgumentCaptor.forClass(StudienangebotsucheAnfrage.class);
		Mockito.verify(angebotsucheCommand).execute(argument.capture());
		List<StudienangebotsucheAnfrage> allValues = argument.getAllValues();

		assertNotNull("command sollte executed worden sein", allValues);
		assertEquals("command sollte einmal executed worden sein", 1, allValues.size());

		StudienangebotsucheAnfrage anfrage = allValues.get(0);
		Paging paging = anfrage.getPaging();
		assertEquals("offset falsch berechnet  fuer page"+page+" , reload "+reload,
					 Paging.COUNT_DEFAULT * (page -1), paging.getOffset());
		assertEquals("count falsch berechnet fuer page"+page+" , reload "+reload, 
					 Paging.COUNT_DEFAULT, paging.getCount());
	}
	
	@Test
	public void testSucheAngeboteAngebotNullPageAndReload() {
		//branch coverage erhoehung noch ohne funktionalen weitern test
		page = null;
		reload = Integer.valueOf(1);

		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielOrtAbstand = responseEntity.getBody().getItems().get(0).getAbstaende().get(0).getSuchort();
		assertEquals("suchortabstand sollte aus gemockten service durchschlagen", "Paderborn", beispielOrtAbstand);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);
	}

	@Test
	public void testSucheAngeboteAngebotLoggingAllFacettes() {
		// setzen der weiteren facetten
		hochschulartFacette = new HochschulartFacette();
		umkreisFacette = mock(UmkreisFacette.class);
		fitFuerStudiumFacette = mock(FitFuerStudiumFacette.class);
		bundeslandFacette = mock(RegionenFacette.class);

	    // sucheAngebote
		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);

		// logger gibt methode an
		verify(logger).debug(Mockito.contains("sucheAngebote called"));

		// aber ohne facetten, wenn null
		verify(logger).debug(Mockito.contains(StudisuController.URL_PARAM_STUDIENFORM+"="));
		verify(logger).debug(Mockito.contains(StudisuController.URL_PARAM_HOCHSCHULART+"="));
		verify(logger).debug(Mockito.contains(StudisuController.URL_PARAM_UMKREIS+"="));
		verify(logger).debug(Mockito.contains(StudisuController.URL_PARAM_STUDIENTYP+"="));
		verify(logger).debug(Mockito.contains(StudisuController.URL_PARAM_FITFUERSTUDIUM+"="));
		verify(logger).debug(Mockito.contains(StudisuController.URL_PARAM_REGION+"="));
	}

	@Test
	public void testSucheAngeboteAngebotLoggingNullFacettes() {
	    // NULL setzen aller facetten
		studienformFacette = null;
		hochschulartFacette = null;
		umkreisFacette = null;
		studientypFacette = null;
		fitFuerStudiumFacette = null;
		bundeslandFacette = null;

	    // sucheAngebote
		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);

		// logger gibt methode an
		verify(logger).debug(Mockito.contains("sucheAngebote called"));

		// aber ohne facetten, wenn null
		verify(logger, Mockito.never()).debug(Mockito.contains(StudisuController.URL_PARAM_STUDIENFORM+"="));
		verify(logger, Mockito.never()).debug(Mockito.contains(StudisuController.URL_PARAM_HOCHSCHULART+"="));
		verify(logger, Mockito.never()).debug(Mockito.contains(StudisuController.URL_PARAM_UMKREIS+"="));
		verify(logger, Mockito.never()).debug(Mockito.contains(StudisuController.URL_PARAM_STUDIENTYP+"="));
		verify(logger, Mockito.never()).debug(Mockito.contains(StudisuController.URL_PARAM_FITFUERSTUDIUM+"="));
		verify(logger, Mockito.never()).debug(Mockito.contains(StudisuController.URL_PARAM_REGION+"="));
	}

	@Test
	public void testSucheAngeboteAngebotLoggingNone() {
		when(logger.isDebugEnabled()).thenReturn(Boolean.FALSE);
		// sucheAngebote
		ResponseEntity<StudienangebotsucheErgebnis> responseEntity = instance.sucheAngebote(
				studienfelder, studienfaecher, orte, 
				studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
				bundeslandFacette,
				page, reload, request, response);
		
		String beispielInhalt = responseEntity.getBody().getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);
		
		// im log darf zwar der constructor auftauchen (weil dieser leider nicht guarded ist)
		verify(logger).debug("controller constructed");
		// aber sonst kein debug log, also v.a. "kein methodeXY called"
		verify(logger, Mockito.never()).debug(Mockito.contains("called"));
	}
}