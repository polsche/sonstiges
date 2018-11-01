package de.ba.bub.studisu.common.integration.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.baintern.dst.services.types.basis.systematiken.dkztypes_internal_use_only.FehlerResponseType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.DKZFaultType;
import de.baintern.dst.services.types.basis.systematiken.dkztypes_v_2.DKZServiceFaultType;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServiceFaultException;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.FehlerResponseMessage;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServiceFaultException;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.uebergreifend_v_1.BildungsangebotServiceFault;

public class PfcrLogMessageUtilTest {

	//methodenparameter
	String service; 
	String methode;
	long dauer;
	Exception fehler; 
	Integer anzahlElemente; 
	String uuid;
	
	private final static String FEHLERMELDUNG = "jUnit Fehlermeldung";

	@Before
	public void setUp() throws Exception {
		service = "jUnit Service"; 
		methode = "get it all";
		dauer = 33100;
		fehler = null; 
		anzahlElemente = 20; 
		uuid = "10002-uuid1234567890";
	}
	
	@Test
	public final void testgetLogMessageFehlerfreiAllesDrin() {
		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);

		// von kompletten string vergleich abgesehen, wie unten beispielsweise angegeben
		// ein solcher vergleich waere sehr restriktiv fuer den produktiven code
		// String expected = "my log msg";
		// Assert.assertEquals("", expected, pfcrLogMessage);

		// stattdessen nur fuer alle relevanten eingaben geprueft, dass sie in ausgabe enthalten sind
		Assert.assertTrue("alle eingegebenen bestandteile sollten in msg enthalten sein", 
				pfcrLogMessage.contains(service));
		Assert.assertTrue("alle eingegebenen bestandteile sollten in msg enthalten sein", 
				pfcrLogMessage.contains(methode));
		Assert.assertTrue("alle eingegebenen bestandteile sollten in msg enthalten sein", 
				pfcrLogMessage.contains(String.valueOf(dauer)));
		Assert.assertTrue("alle eingegebenen bestandteile sollten in msg enthalten sein", 
				pfcrLogMessage.contains(String.valueOf(anzahlElemente)));
		Assert.assertTrue("alle eingegebenen bestandteile sollten in msg enthalten sein", 
				pfcrLogMessage.contains(uuid));
	}
	
	@Test 
	public final void testgetLogMessageDKZServiceFaultException() {
		DKZServiceFaultType fault = mock(DKZServiceFaultType.class);
		DKZServiceFaultException fehler = new DKZServiceFaultException("junit exception", fault);

		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);

		Assert.assertTrue("exception msg sollte in msg enthalten sein", 
				pfcrLogMessage.contains(service));
	}

	@Test 
	public final void testgetLogMessageDKZServiceFaultExceptionFaultInfo() {
		DKZServiceFaultType fault = mock(DKZServiceFaultType.class);
		List<DKZFaultType> faults = new ArrayList<>();
		DKZFaultType innerFault1 = new DKZFaultType();//mock(DKZFaultType.class);
		innerFault1.setFehlermeldung(FEHLERMELDUNG);
		faults.add(innerFault1);
		when(fault.getFehler()).thenReturn(faults);
		DKZServiceFaultException fehler = new DKZServiceFaultException("junit exception", fault);

		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);

		Assert.assertTrue("fault fehlermeldung sollte in msg enthalten sein", 
				pfcrLogMessage.contains(FEHLERMELDUNG));
	}
	
	@Test 
	public final void testgetLogMessageBildungsangebotServiceFaultException() {
		BildungsangebotServiceFault fault = mock(BildungsangebotServiceFault.class);
		BildungsangebotServiceFaultException fehler = new BildungsangebotServiceFaultException("junit exception", fault);

		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);

		Assert.assertTrue("exception msg sollte in msg enthalten sein", 
				pfcrLogMessage.contains(service));
	}

	@Test 
	public final void testgetLogMessageBildungsangebotServiceFaultExceptionFaultInfo() {
		BildungsangebotServiceFault fault = mock(BildungsangebotServiceFault.class);
		when(fault.getFaultInfo()).thenReturn(FEHLERMELDUNG);
		BildungsangebotServiceFaultException fehler = new BildungsangebotServiceFaultException("junit exception", fault);

		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);
		
		Assert.assertTrue("fault fehlermeldung sollte in msg enthalten sein", 
				pfcrLogMessage.contains(FEHLERMELDUNG));
	}
	
	@Test
	public final void testgetLogMessageFehlerResponseMessage() {
		FehlerResponseType fault = mock(FehlerResponseType.class);
		FehlerResponseMessage fehler = new FehlerResponseMessage("junit exception", fault);

		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);

		Assert.assertTrue("exception msg sollte in msg enthalten sein", 
				pfcrLogMessage.contains(service));
	}

	@Test
	public final void testgetLogMessageFehlerResponseMessageFaultInfo() {
		FehlerResponseType fault = mock(FehlerResponseType.class);
		when(fault.getFehlermeldung()).thenReturn(FEHLERMELDUNG);
		FehlerResponseMessage fehler = new FehlerResponseMessage("junit exception", fault);

		String pfcrLogMessage = PfcrLogMessageUtil.getLogMessage(service, 
				methode, dauer, fehler, anzahlElemente, uuid);
		
		Assert.assertTrue("fault fehlermeldung sollte in msg enthalten sein", 
				pfcrLogMessage.contains(FEHLERMELDUNG));
	}

}