package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zugangsbedingung;

/**
 * Mappt die Zugangsbedingung auf einen Anzeige-Text.
 */
public class ZugangsbedingungMapper {

	private final static Logger LOGGER = LoggerFactory.getLogger(ZugangsbedingungMapper.class);

	private Map<Zugangsbedingung, String> zugangsbedingungMap = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public ZugangsbedingungMapper() {
		zugangsbedingungMap.put(
				Zugangsbedingung.MEHRJAEHRIGE_BERUFSAUSBILDUNG_UND_ODER_BERUFSERFAHRUNG_IN_EINEM_BERUFSFELD_MIT_FACHLICHER_NAEHE_ZUM_STUDIENFACH,
				"Mehrjährige Berufsausbildung und/oder Berufserfahrung in einem Berufsfeld mit fachlicher Nähe zum Studienfach");
		zugangsbedingungMap.put(
				Zugangsbedingung.MEHRJAEHRIGE_BERUFSAUSBILDUNG_UND_ODER_BERUFSERFAHRUNG_IN_EINEM_BERUFSFELD_OHNE_FACHLICHE_NAEHE_ZUM_STUDIENFACH,
				"Mehrjährige Berufsausbildung und/oder Berufserfahrung in einem Berufsfeld ohne fachliche Nähe zum Studienfach");
		zugangsbedingungMap.put(Zugangsbedingung.MEISTERPRUEFUNG_ODER_GLEICHWERTIGE_AUFSTIEGSFORTBILDUNG,
				"Meisterprüfung oder gleichwertige Aufstiegsfortbildung");
	}

	/**
	 * Mappt die Zugangsbedingung auf einen Anzeige-Text.
	 * 
	 * @param Zugangsbedingung
	 *            die Zugangsbedingung
	 * @return Anzeigetext (null, falls kein Mapping vorhanden oder der Parameter null ist)
	 */
	public String map(Zugangsbedingung zugangsbedingung) {
		if (zugangsbedingung == null) {
			return null;
		}
		String text = zugangsbedingungMap.get(zugangsbedingung);
		if (text == null) {
			LOGGER.warn("Unbekannte Zugangsbedingung: " + zugangsbedingung);
		}
		return text;
	}
}
