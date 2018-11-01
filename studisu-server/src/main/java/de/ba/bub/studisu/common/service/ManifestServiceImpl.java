package de.ba.bub.studisu.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementierung des {@link ManifestService}.
 *
 * @author OettlJ
 */
@Service("manifest") //NOSONAR
public class ManifestServiceImpl implements ManifestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManifestServiceImpl.class);
	
	/** Konstante für Manifest-Wert 'Implementation-Version' */
	private static final String MANIFEST_IMPLEMENTATION_VERSION = "Implementation-Version";
	/** Konstante für Dateiendung '.jar' */
	private static final String SUFFIX_JAR = ".jar";
	/** Konstante für Teilpfad 'WEB-INF/lib/' */
	private static final String PATH_WEBINF_LIB = "WEB-INF/lib/";
	/** Konstante für Teilpfad 'BOOT-INF/' */
	private static final String PATH_BOOT_INF = "BOOT-INF";

	private transient Manifest manifest = null;

	/**
     * Liest das Manifest ein.
     */
    @PostConstruct
    private void postConstruct() {
        try {
	        // ermittle aktuellen Pfad der Anwendung
	    	String location = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();        

			// wenn aktueller Pfad der Anwendung in JAR-Datei(Weblogic) liegt,
			// muss Manifest aus übergeordnetem Pfad gelesen werden.
			// Auf dem Tomcat (Spring-Boot-Fat-Jar) liegt es parallel zum
			// BOOT-INF. Andernfalls kann Manifest aus META-INF in aktuellem
			// Pfad gelesen werden.
			if (location.toLowerCase().endsWith(SUFFIX_JAR)) {
				// Manifest liegt in META-INF parallel zu WEB-INF
				int startWebinf = location.indexOf(PATH_WEBINF_LIB);
				location = location.substring(0, startWebinf);
			} else if (location.contains(PATH_BOOT_INF)) {
				int startBootinf = location.indexOf(PATH_BOOT_INF);
				location = location.substring(0, startBootinf);
        	}
			loadManifestFile(new URL(location + JarFile.MANIFEST_NAME));
        } catch (Exception e) {
            LOGGER.error("Fehler beim Einlesen des Manifestes: ", e);
        }
    }
    
	/**
	 * Liest das Manifest unter der übergebenen URL ein.
	 * 
	 * @param url
	 *            URL der einzulesenden Manifest-Datei.
	 */
    private void loadManifestFile(URL url) {
    	InputStream is = null;
    	try {
            // Manifest öffnen
            is = url.openStream();
            if (is != null) {
                // Manifest lesen und merken
                this.manifest = new Manifest(is);
            }
        } catch (Exception e) {
            LOGGER.error("Fehler beim Einlesen von '" + url.toString() + "': ", e);
        } finally {
        	if (is != null) {
        		try {
					is.close();
				} catch (IOException e) {
					// ignoriere Fehler
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Fehler beim Schliessen des InputStreams vom Manifest",e);
					}
				}
        	}
        }
    }

    /**
	 * Liefert die Implementation-Version aus dem Manifest.
	 * @return die Implementation-Version aus dem Manifest.
	 */
    @Override
	public String getImplementationVersion() {
    	if(this.manifest != null) {
    		return this.manifest.getMainAttributes().getValue(MANIFEST_IMPLEMENTATION_VERSION);
    	}
    	LOGGER.warn("Manifest ist null");
    	return null;
    }
}
