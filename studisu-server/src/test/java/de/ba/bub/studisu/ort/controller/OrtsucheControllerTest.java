package de.ba.bub.studisu.ort.controller;

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

import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.ort.command.OrtsucheCommand;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class OrtsucheControllerTest {

	@Mock
	private static Logger logger;
	
	OrtsucheController instance;
	
	@Mock
	OrtsucheCommand ortsucheCommand;
	
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
		// the loggers behaviour gets reset from tearDown beside its counts, 
		// therefore we set it after the reset
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);

		instance = new OrtsucheController(ortsucheCommand);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}

	@Test
	public void testSucheOrte() {
		List<Ort> orte = new ArrayList<>();
		Double breitengrad = 52.5183;
		Double laengengrad = 13.4058;
		String bundesland = "Auenland";
		orte.add(new Ort("Berlin", "14197", breitengrad, laengengrad, bundesland));
		OrtsucheErgebnis ortsucheErgebnis = new OrtsucheErgebnis(orte);
		when(ortsucheCommand.execute(any(OrtsucheAnfrage.class))).thenReturn(ortsucheErgebnis);
		String ortsuche = "Berlin";
		
		ResponseEntity<List<Ort>> responseEntity = instance.sucheOrte(ortsuche, httpServletResponse);

		// status ok
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());

		// inhalt pruefen
		List<Ort> responseEntityOrte = responseEntity.getBody();
		Ort testOrt = responseEntityOrte.get(0);
		assertEquals("Ort Name wird erwartet", "Berlin", testOrt.getName());
		assertEquals("Ort PLZ wird erwartet", "14197", testOrt.getPostleitzahl());
		assertEquals("Ort Breitengrad wird erwartet", breitengrad, testOrt.getBreitengrad());
		assertEquals("Ort Laengengrad wird erwartet", laengengrad, testOrt.getLaengengrad());
		assertEquals("Ort Breitengrad wird erwartet", bundesland, testOrt.getBundesland());
		
		// logging
		verify(logger).debug(Mockito.contains("sucheOrte called"));
	}

	@Test
	public void testSucheOrteCaching() {
		List<Ort> orte = new ArrayList<>();
		orte.add(new Ort("Berlin", "14197", null, null, null));
		OrtsucheErgebnis ortsucheErgebnis = new OrtsucheErgebnis(orte);
		when(ortsucheCommand.execute(any(OrtsucheAnfrage.class))).thenReturn(ortsucheErgebnis);
		String ortsuche = "Berlin";
		
        // set caching time which is injected by config via spring
        String cachingTime = "1234";
        Whitebox.setInternalState(instance, "staticContentCachingTime", cachingTime);

        instance.sucheOrte(ortsuche, httpServletResponse);
		
        //der http cache control header sollte gesetzt werden
        //verify(response).setHeader(Mockito.eq("Cache-Control"), Mockito.startsWith("public,max-age="));
        verify(httpServletResponse).setHeader("Cache-Control", "public,max-age="+cachingTime);
	}
}
