package de.ba.bub.studisu.common.integration.dkz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import de.ba.bub.studisu.common.integration.WsAuthType;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServicePortType;

/**
 * Testklasse fuer {@link DKZServiceImpl}
 * 
 * waehrend wir fuer Powermocks mockstatic, um den Logger zu mocken den PowerMockRunner brauchen,
 * veraendert dieser das Verhalten bei expected exceptions sowie Whitebox-Zugriffen negativ
 * deswegen wurden verschiedene Unittests auf zwei Klassen aufgeteilt mit den verschiedenen 
 * 
 * @author KunzmannC
 */
@RunWith(value = MockitoJUnitRunner.class)
public class DKZServiceImplMockitoRunnerTest {

@Mock 
private de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZService dkzService;

	//@Mock
	private DKZServiceImpl instance;

	@Before
	public void setUp() throws Exception {
		// INSTANZ AUFBAUEN UND SERVICEPORT MOCKEN
		instance = new DKZServiceImpl();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testfindStudienfeldgruppeSystematikenNull() {
		String obercodenr = null;
		try {
			instance.findStudienfeldgruppeSystematiken(obercodenr);
		} catch (IllegalArgumentException iae) {
			assertEquals("ohne obercodenummer erwarten wir exception", 
					"obercodenr empty...", iae.getMessage());
			// erfuellen des expected
			throw iae;
		}
	}

	/**
	 * Reflection BlackMagic uebernommen von ManifestServiceImplTest
	 * Aufruf von {@link DKZServiceImpl#postConstruct()}
	 * 
	 * having trouble with PowerMock runner and (expected=...Exception)
	 */
	@Test(expected=InvocationTargetException.class)
	public void testGetImplementationVersionWithPostConstructOnMock() throws InvocationTargetException {
        Method mUnderTest;
		try {
			mUnderTest = DKZServiceImpl.class.getDeclaredMethod("postConstruct");
	        mUnderTest.setAccessible(true);
	        mUnderTest.invoke(instance);
			// System.out.println("success");
		} catch (InvocationTargetException ite) {
			//this would be fine on Mock in Proxy.newProxyInstance(...
			//ite.getTargetException().printStackTrace();
			assertTrue(ite.getTargetException() instanceof IllegalStateException);
			assertTrue(ite.getTargetException()!= null && ite.getTargetException().getMessage().contains("authType"));
			throw ite;
		} catch (Exception e) {
			fail("shouldnt get this exceptions");
		}
	}

	/**
	 * Reflection BlackMagic uebernommen von ManifestServiceImplTest
	 * Aufruf von {@link DKZServiceImpl#postConstruct()}
	 */
	@Test
	public void testGetImplementationVersionWithPostConstructBasic() throws InvocationTargetException {
        instance = new DKZServiceImpl();
        
        // mocken der konfig, die sonst durch spring injiziert wird
        String authType = WsAuthType.BASIC.name();
        Whitebox.setInternalState(instance, "authType", authType);
        String user = "jUnit user";
        Whitebox.setInternalState(instance, "user", user);
        String password = "jUnit pw";
        Whitebox.setInternalState(instance, "password", password);
        // not mocked now: private String location;  private String policyName;

        Method mUnderTest;
		try {
			mUnderTest = DKZServiceImpl.class.getDeclaredMethod("postConstruct");
	        mUnderTest.setAccessible(true);
	        mUnderTest.invoke(instance);
		} catch (InvocationTargetException ite) {
			//this would be fine on Mock in Proxy.newProxyInstance(...
		} catch (ClassCastException cce) {
			//this would be fine on Mock in Proxy.newProxyInstance(...
		}
		catch (Exception e) {
			fail("shouldnt get this exceptions");
		}
        
		// nevertheless other logic worked and we are going to test ist
		
		DKZServicePortType port = Whitebox.getInternalState(instance, "dkzServicePort");
		//JAX-WS RI 2.2.9-b14002 svn-revision#14004: Stub for http://illegale.adresse/
		assertTrue("jax ws sollte in binding provider auftauchen",
				port != null && port.toString().contains("JAX-WS RI"));

		Map<String, Object> rc = ((BindingProvider) port).getRequestContext();
		assertTrue("user sollte aus gemockter konfig in binding provider geschrieben werden",
				user.equals(rc.get(BindingProvider.USERNAME_PROPERTY)));
		assertTrue("pw sollte aus gemockter konfig in binding provider geschrieben werden",
				password.equals(rc.get(BindingProvider.PASSWORD_PROPERTY)));
	}	

	/**
	 * Reflection BlackMagic uebernommen von ManifestServiceImplTest
	 * Aufruf von {@link DKZServiceImpl#postConstruct()}
	 */
	@Test
	public void testGetImplementationVersionWithPostConstructSaml() throws InvocationTargetException {
        instance = new DKZServiceImpl();
        
        // mocken der konfig, die sonst durch spring injiziert wird
        String authType = WsAuthType.SAML.name();
        Whitebox.setInternalState(instance, "authType", authType);
        String location = "jUnit location";
        Whitebox.setInternalState(instance, "location", location);
        // not mocked now: private String location;  private String policyName;
        
		Method mUnderTest;
		try {
			mUnderTest = DKZServiceImpl.class.getDeclaredMethod("postConstruct");
	        mUnderTest.setAccessible(true);
	        mUnderTest.invoke(instance);
		} catch (InvocationTargetException ite) {
			//this would be fine on Mock in Proxy.newProxyInstance(...
		} catch (ClassCastException cce) {
			//this would be fine on Mock in Proxy.newProxyInstance(...
		}
		catch (Exception e) {
			fail("shouldnt get this exceptions");
		}

		// nevertheless other logic worked and we are going to test ist
		
		DKZServicePortType port = Whitebox.getInternalState(instance, "dkzServicePort", DKZServiceImpl.class);
		Assert.assertNotNull("whitebox sollte internal state preisgeben", port);
		//securityPolicyFeature keine API gefunden, um das zu testen
		BindingProvider provider = ((BindingProvider) port);
		Map<String, Object> rc = provider.getRequestContext();
		String validLocation = "http://illegale.adresse/";
		assertTrue("location sollte aus gemockter konfig in binding provider geschrieben werden bzw. durch dessen validierung ersetzt worden sein",
				   location.equals(     rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY))
				|| validLocation.equals(rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY)));
	}	
}
