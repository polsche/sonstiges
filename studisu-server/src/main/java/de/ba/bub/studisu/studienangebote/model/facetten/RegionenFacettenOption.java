package de.ba.bub.studisu.studienangebote.model.facetten;

import java.io.Serializable;
import java.util.Objects;

import de.ba.bub.studisu.common.model.RegionEnum;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.common.model.facetten.FacettenOptionImpl;

/**
 * Backend für die Facette zu den Regionen bei der Suche nach
 * Studienangeboten.
 * Regionen bestehen zum einen aus den Bundesländern mit den 
 * Abkürzungen nach ISO31662 und zum anderen aus Nachbarländern
 * mit den Kennzeichen und vorangestelltem "i" für international.
 *
 */
public class RegionenFacettenOption extends FacettenOptionImpl implements Serializable, FacettenOption {

	private static final long serialVersionUID = 1010401751965211561L;

	private String key;

	public static final RegionenFacettenOption BADEN_WUERTTEMBERG = new RegionenFacettenOption(RegionEnum.BADEN_WUERTTEMBERG.serviceName(),
			RegionEnum.BADEN_WUERTTEMBERG.internalName(), 0, 1, "Baden-Württemberg");
	public static final RegionenFacettenOption BAYERN = new RegionenFacettenOption(RegionEnum.BAYERN.serviceName(),
			RegionEnum.BAYERN.internalName(), 1, 2, "Bayern");
	public static final RegionenFacettenOption BERLIN = new RegionenFacettenOption(RegionEnum.BERLIN.serviceName(),
			RegionEnum.BERLIN.internalName(), 2, 3, "Berlin");
	public static final RegionenFacettenOption BRANDENBURG = new RegionenFacettenOption(RegionEnum.BRANDENBURG.serviceName(),
			RegionEnum.BRANDENBURG.internalName(), 3, 4, "Brandenburg");
	public static final RegionenFacettenOption BREMEN = new RegionenFacettenOption(RegionEnum.BREMEN.serviceName(),
			RegionEnum.BREMEN.internalName(), 4, 5, "Bremen");
	public static final RegionenFacettenOption HAMBURG = new RegionenFacettenOption(RegionEnum.HAMBURG.serviceName(),
			RegionEnum.HAMBURG.internalName(), 5, 6, "Hamburg");
	public static final RegionenFacettenOption HESSEN = new RegionenFacettenOption(RegionEnum.HESSEN.serviceName(),
			RegionEnum.HESSEN.internalName(), 6, 7, "Hessen");
	public static final RegionenFacettenOption MECKLENBURG_VORPOMMERN = new RegionenFacettenOption(RegionEnum.MECKLENBURG_VORPOMMERN.serviceName(),
			RegionEnum.MECKLENBURG_VORPOMMERN.internalName(), 7, 8, "Mecklenburg-Vorpommern");
	public static final RegionenFacettenOption NIEDERSACHSEN = new RegionenFacettenOption(RegionEnum.NIEDERSACHSEN.serviceName(),
			RegionEnum.NIEDERSACHSEN.internalName(), 8, 9, "Niedersachsen");
	public static final RegionenFacettenOption NORDRHEIN_WESTFALEN = new RegionenFacettenOption(RegionEnum.NORDRHEIN_WESTFALEN.serviceName(),
			RegionEnum.NORDRHEIN_WESTFALEN.internalName(), 9, 10, "Nordrhein-Westfalen");
	public static final RegionenFacettenOption RHEINLAND_PFALZ = new RegionenFacettenOption(RegionEnum.RHEINLAND_PFALZ.serviceName(),
			RegionEnum.RHEINLAND_PFALZ.internalName(), 10, 11, "Rheinland-Pfalz");
	public static final RegionenFacettenOption SAARLAND = new RegionenFacettenOption(RegionEnum.SAARLAND.serviceName(),
			RegionEnum.SAARLAND.internalName(), 11, 12, "Saarland");
	public static final RegionenFacettenOption SACHSEN = new RegionenFacettenOption(RegionEnum.SACHSEN.serviceName(),
			RegionEnum.SACHSEN.internalName(), 12, 13, "Sachsen");
	public static final RegionenFacettenOption SACHSEN_ANHALT = new RegionenFacettenOption(RegionEnum.SACHSEN_ANHALT.serviceName(),
			RegionEnum.SACHSEN_ANHALT.internalName(), 13, 14, "Sachsen-Anhalt");
	public static final RegionenFacettenOption SCHLESWIG_HOLSTEIN = new RegionenFacettenOption(RegionEnum.SCHLESWIG_HOLSTEIN.serviceName(),
			RegionEnum.SCHLESWIG_HOLSTEIN.internalName(), 14, 15, "Schleswig-Holstein");
	public static final RegionenFacettenOption THUERINGEN = new RegionenFacettenOption(RegionEnum.THUERINGEN.serviceName(),
			RegionEnum.THUERINGEN.internalName(), 15, 16, "Thüringen");
	public static final RegionenFacettenOption GROSSBRITANNIEN = new RegionenFacettenOption(
			RegionEnum.GROSSBRITANNIEN.serviceName(), RegionEnum.GROSSBRITANNIEN.internalName(), 16, 17,
			"Großbritannien");
	public static final RegionenFacettenOption NIEDERLANDE = new RegionenFacettenOption(
			RegionEnum.NIEDERLANDE.serviceName(), RegionEnum.NIEDERLANDE.internalName(), 17, 18, "Niederlande");
	public static final RegionenFacettenOption BELGIEN = new RegionenFacettenOption(
			RegionEnum.BELGIEN.serviceName(), RegionEnum.BELGIEN.internalName(), 18, 19, "Belgien");
	public static final RegionenFacettenOption LUXEMBURG = new RegionenFacettenOption(
			RegionEnum.LUXEMBURG.serviceName(), RegionEnum.LUXEMBURG.internalName(), 19, 20, "Luxemburg");
	public static final RegionenFacettenOption FRANKREICH = new RegionenFacettenOption(
			RegionEnum.FRANKREICH.serviceName(), RegionEnum.FRANKREICH.internalName(), 20, 21, "Frankreich");
	public static final RegionenFacettenOption DAENEMARK = new RegionenFacettenOption(
			RegionEnum.DAENEMARK.serviceName(), RegionEnum.DAENEMARK.internalName(), 21, 22, "Dänemark");
	public static final RegionenFacettenOption POLEN = new RegionenFacettenOption(RegionEnum.POLEN.serviceName(),
			RegionEnum.POLEN.internalName(), 22, 23, "Polen");
	public static final RegionenFacettenOption TSCHECHISCHE_REPUBLIK = new RegionenFacettenOption(
			RegionEnum.TSCHECHISCHE_REPUBLIK.serviceName(), RegionEnum.TSCHECHISCHE_REPUBLIK.internalName(), 23, 24,
			"Tschechien");
	public static final RegionenFacettenOption OESTERREICH = new RegionenFacettenOption(
			RegionEnum.OESTERREICH.serviceName(), RegionEnum.OESTERREICH.internalName(), 24, 25, "Österreich");
	public static final RegionenFacettenOption SCHWEIZ = new RegionenFacettenOption(
			RegionEnum.SCHWEIZ.serviceName(), RegionEnum.SCHWEIZ.internalName(), 25, 26, "Schweiz");
	public static final RegionenFacettenOption NORWEGEN = new RegionenFacettenOption(
			RegionEnum.NORWEGEN.serviceName(), RegionEnum.NORWEGEN.internalName(), 26, 27, "Norwegen");
	public static final RegionenFacettenOption SPANIEN = new RegionenFacettenOption(
			RegionEnum.SPANIEN.serviceName(), RegionEnum.SPANIEN.internalName(), 27, 28, "Spanien");
	public static final RegionenFacettenOption ITALIEN = new RegionenFacettenOption(
			RegionEnum.ITALIEN.serviceName(), RegionEnum.ITALIEN.internalName(), 28, 29, "Italien");
	public static final RegionenFacettenOption SCHWEDEN = new RegionenFacettenOption(
			RegionEnum.SCHWEDEN.serviceName(), RegionEnum.SCHWEDEN.internalName(), 29, 30, "Schweden");
	public static final RegionenFacettenOption SLOWAKEI = new RegionenFacettenOption(
			RegionEnum.SLOWAKEI.serviceName(), RegionEnum.SLOWAKEI.internalName(), 30, 31, "Slowakei");
	public static final RegionenFacettenOption UNGARN = new RegionenFacettenOption(
			RegionEnum.UNGARN.serviceName(), RegionEnum.UNGARN.internalName(), 31, 32, "Ungarn");
	public static final RegionenFacettenOption DEUTSCHLAND = new RegionenFacettenOption(
			RegionEnum.DEUTSCHLAND.serviceName(), RegionEnum.DEUTSCHLAND.internalName(), 32, 33, "Deutschland");
	public static final RegionenFacettenOption KEINE_ZUORDNUNG_MOEGLICH = new RegionenFacettenOption(null, "ZZ", 33,
			34,
			"Keine Zuordnung möglich");

