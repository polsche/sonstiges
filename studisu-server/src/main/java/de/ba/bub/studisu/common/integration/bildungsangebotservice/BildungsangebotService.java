package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.List;

import de.ba.bub.studisu.common.integration.ExternalService;
import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.common.model.Signet;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;

/**
 * BildungsangebotService Interface
 * eingefuegt, damit Spring Dependency Injection funktioniert 
 * trotz Cacheable Annotation an implementierten Methoden
 * sonst bekamen wir hier einen Fehler
 * Consider injecting the bean as one of its interfaces
 *
 * @author KunzmannC
 */
public interface BildungsangebotService extends ExternalService {

	/**
	 * Gibt zurueck Anzahl der Bildungsangebote pro <code>DKZ-Id</code>
	 *
	 * @param dkzId
	 * @return
	 */
	int holeAnzahlBildungsangebote(Integer dkzId);

	/**
	 * Gibt die Studienangebotinformationen für die Veranstaltung mit der übergebenen
	 * ID zurück.
	 * @param vgId ID der Veranstaltung (= Studienangebot).
	 * @return Studienangebotinformationen für die Veranstaltung mit der übergebenen ID.
	 */
	StudienangebotInformationen holeStudienangebotInformationen(Integer vgId);

	/**
	 * Gibt eine Liste von Studienangeboten, die mit den übergebenen
	 * Informationen gefunden werden konnten, zurück.
	 * @param aort Anfrageort mit Koordinaten.
	 * @param umkreis Umkreis.
	 * @param studienfaecher Studienfächer-IDs.
	 * @return eine Liste von Studienangeboten, die mit den übergebenen
	 * Informationen gefunden werden konnten.
	 */
	List<Studienangebot> findStudienangebote(AnfrageOrt aort, int umkreis, List<Integer> studienfaecher);

	/**
	 * Gibt eine Liste von Orten, die mit den übergebenen
	 * Informationen gefunden werden konnten, zurück.
	 * @param plz die PLZ, nach der gesucht werden soll.
	 * @param ort der Ortsname, nach dem gesucht werden soll.
	 * @return eine Liste von Orten, die mit den übergebenen
	 * Informationen gefunden werden konnten.
	 */
	List<Ort> findOrte(String plz, String ort);

	/**
	 * Gibt das Signet Fuer einen Bildungsanbieter zurück.
	 * @param banid die BAN-ID.
	 * @return das Signet sofern vorhanden
	 */
	Signet getSignet(Integer banid);

}