package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Lehramtstyp;

/**
 * Mappt den Lehramtstyp auf einen Anzeige-Text.
 */
public class LehramtstypMapper {

	private final static Logger LOGGER = LoggerFactory.getLogger(LehramtstypMapper.class);

	private Map<Lehramtstyp, String> lehramtstypMap = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public LehramtstypMapper() {
		lehramtstypMap.put(Lehramtstyp.ANDERE, "andere");
		lehramtstypMap.put(Lehramtstyp.LEHRAEMTER_DER_GRUNDSCHULE_BZW_PRIMARSTUFE,
				"Lehrämter der Grundschule bzw. Primarstufe");
		lehramtstypMap.put(
				Lehramtstyp.LEHRAEMTER_DER_SEKUNDARSTUFE_II_ALLGEMEINBILDENDE_FAECHER_ODER_FUER_DAS_GYMNASIUM,
				"Lehrämter der Sekundarstufe II [allgemeinbildende Fächer] oder für das Gymnasium");
		lehramtstypMap.put(
				Lehramtstyp.LEHRAEMTER_DER_SEKUNDARSTUFE_II_BERUFLICHE_FAECHER_ODER_FUER_DIE_BERUFLICHEN_SCHULEN,
				"Lehrämter der Sekundarstufe II [berufliche Fächer] oder für die beruflichen Schulen");
		lehramtstypMap.put(Lehramtstyp.LEHRAEMTER_FUER_ALLE_ODER_EINZELNE_SCHULARTEN_DER_SEKUNDARSTUFE_I,
				"Lehrämter für alle oder einzelne Schularten der Sekundarstufe I");
		lehramtstypMap.put(Lehramtstyp.SONDERPAEDAGOGISCHE_LEHRAEMTER, "Sonderpädagogische Lehrämter");
		lehramtstypMap.put(
				Lehramtstyp.UEBERGREIFENDE_LEHRAEMTER_DER_PRIMARSTUFE_UND_ALLER_ODER_EINZELNER_SCHULARTEN_DER_SEKUNDARSTUFE_I,
				"Übergreifende Lehrämter der Primarstufe und aller oder einzelner Schularten der Sekundarstufe I");
	}

	/**
	 * Mappt den Lehramtstyp auf einen Anzeige-Text.
	 * 
	 * @param lehramtstyp
	 *            der Lehramtstyp
	 * @return Anzeigetext (null, falls kein Mapping vorhanden oder der
	 *         Parameter null ist)
	 */
	public String map(Lehramtstyp lehramtstyp) {
		if (lehramtstyp == null) {
			return null;
		}
		String text = lehramtstypMap.get(lehramtstyp);
		if (text == null) {
			LOGGER.warn("Unbekannter Lehramtstyp: " + lehramtstyp);
		}
		return text;
	}
}
