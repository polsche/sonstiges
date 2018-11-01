package de.ba.bub.studisu.studienangebotinformationen.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.SuchortAbstand;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;
import de.ba.bub.studisu.studienangebotinformationen.command.StudienangebotInformationenCommand;
import de.ba.bub.studisu.studienangebotinformationen.model.StudienangebotInformationenAnfrage;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class StudienangebotInformationenControllerTest {
	
	@Mock
	private static Logger logger;
	
	StudienangebotInformationenController instance;
	
	@Mock
	StudienangebotInformationenCommand command;
	
	@Mock
	HttpServletResponse httpServletResponse;
	
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
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
	    
	    instance = new StudienangebotInformationenController(command);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}
	
	@Test
	public void testSucheStudienangebotWithStudienfaecher() {
		int id = 8287680;
		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = new Studienfaecher("93741");
		AnfrageOrte orte = new  AnfrageOrte("Ansbach, Mittelfranken_49.3006_10.5714");
		StudienformFacette studienformFacette = null;
		HochschulartFacette hochschulartFacette = null;
		UmkreisFacette umkreisFacette = new UmkreisFacette();
		StudientypFacette studientypFacette = null;
		FitFuerStudiumFacette fitFuerStudiumFacette = null;
		RegionenFacette bundeslandFacette = null;

		StudienangebotInformationen studienangebotInformationen = new StudienangebotInformationen();
		studienangebotInformationen.setId("8287680");
		studienangebotInformationen.setStudienfaecherCsv("93676;93741");
		studienangebotInformationen.setBezeichnung("Bautechnik/Bauingenieurtechnik Bachelor");
		
		when(command.execute(any(StudienangebotInformationenAnfrage.class))).thenReturn(studienangebotInformationen);

		List<StudienangebotWrapperMitAbstand> studienangebotWrapperMitAbstands = new ArrayList<>();

		//wrapper1
		SuchortAbstand suchortAbstand = new SuchortAbstand("Berlin", 0.0);
		List<SuchortAbstand> abstaende = new ArrayList<>();
		abstaende.add(suchortAbstand);
		
		Studienangebot studienangebot = new Studienangebot();
		studienangebot.setAbstand(0.0);
		studienangebot.setBildungsanbieterName("Technische Universität Berlin");
		studienangebot.setId("8287681");
		StudienangebotWrapperMitAbstand studienangebotWrapperMitAbstand = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);
		studienangebotWrapperMitAbstands.add(studienangebotWrapperMitAbstand);
		
		//wrapper2 = previous
		studienangebot = new Studienangebot();
		studienangebot.setAbstand(0.0);
		studienangebot.setBildungsanbieterName("Technische Universität Berlin");
		studienangebot.setId("8287682");
		studienangebotWrapperMitAbstand = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);
		studienangebotWrapperMitAbstands.add(studienangebotWrapperMitAbstand);

		//wrapper3 = gesuchter eintrag
		studienangebot = new Studienangebot();
		studienangebot.setAbstand(0.0);
		studienangebot.setBildungsanbieterName("Technische Universität Berlin");
		studienangebot.setId("8287680");
		studienangebotWrapperMitAbstand = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);
		studienangebotWrapperMitAbstands.add(studienangebotWrapperMitAbstand);
		
		//wrapper4 = next
		studienangebot = new Studienangebot();
		studienangebot.setAbstand(0.0);
		studienangebot.setBildungsanbieterName("Technische Universität Berlin");
		studienangebot.setId("8287679");
		studienangebotWrapperMitAbstand = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);
		studienangebotWrapperMitAbstands.add(studienangebotWrapperMitAbstand);

		when(command.sucheStudienangebote(any(StudienangebotsucheAnfrage.class))).thenReturn(studienangebotWrapperMitAbstands);

		// anfrage
		ResponseEntity<StudienangebotInformationen> responseEntity = 
				instance.sucheStudienangebot(id, studienfelder, studienfaecher, orte, studienformFacette, 
						hochschulartFacette, umkreisFacette, studientypFacette, fitFuerStudiumFacette,
						bundeslandFacette,
						httpServletResponse);
		
		// pruefe status
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		
		// pruefe inhalt
		StudienangebotInformationen infos = responseEntity.getBody();
		assertEquals("gesuchte id sollte gefunden worden sein ", String.valueOf(id), infos.getId());
		assertEquals("bezeichnung sollte passen", "Bautechnik/Bauingenieurtechnik Bachelor", infos.getBezeichnung() );
		assertEquals("studienfaecher csv sollte übernommmen worden sein",
				"93676;93741", infos.getStudienfaecherCsv());
		
		// pruefe navigation info
		assertEquals("previous sollte das vorherige aus liste sein", infos.getPrevElementId(), "8287682") ;
		assertEquals("next sollte das folgende aus liste sein", infos.getNextElementId(), "8287679") ;
		assertEquals("wir sollten auf page 1 sein", 1, infos.getCurrentPage());
		
		// pruefe logging
		verify(logger).debug(startsWith("sucheStudienangebot called"));
		verify(logger).debug(contains(StudisuController.URL_PARAM_STUDIENANGEBOT_ID+"="+id));
	}
	
	@Test
	public void testSucheStudienangebotWithStudienfelderAndLoggerDisabled() {
		int id = 8287680;
		Studienfelder studienfelder = new Studienfelder("93741");
		Studienfaecher studienfaecher = null;
		AnfrageOrte orte = new  AnfrageOrte("Ansbach, Mittelfranken_49.3006_10.5714");
		StudienformFacette studienformFacette = null;
		HochschulartFacette hochschulartFacette = null;
		UmkreisFacette umkreisFacette = new UmkreisFacette();
		StudientypFacette studientypFacette = null;
		FitFuerStudiumFacette fitFuerStudiumFacette = null;
		RegionenFacette bundeslandFacette = null;

		StudienangebotInformationen studienangebotInformationen = new StudienangebotInformationen();
		studienangebotInformationen.setStudienfaecherCsv("93676;93741");
		studienangebotInformationen.setBezeichnung("Bautechnik/Bauingenieurtechnik Bachelor");
		
		when(logger.isDebugEnabled()).thenReturn(Boolean.FALSE);
		
		when(command.execute(any(StudienangebotInformationenAnfrage.class))).thenReturn(studienangebotInformationen);
		
		// anfrage
		ResponseEntity<StudienangebotInformationen> responseEntity = instance.sucheStudienangebot(id, studienfelder,
				studienfaecher, orte, studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette,
				fitFuerStudiumFacette, bundeslandFacette, httpServletResponse);
		
		// pruefe status
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		
		// pruefe logging - hier kein debug
		verify(logger, never()).debug(anyString());
	}
	
	@Test
	public void testSucheStudienangebotStudienfelderAndStudienfaecherAreNull() {
		int id = 8287680;
		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte orte = new  AnfrageOrte("Ansbach, Mittelfranken_49.3006_10.5714");
		StudienformFacette studienformFacette = null;
		HochschulartFacette hochschulartFacette = null;
		UmkreisFacette umkreisFacette = new UmkreisFacette();
		StudientypFacette studientypFacette = null;
		FitFuerStudiumFacette fitFuerStudiumFacette = null;
		RegionenFacette bundeslandFacette = null;

		StudienangebotInformationen studienangebotInformationen = new StudienangebotInformationen();
		studienangebotInformationen.setStudienfaecherCsv("93676;93741");
		studienangebotInformationen.setBezeichnung("Bautechnik/Bauingenieurtechnik Bachelor");
		
		when(command.execute(any(StudienangebotInformationenAnfrage.class))).thenReturn(studienangebotInformationen);
		
		// anfrage
		ResponseEntity<StudienangebotInformationen> responseEntity = instance.sucheStudienangebot(id, studienfelder,
				studienfaecher, orte, studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette,
				fitFuerStudiumFacette, bundeslandFacette, httpServletResponse);
		
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSucheStudienangebotStudienAngebotIsNull() {
		int id = 8287680;
		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte orte = new  AnfrageOrte("Ansbach, Mittelfranken_49.3006_10.5714");
		StudienformFacette studienformFacette = null;
		HochschulartFacette hochschulartFacette = null;
		UmkreisFacette umkreisFacette = new UmkreisFacette();
		StudientypFacette studientypFacette = null;
		FitFuerStudiumFacette fitFuerStudiumFacette = null;
		RegionenFacette bundeslandFacette = null;
		
		when(command.execute(any(StudienangebotInformationenAnfrage.class))).thenReturn(null);
		
		// anfrage
		ResponseEntity<StudienangebotInformationen> responseEntity = instance.sucheStudienangebot(id, studienfelder,
				studienfaecher, orte, studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette,
				fitFuerStudiumFacette, bundeslandFacette, httpServletResponse);
		assertEquals("Status Code 200 wird erwartet", HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSucheStudienangebotFacettenNotNull() {
		int id = 8287680;
		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte orte = new  AnfrageOrte("Ansbach, Mittelfranken_49.3006_10.5714");
		StudienformFacette studienformFacette = new StudienformFacette();
		HochschulartFacette hochschulartFacette = new HochschulartFacette();
		UmkreisFacette umkreisFacette = new UmkreisFacette();
		StudientypFacette studientypFacette = new StudientypFacette();
		FitFuerStudiumFacette fitFuerStudiumFacette = null;
		RegionenFacette bundeslandFacette = null;

		StudienangebotInformationen studienangebotInformationen = new StudienangebotInformationen();
		studienangebotInformationen.setStudienfaecherCsv("93676;93741");
		studienangebotInformationen.setBezeichnung("Bautechnik/Bauingenieurtechnik Bachelor");
		
		when(command.execute(any(StudienangebotInformationenAnfrage.class))).thenReturn(studienangebotInformationen);
		
		ResponseEntity<StudienangebotInformationen> responseEntity = instance.sucheStudienangebot(id, studienfelder,
				studienfaecher, orte, studienformFacette, hochschulartFacette, umkreisFacette, studientypFacette,
				fitFuerStudiumFacette, bundeslandFacette, httpServletResponse);
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
	}
}
