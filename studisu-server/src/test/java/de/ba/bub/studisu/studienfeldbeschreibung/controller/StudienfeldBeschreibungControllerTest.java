package de.ba.bub.studisu.studienfeldbeschreibung.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldbeschreibung.command.StudienfeldBeschreibungCommand;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class StudienfeldBeschreibungControllerTest {

	@Mock
	private static Logger logger;
	
	StudienfeldBeschreibungController instance;
	
	@Mock
	StudienfeldBeschreibungCommand command;
	
	@Mock
	WebRequest request;
	
	@Mock
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
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
	    
	    instance = new StudienfeldBeschreibungController(command);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}
	
	@Test
	public void testSucheAngebote() {
		String neutralKurzBezeichnung = "neutralKurzBezeichnung";
		String studienfeldbeschreibung = "studienfeldbeschreibung";
		StudienfeldBeschreibungErgebnis studienfeldBeschreibungErgebnis = new StudienfeldBeschreibungErgebnis();
		studienfeldBeschreibungErgebnis.setNeutralKurzBezeichnung(neutralKurzBezeichnung);
		List<String> studienfeldbeschreibungen = new ArrayList<>();
		studienfeldbeschreibungen.add(studienfeldbeschreibung);
		studienfeldBeschreibungErgebnis.setStudienfeldbeschreibungen(studienfeldbeschreibungen);
		when(command.execute(any(StudienfeldBeschreibungAnfrage.class))).thenReturn(studienfeldBeschreibungErgebnis);
		int dkzId = 8287681;
		ResponseEntity<StudienfeldBeschreibungErgebnis> responseEntity = instance.sucheAngebote(dkzId, request, response);
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Neutralkurzbezeichnung " + neutralKurzBezeichnung + " wird erwartet", 
				neutralKurzBezeichnung, responseEntity.getBody().getNeutralKurzBezeichnung());
		assertEquals("Studienfeldbeschreibung " + studienfeldbeschreibung + " wird erwartet", 
				studienfeldbeschreibung, responseEntity.getBody().getStudienfeldbeschreibungen().get(0));
	}
	
	@Test
	public void testSucheAngeboteMitException() {
		when(command.execute(any(StudienfeldBeschreibungAnfrage.class))).thenThrow(new ValidationException("message"));
		int dkzId = 8287681;
		ResponseEntity<StudienfeldBeschreibungErgebnis> responseEntity = instance.sucheAngebote(dkzId, request, response);
		assertEquals("Status Code 404 wird erwartet", HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		verify(logger).error(Mockito.contains("Validierung der DKZ-IDs fehlgeschlagen."), any(ValidationException.class));
		assertNull("Body von ResponseEntity ist null", responseEntity.getBody());
	}
}
