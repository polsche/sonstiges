package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;

/**
 * Bean-Klasse als Datenmodell fuer die detaillierten Studienangebot Informationen aus dem Bildungsangebotsservice.
 * basiert auf de.ba.bub.studisu.studienangebote.model.Studienangebot;
 */
public class StudienangebotInformationen implements Serializable {

	private static final long serialVersionUID = 297219071957853762L;

	// studienbezeichnung
	private String bezeichnung;

	// studieninhalte
	private String inhalt;
	private String veranstaltungZusatzlink;
	private String studienschwerpunkte;
	private String studiuminformationen;
	
	// dauer und termine
	private Dauer dauer;

	// kosten/gebühren/förderung
	private String kosten;
	
	// zugangsinformationen;
	private Zugangsinformationen zugangsinformationen;
	
	// studiengangsinformationen
	private Studiengangsinformationen studiengangsinformationen;
    
	private StudienformFacettenOption studienform;
	private HochschulartFacettenOption hochschulart;
	
	// studienort
	private AdresseKurz studienort;

	// kontakt 
	private Kontakt kontakt;
	
	// studienanbieter
	private AdresseKurz bildungsanbieter;
	private int bildungsanbieterId;
	private boolean bildungsanbieterHasSignet;
	
	private boolean isHrkDatensatz;
	
	// veroeffentlichungsinfos
	/**
	 * ID der Veranstaltung.
	 */
	private String id;
	private String  aktualisierungsdatum;
	
	private String studienfaecherCsv;

	private StudientypFacettenOption studientyp;
	
	// map of links for example osa or studicheck
	private List<ExternalLink> externalLinks = new ArrayList<ExternalLink>();

	// Details zur Navigation (vor/zurueck)
	private String prevElementId = null;
	private int numPrevElements = 0;
	private String nextElementId = null;
	private int numNextElements = 0;
	private int currentPage = 1;

	/**
	 * Liefert die StudientypFacettenOption.
	 *
	 * @return Die zu diesem Studienangebot gehoerige Belegung der Studientyp-Facette.
	 */
	public StudientypFacettenOption getStudientyp() {
		return studientyp;
	}

	/**
	 * Setzt die StudientypFacettenOption.
	 *
	 * @param studientyp
	 *            Die Belegung der Studientyp-Facette fuer dieses Studienangebot.
	 */
	public void setStudientyp(StudientypFacettenOption studientyp) {
		this.studientyp = studientyp;
	}

	/**
	 * getter for dauer
	 * @return the dauer
	 */
	public Dauer getDauer() {
		return dauer;
	}

	/**
	 * setter for dauer
	 * @param dauer the dauer to set
	 */
	public void setDauer(Dauer dauer) {
		this.dauer = dauer;
	}

	/**
	 * getter for studienort
	 * @return the studienort
	 */
	public AdresseKurz getStudienort() {
		return studienort;
	}

	/**
	 * setter for studienort
	 * @param studienort the studienort to set
	 */
	public void setStudienort(AdresseKurz studienort) {
		this.studienort = studienort;
	}

	/**
	 * Liefert die HochschulartFacettenOption.
	 *
	 * @return Die zu diesem Studienangebot gehoerige Belegung der Hochschulart-Facette.
	 */
	public HochschulartFacettenOption getHochschulart() {
		return this.hochschulart;
	}

	/**
	 * Setzt die HochschulartFacettenOption.
	 *
	 * @param hochschulart
	 *            Die Belegung der Hochschulart-Facette fuer dieses Studienangebot.
	 */
	public void setHochschulart(HochschulartFacettenOption hochschulart) {
		this.hochschulart = hochschulart;
	}

	/**
	 * Liefert die StudienformFacettenOption.
	 *
	 * @return Die zu diesem Studienangebot gehoerige Belegung der Studienform-Facette.
	 */
	public StudienformFacettenOption getStudienform() {
		return this.studienform;
	}

	/**
	 * Setzt die StudienformFacettenOption.
	 *
	 * @param studienform
	 *            Die Belegung der Studienform-Facette fuer dieses Studienform.
	 */
	public void setStudienform(StudienformFacettenOption studienform) {
		this.studienform = studienform;
	}

	/**
	 * Liefert die ID der Veranstaltung (nicht die des Studienangebots).
	 * 
	 * @return id ID der Veranstaltung
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setzt die ID der Veranstaltung (nicht die des Studienangebots).
	 * 
	 * @param id
	 *            ID der Veranstaltung
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getter for bezeichnung
	 * @return the bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * setter for bezeichnung
	 * @param bezeichnung the studiBezeichnung to set
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * getter for inhalt
	 * @return the inhalt
	 */
	public String getInhalt() {
		return inhalt;
	}

