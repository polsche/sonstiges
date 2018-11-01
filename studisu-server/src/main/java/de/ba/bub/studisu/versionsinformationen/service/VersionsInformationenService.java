package de.ba.bub.studisu.versionsinformationen.service;

import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;

/**
 * Interface für die Abfrage von Datenstand und Version des Backends.
 * @author OettlJ
 *
 */
public interface VersionsInformationenService {

    /**
     * Liefert Informationen zu Datenstand und Version des Backends.
     * @param anfrage
     * @return Informationen zu Datenstand und Version des Backends.
     */
    public VersionsInformationen getVersionsInfo(final VersionsInformationenAnfrage anfrage);	
}
