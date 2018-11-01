package de.ba.bub.studisu.common.service;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.jar.JarFile;

import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse für {@link ManifestServiceImpl}
 * @author OettlJ
 *
 */
public class ManifestServiceImplTest {

	private ManifestServiceImpl testClass;

	@Before
	public void setUp() throws Exception {
		testClass = new ManifestServiceImpl();
	}

	/**
	 * Testmethode für {@link ManifestServiceImpl#getImplementationVersion()}
	 * ohne vorherigen Aufruf von {@link ManifestServiceImpl#postConstruct()}
	 */
	@Test
	public void testGetImplementationVersionWithoutPostConstruct() {
		// postConstruct() nicht aufgerufen, daher kein Manifest geladen
		assertEquals("Implementation-Version sollte null sein", null, testClass.getImplementationVersion());
	}

	/**
	 * Testmethode für {@link ManifestServiceImpl#getImplementationVersion()}
	 * mit vorherigem Aufruf von {@link ManifestServiceImpl#postConstruct()}
	 */
	@Test
	public void testGetImplementationVersionWithPostConstruct()
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method mUnderTest = ManifestServiceImpl.class.getDeclaredMethod("postConstruct");
        mUnderTest.setAccessible(true);
        mUnderTest.invoke(testClass);

        // postContruct() wird zwar aufgerufen, allerdings kann es sein, je nachdem wo der Test am Jenkins ausgeführt wird,
        // dass dort kein Manifest zur Verfügung steht
        if(manifestExists()) {
        	assertTrue("Implementation-Version sollte nicht null sein", testClass.getImplementationVersion() != null);
        } else {
        	assertEquals("Implementation-Version sollte null sein", null, testClass.getImplementationVersion());
        }
	}

	/**
	 * Überprüft, ob das Manifest überhaupt vorhanden ist.
	 * Je nachdem sollte Implementation-Version vorhanden sein oder nicht.
	 * @return ob das Manifest überhaupt vorhanden ist.
	 */
	private boolean manifestExists() {
		boolean exists = false;

        // ermittle aktuellen Pfad der Anwendung
    	String location = testClass.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        try {
        	// wenn aktueller Pfad der Anwendung in JAR-Datei liegt, muss Manifest aus übergeordnetem Pfad gelesen werden,
        	// andernfalls kann Manifest aus META-INF in aktuellem Pfad gelesen werden
        	if(location.toLowerCase().endsWith(".jar")) {
        		// Manifest liegt in META-INF parallel zu WEB-INF
        		int startWebinf = location.indexOf("WEB-INF/lib/");
        		location = location.substring(0, startWebinf);
        	}
            URL manifestURL = new URL(location + JarFile.MANIFEST_NAME);
            File manifestFile = new File(manifestURL.getPath());

            exists = manifestFile.exists();
        } catch (Exception e) {
            System.out.println("Fehler in manifestExists(): " + e.getMessage());
        }

        return exists;
	}
}
