package de.ba.bub.studisu.common.integration.wcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.exception.HtmlContentClientException.Reason;
import de.ba.bub.studisu.common.model.StudienInformationDTO;
import de.ba.bub.studisu.studienfeldinformationen.model.LocalData;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfachFilm;

/**
 * Testklasse fuer {@link ContentClient}
 * 
 * waehrend wir fuer Powermocks mockstatic, um den Logger zu mocken den PowerMockRunner brauchen,
 * veraendert dieser das Verhalten bei expected exceptions sowie Whitebox-Zugriffen negativ
 * deswegen wurden verschiedene Unittests auf zwei Klassen aufgeteilt mit den verschiedenen
 *
 * PowerMockIgnore("javax.management.*") is there to get rid of logs like
 * java.lang.LinkageError: loader constraint violation
 * ERROR StatusLogger Unable to unregister MBeans: java.lang.LinkageError: javax/management/MBeanServer
 * 
 * @author KunzmannC
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
@PowerMockIgnore("javax.management.*")
public class ContentClientTest {

	// JAX-RS-RS
	@Mock 
	private Client client;
	@Mock
	private WebTarget target;
	@Mock
	private Invocation.Builder builder;

	@Mock
	private static Logger logger;

	private ContentClient instance;
	
	private final String wccUrl = "http://l1537022.sdst.sbaintern.de:57220";
	private final String wccEndpoint = "/cs/idcplg";
	private final String idcService = "BEN_INTERFACE_GET_BERUF";

	private final String url = wccUrl + wccEndpoint;
	
	private final static String MSG = "jUnit Exception";
	
	
	@BeforeClass
	public static void init() {
		//einschalten des loggings auf debug, damit auch diese branches durchlaufen werden
		//vgl. StudienangebotsucheControllerTest
	    mockStatic(LoggerFactory.class);
	    logger = mock(Logger.class);
	    when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);
	}
	
	@Before
	public void setUp() throws Exception {
		target = mock(WebTarget.class);
		when(client.target(url)).thenReturn(target);
		when(target.queryParam(anyString(), any())).thenReturn(target);
		builder = mock(Invocation.Builder.class);
		when(target.request(MediaType.TEXT_HTML)).thenReturn(builder);
		when(builder.get()).thenReturn(mock(Response.class));

		// auch fuer mediatype der bei datenstand genutzt wird, den builder liefern
		when(target.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		
		// INSTANZ AUFBAUEN UND SERVICEPORT MOCKEN
		instance = new ContentClient();
		Whitebox.setInternalState(instance, client, client);
		
		// Konfig injizieren / mocken
		Whitebox.setInternalState(instance, "wccUrl", wccUrl);
		Whitebox.setInternalState(instance, "wccEndpoint", wccEndpoint);
		Whitebox.setInternalState(instance, "idcService", idcService);

		// the loggers behaviour gets reset from tearDown beside its counts, 
		// therefore we set it after the reset
	    when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
	    when(logger.isInfoEnabled()).thenReturn(Boolean.TRUE);
	}
	
	@After
	public void tearDown()  {
		//because consequent tests could run into TooManyActualInvocations
		//we reset the logger and its counters here
		Mockito.reset(logger);
	}

	@Test
	public void testdoHealthcheckPositiv() throws URISyntaxException {
		URI uri = new URI(url);
		when(target.getUri()).thenReturn(uri);

		try {
			instance.doHealthcheck();
			
			// uri sollte geloggt werden
			verify(logger).debug(Mockito.startsWith("URI: {}"), Mockito.any(URI.class));
		} catch (HtmlContentClientException e) {
			fail("service liefert exception obwohl quellsystemmock positiv war, "+e.getClass()+" : "+e.getCause().getMessage());
		}
	}
	
	@Test
	public void testdoHealthcheckNegativ() {
		when(builder.get()).thenThrow(new RuntimeException(MSG));
		
		try {
			instance.doHealthcheck();
			
			fail("service sollte exception liefern bei negativem mock");
		} catch (HtmlContentClientException e) {
			// uri sollte geloggt werden
			verify(logger).debug(Mockito.startsWith("URI: {}"), Mockito.nullable(URI.class));
			assertTrue("sollte technischer fehler sein", Reason.TECHNICAL.equals(e.getReason()));
			assertTrue("message sollte durchgereicht werden", MSG.equals(e.getMessage()));
		}
	}
	
	@Test
	public void testgetDatenstand() throws HtmlContentClientException {
		Response datenstandResp = mock(Response.class);
		//http://http://l1537022.sdst.sbaintern.de:57220/cs/idcplg?IdcService=BEN_INTERFACE_GET_LAST_RELEASE_DATE&IsJson=1

		String docInfo =
			  "{"
			+ "  \"LocalData\": {"
			+ "    \"IdcService\": \"BEN_INTERFACE_GET_LAST_RELEASE_DATE\","
			+ "    \"IsJson\": \"1\","
			+ "    \"changedMonikers\": \"\","
			+ "    \"changedSubjects\": \"\","
			+ "    \"dUser\": \"anonymous\","
			+ "    \"idcToken\": \"\","
			+ "    \"lastReleaseDate\": \"{ts '2017-12-19 08:29:34.643'}\","
			+ "    \"localizedForResponse\": \"1\","
			+ "    \"refreshMonikers\": \"\","
			+ "    \"refreshSubMonikers\": \"\","
			+ "    \"refreshSubjects\": \"\""
			+ "  },"
			+ "  \"ResultSets\": {"
			+ "    \"UserAttribInfo\": {"
			+ "      \"currentRow\": 0,"
			+ "      \"fields\": ["
			+ "        { \"name\": \"dUserName\" },"
			+ "        { \"name\": \"AttributeInfo\" }"
			+ "      ],"
			+ "      \"rows\": ["
			+ "        ["
			+ "          \"anonymous\","
			+ "          \"account,#none,15,account,PEWebCenter/PU,1,account,webcenter/PU,1,account,PUBLIC,1,account,WCILS,1,role,guest,15,role,PersonalSpacesRole,1,role,webcenterUser,1\""
			+ "        ]"
			+ "      ]"
			+     "}"
			+   "}"
			+ "}";		
		
		when(datenstandResp.readEntity(String.class)).thenReturn(docInfo);
		when(builder.get()).thenReturn(datenstandResp);
		String datenstand = instance.getDatenstand();
		
		assertTrue("datenstand sollte nicht leer sein", !StringUtils.isEmpty(datenstand));
		assertTrue("datenstand sollte dem mock entsprechen", "19.12.2017".equals(datenstand));
		
		//logger ausgaben nicht geprueft, da fachlich ueber datenstand getestet
	}
	
	@Test
	public void testgetDatenstandFormat() throws HtmlContentClientException {
		Response datenstandResp = mock(Response.class);

		// analog obigem positivtest jedoch mit leerem datenstand
		//                                   \"lastReleaseDate\": \"{ts '2017-12-19 08:29:34.643'}\"
		String docInfo = "{ \"LocalData\": { \"lastReleaseDate\": \"{ts  2017-12-19 08:29:34.643 }\"     } }";
		when(datenstandResp.readEntity(String.class)).thenReturn(docInfo);
		when(builder.get()).thenReturn(datenstandResp);
		String datenstand = instance.getDatenstand();

		// warnung sollte geloggt werden
		verify(logger).warn(Mockito.startsWith("Datenstand "), Mockito.any(Throwable.class));
		assertTrue("datenstand sollte leer sein", "".equals(datenstand));
	}
	
	@Test(expected=HtmlContentClientException.class)
	public void testgetDatenstandServiceUnavailableException() throws HtmlContentClientException {
		when(builder.get()).thenThrow(new ServiceUnavailableException(MSG));
		try {
			instance.getDatenstand();
			fail("service sollte exception liefern bei negativem mock");
		} catch (HtmlContentClientException e) {
			assertEquals("falsche begruendung", Reason.TECHNICAL, e.getReason());
			assertEquals("falsche message", ContentClient.ERRORMESSAGE_SERVICE_UNAVAILABLE, e.getMessage());
			assertEquals("cause hat falsche message", MSG, e.getCause().getMessage());
			throw e;
		}
	}

	@Test(expected=HtmlContentClientException.class)
	public void testgetDatenstandServerErrorException() throws HtmlContentClientException {
		when(builder.get()).thenThrow(mock(ServerErrorException.class));
		try {
			instance.getDatenstand();
			fail("service sollte exception liefern bei negativem mock");
		} catch (HtmlContentClientException e) {
			assertEquals("falsche begruendung", Reason.TECHNICAL, e.getReason());
			assertEquals("falsche message", ContentClient.ERRORMESSAGE_WCCCALL_FAILED, e.getMessage());
			throw e;
		}
	}

	@Test(expected=HtmlContentClientException.class)
	public void testgetDatenstandProcessingException() throws HtmlContentClientException {
		when(builder.get()).thenThrow(mock(ProcessingException.class));
		try {
			instance.getDatenstand();
			fail("service sollte exception liefern bei negativem mock");
		} catch (HtmlContentClientException e) {
			assertEquals("falsche begruendung", Reason.TECHNICAL, e.getReason());
			assertEquals("falsche message", ContentClient.ERRORMESSAGE_WCCCALL_FAILED, e.getMessage());
			throw e;
		}
	}

	@Test(expected=HtmlContentClientException.class)
	public void testgetDatenstandNotFoundException() throws HtmlContentClientException {
		when(builder.get()).thenThrow(mock(NotFoundException.class));
		try {
			instance.getDatenstand();
			fail("service sollte exception liefern bei negativem mock");
		} catch (HtmlContentClientException e) {
			assertEquals("falsche begruendung", Reason.TECHNICAL, e.getReason());
			assertEquals("falsche message", ContentClient.ERRORMESSAGE_WCCCALL_FAILED, e.getMessage());
			throw e;
		}
	}

	//TODO throw IOException, um das handling abzudecken
	//     nicht hinbekommen mit akzeptablem zeiteinsatz und ist fachlich nicht spannender als die anderen exceptions
	/*@Test(expected=HtmlContentClientException.class)
	public void testgetDatenstandIOException() throws HtmlContentClientException, JsonParseException, JsonMappingException, IOException {
		try {
			//when(builder.get()).thenThrow(mock(IOException.class));
			ObjectMapper mapper = mock(ObjectMapper.class);
			when(mapper.readValue(Mockito.anyString(), new TypeReference<Map<String,Object>>(){})).thenThrow(new IOException(MSG));
			//when(mapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenThrow(new IOException(MSG));
			
			instance.getDatenstand();
			fail("service sollte exception liefern bei negativem mock");
		} catch (HtmlContentClientException e) {
			assertEquals("falsche begruendung", Reason.TECHNICAL, e.getReason());
			assertEquals("falsche message", ContentClient.ERRORMESSAGE_JSON_UNPARSABLE, e.getMessage());
			assertEquals("cause hat falsche message", MSG, e.getCause().getMessage());
			throw e;
		}
	}*/

	@Test
	public void testgetStudienfeldInformationen() throws HtmlContentClientException {
		when(target.request(MediaType.TEXT_HTML_TYPE)).thenReturn(builder);

		// entweder wir mocken den response sauber 
		// oder wir haben hier einen dummy und bringen dann aus gemockten jsoup die daten rein
		Response studfeldinfo = mock(Response.class);
		when(builder.get()).thenReturn(studfeldinfo);
		
		String content1 = "my string has to be filled"; 
		when(studfeldinfo.readEntity(String.class)).thenReturn("<div id=\"minininneContent\">"+content1+"</div>");

		//"93796;93701;93802;93986;94014;94163"
		StudienInformationDTO infoDto = instance.getStudienfeldInformationen(93802);
		
		assertTrue("content 1 sollte aus html extrahiert worden sein", 
				infoDto.getStudienfeldbeschreibungen().get(0).contains(content1));
	}

	@Test(expected=HtmlContentClientException.class)
	public void testgetStudienfeldInformationenEmpty() throws HtmlContentClientException {
		when(target.request(MediaType.TEXT_HTML_TYPE)).thenReturn(builder);
		Response studfeldinfo = mock(Response.class);
		when(builder.get()).thenReturn(studfeldinfo);
		
		// empty content 
		when(studfeldinfo.readEntity(String.class)).thenReturn("<div id=\"nurWerbung\"></div>");

		try {
			// should throw Exception
			instance.getStudienfeldInformationen(94163);
		} catch (HtmlContentClientException e) {
			
			assertEquals("reason falsch gesetzt", Reason.NO_CONTENT, e.getReason());
			assertEquals("message sollte gesetzt werden", ContentClient.ERRORMESSAGE_NO_CONTENT, e.getMessage());
			
			throw e;
		}
	}

	@Test//(expected=HtmlContentClientException.class)
	public void testgetStudienfeldInformationenServiceUnavailableException() throws HtmlContentClientException {
		when(target.request(MediaType.TEXT_HTML_TYPE)).thenThrow(new ServiceUnavailableException(MSG));

		try {
			instance.getStudienfeldInformationen(94163);
			fail("expected exception");
		} catch (HtmlContentClientException e) {
			assertEquals("sollte technischer fehler sein", Reason.TECHNICAL, e.getReason());
			assertEquals("message sollte gesetzt werden", ContentClient.ERRORMESSAGE_SERVICE_UNAVAILABLE_TEMPLATE, e.getMessage());
			assertEquals("cause message sollte durchgereicht werden", MSG, e.getCause().getMessage());
			//throw e;
		}
	}
	
	@Test//(expected=HtmlContentClientException.class)
	public void testgetStudienfeldInformationenServerErrorException() throws HtmlContentClientException {
		// error 504, server error
		Status s = Status.GATEWAY_TIMEOUT;
		when(target.request(MediaType.TEXT_HTML_TYPE)).thenThrow(new ServerErrorException(s));

		try {
			instance.getStudienfeldInformationen(94163);
			fail("expected exception");
		} catch (HtmlContentClientException e) {
			assertEquals("sollte technischer fehler sein", Reason.TECHNICAL, e.getReason());
			assertEquals("message sollte gesetzt werden", ContentClient.ERRORMESSAGE_SERVICE_NOT_REACHABLE, e.getMessage());
			assertTrue("cause message sollte statuscode enthalten", 
					e.getCause().getMessage().contains(String.valueOf(s.getStatusCode())));
			//throw e;
		}
	}

	
	@Test
	public void testgetStudienfachInformationen() throws HtmlContentClientException {
		when(target.request(MediaType.TEXT_HTML_TYPE)).thenReturn(builder);
		when(target.request()).thenReturn(builder);
		Response studfachinfo = mock(Response.class);
		when(builder.accept("application/json")).thenReturn(builder);
		Invocation invo = mock(Invocation.class);
		StudienfachFilm studienfachFilm = new StudienfachFilm();
		LocalData localdata = new LocalData();
		int filmId = 1000411;
		localdata.setXBenBpBatchBerufeTvID(filmId);
		studienfachFilm.setLocalData(localdata);
		when(invo.invoke(StudienfachFilm.class)).thenReturn(studienfachFilm);
		when(builder.buildGet()).thenReturn(invo);
		when(builder.get()).thenReturn(studfachinfo);

		String content10 = "studienfach a 10 beschreibung"; 
		String content01 = "studienfach a 01 bezeichnung"; 
		when(studfachinfo.readEntity(String.class)).thenReturn(
				"<a10-0> <div id=\"minininneContent\">"+content10+"</div> </a10-0>" +
				"<A01> <div id=\"minininneContent\">"+content01+"</div> <ul><li>eins</li></ul></A01>");

		//"93615;94028;94080"
		StudienInformationDTO infoDto = instance.getStudienfachInformationen(94028);
		
		assertTrue("content 01 sollte bezeichnung sein", 
				infoDto.getStudiengangsbezeichnungen().get(0).contains(content01));
		assertTrue("content 10 sollte aus html extrahiert worden sein", 
				infoDto.getStudienfeldbeschreibungen().get(0).contains(content10));

		assertEquals("film id sollte extrahiert worden sein", 
				filmId, infoDto.getStudienfachFilmId() );
	}
}