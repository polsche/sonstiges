package de.ba.bub.studisu.monitoring.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
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

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class MonitoringControllerTest {

	Logger logger;
	
	MonitoringController instance;
	
	@Mock
	DKZService dkzService;
	
	@Mock
	BildungsangebotService baClient;
	
	@Before
	public void setUp() {
		//einschalten des loggings auf debug, damit auch diese branches durchlaufen werden
	    mockStatic(LoggerFactory.class);
	    logger = mock(Logger.class, Mockito.CALLS_REAL_METHODS);
	    Mockito.doNothing().when(logger).debug(Mockito.anyString());
	    when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);

	    instance = new MonitoringController(dkzService, baClient);
	}

	@Test
	public void testGetMonitoringStatusFromBildungsangebotServiceIsNull(){
		ExternalServiceStatus nichtVerfuegbar = null; 
		when(baClient.getServiceStatus()).thenReturn(nichtVerfuegbar);
		
		ResponseEntity<?> responseEntity = instance.getMonitoringStatus();
		assertEquals("Status Code 500 wird erwartet", HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}

	@Test
	public void testGetMonitoringStatusFromBildungsangebotServiceIsNichtVerfuegbar(){
		ExternalServiceStatus nichtVerfuegbar = new ExternalServiceStatus("unit service", ExternalServiceStatus.Status.NICHT_VERFUEGBAR);
		when(baClient.getServiceStatus()).thenReturn(nichtVerfuegbar);
		
		ResponseEntity<?> responseEntity = instance.getMonitoringStatus();
		assertEquals("Status Code 500 wird erwartet", HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
	
	@Test
	public void testGetMonitoringStatusFromDKZServiceIsNull(){
		ExternalServiceStatus verfuegbar = null;
		when(baClient.getServiceStatus()).thenReturn(verfuegbar);
		
		ExternalServiceStatus nichtVerfuegbar = null;
		when(dkzService.getServiceStatus()).thenReturn(nichtVerfuegbar);
		
		ResponseEntity<?> responseEntity = instance.getMonitoringStatus();
		assertEquals("Status Code 500 wird erwartet", HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
	
	@Test
	public void testGetMonitoringStatusFromDKZServiceIsNichtVerfuegbar(){
		ExternalServiceStatus verfuegbar = new ExternalServiceStatus("unit service", ExternalServiceStatus.Status.VERFUEGBAR);
		when(baClient.getServiceStatus()).thenReturn(verfuegbar);
		
		ExternalServiceStatus nichtVerfuegbar = new ExternalServiceStatus("unit service", ExternalServiceStatus.Status.NICHT_VERFUEGBAR); 
		when(dkzService.getServiceStatus()).thenReturn(nichtVerfuegbar);
		
		ResponseEntity<?> responseEntity = instance.getMonitoringStatus();
		assertEquals("Status Code 500 wird erwartet", HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
	
	@Test
	public void testGetMonitoringStatusFromBildungsangebotServiceAndDKZServiceIsVerfuegbar(){
		ExternalServiceStatus verfuegbar = new ExternalServiceStatus("unit service", ExternalServiceStatus.Status.VERFUEGBAR);
		when(baClient.getServiceStatus()).thenReturn(verfuegbar);

		when(dkzService.getServiceStatus()).thenReturn(verfuegbar);
		
		ResponseEntity<?> responseEntity = instance.getMonitoringStatus();
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void testBehandleFehler() {
		Exception e = new Exception("Meldung");
		ResponseEntity<?> responseEntity = instance.behandleFehler(e);
		assertEquals("Status Code 500 wird erwartet", HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
}
