package de.ba.bub.studisu.studienfach.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienfach.command.StudienfachSucheCommand;
import de.ba.bub.studisu.studienfach.model.StudienfachAnfrage;
import de.ba.bub.studisu.studienfach.model.StudienfachErgebnis;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class StudienfachControllerTest {
	@Mock
	private static Logger logger;
	
	StudienfachController instance;
	
	@Mock
	StudienfachSucheCommand command;
	
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
	    
	    instance = new StudienfachController(command);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}

	@Test
	public void testSucheStudienfach() {
		Studienfach studienfach = new Studienfach(8287681, "Studienfachname");
		List<Studienfach> studienfachListe = new ArrayList<>();
		studienfachListe.add(studienfach);
		StudienfachErgebnis studienfachErgebnis = new StudienfachErgebnis(studienfachListe);
		Studienfaecher studienfaecher = new Studienfaecher("");
		when(command.execute(any(StudienfachAnfrage.class))).thenReturn(studienfachErgebnis);
		
		// set caching time which is injected by config via spring
		String cachingTime = "1234";
		Whitebox.setInternalState(instance, "staticContentCachingTime", cachingTime);
		
		ResponseEntity<List<Studienfach>> responseEntity = instance.sucheStudienfach("Studienfach", studienfaecher, response);

		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("DKZ ID 8287681 wird erwartet", 8287681, responseEntity.getBody().get(0).getDkzId());

		//der http cache control header sollte gesetzt werden
		//verify(response).setHeader(Mockito.eq("Cache-Control"), Mockito.startsWith("public,max-age="));
		verify(response).setHeader("Cache-Control", "public,max-age="+cachingTime);
	}
}
