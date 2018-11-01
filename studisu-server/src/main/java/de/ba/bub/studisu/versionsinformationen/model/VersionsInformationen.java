package de.ba.bub.studisu.versionsinformationen.model;


/**
 * Bean-Klasse als Datenmodell fuer die Informationen zu Datenstand und Version des Backends.
 */
public class VersionsInformationen {
	
	private String datenstand;
	private String version;

	/**
	 * Getter fuer datenstand
	 * @return the datenstand
	 */
	public String getDatenstand() {
		return datenstand;
	}
	/**
	 * Setter fuer datenstand
	 * @param datenstand the datenstand to set
	 */
	public void setDatenstand(String datenstand) {
		this.datenstand = datenstand;
	}
	/**
	 * Getter fuer version
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * Setter fuer version
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
