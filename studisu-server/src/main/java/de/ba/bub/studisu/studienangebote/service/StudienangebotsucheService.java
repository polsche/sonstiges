package de.ba.bub.studisu.studienangebote.service;

import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;

/**
 * Service interface for Studienangebote
 * Created by loutzenhj on 03.04.2017.
 */
public interface StudienangebotsucheService {

    /**
     * Such methode
     * @param anfrage
     * @return
     */
    StudienangebotsucheErgebnis suche(final StudienangebotsucheAnfrage anfrage);
}
