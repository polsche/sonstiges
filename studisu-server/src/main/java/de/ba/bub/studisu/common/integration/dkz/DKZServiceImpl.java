package de.ba.bub.studisu.common.integration.dkz;

import static de.ba.bub.studisu.common.integration.WsAuthType.BASIC;
import static de.ba.bub.studisu.common.integration.WsAuthType.CONSAML;
import static de.ba.bub.studisu.common.integration.WsAuthType.SAML;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.integration.soa.SamlHandler;
import de.ba.bub.studisu.common.integration.soa.X509CredentialFactory;
import de.ba.bub.studisu.common.integration.util.PfcrHandler;
import de.ba.bub.studisu.common.integration.util.PfcrLogMessageUtil;
import de.ba.bub.studisu.common.model.BeschreibungsZustand;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.FindSystematikRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.FindSystematikResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.SystematikPositionType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.BerufBasisType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.BerufskundlicheGruppe2014Type;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.BeschreibungszustandType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindAusbildungenBySuchwortRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindAusbildungenBySuchwortResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindBerufeFuerStudienbereichRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindBerufeFuerStudienbereichResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindStudienbereicheByObercodenrRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.FindStudienbereicheByObercodenrResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.GetBerufeByIDRequestType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.GetBerufeByIDResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.QualifikationsniveauType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.SystematikBasisType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.ZustandType;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZService;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServiceFaultException;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServicePortType;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.FehlerResponseMessage;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;

/**
 * Implementierung des {@link DKZService}.
 * <p>
 * Hinweis: Die Menge an importierten Klassen ist aufgrund der Struktur der
 * DKZ-Schnittstelle erforderlich, daher muss sie von Sonar ignoriert werden (
 * "False Positive").
 *
 * @author OettlJ
 */
