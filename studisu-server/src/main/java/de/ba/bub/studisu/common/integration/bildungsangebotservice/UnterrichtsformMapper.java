package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;

/**
 * Mappt die Unterrichtsform auf einen Anzeige-Text.
 */
public class UnterrichtsformMapper {

	private final static Logger LOGGER = LoggerFactory.getLogger(UnterrichtsformMapper.class);

	private Map<Unterrichtsform, String> unterrichtsformMap = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public UnterrichtsformMapper() {
		unterrichtsformMap.put(Unterrichtsform.AUF_ANFRAGE, "Auf Anfrage");
		unterrichtsformMap.put(Unterrichtsform.BLOCKUNTERRICHT, "Blockunterricht");
		unterrichtsformMap.put(Unterrichtsform.EINZELMASSNAHME_EINZELBETREUUNG, "Einzelmaßnahme (Einzelbetreuung)");
		unterrichtsformMap.put(Unterrichtsform.FERNUNTERRICHT_FERNSTUDIUM, "Fernunterricht/ Fernstudium");
		unterrichtsformMap.put(Unterrichtsform.GRUPPENMASSNAHME, "Gruppenmaßnahme");
		unterrichtsformMap.put(Unterrichtsform.INHOUSE_FIRMENSEMINAR, "Inhouse-/ Firmenseminar");
		unterrichtsformMap.put(Unterrichtsform.SELBSTSTUDIUM_E_LEARNING_BLENDED_LEARNING, "Selbststudium/ E-learning/ Blended Learning");
		unterrichtsformMap.put(Unterrichtsform.TEILZEIT, "Teilzeit");
		unterrichtsformMap.put(Unterrichtsform.VOLLZEIT, "Vollzeit");
		unterrichtsformMap.put(Unterrichtsform.WOCHENENDVERANSTALTUNG, "Wochenendveranstaltung");
	}

	/**
	 * Mappt die Unterrichtsform auf einen Anzeige-Text.
	 * 
	 * @param Unterrichtsform
	 *            die Unterrichtsform
	 * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
	 */
	public String map(Unterrichtsform unterrichtsform) {
		if (unterrichtsform == null) {
			return null;
		}
		String text = unterrichtsformMap.get(unterrichtsform);
		if (text == null) {
			LOGGER.warn("Unbekannte Unterrichtsform: " + unterrichtsform);
		}
		return text;
	}
}
