package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hauptunterrichtssprache;

/**
 * Mappt die Hauptunterrichtssprache auf einen Anzeige-Text.
 */
public class UnterrichtsspracheMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UnterrichtsspracheMapper.class);

	private Map<Hauptunterrichtssprache, String> unterrichtsspracheMap = new HashMap<>();
    
	/**
	 * Konstruktor.
	 */
	public UnterrichtsspracheMapper() {
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.ANDERE, "Andere");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.CHINESISCH, "Chinesisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.DAENISCH, "Dänisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.DEUTSCH, "Deutsch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.ENGLISCH, "Englisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.FRANZOESISCH, "Französisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.ITALIENISCH, "Italienisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.KEINE_ANGABE, "keine Angabe");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.NIEDERLAENDISCH, "Niederländisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.POLNISCH, "Polnisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.SCHWEDISCH, "Schwedisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.SLOWAKISCH, "Slowakisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.SPANISCH, "Spanisch");
    	unterrichtsspracheMap.put(Hauptunterrichtssprache.TSCHECHISCH, "Tschechisch");
    }
    
    /**
     * Mappt die Hauptunterrichtssprache auf einen Anzeige-Text.
     * 
     * @param unterrichtssprache die Unterrichtssprache
     * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
     */
    public String map(Hauptunterrichtssprache unterrichtssprache) {
    	if (unterrichtssprache == null) {
    		return null;
    	}
    	String text = unterrichtsspracheMap.get(unterrichtssprache);
    	if (text == null) {
    		LOGGER.warn("Unbekannte Unterrichtssprache: " + unterrichtssprache);
    	}
		return text;
	}
}
