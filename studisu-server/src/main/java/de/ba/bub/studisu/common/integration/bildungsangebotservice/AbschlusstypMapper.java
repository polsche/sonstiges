package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlusstyp;

/**
 * Mappt den Abschlusstyp auf einen Anzeige-Text.
 */
public class AbschlusstypMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AbschlusstypMapper.class);

	private Map<Abschlusstyp, String> abschlusstypMap = new HashMap<>();
    
	/**
	 * Konstruktor.
	 */
	public AbschlusstypMapper() {
    	abschlusstypMap.put(Abschlusstyp.KEINE_ANGABE, "keine Angabe");
    	abschlusstypMap.put(Abschlusstyp.KONSEKUTIV, "konsekutiv");
    	abschlusstypMap.put(Abschlusstyp.NICHT_KONSEKUTIV, "nicht konsekutiv");
    	abschlusstypMap.put(Abschlusstyp.WEITERBILDEND, "konsekutiv");
    }
    
    /**
     * Mappt den Abschlusstyp auf einen Anzeige-Text.
     * 
     * @param abschlusstyp der Abschlusstyp
     * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
     */
    public String map(Abschlusstyp abschlusstyp) {
    	if (abschlusstyp == null) {
    		return null;
    	}
    	String text = abschlusstypMap.get(abschlusstyp);
    	if (text == null) {
    		LOGGER.warn("Unbekannter Abschlusstyp: " + abschlusstyp);
    	}
		return text;
	}
}
