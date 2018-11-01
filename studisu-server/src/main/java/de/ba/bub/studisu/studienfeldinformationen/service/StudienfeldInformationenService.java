package de.ba.bub.studisu.studienfeldinformationen.service;

import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenAnfrage;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;

/**
 * Service zur Abfrage der StudienfeldInformationen.
 */
public interface StudienfeldInformationenService {

	/**
	 * Anhand der DKZ-Id werden Informationen zu dem Studienfeld ermittelt.
	 * Vorab findet eine Validierung der DKZ-Id auf Zugehörigkeit zur HA / HC
	 * Gruppe statt.
	 * 
	 * @param anfrage
	 *            StudienfeldInformationenAnfrage
	 * @return nie null
	 * @throws ValidationException
	 *             falls Systematik zur DKZ-ID in der anfrage nicht existiert
	 * @throws NullPointerException
	 *             falls anfrage null ist
	 */
	StudienfeldInformationenErgebnis suche(final StudienfeldInformationenAnfrage anfrage);
}
