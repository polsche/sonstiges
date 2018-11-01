package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bildungsart;

/**
 * Mappt die Bildungsart auf einen Anzeige-Text.
 */
public class BildungsartMapper {

	private final static Logger LOGGER = LoggerFactory.getLogger(BildungsartMapper.class);

	private Map<Bildungsart, String> bildungsartMap = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public BildungsartMapper() {
		bildungsartMap.put(Bildungsart.AKTIVIERUNG_BERUFLICHE_EINGLIEDERUNG, "Aktivierung/Berufliche Eingliederung");
		bildungsartMap.put(Bildungsart.ALLGEMEINBILDUNG, "Allgemeinbildung");
		bildungsartMap.put(Bildungsart.BERUFLICHE_GRUNDQUALIFIKATION, "Berufliche Grundqualifikation");
		bildungsartMap.put(Bildungsart.BERUFSAUSBILDUNG, "Berufsausbildung");
		bildungsartMap.put(Bildungsart.FORTBILDUNG_QUALIFIZIERUNG, "Fortbildung/Qualifizierung");
		bildungsartMap.put(Bildungsart.GESETZLICH_GESETZESAEHNLICH_GEREGELTE_FORTBILDUNG_QUALIFIZIERUNG, "Gesetzlich/gesetzesähnlich geregelte Fortbildung/Qualifizierung");
		bildungsartMap.put(Bildungsart.INTEGRATIONSKURS, "Integrationskurs");
		bildungsartMap.put(Bildungsart.KEINE_ZUORDNUNG_MOEGLICH, "Keine Zuordnung möglich");
		bildungsartMap.put(Bildungsart.NACHHOLEN_DES_BERUFSABSCHLUSSES, "Nachholen des Berufsabschlusses");
		bildungsartMap.put(Bildungsart.REHABILITATION, "Rehabilitation");
		bildungsartMap.put(Bildungsart.STUDIENANGEBOT_GRUNDSTAENDIG, "Studienangebot - grundständig");
		bildungsartMap.put(Bildungsart.STUDIENANGEBOT_WEITERFUEHREND, "Studienangebot - weiterführend");
		bildungsartMap.put(Bildungsart.UMSCHULUNG, "Umschulung");
	}

	/**
	 * Mappt die Bildungsart auf einen Anzeige-Text.
	 * 
	 * @param Bildungsart
	 *            die Bildungsart
	 * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
	 */
	public String map(Bildungsart bildungsart) {
		if (bildungsart == null) {
			return null;
		}
		String text = bildungsartMap.get(bildungsart);
		if (text == null) {
			LOGGER.warn("Unbekannte Bildungsart: " + bildungsart);
		}
		return text;
	}
}
