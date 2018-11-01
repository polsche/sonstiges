package de.ba.bub.studisu.common.integration.wcc;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.exception.HtmlContentClientException.Reason;
import de.ba.bub.studisu.common.model.StudienInformationDTO;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfachFilm;

/**
 * Client Wrapper zum Zugriff auf Content des BEN WCC nach Vorlage der ZAPP
 * Implementierung:
 * server/zapp-berufe/src/main/java/de/ba/zapp/beruf/content/ContentClient.java
 */
@Component
public class ContentClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentClient.class);

	private static final String WEBTARGET_PARAM_IS_JSON = "IsJson";
	private static final String WEBTARGET_PARAM_D_DOC_NAME = "dDocName";
	private static final String WEBTARGET_PARAM_REVISION_SELECTION = "RevisionSelectionMethod";
	private static final String WEBTARGET_PARAM_IDCSERVICE = "IdcService";
	private static final String WEBTARGET_PARAM_METAFLDS = "METAFLDS";
	private static final String WEBTARGET_PARAM_PROPERTY_ID = "PropertyID";
	private static final String WEBTARGET_PARAM_CONVERSION_TEMPLATE = "conversionTemplate";	
	
	private static final String BEN_INTERFACE_GET_METADATA_FOR_ID = "BEN_INTERFACE_GET_METADATA_FOR_ID";
	private static final String BEN_INTERFACE_GET_LAST_RELEASE_DATE = "BEN_INTERFACE_GET_LAST_RELEASE_DATE";
	private static final String BEN_GET_DOKUMENTENSTATUS = "BEN_GET_DOKUMENTENSTATUS";
	private static final String BEN_STATUS_PROPERTY = "BEN_STATUS_PROPERTY";
	private static final String REVISION_SELECTION_LATEST = "Latest";
	private static final String DOC_NAME_PREFIX_BP2_BE = "BP2_BE_";

	protected static final String ERRORMESSAGE_WCCCALL_FAILED = "Aufruf des WCC konnte nicht durchgeführt werden (z.B. UnkownHostException)";
	protected static final String ERRORMESSAGE_SERVICE_UNAVAILABLE = "Service am WCC nicht verfügbar";
	protected static final String ERRORMESSAGE_JSON_UNPARSABLE = "Antwort des WCC konnte nicht zu JSON geparst werden";
	
	protected static final String ERRORMESSAGE_SERVICE_NOT_REACHABLE = "Aufruf des WCC konnte nicht durchgeführt werden (z.B. UnkownHostException)";
	protected static final String ERRORMESSAGE_SERVICE_UNAVAILABLE_TEMPLATE = "Service am WCC nicht verfügbar, wahrscheinlich fehlendes Template";
	protected static final String ERRORMESSAGE_NO_CONTENT = "Die HTML-Response enthält keine Beschreibung.";

	protected static final String LOGMESSAGE_URI = "URI: {}";
	private static final String LOGMESSAGE_TIMEINFO = "*** TIME-INFO *** - ";

	private static final String DIV_ID_CONTENT = "div[id~=Content]";
	private static final String STUDIENFELD_XSL = "STUDIENFELD_XSL";
	private static final String STUDIENFACH_XSL = "STUDIENFACH_XSL";
	private static final String A01 = "a01 > div,li";
	private static final String A10_0 = "a10-0 > div";
	
	// JAX-RS-RS Client
	private Client client;

	@Value("${studisu.wcc.url}")
	private String wccUrl;

	@Value("${studisu.wcc.endpoint}")
	private String wccEndpoint;

	@Value("${studisu.wcc.service}")
	private String idcService;

	// java variablen fuer container deployments
	private String httpProxyHost;
	private String httpProxyPort;
	
	private final static String CONSTANT_PROXY_URI = "jersey.config.client.proxy.uri";
	
	/**
	 * C-tor.
	 *
	 * Initialisiert den WCC-Client.
	 */
	public ContentClient() {
		// create jersey client
		
		httpProxyHost = System.getProperty("http.proxyHost");
		httpProxyPort = System.getProperty("http.proxyPort");

		if (!StringUtils.isEmpty(httpProxyHost) && !StringUtils.isEmpty(httpProxyPort)) {
	        LOGGER.info("http.proxyHost="+httpProxyHost);
	        LOGGER.info("http.proxyPort="+httpProxyPort);
	        
	        ClientConfig config = new ClientConfig();
	        //config.property(ClientProperties.PROXY_URI, "192.168.1.254:8080");
	        String configuredProxyUri = httpProxyHost + ":" + httpProxyPort;
	        config.property(CONSTANT_PROXY_URI, configuredProxyUri);
	        LOGGER.info("ContentClient Proxy Settings found ClientBuilder.withConfig("+configuredProxyUri+")");
			// https://docs.oracle.com/javaee/7/api/javax/ws/rs/client/ClientBuilder.html
			client = ClientBuilder.newClient(config);
			
			// https://jersey.github.io/apidocs/latest/jersey/org/glassfish/jersey/client/JerseyClientBuilder.html
			// JerseyClientBuilder.withConfig(config);
			
		} else {
			LOGGER.info("ContentClient WITHOUT Proxy");
			client = ClientBuilder.newClient();
		}
	}

	/**
	 * Prüft ob der WCC erreichbar ist und Anfragen entgegennimmt.
	 * 
	 * @throws HtmlContentClientException
	 *
	 * @throws RuntimeException
	 *             wenn der Zugriff fehlschlägt.
	 */
	public void doHealthcheck() throws HtmlContentClientException {
		try {
			WebTarget t = client.target(wccUrl + wccEndpoint)
					.queryParam(WEBTARGET_PARAM_REVISION_SELECTION, REVISION_SELECTION_LATEST)
					.queryParam(WEBTARGET_PARAM_IDCSERVICE, BEN_GET_DOKUMENTENSTATUS)
					.queryParam(WEBTARGET_PARAM_PROPERTY_ID, BEN_STATUS_PROPERTY);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(LOGMESSAGE_URI, t.getUri());
			}
			t.request(MediaType.TEXT_HTML).get();
		} catch (Exception ex) {
			throw new HtmlContentClientException(Reason.TECHNICAL, ex.getMessage(), ex);
		}
	}

	/**
	 * Liefert eine Studienfeldbeschreibung
	 *
	 * @param id
	 *            DKZ-ID einer Studienfeldbeschreibung
	 * @return Studienfeldbeschreibung
	 * @throws HtmlContentClientException
	 */
	@Cacheable(cacheNames = "studienfeldInformationen", key = "{ #root.methodName, #id }")
	public StudienInformationDTO getStudienfeldInformationen(int id) throws HtmlContentClientException {
		return getWccContentById(id, STUDIENFELD_XSL);
	}

	/**
	 * Liefert eine Studienfachbeschreibung
	 *
	 * @param id
	 *            DKZ-ID einer Studienfachbeschreibung
	 * @return Studienfachbeschreibung
	 * @throws HtmlContentClientException
	 */
	@Cacheable(cacheNames = "studienfachInformationen", key = "{ #root.methodName, #id }")
	public StudienInformationDTO getStudienfachInformationen(int id) throws HtmlContentClientException {
		return getWccContentById(id, STUDIENFACH_XSL);
	}

	/**
	 * Liefert einen WCC-Inhalt gemäß übergebener ID und gewähltem
	 * Conversion-Template.
	 *
	 * @param id
	 *            DKZ-ID für WCC-Abfrage
	 * @param conversionTemplate
	 *            Zu verwendendes Conversion-Template
	 * @return
	 * @throws HtmlContentClientException
	 */
	private StudienInformationDTO getWccContentById(int id, String conversionTemplate) throws HtmlContentClientException {
        try {
            long startTs = System.currentTimeMillis();

        	WebTarget t = client.target(wccUrl + wccEndpoint)
                    .queryParam(WEBTARGET_PARAM_REVISION_SELECTION, REVISION_SELECTION_LATEST)
					.queryParam(WEBTARGET_PARAM_IDCSERVICE, idcService)
					.queryParam(WEBTARGET_PARAM_CONVERSION_TEMPLATE, conversionTemplate)
					.queryParam(WEBTARGET_PARAM_D_DOC_NAME, DOC_NAME_PREFIX_BP2_BE + id);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("ContentClient.getWccContentById(id="+ id+", conversionTemplate="+conversionTemplate+") ");
				LOGGER.debug(LOGMESSAGE_URI, t.getUri());
			}
            Builder builder = t.request(MediaType.TEXT_HTML_TYPE);
            Response response = builder.get();
            Document document = Jsoup.parse(response.readEntity(String.class));
			response.close();
			
			StudienInformationDTO wccContent =  parseDocument(document, conversionTemplate, id);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("*** TIME-INFO *** - getWccContentById("+id+", "+String.valueOf(conversionTemplate)+") = " 
			              + (System.currentTimeMillis()-startTs));
			}
			
			return wccContent;
					
        } catch(ServiceUnavailableException ex) {
            throw new HtmlContentClientException(Reason.TECHNICAL, ERRORMESSAGE_SERVICE_UNAVAILABLE_TEMPLATE, ex);
        } catch(ServerErrorException | ProcessingException | NotFoundException ex) {
           throw new HtmlContentClientException(Reason.TECHNICAL, ERRORMESSAGE_SERVICE_NOT_REACHABLE, ex);
        }
    }

	/**
	 * Parst die StudienfeldInformation. Wirft eine Exception, falls das HTML
	 * diese nicht enthält.
	 *
	 * @param html
	 * @return
	 * @throws HtmlContentClientException
	 */
	private StudienInformationDTO parseDocument(Document doc, String docType, int id) throws HtmlContentClientException {
		StudienInformationDTO studienInformationDTO = new StudienInformationDTO();
		
		if(docType.equals(STUDIENFELD_XSL)){
			studienInformationDTO.setStudienfeldbeschreibungen(getElementText(doc, DIV_ID_CONTENT));
		}
		else{
				Invocation wccJson =  getVideoId(id);
				studienInformationDTO.setStudienfeldbeschreibungen(getElementText(doc, A10_0));
				studienInformationDTO.setStudiengangsbezeichnungen(getElementText(doc, A01));
				studienInformationDTO.setStudienfachFilmId(getMultimediaId(wccJson));
			}
		
		if (studienInformationDTO.getStudienfeldbeschreibungen().isEmpty()) {
			throw new HtmlContentClientException(Reason.NO_CONTENT, ERRORMESSAGE_NO_CONTENT);
		}
		
		return studienInformationDTO;
	}
	
	/**
	 * StudienfeldInformation
	 * @param Document document create using Jsoup, String tag used for element selection in document  
	 * @return StudienfeldInformation Text-List
	 */
	private List<String> getElementText(Document document, String tag){
		List<String> studienfeldText = new ArrayList<String>();
		Elements content = document.select(tag);
		
		for (int i = 0, l = content.size(); i < l; i++) {
			String contentText = content.get(i).text();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Content der Response: " + contentText);
			}
			studienfeldText.add(contentText);	
		}
		
		return studienfeldText;
	}

	/**
	 * Prepare WCC Call
	 * @param  
	 * @return Invocation call to wcc
	 */
	
	// http://l1537022.sdst.sbaintern.de:57220/cs/idcplg?IdcService=BEN_INTERFACE_GET_METADATA_FOR_ID&dDocName=BP2_BE_93868&METAFLDS=xBenBpBatchBerufeTvID&IsJava=1
	private Invocation getVideoId(int id){
		Invocation wccResponceVideoId = client.target(wccUrl + wccEndpoint)
									.queryParam(WEBTARGET_PARAM_IDCSERVICE, BEN_INTERFACE_GET_METADATA_FOR_ID)
                				 	.queryParam(WEBTARGET_PARAM_D_DOC_NAME, DOC_NAME_PREFIX_BP2_BE + id)
                				 	.queryParam(WEBTARGET_PARAM_METAFLDS, "xBenBpBatchBerufeTvID")
									.queryParam(WEBTARGET_PARAM_IS_JSON, "1")
                                 	.request()
                                 	.accept("application/json")
                                 	.buildGet();
	   return wccResponceVideoId;

	}
	
	/**
	 * 
	 *   
	 * @return Video ID
	 */
	private int getMultimediaId(Invocation wccJson){
		StudienfachFilm studienfachFilm = wccJson.invoke(StudienfachFilm.class);
		return studienfachFilm.getLocalData().getXBenBpBatchBerufeTvID();
	}

	/**
	 * Ermittelt per WCC-Aufruf das Datum der letzten Datenaktualisierung
	 * 
	 * @return Datum des letzten Aktualisierungslaufs in WCC; falls der
	 *         ausgelesene Wert null ist, gibt die Methode Leerstring zurueck
	 * @throws HtmlContentClientException
	 *             Fehler beim WCC-Aufruf oder Parsen der Antwort
	 */
	@Cacheable(cacheNames = "datenstand", key = "{ #root.methodName }")
	public String getDatenstand() throws HtmlContentClientException {

		try {
			long startTs = System.currentTimeMillis();

			// Aufruf
			// http://<server>:<port>/cs/idcplg?IdcService=BEN_INTERFACE_GET_LAST_RELEASE_DATE&IsJson=1
			WebTarget t = client.target(wccUrl + wccEndpoint)
					.queryParam(WEBTARGET_PARAM_IDCSERVICE, BEN_INTERFACE_GET_LAST_RELEASE_DATE)
					.queryParam(WEBTARGET_PARAM_IS_JSON, 1);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(LOGMESSAGE_URI, t.getUri());
			}

			Builder builder = t.request(MediaType.APPLICATION_JSON_TYPE);
			Response response = builder.get();
			String docInfo = response.readEntity(String.class);
			response.close();
			
			String datenstand = parseDatenstand(docInfo);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(LOGMESSAGE_TIMEINFO + "ermittleDatenstand = " + (System.currentTimeMillis() - startTs));
			}

			return datenstand;

		} catch (ServiceUnavailableException ex) {
			throw new HtmlContentClientException(Reason.TECHNICAL, ERRORMESSAGE_SERVICE_UNAVAILABLE, ex);
		} catch (ServerErrorException | ProcessingException | NotFoundException ex) {
			throw new HtmlContentClientException(Reason.TECHNICAL, ERRORMESSAGE_WCCCALL_FAILED, ex);
		} catch (IOException ex) {
			throw new HtmlContentClientException(Reason.TECHNICAL, ERRORMESSAGE_JSON_UNPARSABLE, ex);
		}

	}

	/**
	 * Liest den Datenstand aus der vom WCC erhaltene Dokumenteninformation aus
	 * 
	 * @param docInfo
	 *            vom WCC erhaltene Dokumenteninformation als JSON-String
	 * @return ausgelesener Datenstand oder Leerstring, falls an der vermuteten Stelle
	 *         kein Wert zur Verfuegung steht
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private String parseDatenstand(String docInfo) throws JsonParseException, JsonMappingException, IOException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ContentClient.parseDatenstand(docInfo="+ docInfo+") called");
		}
		
		String datenstand = "";		
		if(docInfo == null) {
			return datenstand;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		Data data = mapper.readValue(docInfo, new TypeReference<Data>(){});
		String datenstandAusDocInfo = null;
		if(data.getLocalData() != null) {
			datenstandAusDocInfo = data.getLocalData().getLastReleaseDate();
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Datenstand aus WCC: " + (datenstandAusDocInfo == null ? null : datenstandAusDocInfo.trim()));
		}

		if (datenstandAusDocInfo != null) {
	   		datenstand = this.formatiereDatumsstand(datenstandAusDocInfo.trim());
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Datenstand: " + datenstand);
		}

		return datenstand;
	}

	/**
	 * Formatiert den uebergebenen Datumsstand als Datum ohne Zeit. Tritt dabei eine ParseException auf, 
	 * gibt die Methode Leerstring zurueck.
	 * 
	 * @param datenstand
	 *            zu formatierender Datenstand, enthält das tatsächliche Datum innerhalb von einfachen
	 *            Anführungszeichen
	 * @return formatierter Datenstand
	 */
	private String formatiereDatumsstand(String datenstand) {
		String datum;
		DateFormat wccFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.GERMANY);
		DateFormat newFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
		try {
			int dateStart = datenstand.indexOf('\'');
			int dateEnd = datenstand.lastIndexOf('\'');
			if (dateStart < 0 || dateStart == dateEnd) {
				throw new ParseException("Einfache Anführungszeichen vor/nach Datum wurden nicht gefunden.", -1);
			}
			
			// prüfe, ob Datum dem erwarteten Format entspricht
			Date myDatum = wccFormat.parse(datenstand.substring(dateStart + 1, dateEnd));
			// formatiere Datum ohne Zeitangabe
			datum = newFormat.format(myDatum);
		} catch (ParseException ex) {
			datum = "";
			LOGGER.warn("Datenstand " + datenstand + " hat kein gueltiges Format (dd.MM.yyyy HH:mm)", ex);
		}
		return datum;
	}

}