	/**
	 * Liste aller Optionen von {@link StudienformFacettenOption}
	 */
	final static RegionenFacettenOption[] ALL_OPTIONS = { BADEN_WUERTTEMBERG, BAYERN, BERLIN, BRANDENBURG, BREMEN,
			HAMBURG, HESSEN, MECKLENBURG_VORPOMMERN, NIEDERSACHSEN, NORDRHEIN_WESTFALEN, RHEINLAND_PFALZ, SAARLAND,
			SACHSEN, SACHSEN_ANHALT, SCHLESWIG_HOLSTEIN, THUERINGEN, GROSSBRITANNIEN, NIEDERLANDE, BELGIEN, LUXEMBURG,
			FRANKREICH, DAENEMARK, POLEN, TSCHECHISCHE_REPUBLIK, OESTERREICH, SCHWEIZ, NORWEGEN, SPANIEN, ITALIEN, 
			SCHWEDEN, SLOWAKEI, UNGARN, DEUTSCHLAND, KEINE_ZUORDNUNG_MOEGLICH };

	/**
	 * Erzeugt eine Facetten Option für die Regionenfacette.
	 * 
	 * @param name
	 *            Der Name der Facetten-Option.
	 * @param key
	 * 		      Der interne Schlüssel für die Bezeichnung der Region.
	 * @param id
	 *            Die ID der Facetten-Option.
	 * @param displayOrder
	 *            Sortierreihenfolge für die Facetten-Option, Anzeige aufsteigend nach Wert.
	 * @param label
	 *            Das anzuzeigende Label (Bezeichnung) für die Facetten-Option.
	 */
	private RegionenFacettenOption(String name, String key, int id, int displayOrder, String label) {
		super(name, id, displayOrder, label, true);
		this.key = (null == key) ? "" : key;
	}

	/**
	 * getter für isoName/key dieses Objekts
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final RegionenFacettenOption that = (RegionenFacettenOption) o;
		return super.equals(o) && Objects.equals(this.key, that.key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode() * 31 + key.hashCode();
	}

	/**
	 * Accessor für die RegionenFacettenOption auf Basis des angegebenen
	 * Namens.
	 *
	 * @param name
	 *            Der Name der gesuchten RegionenFacettenOption.
	 * @return Die zum Namen passende RegionenFacettenOption.
	 */
	public static RegionenFacettenOption forRegionServiceName(String region) {
		for (final RegionenFacettenOption option : ALL_OPTIONS) {
			if (option.key.equals(RegionEnum.internalNameFromServiceName(region))) {
				return option;
			}
		}
		return KEINE_ZUORDNUNG_MOEGLICH;
	}

	/**
	 * Liefer die Abkürzung anhand des Regionnamens.
	 * 
	 * @param region
	 * @return
	 */
	public static RegionenFacettenOption forRegionISOName(String region) {
		for (final RegionenFacettenOption option : ALL_OPTIONS) {
			if (option.key.equals(region)) {
				return option;
			}
		}
		return KEINE_ZUORDNUNG_MOEGLICH;
	}

}
