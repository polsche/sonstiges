package de.ba.bub.studisu.common.service;


/**
 * Service-Fassade für Zugriff auf Manifest.
 *
 * @author OettlJ
 */
public interface ManifestService {
	
	/**
	 * Liefert die Implementation-Version aus dem Manifest.
	 * @return die Implementation-Version aus dem Manifest.
	 */
	public String getImplementationVersion();
}
