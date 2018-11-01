package de.ba.bub.studisu.studienfelder.service;

import java.util.Map;
import java.util.Set;

import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;

/**
 * Service für Studienfeldersuche und zur Ermittlung von DKZ-Studienfeldern zu einem Studienfach.
 * Beachte: ein Studisu-Studienfeld ist ein abstraktes Konstrukt ohne direkte Abbildung in der Datenbank. 
 * Es wird im Studisu-Server erstellt und gecacht. Es stellt ein Oberelement fuer in der Regel zwei 
 * DKZ-Studienfelder grundstaendig und weiterfuehrend dar.
 * 
 * 
 * @author StraubP
 */
public interface StudienfeldSucheService {

	/**
	 * Sucht Studienfelder und liefert sie gruppiert nach Studienbereichen zurück
	 * 
	 * @param anfrage
	 *            Anfrage-Parameter
	 * @return Studienfelder gruppiert nach Studienbereichen
	 * @throws NullPointerException
	 *             falls anfrage null ist
	 */
	StudienfeldSucheErgebnis suche(final StudienfeldSucheAnfrage anfrage);
	
	/**
	 * gibt fuer die Studienfaecher aller Studienfeldbereiche eine Zuordnung zu den DKZ-Studienfeldern grundstaendig und weiterfuehrend
     * zurueck. Jeder Eintrag der zurueckgegebenen Map enthaelt:
	 * <ul>
     * <li>Key: DKZ-ID des Studienfachs</li>
     * <li>Value: DKZ-IDs der beiden DKZ-Studienfelder grundstaendig und weiterfuehrend zum Studienfach</li>
     * </ul>
	 * @return Zuordnung Studienfach - DKZ-Ids der zugehoerigen DKZ-Studienfelder grundstaendig und weiterfuehrend
	 */
    Map<Integer, Set<Integer>> getStudienfachToStudienfeldMap();
	

		
}
