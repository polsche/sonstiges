package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.integration.soa.SamlHandler;
import de.ba.bub.studisu.common.integration.soa.X509CredentialFactory;
import de.ba.bub.studisu.common.integration.util.PfcrHandler;
import de.ba.bub.studisu.common.integration.util.PfcrLogMessageUtil;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.common.model.Signet;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotComparator;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServiceFaultException;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServicePortType;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServiceV1;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.calculateanzahlveranstaltungen_v_1.CalculateAnzahlVeranstaltungenAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.calculateanzahlveranstaltungen_v_1.CalculateAnzahlVeranstaltungenEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.calculateanzahlveranstaltungen_v_1.ParameterEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.FindOrteAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.FindOrteEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.FindStudienveranstaltungenAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.FindStudienveranstaltungenEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getsignetfuerbildungsanbieter_v_1.GetSignetFuerBildungsanbieterAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getsignetfuerbildungsanbieter_v_1.GetSignetFuerBildungsanbieterEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.GetStudienveranstaltungByIDAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.GetStudienveranstaltungByIDEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;

import static de.ba.bub.studisu.common.integration.WsAuthType.BASIC;
import static de.ba.bub.studisu.common.integration.WsAuthType.CONSAML;
import static de.ba.bub.studisu.common.integration.WsAuthType.SAML;
import static de.ba.bub.studisu.common.integration.WsAuthType.NONE;

/**
 * Connector to Bildungsangebotservice Created by loutzenhj on 03.04.2017.
 */
@Component
public class BildungsangebotServiceClient implements BildungsangebotService {

	private final static Logger LOGGER = LoggerFactory.getLogger(BildungsangebotServiceClient.class);
	private final static StudienangebotComparator STUDIENANGEBOT_COMPARATOR = new StudienangebotComparator();
	private transient BildungsangebotServicePortType bildungsServicePort = null;
	
	private StudienangebotInformationenMapper studienangebotInformationenMapper = new StudienangebotInformationenMapper();

	@Value("${bildungsangebotservice.ws.auth.type}")
	private String authType;
	@Value("${bildungsangebotservice.ws.auth.user}")
	private String user;
	@Value("${bildungsangebotservice.ws.auth.password}")
	private String password;
	@Value("${bildungsangebotservice.ws.service.location}")
	private String location;
	@Value("${bildungsangebotservice.ws.policy.name}")
	private String policyName;

	/*static {
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "100000");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dumpTreshold", "100000");
	}*/
	
	/*
	 * CKU unused on 13.06.17 not deleted as the values are set via pacify and
	 * this could potentially be needed again soon if unused after some time,
	 * please remove unused code
	 *
	 * @Value("${bildungsangebotservice.ws.wsdl.name}") private String wsdlName;
	 */

