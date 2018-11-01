package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungsmodus;

/**
 * Mappt den Zulassungsmodus auf einen Anzeige-Text.
 */
public class ZulassungsmodusMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ZulassungsmodusMapper.class);

	private Map<Zulassungsmodus, String> zulassungsmodusMap = new HashMap<>();
	
	/**
	 * Konstruktor.
	 */
	public ZulassungsmodusMapper() {
    	zulassungsmodusMap.put(Zulassungsmodus.AUSWAHLVERFAHREN_EIGNUNGSPRUEFUNG, "Auswahlverfahren/Eignungsprüfung");
    	zulassungsmodusMap.put(Zulassungsmodus.BUNDESWEITE_ZULASSUNGSBESCHRAENKUNG, "Bundesweite Zulassungsbeschränkung");
    	zulassungsmodusMap.put(Zulassungsmodus.KEINE_ZULASSUNG_VON_STUDIENANFAENGERN, "Keine Zulassung von Studienanfängern");
    	zulassungsmodusMap.put(Zulassungsmodus.KEINE_ZULASSUNGSBESCHRAENKUNG, "Keine Zulassungsbeschränkung");
    	zulassungsmodusMap.put(Zulassungsmodus.OERTLICHE_ZULASSUNGSBESCHRAENKUNG, "Örtliche Zulassungsbeschränkung");
    }
    
    /**
     * Mappt den Zulassungsmodus auf einen Anzeige-Text.
     * 
     * @param Zulassungsmodus das Zulassungsmodus
     * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
     */
    public String map(Zulassungsmodus zulassungsmodus) {
    	if (zulassungsmodus == null) {
    		return null;
    	}
    	String text = zulassungsmodusMap.get(zulassungsmodus);
    	if (text == null) {
    		LOGGER.warn("Unbekannter Zulassungsmodus: " + zulassungsmodus);
    	}
		return text;
	}
}
