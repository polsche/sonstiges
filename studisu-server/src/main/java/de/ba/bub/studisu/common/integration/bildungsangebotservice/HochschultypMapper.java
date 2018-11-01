package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Hochschultyp;

/**
 * Mappt den Hochschultyp auf einen Anzeige-Text.
 */
public class HochschultypMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HochschultypMapper.class);

	private Map<Hochschultyp, String> hochschultypMap = new HashMap<>();
    
	/**
	 * Konstruktor.
	 */
	public HochschultypMapper() {
    	hochschultypMap.put(Hochschultyp.BERUFSAKADEMIE_DUALE_HOCHSCHULE, "Berufsakademie / Duale Hochschule");
    	hochschultypMap.put(Hochschultyp.FACHHOCHSCHULE_HOCHSCHULE_FUER_ANGEWANDTE_WISSENSCHAFTEN, "Fachhochschule / Hochschule für angewandte Wissenschaften");
    	hochschultypMap.put(Hochschultyp.HOCHSCHULE_EIGENEN_TYPS, "Hochschule eigenen Typs");
    	hochschultypMap.put(Hochschultyp.KIRCHLICHE_HOCHSCHULE, "Kirchliche Hochschule");
    	hochschultypMap.put(Hochschultyp.KUNST_UND_MUSIKHOCHSCHULE, "Kunst- und Musikhochschule");
    	hochschultypMap.put(Hochschultyp.PRIVATE_HOCHSCHULE, "Private Hochschule");
    	hochschultypMap.put(Hochschultyp.UNIVERSITAET, "Universität");
    	hochschultypMap.put(Hochschultyp.VERWALTUNGSHOCHSCHULE, "Verwaltungshochschule");
    }
    
    /**
     * Mappt den Hochschultyp auf einen Anzeige-Text.
     * 
     * @param hochschultyp der Hochschultyp
     * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
     */
    public String map(Hochschultyp hochschultyp) {
    	if (hochschultyp == null) {
    		return null;
    	}
    	String text = hochschultypMap.get(hochschultyp);
    	if (text == null) {
    		LOGGER.warn("Unbekannter Hochschultyp: " + hochschultyp);
    	}
		return text;
	}
}