	@SuppressWarnings("rawtypes")
	@PostConstruct
	private void postConstruct() {
		BildungsangebotServiceV1 bildungsangebotService = new BildungsangebotServiceV1();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BildungsangebotServiceLocator createPort\n\tauthType: " + authType + "\n\tuser: " + user
					+ "\n\tpassword (size):" + (password == null ? 0 : password.length()) + "\n\tlocation: "
					+ location);
		}

		if (bildungsServicePort == null) {
			BindingProvider bindingProvider;
			List<Handler> soapHandler = new ArrayList<>();
			
			if (BASIC.getValue().equalsIgnoreCase(authType)) {
				bildungsServicePort = bildungsangebotService.getBildungsangebotServicePort();
				bindingProvider = (BindingProvider) bildungsServicePort;
                soapHandler = bindingProvider.getBinding().getHandlerChain();
				Map<String, Object> rc = bindingProvider.getRequestContext();
				rc.put(BindingProvider.USERNAME_PROPERTY, user); // Basic Auth User
				rc.put(BindingProvider.PASSWORD_PROPERTY, password); // Basic Auth Passwort
				rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location); // zu verwendende Webservice-Adresse
			}
			
			else if (SAML.getValue().equalsIgnoreCase(authType)) {
				SecurityPolicyFeature securityFeature = new SecurityPolicyFeature(policyName);
				//SAML-Policy aktivieren
				bildungsServicePort = bildungsangebotService.getBildungsangebotServicePort(securityFeature);
				bindingProvider = (BindingProvider) bildungsServicePort;
                soapHandler = bindingProvider.getBinding().getHandlerChain();
				bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
			}

			// SAML specific to container environment, see
			// 		SOA Service Spring-Boot/Container-Platform
			// 		http://confluence.webapp.sdst.sbaintern.de/pages/viewpage.action?pageId=54654108
			else if (CONSAML.getValue().equalsIgnoreCase(authType)) {
				bildungsServicePort = bildungsangebotService.getBildungsangebotServicePort();
				bindingProvider = (BindingProvider) bildungsServicePort;
                soapHandler = bindingProvider.getBinding().getHandlerChain();
				//String requesttimeout = "";   //soapProperties.getRequesttimeout()
				//String connectinotimeout= ""; //soapProperties.getConnecttimeout()
				bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
				//bindingProvider.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", requesttimeout);
				//bindingProvider.getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", connectinotimeout);
				X509CredentialFactory x509CredentialFactory = new X509CredentialFactory();
				//soapHandler.add(new MustUnderstandHandler());
				soapHandler.add(new SamlHandler(x509CredentialFactory.createX509Credential()));
				LOGGER.info("SamlHandler(X509Credential) added");
			}
			
			// Lokaler BildungsangebotService verwendet klassischerweise keine Authentifizierung.
			// Dies ist nur auf Entwicklerrechnern relevant und sollte sonst nicht verwendet werden!
			else if (NONE.getValue().equalsIgnoreCase(authType)) {
				bildungsServicePort = bildungsangebotService.getBildungsangebotServicePort();
				bindingProvider = (BindingProvider) bildungsServicePort;
                soapHandler = bindingProvider.getBinding().getHandlerChain();
				Map<String, Object> rc = bindingProvider.getRequestContext();
				rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location); // zu verwendende Webservice-Adresse
				LOGGER.warn("Unsicherer BildungsangebotService ohne Authentifizierung konfiguriert!");
			}
			
			else {
				LOGGER.error("Fehlkonfiguration bei authType=" + String.valueOf(authType));
				return;
			}

			bindingProvider.getBinding().setHandlerChain(soapHandler);
		}

		bildungsServicePort = (BildungsangebotServicePortType) Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class[] { BildungsangebotServicePortType.class, BindingProvider.class },
				new PfcrHandler(PfcrLogMessageUtil.SERVICE_BAS, (BindingProvider) bildungsServicePort));
	}

	/**
	 * service port accessor
	 * 
	 * @return service port
	 */
	private BildungsangebotServicePortType getPort() {
		return bildungsServicePort;
	}

	/**
	 * Service Status ping for monitoring
	 * 
	 * @return ExternalServiceStatus
	 * @throws CommonServiceException
	 */
	@Override
	public ExternalServiceStatus getServiceStatus() throws CommonServiceException {
		String statVersion = getPort().getServiceStatus().getWsversion();
		ExternalServiceStatus.Status status = StringUtils.isEmpty(statVersion)
				? ExternalServiceStatus.Status.NICHT_VERFUEGBAR : ExternalServiceStatus.Status.VERFUEGBAR;
		ExternalServiceStatus exstat = new ExternalServiceStatus("BildungsangebotService", status);
		return exstat;
	}

	/**
	 * Gibt zurueck Anzahl der Bildungsangebote pro <code>DKZ-Id</code>
	 *
	 * @param dkzId
	 * @return
	 */
	@Override
	@Cacheable(cacheNames = "angeboteanzahl", key = "{ #root.methodName, #dkzId }")
	public int holeAnzahlBildungsangebote(Integer dkzId) {
		int result = 0;

		ParameterEingabe param = new ParameterEingabe();
		param.setBreitengrad(UmkreisFacette.DE_MITTE.getKoordinaten().getBreitengrad());
		param.setLaengengrad(UmkreisFacette.DE_MITTE.getKoordinaten().getLaengengrad());
		param.getDkzId().add(dkzId);
		param.setUmkreis(UmkreisFacette.BUNDESWEIT.getUmkreisKm());

		if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("BildungsangebotServiceClient.holeAnzahlBildungsangebote(dkzId="+dkzId+") called");
		}

		try {
			CalculateAnzahlVeranstaltungenEingabe calculateAnzahlVeranstaltungenEingabe = 
					new CalculateAnzahlVeranstaltungenEingabe();
			calculateAnzahlVeranstaltungenEingabe.setParameterEingabe(param);

			CalculateAnzahlVeranstaltungenAusgabe anzahlVeranstaltungenAusgabe = getPort()
					.calculateAnzahlVeranstaltungen(calculateAnzahlVeranstaltungenEingabe);
			result = anzahlVeranstaltungenAusgabe.getErgebnis() != null
					? anzahlVeranstaltungenAusgabe.getErgebnis().getAnzahl() : 0;
		} catch (BildungsangebotServiceFaultException e) {
			LOGGER.trace(e.getMessage());
			throw new CommonServiceException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Gibt die Studienangebotinformationen für die Veranstaltung mit der
	 * übergebenen ID zurück.
	 * 
	 * @param vgId
	 *            ID der Veranstaltung (= Studienangebot).
	 * @return Studienangebotinformationen für die Veranstaltung mit der
	 *         übergebenen ID.
	 */
	@Override
	@Cacheable(cacheNames = "angebotinformationen", key = "{ #root.methodName, #vgId }")
	public StudienangebotInformationen holeStudienangebotInformationen(Integer vgId) {
		long startTs = System.currentTimeMillis();

		GetStudienveranstaltungByIDEingabe eingabe = new GetStudienveranstaltungByIDEingabe();
		de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.ParameterEingabe para =
				new de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.ParameterEingabe();
		para.setId(vgId);
		eingabe.setParameterEingabe(para);

		if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("BildungsangebotServiceClient.holeStudienangebotInformationen(vgId="+vgId+") called");
		}
		
		try {
			GetStudienveranstaltungByIDAusgabe ausgabe = getPort().getStudienveranstaltungByID(eingabe);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("*** TIME-INFO *** - getVeranstaltungById(eingabe) = "
						+ (System.currentTimeMillis() - startTs));
			}
			Studienveranstaltung studienveranstaltung = ausgabe.getStudienveranstaltung();
			StudienangebotInformationen result = null;
			if (isErgebnisVorhanden(studienveranstaltung)) {
				Veranstaltung veranstaltung = studienveranstaltung.getVeranstaltung().get(0);
				result = studienangebotInformationenMapper.map(studienveranstaltung, veranstaltung);
			}
			return result;
		} catch (BildungsangebotServiceFaultException e) {
			LOGGER.error("Failed to Load Studienangebot with id: " + vgId
					+ " due to BildungsangebotServiceFaultException: " + e.getMessage());
			// exception aufgetreten beim zugriff auf soa service - internal
			// server error werfen
			throw new CommonServiceException(e.getMessage(), e);
		} catch (WebServiceException wse) {
			LOGGER.error("Failed to Load Studienangebot with id: " + vgId + " due to WebServiceException: "
					+ wse.getMessage());
			// z.B. wenn Service down ist
			throw new CommonServiceException(wse.getMessage(), wse);
		}
	}

	/**
	 * Gibt zurück, ob das übergebene Studienveranstaltung, das vom Aufruf von
	 * getStundienveranstaltungById() stammt, ein Ergebnis enthält. Die studienveranstaltung
	 * darf nicht null sein und muss mindestens 1 Veranstaltung beinhalten.
	 * 
	 * @param studienveranstaltung
	 *            das zu prüfende Studienveranstaltung.
	 * @return true, wenn das übergebenen Bildungsangebot ein Ergebnis enthält,
	 *         andernfalls false.
	 */
	private boolean isErgebnisVorhanden(Studienveranstaltung studienveranstaltung) {
		return studienveranstaltung != null && studienveranstaltung.getVeranstaltung() != null
				&& !studienveranstaltung.getVeranstaltung().isEmpty();
	}

	/**
	 * Gibt die nach dem Abstand vom Abfrageort sortierte Liste von Studienangeboten, die mit den übergebenen
	 * Informationen gefunden werden konnten, zurück.
	 * 
	 * @param aort
	 *            Anfrageort mit Koordinaten.
	 * @param umkreis
	 *            Umkreis.
	 * @param studienfaecher
	 *            Studienfächer-IDs.
	 * @return eine Liste von Studienangeboten, die mit den übergebenen
	 *         Informationen gefunden werden konnten.
	 */
	@Override
	@Cacheable(cacheNames = "angeboteliste", key = "{ #root.methodName, #aort.cacheKey, #umkreis, #studienfaecher }")
	public List<Studienangebot> findStudienangebote(AnfrageOrt aort, int umkreis, List<Integer> studienfaecher) {
		long startTs = System.currentTimeMillis();

		FindStudienveranstaltungenEingabe eingabe = new FindStudienveranstaltungenEingabe();
		// build up parameter object
		de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.ParameterEingabe pe = 
				new de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.ParameterEingabe();

		pe.setUmkreis(umkreis);

		pe.getDkzId().addAll(studienfaecher);

		pe.setLaengengrad(aort.getKoordinaten().getLaengengrad());
		pe.setBreitengrad(aort.getKoordinaten().getBreitengrad());

		eingabe.setParameterEingabe(pe);

		if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("BildungsangebotServiceClient.findStudienangebote(aort="+aort+", umkreis="+umkreis+", studienfaecher="+studienfaecher+") called");
		}
		
		try {
			FindStudienveranstaltungenAusgabe ausgabe = getPort().findStudienveranstaltungen(eingabe);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("*** TIME-INFO *** - findStudienveranstaltungen(eingabe) = "
						+ (System.currentTimeMillis() - startTs));
			}
			List<Studienangebot> angeboteliste = StudienangeboteMapper.map(
					ausgabe.getStudienveranstaltung(), aort.getKoordinaten(), umkreis
			);

			// Sortierung der Angebotsliste mit Hilfe des definierten Comparators
			// (nach Abstand vom Suchort bzw. Verstaltungs-ID).
			Collections.sort(angeboteliste, STUDIENANGEBOT_COMPARATOR);

			return angeboteliste;
		} catch (BildungsangebotServiceFaultException e) {
			LOGGER.error("Failed to Find Studienveranstaltungen for Anfrage: Ort=" + aort.getOrtsname() + "|Umkreis="
					+ umkreis + "|Studienfächer=" + studienfaecher.toString()
					+ " due to BildungsangebotServiceFaultException: " + e.getMessage());
			// exception aufgetreten beim zugriff auf soa service - internal server error werfen
			throw new CommonServiceException(e.getMessage(), e);
		} catch (WebServiceException wse) {
			LOGGER.error("Failed to Find Studienveranstaltungen for Anfrage: Ort=" + aort.getOrtsname() + "|Umkreis="
					+ umkreis + "|Studienfächer=" + studienfaecher.toString() + " due to WebServiceException: "
					+ wse.getMessage());
			// z.B. wenn Service down ist
			throw new CommonServiceException(wse.getMessage(), wse);
		}
	}

	/**
	 * Gibt eine Liste von Orten, die mit den übergebenen Informationen gefunden
	 * werden konnten, zurück.
	 * 
	 * @param plz
	 *            die PLZ, nach der gesucht werden soll.
	 * @param ort
	 *            der Ortsname, nach dem gesucht werden soll.
	 * @return eine Liste von Orten, die mit den übergebenen Informationen
	 *         gefunden werden konnten.
	 */
	@Override
	@Cacheable(cacheNames = "orte", key = "{ #root.methodName, #plz, #ort }")
	public List<Ort> findOrte(String plz, String ort) {
		FindOrteEingabe request = getFindOrteEingabe(plz, ort);

		FindOrteAusgabe response = null;

		if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("BildungsangebotServiceClient.findOrte(plz="+plz+", ort="+ort+") called");
		}

		long startTs = System.currentTimeMillis();
		try {
			response = getPort().findOrte(request);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"*** TIME-INFO *** - findOrte(request) = " + (System.currentTimeMillis() - startTs));
			}
			return OrtMapper.map(response.getOrt());
		} catch (BildungsangebotServiceFaultException ebs) {
			//TODO pruefen ob hier nicht auch WARN genutzt werden sollte, wie sonst im client
			LOGGER.trace(ebs.getMessage());
			throw new CommonServiceException(ebs.getMessage(), ebs);
		} catch (SOAPFaultException esoa) {
			//TODO pruefen ob hier nicht auch WARN genutzt werden sollte, wie sonst im client
			LOGGER.trace(esoa.getMessage());
			throw new CommonServiceException(esoa.getMessage(), esoa);
		} catch (Exception ex) {
			// wir sollten uns der moeglichen exceptions bewusst sein, 
			// deswegen werden hier nicht naeher spezifizierte auf hoherem level geloggt
			LOGGER.error(ex.getMessage());
			throw new CommonServiceException(ex.getMessage(), ex);
		}
	}

	/**
	 * Erzeugt die Eingabe für die Suche nach Orten anhand der übergebenen
	 * Parameter.
	 * 
	 * @param plz
	 *            die PLZ, nach der gesucht werden soll.
	 * @param ort
	 *            der Ortsname, nach dem gesucht werden soll.
	 * @return die Eingabe für die Suche nach Orten mit den übergebenen
	 *         Parametern.
	 */
	private FindOrteEingabe getFindOrteEingabe(String plz, String ort) {
		FindOrteEingabe request = new FindOrteEingabe();
		de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.ParameterEingabe params = 
				new de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.ParameterEingabe();
		if (plz != null) {
			params.setPostleitzahl(plz);
		}
		if (ort != null) {
			params.setName(ort);
		}
		request.setParameterEingabe(params);
		return request;
	}

	/**
	 * Erzeugt die Eingabe für die Abfrage eines Signets
	 * 
	 * @param banid
	 *            Bildunsanbieter-ID für den das Signet abgefragt werden soll
	 * @return die Eingabe die Eingabe für die Abfrage eines Signets
	 */
	public GetSignetFuerBildungsanbieterEingabe getGetSignetFuerBildungsanbieterEingabe(Integer banid) {
		GetSignetFuerBildungsanbieterEingabe request = new GetSignetFuerBildungsanbieterEingabe();
		de.baintern.dst.services.wsdl.operativ.unterstuetzung.getsignetfuerbildungsanbieter_v_1.ParameterEingabe params = 
				new de.baintern.dst.services.wsdl.operativ.unterstuetzung.getsignetfuerbildungsanbieter_v_1.ParameterEingabe();
		if (banid != null) {
			params.setBanId(banid);
		}
		//Thumbnail anfragen
		params.setFullSize(false);
		
		request.setParameterEingabe(params);
		return request;
	}

	/* (non-Javadoc)
	 * @see de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService#getSignet(java.lang.Integer)
	 */
	@Override
	@Cacheable(cacheNames = "signets", key = "{ #root.methodName, #banid }")
	public Signet getSignet(Integer banid) {
		GetSignetFuerBildungsanbieterEingabe request = getGetSignetFuerBildungsanbieterEingabe(banid);

		GetSignetFuerBildungsanbieterAusgabe response = null;
		long startTs = System.currentTimeMillis();
		try {
			response = getPort().getSignetFuerBildungsanbieter(request);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"*** TIME-INFO *** - getSignetFuerBildungsanbieter(request) = " + (System.currentTimeMillis() - startTs));
			}

			return SignetMapper.map(response.getSignet());

		} catch (BildungsangebotServiceFaultException ebs) {
			//TODO pruefen ob hier nicht auch WARN genutzt werden sollte, wie sonst im client
			LOGGER.trace(ebs.getMessage());
			throw new CommonServiceException(ebs.getMessage(), ebs);
		} catch (SOAPFaultException esoa) {
			//TODO pruefen ob hier nicht auch WARN genutzt werden sollte, wie sonst im client
			LOGGER.trace(esoa.getMessage());
			throw new CommonServiceException(esoa.getMessage(), esoa);
		} catch (Exception ex) {
			// wir sollten uns der moeglichen exceptions bewusst sein, 
			// deswegen werden hier nicht naeher spezifizierte auf hoherem level geloggt
			LOGGER.error(ex.getMessage());
			throw new CommonServiceException(ex.getMessage(), ex);
		}
	}
}
