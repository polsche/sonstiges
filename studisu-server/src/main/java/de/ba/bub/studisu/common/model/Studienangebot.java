package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.ort.model.GeoKoordinaten;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;

/**
 * Bean-Klasse als Datenmodell für die Studienangebote aus dem
 * Bildungsangebotsservice.
 */
public class Studienangebot implements Serializable {

	private static final long serialVersionUID = -3983899129983663920L;

	private String id;
	private String bildungsanbieterName;
	private Integer bildungsanbieterId;
	private boolean bildungsanbieterHasSignet;
	private String studiBezeichnung;
	private String studiInhalt;
	private String studiBeginn;
	private List<String> studienfaecher;

	// studienort
	private AdresseKurz studienort;
	private GeoKoordinaten koordinaten;

	// Kleiner als "bundesweit", aber größer als Deutschland...
	private Double abstand = (double) UmkreisFacette.BUNDESWEIT.getUmkreisKm() - 1;  

	private StudienformFacettenOption studienform;
	private HochschulartFacettenOption hochschulart;
	private StudientypFacettenOption studientyp;
	private RegionenFacettenOption region;
	private Set<FacettenOption> fitFuerStudiumFacettenOptions = new HashSet<>();

	/**
	 * Liefert die {@link FitFuerStudiumFacettenOption}.
	 *
	 * @return Die zu diesem Studienangebot gehörige Belegung der
	 *         ffStudium-Facette.
	 */
	public Set<FacettenOption> getFitFuerStudiumFacettenOptions() {
		return new HashSet<>(fitFuerStudiumFacettenOptions);
	}

	/**
	 * Setzt die {@link FitFuerStudiumFacettenOption}.
	 *
	 * @param fitFuerStudiumFacettenOption
	 *            Die Belegung der ffStudium-Facette für dieses Studienangebot.
	 */
	public void addFitFuerStudiumFacettenOption(FitFuerStudiumFacettenOption fitFuerStudiumFacettenOption) {
		if (fitFuerStudiumFacettenOption != null) {
			this.fitFuerStudiumFacettenOptions.add(fitFuerStudiumFacettenOption);
		}
	}

	// map of links for example osa or studicheck
	private List<ExternalLink> externalLinks = new ArrayList<>();

	/**
	 * Liefert die StudientypFacettenOption.
	 *
	 * @return Die zu diesem Studienangebot gehörige Belegung der
	 *         Studientyp-Facette.
	 */
	public StudientypFacettenOption getStudientyp() {
		return studientyp;
	}

	/**
	 * Setzt die StudientypFacettenOption.
	 *
	 * @param studientyp
	 *            Die Belegung der Studientyp-Facette für dieses Studienangebot.
	 */
	public void setStudientyp(StudientypFacettenOption studientyp) {
		this.studientyp = studientyp;
	}

	/**
	 * Liefert die HochschulartFacettenOption.
	 *
	 * @return Die zu diesem Studienangebot gehörige Belegung der
	 *         Hochschulart-Facette.
	 */
	public HochschulartFacettenOption getHochschulart() {
		return this.hochschulart;
	}

	/**
	 * Setzt die HochschulartFacettenOption.
	 *
	 * @param hochschulart
	 *            Die Belegung der Hochschulart-Facette für dieses
	 *            Studienangebot.
	 */
	public void setHochschulart(HochschulartFacettenOption hochschulart) {
		this.hochschulart = hochschulart;
	}

	/**
	 * Liefert die StudienformFacettenOption.
	 *
	 * @return Die zu diesem Studienangebot gehörige Belegung der
	 *         Studienform-Facette.
	 */
	public StudienformFacettenOption getStudienform() {
		return this.studienform;
	}

	/**
	 * Setzt die StudienformFacettenOption.
	 *
	 * @param studienform
	 *            Die Belegung der Hochschulart-Facette für dieses Studienform.
	 */
	public void setStudienform(StudienformFacettenOption studienform) {
		this.studienform = studienform;
	}

	/**
	 * getter for id
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * setter for id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getter for bildungsanbieterName
	 * 
	 * @return the bildungsanbieterName
	 */
	public String getBildungsanbieterName() {
		return bildungsanbieterName;
	}

	/**
	 * setter for bildungsanbieterName
	 * 
	 * @param bildungsanbieterName
	 *            the bildungsanbieterName to set
	 */
	public void setBildungsanbieterName(String bildungsanbieterName) {
		this.bildungsanbieterName = bildungsanbieterName;
	}

	/**
	 * getter for bildungsanbieterId
	 * 
	 * @return the bildungsanbieterId
	 */
	public Integer getBildungsanbieterId() {
		return bildungsanbieterId;
	}

