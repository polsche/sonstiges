package de.ba.bub.studisu.studienfeldinformationen.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

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
import org.springframework.web.context.request.WebRequest;

import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldinformationen.command.StudienfeldInformationenCommand;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class StudienfeldInformationenControllerTest {

	@Mock
	private static Logger logger;
	
	StudienfeldInformationenController instance;
	
	@Mock
	StudienfeldInformationenCommand command;
	
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
	    
	    instance = new StudienfeldInformationenController(command);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}
	
	@Test
	public void testSucheAngebote() {
		String dkzId = "8287681";

		// set caching time which is injected by config via spring
		String cachingTime = "1234";
		Whitebox.setInternalState(instance, "staticContentCachingTime", cachingTime);

		ResponseEntity<StudienfeldInformationenErgebnis> responseEntity = instance.sucheAngebote(dkzId, request, response);
		
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		//TODO ergebnis pruefen
		
		//der http cache control header sollte gesetzt werden
		//verify(response).setHeader(Mockito.eq("Cache-Control"), Mockito.startsWith("public,max-age="));
		verify(response).setHeader("Cache-Control", "public,max-age="+cachingTime);
	}
	
	@Test(expected=ValidationException.class)
	public void testSucheAngeboteDkzIdAsString() {
		try {
			String dkzId = "TEST";
			
			instance.sucheAngebote(dkzId, request, response);
		} catch (ValidationException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("DKZ-ID hat ungültiges Format"));
			verify(logger).info(Mockito.contains("DKZ-ID ist nicht numerisch."), Mockito.any(Throwable.class));
			throw e;
		}
	}
}