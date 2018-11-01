package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungssemester;

/**
 * Mappt das Zulassungssemester auf einen Anzeige-Text.
 */
public class ZulassungssemesterMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ZulassungssemesterMapper.class);

	private Map<Zulassungssemester, String> zulassungssemesterMap = new HashMap<>();
	
	/**
	 * Konstruktor.
	 */
	public ZulassungssemesterMapper() {
    	zulassungssemesterMap.put(Zulassungssemester.FRUEHJAHRSSEMESTER, "Frühjahrssemester");
    	zulassungssemesterMap.put(Zulassungssemester.FRUEHJAHRSTRIMESTER, "Frühjahrstrimester");
    	zulassungssemesterMap.put(Zulassungssemester.HERBSTSEMESTER, "Herbstsemester");
    	zulassungssemesterMap.put(Zulassungssemester.HERBSTTRIMESTER, "Herbsttrimester");
    	zulassungssemesterMap.put(Zulassungssemester.KEINE_ANGABE, "keine Angabe");
    	zulassungssemesterMap.put(Zulassungssemester.SOMMERSEMESTER, "Sommersemester");
    	zulassungssemesterMap.put(Zulassungssemester.SOMMERTRIMESTER, "Sommertrimester");
    	zulassungssemesterMap.put(Zulassungssemester.WINTERSEMESTER, "Wintersemester");
    	zulassungssemesterMap.put(Zulassungssemester.WINTERTRIMESTER, "Wintertrimester");
    }
    
    /**
     * Mappt das Zulassungssemester auf einen Anzeige-Text.
     * 
     * @param Zulassungssemester das Zulassungssemester
     * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
     */
    public String map(Zulassungssemester zulassungssemester) {
    	if (zulassungssemester == null) {
    		return null;
    	}
    	String text = zulassungssemesterMap.get(zulassungssemester);
    	if (text == null) {
    		LOGGER.warn("Unbekanntes Zulassungssemester: " + zulassungssemester);
    	}
		return text;
	}
}
