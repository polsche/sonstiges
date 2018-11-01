package de.ba.bub.studisu.common.model;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bundesland;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Staat;

/**
 * Enum mit Bundesländern und deren Werten im ISO-Standard sowie Autokennzeichen für Länder.
 */
public enum RegionEnum {
	
	// Deutsche Bundesländer
	BADEN_WUERTTEMBERG (Bundesland.BADEN_WUERTTEMBERG.value(), "BW"), 
	BAYERN (Bundesland.BAYERN.value(), "BY"), 
	BERLIN (Bundesland.BERLIN.value(), "BE"), 
	BRANDENBURG (Bundesland.BRANDENBURG.value(), "BB"), 
	BREMEN (Bundesland.BREMEN.value(), "HB"), 
	HAMBURG (Bundesland.HAMBURG.value(), "HH"), 
	HESSEN (Bundesland.HESSEN.value(), "HE"), 
	MECKLENBURG_VORPOMMERN (Bundesland.MECKLENBURG_VORPOMMERN.value(), "MV"), 
	NIEDERSACHSEN (Bundesland.NIEDERSACHSEN.value(), "NI"), 
	NORDRHEIN_WESTFALEN (Bundesland.NORDRHEIN_WESTFALEN.value(), "NW"), 
	RHEINLAND_PFALZ (Bundesland.RHEINLAND_PFALZ.value(), "RP"), 
	SAARLAND (Bundesland.SAARLAND.value(), "SL"), 
	SACHSEN (Bundesland.SACHSEN.value(), "SN"), 
	SACHSEN_ANHALT (Bundesland.SACHSEN_ANHALT.value(), "ST"), 
	SCHLESWIG_HOLSTEIN (Bundesland.SCHLESWIG_HOLSTEIN.value(), "SH"), 
	THUERINGEN (Bundesland.THUERINGEN.value(), "TH"),
	
	// Europäische Staaten
	POLEN (Staat.POLEN.value(), "iPL"), 
	TSCHECHISCHE_REPUBLIK (Staat.TSCHECHISCHE_REPUBLIK.value(), "iCZ"), 
	BELGIEN (Staat.BELGIEN.value(), "iB"), 
	DEUTSCHLAND (Staat.DEUTSCHLAND.value(), "iD"), 
	SCHWEIZ (Staat.SCHWEIZ.value(), "iCH"), 
	GROSSBRITANNIEN (Staat.GROSSBRITANNIEN.value(),	"iGB"), 
	OESTERREICH (Staat.OESTERREICH.value(), "iA"), 
	DAENEMARK (Staat.DAENEMARK.value(), "iDK"), 
	NIEDERLANDE (Staat.NIEDERLANDE.value(), "iNL"), 
	LUXEMBURG (Staat.LUXEMBURG.value(), "iLU"), 
	FRANKREICH (Staat.FRANKREICH.value(), "iF"),
	NORWEGEN (Staat.NORWEGEN.value(), "iN"),
	SPANIEN (Staat.SPANIEN.value(), "iE"),
	ITALIEN (Staat.ITALIEN.value(), "iI"),
	SCHWEDEN (Staat.SCHWEDEN.value(), "iS"),
	SLOWAKEI (Staat.SLOWAKEI.value(), "iSK"),
	UNGARN (Staat.UNGARN.value(), "iH");

	private final String serviceName;
	private final String internalName;

	/**
	 * Kontruktor mit Regionennamen im Service und bei uns in der Studisu-Anwendung.
	 * 
	 * @param serviceName
	 * 		Der Name der Region im BildungsangebotService.
	 * @param internalName
	 * Die Bezeichnung der Region (Abkürzung für Bundesländer aus ISO31662, für Länder nach Autokennzeichen) 
	 */
	RegionEnum(String serviceName, String internalName) {
		this.serviceName = serviceName;
		this.internalName = internalName;
	}

	/**
	 * Liefert den Servie-Namen der Region.
	 * 
	 * @return Service-Name der Region.
	 */
	public String serviceName() {
		return serviceName;
	}

	/**
	 * Liefert den Applikations-internen Namen der Region.
	 *  
	 * @return interner Name der Region.
	 */
	public String internalName() {
		return internalName;
	}

	/**
	 * Liefert den internen Namen der Region zu einem gegebenen Service-Namen.
	 * 
	 * Liefert Leerstring, wenn der Service-Name nicht gefunden wurde.
	 * 
	 * @param serviceName
	 * 		Der Service-Name der Region.
	 * @return interner Name der Region.
	 */
	public static String internalNameFromServiceName(String serviceName) {
		for (RegionEnum c : RegionEnum.values()) {
			if (c.serviceName.equals(serviceName)) {
				return c.internalName();
			}
		}
		return "";
	}

	/**
	 * Liefert den Service-Namen der Region zu einem gegebenen internen Namen.
	 * 
	 * Liefert Leerstring, wenn der interne Name nicht gefunden wurde.
	 * 
	 * @param internalName
	 * 		Der interne Name der Region.
	 * @return Service-Name der Region.
	 */
	public static String serviceNameFromInternalName(String internalName) {
		for (RegionEnum c : RegionEnum.values()) {
			if (c.internalName.equals(internalName)) {
				return c.serviceName();
			}
		}
		throw new IllegalArgumentException(internalName);
	}

}
