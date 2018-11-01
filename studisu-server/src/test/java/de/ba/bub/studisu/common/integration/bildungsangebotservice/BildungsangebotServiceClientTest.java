package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

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

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.integration.dkz.DKZServiceImpl;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.baintern.dst.services.types.common.serviceinfo_v_1.ServiceStatus;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bildungsart;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bundesland;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hochschultyp;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Schulart;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServiceFaultException;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServicePortType;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.calculateanzahlveranstaltungen_v_1.CalculateAnzahlVeranstaltungenAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.calculateanzahlveranstaltungen_v_1.CalculateAnzahlVeranstaltungenEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.calculateanzahlveranstaltungen_v_1.Ergebnis;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.FindOrteAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.FindOrteEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Adresse;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.FindStudienveranstaltungenAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.FindStudienveranstaltungenEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.GetStudienveranstaltungByIDAusgabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.GetStudienveranstaltungByIDEingabe;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.uebergreifend_v_1.BildungsangebotServiceFault;

/**
 * Testklasse fuer {@link DKZServiceImpl}
 * <p>
 * waehrend wir fuer Powermocks mockstatic, um den Logger zu mocken den PowerMockRunner brauchen,
 * veraendert dieser das Verhalten bei expected exceptions sowie Whitebox-Zugriffen negativ
 * deswegen wurden verschiedene Unittests auf zwei Klassen aufgeteilt mit den verschiedenen
 * <p>
 * PowerMockIgnore("javax.management.*") is there to get rid of logs like
 * java.lang.LinkageError: loader constraint violation
 * ERROR StatusLogger Unable to unregister MBeans: java.lang.LinkageError: javax/management/MBeanServer
 *
 * @author KunzmannC
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
@PowerMockIgnore("javax.management.*")
public class BildungsangebotServiceClientTest {

    @Mock
    private de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZService dkzService;

    @Mock
    private BildungsangebotServicePortType bildungsServicePort;

    @Mock
    private static Logger logger;

    private BildungsangebotServiceClient instance;

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
        // ANTWORT calculateAnzahlVeranstaltungen
        CalculateAnzahlVeranstaltungenAusgabe calculateAV = mock(CalculateAnzahlVeranstaltungenAusgabe.class);
        Ergebnis ergebnis = mock(Ergebnis.class);
        when(calculateAV.getErgebnis()).thenReturn(ergebnis);
        when(ergebnis.getAnzahl()).thenReturn(Integer.valueOf(69));
        when(bildungsServicePort.calculateAnzahlVeranstaltungen(any(CalculateAnzahlVeranstaltungenEingabe.class))).thenReturn(calculateAV);

        // INSTANZ AUFBAUEN UND SERVICEPORT MOCKEN
        instance = new BildungsangebotServiceClient();
        Whitebox.setInternalState(instance, bildungsServicePort, bildungsServicePort);


        // the loggers behaviour gets reset from tearDown beside its counts,
        // therefore we set it after the reset
        when(logger.isDebugEnabled()).thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() {
        //because consequent tests could run into TooManyActualInvocations
        //we reset the logger and its counters here
        Mockito.reset(logger);
    }


    //@override ExternalServiceStatus getServiceStatus() throws CommonServiceException;
    @Test
    public void testgetServiceStatus() {
        try {
            ServiceStatus status = mock(ServiceStatus.class);
            when(status.getWsversion()).thenReturn("Version Junit 1.12.13");
            when(bildungsServicePort.getServiceStatus()).thenReturn(status);

            ExternalServiceStatus extStatus = instance.getServiceStatus();
            assertEquals("service impl sollte verfuegbar liefern",
                    ExternalServiceStatus.Status.VERFUEGBAR, extStatus.getServiceStatus());
            assertEquals("service impl name falsch",
                    "BildungsangebotService", extStatus.getServiceName());
        } catch (CommonServiceException e) {
            fail("service liefert exception statt status und version");
        }
    }


    @Test
    public void testholeAnzahlBildungsangebote() {
        Integer dkzId = Integer.valueOf(3626);
        int result = instance.holeAnzahlBildungsangebote(dkzId);
        assertEquals("service anzahl des mocks liefern ", 69, result);
    }

    @Test(expected = CommonServiceException.class)
    public void testholeAnzahlBildungsangeboteBildungsangebotServiceFaultException() {
        Integer dkzId = Integer.valueOf(3626);

        BildungsangebotServiceFault fault = mock(BildungsangebotServiceFault.class);
        BildungsangebotServiceFaultException exception = new BildungsangebotServiceFaultException(MSG, fault);

        try {
            when(bildungsServicePort.calculateAnzahlVeranstaltungen(any(CalculateAnzahlVeranstaltungenEingabe.class))).thenThrow(exception);
            instance.holeAnzahlBildungsangebote(dkzId);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));
            Mockito.verify(logger).trace(MSG);

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test
    public void testholeStudienangebotInformationen() throws BildungsangebotServiceFaultException {
        GetStudienveranstaltungByIDAusgabe ausgabe = mock(GetStudienveranstaltungByIDAusgabe.class);
        Studienveranstaltung ba = mock(Studienveranstaltung.class);
        Bildungsanbieter ban = mock(Bildungsanbieter.class);
        when(ba.getBildungsanbieter()).thenReturn(ban);
        when(ban.getId()).thenReturn(123);

        String title = "jUnit Bildungsangebot - learn Mockito and other gangster stuff!";
        when(ba.getTitel()).thenReturn(title);
        Veranstaltung v1 = mock(Veranstaltung.class);
        List<Veranstaltung> vs = new ArrayList<>();
        vs.add(v1);
        when(ba.getVeranstaltung()).thenReturn(vs);
        when(ba.getBildungsart()).thenReturn(Bildungsart.INTEGRATIONSKURS);
        when(v1.getUnterrichtsform()).thenReturn(Unterrichtsform.VOLLZEIT);
        when(ba.getHochschultyp()).thenReturn(Hochschultyp.UNIVERSITAET);
        when(ausgabe.getStudienveranstaltung()).thenReturn(ba);
        when(bildungsServicePort.getStudienveranstaltungByID(any(GetStudienveranstaltungByIDEingabe.class))).thenReturn(ausgabe);


        Integer vgId = Integer.valueOf(123456);
        StudienangebotInformationen result = instance.holeStudienangebotInformationen(vgId);

        verify(logger).debug(Mockito.startsWith("*** TIME-INFO *** - getVeranstaltungById(eingabe) = "));

        assertNotNull(result);
        assertEquals("title sollte in bezeichnung gemappt werden",
                title, result.getBezeichnung());
        assertTrue("unterrichtsform sollte in studienform gemappt werden",
                Unterrichtsform.VOLLZEIT.name().equalsIgnoreCase(result.getStudienform().getName()));
        assertTrue("schulart sollte in hochschulart gemappt werden",
                Schulart.UNIVERSITAET.name().equalsIgnoreCase(result.getHochschulart().getName()));
    }

    @Test(expected = CommonServiceException.class)
    public void testholeStudienangebotInformationenBildungsangebotServiceFaultException() throws BildungsangebotServiceFaultException {
        Integer vgId = Integer.valueOf(123456);

        BildungsangebotServiceFault fault = mock(BildungsangebotServiceFault.class);
        BildungsangebotServiceFaultException exception = new BildungsangebotServiceFaultException(MSG, fault);

        try {
            when(bildungsServicePort.getStudienveranstaltungByID(any(GetStudienveranstaltungByIDEingabe.class))).thenThrow(exception);
            instance.holeStudienangebotInformationen(vgId);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            verify(logger).error(Mockito.startsWith("Failed to Load Studienangebot with id: "));
            verify(logger).error(Mockito.contains(String.valueOf(vgId)));
            verify(logger).error(Mockito.contains("BildungsangebotServiceFaultException"));
            verify(logger).error(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test(expected = CommonServiceException.class)
    public void testholeStudienangebotInformationenWebServiceException() throws BildungsangebotServiceFaultException {
        Integer vgId = Integer.valueOf(9999);

        WebServiceException exception = new WebServiceException(MSG);

        try {
            when(bildungsServicePort.getStudienveranstaltungByID(any(GetStudienveranstaltungByIDEingabe.class))).thenThrow(exception);
            instance.holeStudienangebotInformationen(vgId);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            // got TooManyActualInvocations before we resetted the logger
            // now we should be fine thanks to tearDown
            verify(logger).error(Mockito.startsWith("Failed to Load Studienangebot with id: "));
            verify(logger).error(Mockito.contains(String.valueOf(vgId)));
            verify(logger).error(Mockito.contains("WebServiceException"));
            verify(logger).error(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test
    public void testfindStudienangebote() throws BildungsangebotServiceFaultException {
        // die ausgabe enthaelt eine veranstaltung, die ein angebot beinhaltet
        // daher hier viel code zum vorbereiten der antwort, bevor es los geht
        FindStudienveranstaltungenAusgabe ausgabe = mock(FindStudienveranstaltungenAusgabe.class);
        // wegen mehrfach gleicher klassennamen hier vollqualifiziert
        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Studienveranstaltung v1 =
                mock(de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Studienveranstaltung.class);

        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsangebot ba =
                mock(de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsangebot.class);

        when(v1.getBildungsangebot()).thenReturn(ba);
        Adresse adresse = mock(Adresse.class);
        //vollqualifiziert wegen Namenskonflikt - nervt :/
        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Ort ort =
                new de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Ort();
        ort.setBreitengrad(51.7194);
        ort.setLaengengrad(8.7572);
        when(adresse.getOrt()).thenReturn(ort);
        when(v1.getAdresse()).thenReturn(adresse);
        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsanbieter anbieter = mock(de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsanbieter.class);
        String anbieterName = "jUnit Specials offers";
        Integer anbieterId = 12345;
        when(anbieter.getName()).thenReturn(anbieterName);
        when(anbieter.getId()).thenReturn(anbieterId);
        when(ba.getBildungsanbieter()).thenReturn(anbieter);
        when(ba.getOsaUrl()).thenReturn("www.google.de/selbstchecks/osa");
        when(ba.getStudicheckUrl()).thenReturn("www.studicheck.de");

        List<de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Studienveranstaltung> vs = new ArrayList<>();
        vs.add(v1);
        when(ausgabe.getStudienveranstaltung()).thenReturn(vs);

        // endlich die ausgabe mocken
        when(bildungsServicePort.findStudienveranstaltungen(any(FindStudienveranstaltungenEingabe.class))).thenReturn(ausgabe);

        // anfrageparameter
        String pbParam = "Paderborn_8.7572_51.7194";
        AnfrageOrt aort = new AnfrageOrt(pbParam);
        int umkreis = 50;
        List<Integer> studienfaecher = Arrays.asList(93615, 94028, 94080);

        List<Studienangebot> result = instance.findStudienangebote(aort, umkreis, studienfaecher);

        verify(logger).debug(Mockito.startsWith("*** TIME-INFO *** - findStudienveranstaltungen(eingabe) = "));

        assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
        assertEquals("service sollte angebot des mocks liefern ", 1, result.size());

        assertEquals("abstand sollte 0 sein bzw. max 0.1 abweichung",
                0, result.get(0).getAbstand(), 0.1);
        assertEquals("anbietername sollte gemappt werden",
                anbieterName, result.get(0).getBildungsanbieterName());
        assertEquals("anbieter-id sollte gemappt werden",
                anbieterId, result.get(0).getBildungsanbieterId());

        // sortierung ausgelassen, da der comparator selbst schon abgedeckt war
    }

    @Test(expected = CommonServiceException.class)
    public void testfindStudienangeboteBildungsangebotServiceFaultException() throws BildungsangebotServiceFaultException {
        String pbParam = "Paderborn_8.7572_51.7194";
        AnfrageOrt aort = new AnfrageOrt(pbParam);
        int umkreis = 50;
        List<Integer> studienfaecher = Arrays.asList(93615, 94028, 94080);

        BildungsangebotServiceFault fault = mock(BildungsangebotServiceFault.class);
        BildungsangebotServiceFaultException exception = new BildungsangebotServiceFaultException(MSG, fault);

        try {
            when(bildungsServicePort.findStudienveranstaltungen(any(FindStudienveranstaltungenEingabe.class))).thenThrow(exception);
            instance.findStudienangebote(aort, umkreis, studienfaecher);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            verify(logger).error(Mockito.startsWith("Failed to Find Studienveranstaltungen for Anfrage:"));
            verify(logger).error(Mockito.contains(aort.getOrtsname()));
            verify(logger).error(Mockito.contains("BildungsangebotServiceFaultException"));
            verify(logger).error(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test(expected = CommonServiceException.class)
    public void testfindStudienangeboteWebServiceException() throws BildungsangebotServiceFaultException {
        String pbParam = "Paderborn_8.7572_51.7194";
        AnfrageOrt aort = new AnfrageOrt(pbParam);
        int umkreis = 50;
        List<Integer> studienfaecher = Arrays.asList(93615, 94028, 94080);

        WebServiceException exception = new WebServiceException(MSG);

        try {
            when(bildungsServicePort.findStudienveranstaltungen(any(FindStudienveranstaltungenEingabe.class))).thenThrow(exception);
            instance.findStudienangebote(aort, umkreis, studienfaecher);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            // got TooManyActualInvocations before we resetted the logger
            // now we should be fine thanks to tearDown
            verify(logger).error(Mockito.startsWith("Failed to Find Studienveranstaltungen for Anfrage:"));
            verify(logger).error(Mockito.contains(aort.getOrtsname()));
            verify(logger).error(Mockito.contains("WebServiceException"));
            verify(logger).error(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test
    public void testfindOrte() throws BildungsangebotServiceFaultException {
        // ANTWORT findOrte mocken
        FindOrteAusgabe ausgabe = mock(FindOrteAusgabe.class);
        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort ort1 =
                mock(de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort.class);
        String nabel = "Nabel der Welt";
        when(ort1.getOrtsname()).thenReturn(nabel);
        when(ort1.getBundesland()).thenReturn(Bundesland.NORDRHEIN_WESTFALEN);
        List<de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort> orte =
                new ArrayList<>();
        orte.add(ort1);
        when(ausgabe.getOrt()).thenReturn(orte);

        when(bildungsServicePort.findOrte(any(FindOrteEingabe.class))).thenReturn(ausgabe);

        // anfrageparameter
        String plz = "33100";
        String ort = nabel;

        //vollqualifiziert wegen namenskonflikt - nervt
        List<de.ba.bub.studisu.common.model.Ort> result = instance.findOrte(plz, ort);

        verify(logger).debug(Mockito.startsWith("*** TIME-INFO *** - findOrte"));

        assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
        assertEquals("service sollte angebot des mocks liefern ", 1, result.size());
        assertEquals("ortsname sollte gemappt sein",
                ort, result.get(0).getName());
        assertTrue("bundesland sollte gemappt sein",
                Bundesland.NORDRHEIN_WESTFALEN.value().equalsIgnoreCase(result.get(0).getBundesland()));
    }

    @Test
    public void testfindOrteOhneBundesland() throws BildungsangebotServiceFaultException {
        // ANTWORT findOrte mocken
        FindOrteAusgabe ausgabe = mock(FindOrteAusgabe.class);
        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort ort1 =
                mock(de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort.class);
        // KEIN bundesland gesetzt
        List<de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort> orte =
                new ArrayList<>();
        orte.add(ort1);
        when(ausgabe.getOrt()).thenReturn(orte);

        when(bildungsServicePort.findOrte(any(FindOrteEingabe.class))).thenReturn(ausgabe);

        // anfrageparameter
        String plz = "33100";
        String ort = "Nabel der Welt";

        //vollqualifiziert wegen namenskonflikt - nervt
        List<de.ba.bub.studisu.common.model.Ort> result = instance.findOrte(plz, ort);

        verify(logger).debug(Mockito.startsWith("*** TIME-INFO *** - findOrte"));

        assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
        assertEquals("service sollte angebot des mocks liefern ", 1, result.size());
        assertTrue("bundesland sollte leer aber robust sein", null == result.get(0).getBundesland() || "".equals(result.get(0).getBundesland()));
    }

    @Test
    public void testfindOrteNurOrtOderPlz() throws BildungsangebotServiceFaultException {
        // ANTWORT findOrte mocken
        FindOrteAusgabe ausgabe = mock(FindOrteAusgabe.class);
        de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort ort1 =
                mock(de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort.class);
        List<de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort> orte =
                new ArrayList<>();
        orte.add(ort1);
        when(ausgabe.getOrt()).thenReturn(orte);
        when(bildungsServicePort.findOrte(any(FindOrteEingabe.class))).thenReturn(ausgabe);

        // anfrageparameter
        String plz = "33100";
        String ort = "Nabel der Welt";

        // ort null
        List<de.ba.bub.studisu.common.model.Ort> result = instance.findOrte(plz, null);
        assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
        assertEquals("service sollte angebot des mocks liefern ", 1, result.size());

        // plz null
        result = instance.findOrte(null, ort);
        assertNotNull("service sollte ein nichtleeres ergebnis liefern ", result);
        assertEquals("service sollte angebot des mocks liefern ", 1, result.size());

        // plz und ort null
        // naechste Suche soll leere ausgabe zurueckgeben fuer leere suche...
        Mockito.reset(ausgabe);
        result = instance.findOrte(null, null);
        assertEquals("service sollte leere antwort liefern ", 0, result.size());
    }

    @Test(expected = CommonServiceException.class)
    public void testfindOrteBildungsangebotServiceFaultException() throws BildungsangebotServiceFaultException {
        // anfrageparameter
        String plz = "33100";
        String ort = "Nabel der Welt";

        BildungsangebotServiceFault fault = mock(BildungsangebotServiceFault.class);
        BildungsangebotServiceFaultException exception = new BildungsangebotServiceFaultException(MSG, fault);

        try {
            when(bildungsServicePort.findOrte(any(FindOrteEingabe.class))).thenThrow(exception);
            instance.findOrte(plz, ort);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            //aktuell im log KEINE unterscheidung zwischen den exceptions, nur msg
            //ferner loglevel trace, welches als todo in produktivem code markiert wurde
            //verify(logger).trace(Mockito.contains("BildungsangebotServiceFaultException"));
            verify(logger).trace(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test(expected = CommonServiceException.class)
    public void testfindOrteSOAPFaultException() throws BildungsangebotServiceFaultException {
        // anfrageparameter
        String plz = "33100";
        String ort = "Nabel der Welt";

        // weil das erzeugen einer SOAPFaultException mit test MSG sich als schwer erwies,
        // haben wir einfach mal ihr getMessage gemockt :)
        // ein hoch auf mockito!
        SOAPFaultException exception = mock(SOAPFaultException.class);
        when(exception.getMessage()).thenReturn(MSG);

        try {
            when(bildungsServicePort.findOrte(any(FindOrteEingabe.class))).thenThrow(exception);
            instance.findOrte(plz, ort);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            //aktuell im log KEINE unterscheidung zwischen den exceptions, nur msg
            //ferner loglevel trace, welches als todo in produktivem code markiert wurde
            //verify(logger).trace(Mockito.contains("BildungsangebotServiceFaultException"));
            verify(logger).trace(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }

    @Test(expected = CommonServiceException.class)
    public void testfindOrteException() throws BildungsangebotServiceFaultException {
        // anfrageparameter
        String plz = "33100";
        String ort = "Nabel der Welt";

        RuntimeException exception = new RuntimeException(MSG);

        try {
            when(bildungsServicePort.findOrte(any(FindOrteEingabe.class))).thenThrow(exception);
            instance.findOrte(plz, ort);
        } catch (CommonServiceException cse) {
            assertTrue("message sollte erhalten bleiben", cse.getMessage().contains(MSG));

            //aktuell im log KEINE unterscheidung zwischen den exceptions, nur msg
            //verify(logger).trace(Mockito.contains("BildungsangebotServiceFaultException"));
            verify(logger).error(Mockito.contains(MSG));

            // fullfill expectation
            throw cse;
        } catch (Exception e) {
            fail("unexpected exception");
        }
    }
}