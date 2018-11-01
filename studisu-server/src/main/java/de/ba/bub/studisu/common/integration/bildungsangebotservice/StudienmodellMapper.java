package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Studienmodell;

/**
 * Mappt das Studienmodell auf einen Anzeige-Text.
 */
public class StudienmodellMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(StudienmodellMapper.class);

	private Map<Studienmodell, String> studienmodellMap = new HashMap<>();
    
	/**
	 * Konstruktor.
	 */
	public StudienmodellMapper() {
    	studienmodellMap.put(Studienmodell.AUSBILDUNGSBEGLEITEND, "ausbildungsbegleitend");
    	studienmodellMap.put(Studienmodell.AUSBILDUNGSINTEGRIEREND, "ausbildungsintegrierend");
    	studienmodellMap.put(Studienmodell.BERUFSBEGLEITEND, "berufsbegleitend");
    	studienmodellMap.put(Studienmodell.BERUFSINTEGRIEREND, "berufsintegrierend");
    	studienmodellMap.put(Studienmodell.DUALES_STUDIUM, "Duales Studium");
    	studienmodellMap.put(Studienmodell.KEINE_ANGABE, "keine Angabe");
    	studienmodellMap.put(Studienmodell.PRAXISINTEGRIEREND, "praxisintegrierend");
    }
    
    /**
     * Mappt das Studienmodell auf einen Anzeige-Text.
     * 
     * @param studienmodell das Studienmodell
     * @return Anzeigetext
     */
    public String map(Studienmodell studienmodell) {
    	if (studienmodell == null) {
    		return null;
    	}
    	String text = studienmodellMap.get(studienmodell);
    	if (text == null) {
    		LOGGER.warn("Unbekannter Studienmodell: " + studienmodell);
    	}
		return text;
	}
}
