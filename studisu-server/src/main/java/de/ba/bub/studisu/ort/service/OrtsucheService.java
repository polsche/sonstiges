package de.ba.bub.studisu.ort.service;

import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;

/**
 * Interface für den Service zur Ermöglichung der Ortssuche.
 *
 * @author loutzenhj on 29.03.2017.
 */
public interface OrtsucheService {

	/**
	 * Sucht nach dem angefragten Ort und liefert ein Ergebnis zurück.
	 *
	 * @param anfrage
	 *            Die Suchanfrage nach dem ort.
	 * @return Das Suchergebnis.
	 */
	OrtsucheErgebnis sucheOrte(OrtsucheAnfrage anfrage);
}
