package de.ba.bub.studisu.versionsinformationen.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import de.ba.bub.studisu.versionsinformationen.command.VersionsInformationenCommand;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class VersionsInformationenControllerTest {
	@Mock
	private static Logger logger;
	
	VersionsInformationenController instance;
	
	@Mock
	VersionsInformationenCommand command;
	
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
	    
	    instance = new VersionsInformationenController(command);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}

	@Test
	public void testGetVersionsInfo() {
		VersionsInformationen version = new VersionsInformationen();
		version.setVersion("Version2.0");
		when(command.execute(any(VersionsInformationenAnfrage.class))).thenReturn(version);

		ResponseEntity<VersionsInformationen> responseEntity = instance.getVersionsInfo();
		
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Version Version2.0 wird erwartet", "Version2.0", responseEntity.getBody().getVersion());
		
		verify(logger).debug(Mockito.contains("getVersionsInfo called"));
	}
}