	/**
	 * setter for inhalt
	 * @param inhalt the inhalt to set
	 */
	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}

	/**
	 * getter for the CSV for the studienfaecher IDs
	 * @return the CSV for the studienfaecher IDs.
	 */
	public String getStudienfaecherCsv() {
		return studienfaecherCsv;
	}

	/**
	 * setter for the CSV for the studienfaecher IDs
	 * @param studienfaecherCsv the CSV to set
	 */
	public void setStudienfaecherCsv(String studienfaecherCsv) {
		this.studienfaecherCsv = studienfaecherCsv;
	}
	
	/**
	 * getter for kontakt
	 * @return the kontakt
	 */
	public Kontakt getKontakt() {
		return kontakt;
	}

	/**
	 * setter for kontakt
	 * @param kontakt the kontakt to set
	 */
	public void setKontakt(Kontakt kontakt) {
		this.kontakt = kontakt;
	}

	/**
	 * getter for bildungsanbieter
	 * @return the bildungsanbieter
	 */
	public AdresseKurz getBildungsanbieter() {
		return bildungsanbieter;
	}

	/**
	 * setter for bildungsanbieter
	 * @param bildungsanbieter the bildungsanbieter to set
	 */
	public void setBildungsanbieter(AdresseKurz bildungsanbieter) {
		this.bildungsanbieter = bildungsanbieter;
	}

	/**
	 * Getter for isHrkDatensatz
	 * @return true if this is an hRK record
	 */
	public boolean getIsHrkDatensatz() {
		return isHrkDatensatz;
	}

	/**
	 * Setter for isHrkDatensatz
	 * @param hrkDatensatz true if this is an hRK record
	 */
	public void setIsHrkDatensatz(boolean hrkDatensatz) {
		isHrkDatensatz = hrkDatensatz;
	}

	/**
	 * getter for aktualisierungsdatum
	 * @return the aktualisierungsdatum
	 */
	public String getAktualisierungsdatum() {
		return aktualisierungsdatum;
	}

	/**
	 * setter for aktualisierungsdatum
	 * @param aktualisierungsdatum the aktualisierungsdatum to set
	 */
	public void setAktualisierungsdatum(String aktualisierungsdatum) {
		this.aktualisierungsdatum = aktualisierungsdatum;
	}

	/**
	 * put a link in my link map
	 * @param externalLink link to add
	 */
	public void addExternalLink(ExternalLink externalLink){
		if(externalLink!=null) {
			this.externalLinks.add(externalLink);
		}
	}

	public List<ExternalLink> getExternalLinks() {
		return new ArrayList<ExternalLink>(externalLinks);
	}
	
	/**
	 * Hole den Zusatzlink der Veranstaltung
	 * @return zusatzlink aus BAService
	 */
	public String getVeranstaltungZusatzlink() {
		return veranstaltungZusatzlink;
	}

	/**
	 * Zusatzlink der Veranstaltung
	 * @param veranstaltungZusatzlink
	 */
	public void setVeranstaltungZusatzlink(String veranstaltungZusatzlink) {
		this.veranstaltungZusatzlink = veranstaltungZusatzlink;
	}

	/**
	 * @return the prevElementId
	 */
	public String getPrevElementId() {
		return prevElementId;
	}

	/**
	 * @param prevElementId the prevElementId to set
	 */
	public void setPrevElementId(String prevElementId) {
		this.prevElementId = prevElementId;
	}

	/**
	 * @return the numPrevElements
	 */
	public int getNumPrevElements() {
		return numPrevElements;
	}

	/**
	 * @param numPrevElements the numPrevElements to set
	 */
	public void setNumPrevElements(int numPrevElements) {
		this.numPrevElements = numPrevElements;
	}

	/**
	 * @return the nextElementId
	 */
	public String getNextElementId() {
		return nextElementId;
	}

	/**
	 * @param nextElementId the nextElementId to set
	 */
	public void setNextElementId(String nextElementId) {
		this.nextElementId = nextElementId;
	}

	/**
	 * @return the numNextElements
	 */
	public int getNumNextElements() {
		return numNextElements;
	}

	/**
	 * @param numNextElements the numNextElements to set
	 */
	public void setNumNextElements(int numNextElements) {
		this.numNextElements = numNextElements;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * getter for bildungsanbieterId
	 * 
	 * @return the bildungsanbieterId
	 */
	public int getBildungsanbieterId() {
		return bildungsanbieterId;
	}

	/**
	 * setter for bildungsanbieterId
	 * 
	 * @param bildungsanbieterId
	 *            the bildungsanbieterId to set
	 */
	public void setBildungsanbieterId(int bildungsanbieterId) {
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

	public String getStudienschwerpunkte() {
		return studienschwerpunkte;
	}

	public void setStudienschwerpunkte(String studienschwerpunkte) {
		this.studienschwerpunkte = studienschwerpunkte;
	}

	public String getStudiuminformationen() {
		return studiuminformationen;
	}

	public void setStudiuminformationen(String studiuminformationen) {
		this.studiuminformationen = studiuminformationen;
	}
	
	public String getKosten() {
		return kosten;
	}
	
	public void setKosten(String kosten) {
		this.kosten = kosten;
	}
	
	public Zugangsinformationen getZugangsinformationen() {
		return zugangsinformationen;
	}
	
	public void setZugangsinformationen(Zugangsinformationen zugang) {
		this.zugangsinformationen = zugang;
	}
	
	public Studiengangsinformationen getStudiengangsinformationen() {
		return studiengangsinformationen;
	}
	
	public void setStudiengangsinformationen(Studiengangsinformationen studiengangsinformationen) {
		this.studiengangsinformationen = studiengangsinformationen;
	}
}
