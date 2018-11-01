package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Akkreditierung;

/**
 * Mappt die Akkreditierung auf einen Anzeige-Text.
 */
public class AkkreditierungsMapper {

	private final static Logger LOGGER = LoggerFactory.getLogger(AkkreditierungsMapper.class);

	private Map<Akkreditierung, String> akkreditierungMap = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public AkkreditierungsMapper() {
		akkreditierungMap.put(Akkreditierung.ERSTAKKREDITIERUNG, "Erstakkreditierung");
		akkreditierungMap.put(Akkreditierung.KEINE_ANGABE, "keine Angabe");
		akkreditierungMap.put(Akkreditierung.REAKKREDITIERUNG, "Reakkreditierung");
	}

	/**
	 * Mappt die Akkreditierung auf einen Anzeige-Text.
	 * 
	 * @param Akkreditierung
	 *            die Akkreditierung
	 * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
	 */
	public String map(Akkreditierung akkreditierung) {
		if (akkreditierung == null) {
			return null;
		}
		String text = akkreditierungMap.get(akkreditierung);
		if (text == null) {
			LOGGER.warn("Unbekannte Akkreditierung: " + akkreditierung);
		}
		return text;
	}
}
