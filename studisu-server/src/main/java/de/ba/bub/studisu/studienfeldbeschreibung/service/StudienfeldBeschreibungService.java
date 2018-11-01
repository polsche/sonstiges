package de.ba.bub.studisu.studienfeldbeschreibung.service;

import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;

/**
 * Service zur Abfrage der StudienfeldBeschreibung.
 */
public interface StudienfeldBeschreibungService {

	/**
	 * Anhand der DKZ-Id wird die Beschreibung zu dem Studienfeld ermittelt.
	 *
	 * Vorab findet eine Validierung der DKZ-Id auf Zugehörigkeit zur HA / HC
	 * Gruppe statt.
	 * 
	 * @param anfrage
	 *            Die Anfrage mit der DKZ-ID für das Studienfeld.
	 * @return nie null
	 * @throws ValidationException
	 *             falls Systematik zur DKZ-ID in der anfrage nicht existiert
	 * @throws NullPointerException
	 *             falls anfrage null ist
	 */
	StudienfeldBeschreibungErgebnis suche(final StudienfeldBeschreibungAnfrage anfrage);
}
