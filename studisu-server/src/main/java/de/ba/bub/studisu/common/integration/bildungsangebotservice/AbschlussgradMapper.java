package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Abschlussgrad;

/**
 * Mappt den Abschlussgrad auf einen Anzeige-Text.
 */
public class AbschlussgradMapper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AbschlussgradMapper.class);

	private Map<Abschlussgrad, String> abschlussgradMap = new HashMap<>();
    
	/**
	 * Konstruktor.
	 */
	public AbschlussgradMapper() {
    	abschlussgradMap.put(Abschlussgrad.ABSCHLUSSPRUEFUNG, "Abschlussprüfung");
    	abschlussgradMap.put(Abschlussgrad.BACHELOR_BAKKALAUREUS, "Bachelor/Bakkalaureus");
    	abschlussgradMap.put(Abschlussgrad.DIPLOM, "Diplom");
    	abschlussgradMap.put(Abschlussgrad.DIPLOM_FH, "Diplom (FH)");
    	abschlussgradMap.put(Abschlussgrad.FAKULTAETSEXAMEN, "Fakultätsexamen");
    	abschlussgradMap.put(Abschlussgrad.KEINE_ANGABE, "keine Angabe");
    	abschlussgradMap.put(Abschlussgrad.KIRCHLICHER_ABSCHLUSS, "Kirchlicher Abschluss");
    	abschlussgradMap.put(Abschlussgrad.KONZERTEXAMEN, "Konzertexamen");
    	abschlussgradMap.put(Abschlussgrad.LIZENTIATENPRUEFUNG, "Lizentiatenprüfung");
    	abschlussgradMap.put(Abschlussgrad.MAGISTER, "Magister");
    	abschlussgradMap.put(Abschlussgrad.MAGISTER_DER_THEOLOGIE, "Magister der Theologie");
    	abschlussgradMap.put(Abschlussgrad.MAGISTER_MASTER, "Magister/Master");
    	abschlussgradMap.put(Abschlussgrad.MAGISTER_MASTERSTUDIENGANG, "Magister (Masterstudiengang)");
    	abschlussgradMap.put(Abschlussgrad.MASTER, "Master");
    	abschlussgradMap.put(Abschlussgrad.PROMOTION, "Promotion");
    	abschlussgradMap.put(Abschlussgrad.STAATSEXAMEN, "Staatsexamen");
    	abschlussgradMap.put(Abschlussgrad.THEOLOGISCHE_PRUEFUNG, "Theologische Prüfung");
    }
    
    /**
     * Mappt den Abschlussgrad auf einen Anzeige-Text.
     * 
     * @param abschlussgrad der Abschlussgrad
     * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
     */
    public String map(Abschlussgrad abschlussgrad) {
    	if (abschlussgrad == null) {
    		return null;
    	}
    	String text = abschlussgradMap.get(abschlussgrad);
    	if (text == null) {
    		LOGGER.warn("Unbekannter Abschlussgrad: " + abschlussgrad);
    	}
		return text;
	}
}