	/**
	 * setter for bildungsanbieterId
	 * 
	 * @param bildungsanbieterId
	 *            the bildungsanbieterId to set
	 */
	public void setBildungsanbieterId(Integer bildungsanbieterId) {
		this.bildungsanbieterId = bildungsanbieterId;
	}

	/**
	 * getter for bildungsanbieterHasSignet
	 * 
	 * @return the flag for the existence of a signet for the bildungsanbieter 
	 */
	public boolean getBildungsanbieterHasSignet() {
		return bildungsanbieterHasSignet;
	}

	/**
	 * setter for bildungsanbieterHasSignet
	 * 
	 * @param bildungsanbieterHasSignet
	 *            set flag for the signet
	 */
	public void setBildungsanbieterHasSignet(boolean bildungsanbieterHasSignet) {
		this.bildungsanbieterHasSignet = bildungsanbieterHasSignet;
	}

	/**
	 * getter for studiBezeichnung
	 * 
	 * @return the studiBezeichnung
	 */
	public String getStudiBezeichnung() {
		return studiBezeichnung;
	}

	/**
	 * setter for studiBezeichnung
	 * 
	 * @param studiBezeichnung
	 *            the studiBezeichnung to set
	 */
	public void setStudiBezeichnung(String studiBezeichnung) {
		this.studiBezeichnung = studiBezeichnung;
	}

	/**
	 * getter for studiInhalt
	 * 
	 * @return the studiInhalt
	 */
	public String getStudiInhalt() {
		return studiInhalt;
	}

	/**
	 * setter for studiInhalt
	 * 
	 * @param studiInhalt
	 *            the studiInhalt to set
	 */
	public void setStudiInhalt(String studiInhalt) {
		this.studiInhalt = studiInhalt;
	}

	/**
	 * getter for studiBeginn
	 * 
	 * @return the studiBeginn
	 */
	public String getStudiBeginn() {
		return studiBeginn;
	}

	/**
	 * setter for studiBeginn
	 * 
	 * @param studiBeginn
	 *            the studiBeginn to set
	 */
	public void setStudiBeginn(String studiBeginn) {
		this.studiBeginn = studiBeginn;
	}

	/**
	 * getter for studienort
	 * 
	 * @return the studienort
	 */
	public AdresseKurz getStudienort() {
		return studienort;
	}

	/**
	 * setter for studienort
	 * 
	 * @param studienort
	 *            the studienort to set
	 */
	public void setStudienort(AdresseKurz studienort) {
		this.studienort = studienort;
	}

	/**
	 * getter for studienfaecher
	 * 
	 * @return the studienfaecher
	 */
	public List<String> getStudienfaecher() {
		return Collections.unmodifiableList(studienfaecher);
	}

	/**
	 * setter for studienfaecher
	 * 
	 * @param studienfaecher
	 *            the studienfaecher to set
	 */
	public void setStudienfaecher(List<String> studienfaecher) {
		this.studienfaecher = new ArrayList<>(studienfaecher);
	}

	/**
	 * setter for region
	 * 
	 * @param bundesland
	 *            the bundesland to set
	 */
	public void setRegion(RegionenFacettenOption region) {
		this.region = region;
	}

	/**
	 * getter for region
	 * 
	 * @return the region
	 */
	public RegionenFacettenOption getRegion() {
		return this.region;
	}

	/**
	 * put a link in my link map
	 * 
	 * @param externalLink
	 *            link to add
	 */
	public void addExternalLink(ExternalLink externalLink) {
		if (externalLink != null) {
			this.externalLinks.add(externalLink);
		}
	}

	public List<ExternalLink> getExternalLinks() {
		return new ArrayList<>(externalLinks);
	}

	/**
	 * Ermöglicht die Abstandsberechnung zu den Suchorten.
	 */
	public GeoKoordinaten getKoordinaten() {
		return koordinaten;
	}

	public void setKoordinaten(GeoKoordinaten koordinaten) {
		this.koordinaten = koordinaten;
	}

	/**
	 * Getter für den Abstand der GeoKoordinaten des Studienangebots von einem
	 * beliebigen aber festen anderen Ort.
	 * 
	 * @return Gesetzter Abstand für Sortierung
	 */
	public double getAbstand() {
		return this.abstand;
	}

	/**
	 * Setter für den Abstand der GeoKoordinaten des Studienangebots von einem
	 * beliebigen aber festen anderen Ort.
	 * 
	 * @param Double
	 *            abstand Der für die Sortierung benötigte Abstand zwischen dem
	 *            Studienort und einem anderen Ort.
	 */
	public void setAbstand(Double abstand) {
		this.abstand = abstand;
	}

}