@Service("dkz") //NOSONAR
public class DKZServiceImpl implements de.ba.bub.studisu.common.integration.dkz.DKZService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DKZServiceImpl.class);

    @Value("${dkzservice.ws.auth.type}")
    private String authType;
    @Value("${dkzservice.ws.auth.user}")
    private String user;
    @Value("${dkzservice.ws.auth.password}")
    private String password;
    @Value("${dkzservice.ws.service.location}")
    private String location;
    @Value("${dkzservice.ws.policy.name}")
    private String policyName;

    private transient DKZServicePortType dkzServicePort = null;
    
	/*static {
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "100000");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dumpTreshold", "100000");
	}*/

    /**
     * Initialisierung der Verbindung zum DKZService
     */
    @SuppressWarnings("rawtypes")
	@PostConstruct
    private void postConstruct() {
        DKZService dkzService = new DKZService();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("DKZService createPort\n\tauthType: " + authType + "\n\tuser: " + user + "\n\tpassword (size):"
                    + (password == null ? 0 : password.length()) + "\n\tlocation: " + location);
        }

        // TODO koennte durch spring autowiring ersetzt werden, vgl.
        //      SOA Service Spring-Boot/Container-Platform
        //      http://confluence.webapp.sdst.sbaintern.de/pages/viewpage.action?pageId=54654108
        if (dkzServicePort == null) {
			BindingProvider bindingProvider = null;
			List<Handler> soapHandler = new ArrayList<>();

            // BASIC Auth
            if (BASIC.getValue().equalsIgnoreCase(authType)) {
                dkzServicePort = dkzService.getDKZServicePort();
                bindingProvider = (BindingProvider) dkzServicePort;
                soapHandler = bindingProvider.getBinding().getHandlerChain();                
                
                Map<String, Object> rc = ((BindingProvider) dkzServicePort).getRequestContext();
                // Basic Auth User
                rc.put(BindingProvider.USERNAME_PROPERTY, user);
                // Basic Auth Passwort
                rc.put(BindingProvider.PASSWORD_PROPERTY, password);
                // zu verwendende Webservice-Adresse
                rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);

            } else if (SAML.getValue().equalsIgnoreCase(authType)) {
            	SecurityPolicyFeature securityFeature = new SecurityPolicyFeature(policyName);
                WebServiceFeature[] features = new WebServiceFeature[]{securityFeature};
                dkzServicePort = dkzService.getDKZServicePort(features);
            	bindingProvider = (BindingProvider) dkzServicePort;
                soapHandler = bindingProvider.getBinding().getHandlerChain();
                // zu verwendende Webservice-Adresse
                bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
            }

            // SAML specific to container environment, see
            // 		SOA Service Spring-Boot/Container-Platform
            // 		http://confluence.webapp.sdst.sbaintern.de/pages/viewpage.action?pageId=54654108
            else if (CONSAML.getValue().equalsIgnoreCase(authType)) {
                dkzServicePort = dkzService.getDKZServicePort();
                bindingProvider = (BindingProvider) dkzServicePort;
                X509CredentialFactory x509CredentialFactory = new X509CredentialFactory();
                soapHandler = bindingProvider.getBinding().getHandlerChain();
                soapHandler.add(new SamlHandler(x509CredentialFactory.createX509Credential()));
				LOGGER.info("SamlHandler(X509Credential) added");
                bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
            } else {
				LOGGER.error("Fehlkonfiguration bei authType=" + String.valueOf(authType));
				throw new IllegalStateException("Fehlkonfiguration bei authType=" + String.valueOf(authType));
				//return;
			}

			bindingProvider.getBinding().setHandlerChain(soapHandler);
        }

        dkzServicePort = (DKZServicePortType) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{DKZServicePortType.class, BindingProvider.class},
                new PfcrHandler(PfcrLogMessageUtil.SERVICE_DKZ, (BindingProvider) dkzServicePort));
    }

    private DKZServicePortType getPort() {
        return dkzServicePort;
    }

    /**
     * Liefert alle Systematiken, die Studienfelder
     * repräsentieren und zur angegebenen Ober-Codenummer gehören.
     *
     * @param obercodenr Ober-Codenummer; nicht null
     * @return Liste von Systematiken
     */
    @Override
    @Cacheable(cacheNames = "systematik", key = "{ #root.methodName, #obercodenr }")
    public List<Systematik> findStudienfeldgruppeSystematiken(String obercodenr) {
        if (StringUtils.isEmpty(obercodenr)) {
            throw new IllegalArgumentException("obercodenr empty...");
        }
        long startTs = System.currentTimeMillis();

        FindStudienbereicheByObercodenrRequestType request = new FindStudienbereicheByObercodenrRequestType();
        request.setObercodenr(obercodenr);

        FindStudienbereicheByObercodenrResponseType response = null;
        try {
            response = getPort().findStudienbereicheByObercodenr(request);

            if (LOGGER.isDebugEnabled()) {
                StringBuilder debug = new StringBuilder();
                debug.append("*** TIME-INFO *** - findStudienbereichSystematiken(");
                debug.append(String.valueOf(obercodenr));
                debug.append(") = ").append(System.currentTimeMillis() - startTs);
                LOGGER.debug(debug.toString());
            }

        } catch (DKZServiceFaultException e) {
            LOGGER.trace(e.getMessage());
            throw new CommonServiceException(e.getMessage(), e);
        } catch (WebServiceException wse) {
            LOGGER.error("Failed to findStudienbereichSystematiken for obercodenr: " + String.valueOf(obercodenr)
                    + " due to WebServiceException: " + wse.getMessage());
            // z.B. wenn Service down ist
            throw new CommonServiceException(wse.getMessage(), wse);
        }

        // Ergebnis in Liste von Systematik-Objekten mappen
        return mapFindStudienbereicheByObercodenrResponse(response);
    }

    /**
     * Liefert alle Systematiken anhand einer DKZ-Id. Eine weitere Filterung
     * kann durch optionales Hinzufügen von CodeNummern erfolgen.
     *
     * @param dkzid
     * @param codeNummern Liste von Codenummern zum Filtern des Requests.
     * @return Liste aller gefundenen Systematiken
     */
    @Override
    @Cacheable(cacheNames = "systematik", key = "{ #root.methodName, #dkzid, #codeNummern }")
    public List<Systematik> findSystematik(int dkzid, List<String> codeNummern) {
        long startTs = System.currentTimeMillis();

        FindSystematikRequestType findSystematikRequestType = new FindSystematikRequestType();
        findSystematikRequestType.getID().add(dkzid);

        for (String codeNr : codeNummern) {
            findSystematikRequestType.getCodenr().add(codeNr);
        }

        FindSystematikResponseType findSystematikResponseType = null;
        try {
            findSystematikResponseType = getPort().findSystematik(findSystematikRequestType);

            if (LOGGER.isDebugEnabled()) {
                StringBuilder debug = new StringBuilder();
                debug.append("*** TIME-INFO *** - findSystematik(");
                debug.append(dkzid).append(", ");
                for (String codeNummer : codeNummern) {
                    debug.append(String.valueOf(codeNummer)).append(", ");
                }
                debug.append(") = ").append(System.currentTimeMillis() - startTs);
                LOGGER.debug(debug.toString());
            }

        } catch (FehlerResponseMessage e) {
            throw new CommonServiceException(e.getMessage(), e);
        } catch (WebServiceException wse) {
            StringBuilder error = new StringBuilder();
            error.append("Failed to findSystematik for dkzid: ").append(String.valueOf(dkzid));
            error.append(" codeNummern: ");
            for (String codeNummer : codeNummern) {
                error.append(String.valueOf(codeNummer)).append(", ");
            }
            error.append(" due to WebServiceException: ").append(wse.getMessage());
            LOGGER.error(error.toString());
            // z.B. wenn Service down ist
            throw new CommonServiceException(wse.getMessage(), wse);
        }

        return mapFindSystematikResponse(findSystematikResponseType);
    }

    /**
     * Liefert alle Studienfaecher zur angegebenen Studienfeld-ID.
     *
     * @param studfId Studienfeld-ID; nicht null
     * @return Liste von DKZ-IDs aller gefundenen Studienfaecher
     */
    @Override
    @Cacheable(cacheNames = "studienfaecher", key = "{ #root.methodName, #studfId }")
    public List<Systematik> findStudienfaecherFuerStudienfeld(Integer studfId) {
        return findStudienfaecherFuerStudienfeldAllgemein(studfId, false);
    }

    /**
     * Liefert alle beschriebenen Studienfaecher zur angegebenen Studienfeld-ID.
     *
     * @param studfId Studienfeld-ID; nicht null
     * @return Liste von DKZ-IDs aller gefundenen und beschriebenen Studienfaecher
     */
    @Override
    @Cacheable(cacheNames = "studienfaecher", key = "{ #root.methodName, #studfId }")
    public List<Systematik> findBeschriebeneStudienfaecherFuerStudienfeld(Integer studfId) {
        return findStudienfaecherFuerStudienfeldAllgemein(studfId, true);
    }

    /**
     * Liefert alle Studienfaecher zur angegebenen Studienfeld-ID und filtert
     * ggf. nicht beschriebene aus.
     * <p>
     * Über das Flag "nurBeschriebene" kann ausgewählt werden, ob nur
     * beschriebene Objekte (gemäß #STUDISU-41, AK_2) oder alle Objekte in das
     * Ergebnis übernommen werden.
     * <p>
     * Hinweis: Wenn eine weitere Fallunterscheidung für die Filterung
     * erforderlich würde, sollte man von Flags auf das Übergeben eines
     * Filter-Objekts umstellen (das wäre dann eher Objekt-orientiert, das Flag
     * ist ohnehin eher imperativ).
     *
     * @param studfId
     * @param nurBeschriebene Flag setzen, um nur beschriebene Objekte zu erhalten.
     */
    private List<Systematik> findStudienfaecherFuerStudienfeldAllgemein(Integer studfId, boolean nurBeschriebene) {
        if (studfId == null) {
            throw new IllegalArgumentException("studfId empty...");
        }

        long startTs = System.currentTimeMillis();

        FindBerufeFuerStudienbereichRequestType request = new FindBerufeFuerStudienbereichRequestType();
        request.getStudienbereichid().add(studfId);

        FindBerufeFuerStudienbereichResponseType response = null;
        try {
            response = getPort().findBerufeFuerStudienbereich(request);

            if (LOGGER.isDebugEnabled()) {
                StringBuilder debug = new StringBuilder();
                debug.append("*** TIME-INFO *** - findStudienfaecherFuerStudienfeldAllgemein(");
                debug.append(studfId).append(", ");
                debug.append(Boolean.valueOf(nurBeschriebene));
                debug.append(") = ").append(System.currentTimeMillis() - startTs);
                LOGGER.debug(debug.toString());
            }


        } catch (DKZServiceFaultException e) {
            LOGGER.trace(e.getMessage());
            throw new CommonServiceException(e.getMessage(), e);
        } catch (WebServiceException wse) {
            LOGGER.error("Failed to findStudienfaecherFuerStudienfeldAllgemein for studfId: " + String.valueOf(studfId)
                    + " due to WebServiceException: " + wse.getMessage());
            // z.B. wenn Service down ist
            throw new CommonServiceException(wse.getMessage(), wse);
        }

        // Ergebnis in Liste von Integer-Objekten mappen
        return mapFindBerufeFuerStudienbereichResponse(response, nurBeschriebene);
    }

    /**
     * Mappt die Antwort des Aufrufs von findSystematik() in eine Liste von
     * Systematik-Objekten.
     *
     * @param findSystematikResponseType response
     * @return eine Liste von Systematik-Objekten.
     */
    private List<Systematik> mapFindSystematikResponse(FindSystematikResponseType findSystematikResponseType) {
        List<SystematikPositionType> systematikPositionTypeListe = findSystematikResponseType.getSystematikPosition();
        List<Systematik> systematiken = new ArrayList<Systematik>();

		for (SystematikPositionType systematik : systematikPositionTypeListe) {
			Systematik row = new Systematik(systematik.getID(), systematik.getCodenr(), systematik.getObercodenr(),
					systematik.getKurzBezeichnungNeutral(), systematik.getBezeichnungNeutral(),
					SystematikZustand.fromValue(systematik.getZustand()), BeschreibungsZustand.fromValue(systematik.getBeschreibungsZustand()));
			systematiken.add(row);
		}
		return systematiken;
	}

    /**
     * Mappt die Antwort des Aufrufs von findStudienbereicheByObercodenr() in
     * eine Liste von Systematik-Objekten.
     *
     * @param response Antwort des Aufrufs von findStudienbereicheByObercodenr().
     * @return eine Liste von Systematik-Objekten.
     */
    private List<Systematik> mapFindStudienbereicheByObercodenrResponse(
            FindStudienbereicheByObercodenrResponseType response) {
        List<Systematik> result = new ArrayList<Systematik>();

        for (SystematikBasisType systematik : response.getStudienbereich()) {
            ZustandType zustand = systematik.getZustand();
            SystematikZustand sysZustand = null;
            if (zustand != null) {
                sysZustand = SystematikZustand.fromValue(systematik.getZustand().value());
            } /*else {
                LOGGER.warn("Zustand der Systematik unbekannt, sollte in Betrieb nicht vorkommen");
			}*/

            Systematik row = new Systematik(
                    systematik.getId(),
                    systematik.getCodenr(),
                    systematik.getObercodenr(),
                    systematik.getKurzBezeichnungNeutral(),
                    systematik.getBezeichnungNeutral(),
                    sysZustand);
            result.add(row);
        }

        return result;
    }

    /**
     * Mappt die Antwort des Aufrufs von findBerufeFuerStudienbereich() in eine
     * Liste von Systematik-Objekten.
     * <p>
     * Über das Flag "nurBeschriebene" kann ausgewählt werden, ob nur
     * beschriebene Objekte (gemäß #STUDISU-41, AK_2) oder alle Objekte in das
     * Ergebnis übernommen werden.
     * <p>
     * Hinweis: Wenn eine weitere Fallunterscheidung für die Filterung
     * erforderlich würde, sollte man von Flags auf das Übergeben eines
     * Filter-Objekts umstellen (das wäre dann eher Objekt-orientiert, das Flag
     * ist ohnehin eher imperativ).
     *
     * @param response        Antwort des Aufrufs von findBerufeFuerStudienbereich().
     * @param nurBeschriebene Flag setzen, um nur beschriebene Objekte zu erhalten.
     * @return eine Liste von Systematik-Objekten.
     */
    private List<Systematik> mapFindBerufeFuerStudienbereichResponse(FindBerufeFuerStudienbereichResponseType response,
                                                                     boolean nurBeschriebene) {
        List<Systematik> result = new ArrayList<Systematik>();

        for (BerufBasisType beruf : response.getBeruf()) {
            // Beschränkung gemäß #STUDISU-41, AK_2
            boolean istBeschrieben = BeschreibungszustandType.WERT_J_BESCHRIEBEN.equals(beruf.getBeschreibungszustand());

            if (istBeschrieben || !nurBeschriebene) {
                Systematik row = new Systematik(
                        beruf.getId(),
                        beruf.getCodenr(),
                        beruf.getObercodenr(),
                        beruf.getKurzBezeichnungNeutral(),
                        beruf.getBezeichnungNeutral(),
                        SystematikZustand.fromValue(beruf.getZustand().value()));
                result.add(row);
            }
        }

        return result;
    }

    /**
     * Service status for monitoring/health check
     *
     * @return service status
     * @throws FehlerResponseMessage
     */
    @Override
    public ExternalServiceStatus getServiceStatus() throws CommonServiceException {
        try {
            String sstat = getPort().getServiceStatus().getWsversion();
            ExternalServiceStatus.Status status;
            if (StringUtils.isEmpty(sstat)) {
                status = ExternalServiceStatus.Status.NICHT_VERFUEGBAR;
            } else {
                status = ExternalServiceStatus.Status.VERFUEGBAR;
            }
            ExternalServiceStatus exstat = new ExternalServiceStatus("DKZService", status);
            return exstat;
        } catch (FehlerResponseMessage fehlerResponseMessage) {
            LOGGER.error(fehlerResponseMessage.getMessage(), fehlerResponseMessage);
            return new ExternalServiceStatus("DKZService", ExternalServiceStatus.Status.NICHT_VERFUEGBAR);
        }
    }

    @Override
    @Cacheable(cacheNames = "studienfaecher", key = "{ #root.methodName, #suchbefriff }")
    public List<Studienfach> findStudienfachBySuchwort(String suchbefriff) {

        List<Studienfach> ret = new ArrayList<Studienfach>();

        FindAusbildungenBySuchwortRequestType parameters = new FindAusbildungenBySuchwortRequestType();
        parameters.setBeschreibungszustand(BeschreibungszustandType.WERT_J_BESCHRIEBEN);
        parameters.setMitVeralteten(false);
        parameters.setQualifikationsniveau(QualifikationsniveauType.WERT_4_HOCHSCHULNIVEAU);
        parameters.setSuchbegriff(suchbefriff);

        try {
            FindAusbildungenBySuchwortResponseType response = dkzServicePort.findAusbildungenBySuchwort(parameters);
            
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.debug("DKZServiceImpl.findStudienfachBySuchwort(suchbefriff="+suchbefriff+") called");
            }

            List<BerufBasisType> berufe = response.getBeruf();
            for (BerufBasisType beruf : berufe) {
                BerufskundlicheGruppe2014Type berufskundlicheGruppe = beruf.getBerufskundlicheGruppe();
                if (berufskundlicheGruppe == BerufskundlicheGruppe2014Type.WERT_3120_A_GRUNDST_STUDIENFAECHER_GAENGE
                        || berufskundlicheGruppe == BerufskundlicheGruppe2014Type.WERT_3130_A_WEITERF_STUDIENFAECHER_GAENGE) {

                    ret.add(new Studienfach(beruf.getId(), beruf.getKurzBezeichnungNeutral()));
                }
            }

            return ret;

        } catch (DKZServiceFaultException e) {
            throw new CommonServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Cacheable(cacheNames = "studienfaecher", key = "{ #root.methodName, #dkzIds }")
    public List<Studienfach> findStudienfachById(List<Integer> dkzIds) {

        List<Studienfach> ret = new ArrayList<Studienfach>();

        GetBerufeByIDRequestType parameters = new GetBerufeByIDRequestType();
        parameters.getId().addAll(dkzIds);

        try {
            GetBerufeByIDResponseType response = dkzServicePort.getBerufeByID(parameters);
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.debug("DKZServiceImpl.findStudienfachById(dkzIds="+dkzIds+") called");
            }

            List<BerufBasisType> berufe = response.getBeruf();
            for (BerufBasisType beruf : berufe) {
                ret.add(new Studienfach(beruf.getId(), beruf.getKurzBezeichnungNeutral()));
            }

        } catch (DKZServiceFaultException e) {
            throw new CommonServiceException(e.getMessage(), e);
        }

        return ret;
    }
}
