package de.ba.bub.studisu.versionsinformationen.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

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

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.exception.HtmlContentClientException.Reason;
import de.ba.bub.studisu.common.integration.wcc.ContentClient;
import de.ba.bub.studisu.common.service.ManifestService;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class VersionsInformationenServiceImplTest {

	@Mock
	private static Logger logger;
	
	VersionsInformationenServiceImpl instance;
	
	@Mock
	ContentClient contentClient;
	
	@Mock
	ManifestService manifestService;
	
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
	    
	    instance = new VersionsInformationenServiceImpl(contentClient, manifestService);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}
	
	@Test
	public void testGetVersionsInfo() throws HtmlContentClientException {
		String datenstand = "datenstand";
		when(contentClient.getDatenstand()).thenReturn(datenstand);
		String version = "Version2.0";
		when(manifestService.getImplementationVersion()).thenReturn(version);
		
		VersionsInformationenAnfrage anfrage = new VersionsInformationenAnfrage();
		VersionsInformationen versionsInfo = instance.getVersionsInfo(anfrage);
		assertTrue("message sollte Version2.0 enthalten sein",versionsInfo.getVersion().contains(version));
	}
	
	@Test
	public void testGetVersionsInfoVersionIsNull() throws HtmlContentClientException {
		String datenstand = "datenstand";
		when(contentClient.getDatenstand()).thenReturn(datenstand);
		String version = null;
		when(manifestService.getImplementationVersion()).thenReturn(version);
		
		VersionsInformationenAnfrage anfrage = new VersionsInformationenAnfrage();
		VersionsInformationen versionsInfo = instance.getVersionsInfo(anfrage);
		assertEquals("Version - wird erwartet", "-", versionsInfo.getVersion());
	}
	
	@Test
	public void testGetVersionsInfoVersionGetDatenbestandMitHtmlContentClientException() throws HtmlContentClientException {
		when(contentClient.getDatenstand()).thenThrow(new HtmlContentClientException(Reason.TECHNICAL, "message"));
		String version = "Version2.0";
		when(manifestService.getImplementationVersion()).thenReturn(version);
		VersionsInformationenAnfrage anfrage = new VersionsInformationenAnfrage();
		VersionsInformationen versionsInfo = instance.getVersionsInfo(anfrage);
		verify(logger).error(Mockito.contains("Fehler bei der Datenstand-Abfrage an WCC: "), any(HtmlContentClientException.class));
		assertTrue("message sollte Version2.0 enthalten sein",versionsInfo.getVersion().contains(version));
	}
	
	@Test
	public void testGetVersionsInfoVersionGetDatenbestandMitException() throws HtmlContentClientException {
		String datenstand = "datenstand";
		when(contentClient.getDatenstand()).thenReturn(datenstand);
		when(manifestService.getImplementationVersion()).thenThrow(new RuntimeException());
		VersionsInformationenAnfrage anfrage = new VersionsInformationenAnfrage();
		VersionsInformationen versionsInfo = instance.getVersionsInfo(anfrage);
		verify(logger).error(Mockito.contains("Fehler beim Auslesen der Version aus dem Manifest: "), any(Exception.class));
		assertEquals("Version - wird erwartet", "-", versionsInfo.getVersion());
	}
}
