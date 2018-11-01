package de.ba.bub.studisu.signet.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.model.Signet;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.signet.command.SignetCommand;


@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class SignetControllerTest {
	
	private BildungsangebotService bildungsangebotService;
	private SignetController signetController;
	
	@Mock
	private static Logger logger;
	
	@InjectMocks
	private SignetCommand signetCommand;
	
	@Mock
	private Signet signet;
	
	@BeforeClass
	public static void init() {
	    mockStatic(LoggerFactory.class);
	    logger = mock(Logger.class);
	    when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);
	}

	@Before
	public void setUp() {
		bildungsangebotService = mock(BildungsangebotService.class);
	    signetCommand = new SignetCommand(bildungsangebotService);	
		signetController = new SignetController(signetCommand);
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
	    when(bildungsangebotService.getSignet(26383)).thenReturn(signet);
		when(signet.getDaten()).thenReturn("ThisRepresentsImageData".getBytes());
		when(signet.getMimetype()).thenReturn("image/png");
	}
	
	@After
	public void tearDown()  {
		Mockito.reset(logger);
	}
	
	/* no exception expected */
	@Test(expected = Test.None.class)
	public void testGetSignet() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(signetController).build();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(WebConfig.RequestMapping.Constants.SIGNET+"?banid=26383");
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertTrue(response.getContentLength() == 23);
		assertTrue(response.getContentType().compareTo("image/png") == 0);
		assertTrue(response.getStatus() == 200);
		
	}
}
