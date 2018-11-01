package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Studiengangsinformationen.
 */
public class Studiengangsinformationen implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bildungsart;
	
	private String abschlusstyp;
	
	private String studienform;
	
	private String schulart;
	
	private String abschlussgrad;
	
	private String abschlussgradIntern;
	
	private String regelstudienzeit;
	
	private Boolean lehramtsbefaehigung;
	
	private String lehramtstyp;
	
	private String unterrichtssprache;
	
	private Boolean internationalerDoppelabschluss;
	
	private final List<String> dualeStudienmodelle = new ArrayList<String>();
	
	private final List<String> studienfaecher = new ArrayList<String>();

	public String getBildungsart() {
		return bildungsart;
	}

	public void setBildungsart(String bildungsart) {
		this.bildungsart = bildungsart;
	}

	public String getAbschlusstyp() {
		return abschlusstyp;
	}

	public void setAbschlusstyp(String mastertyp) {
		this.abschlusstyp = mastertyp;
	}

	public String getStudienform() {
		return studienform;
	}

	public void setStudienform(String studienform) {
		this.studienform = studienform;
	}

	public String getSchulart() {
		return schulart;
	}

	public void setSchulart(String schulart) {
		this.schulart = schulart;
	}

	public String getAbschlussgrad() {
		return abschlussgrad;
	}

	public void setAbschlussgrad(String abschlussgrad) {
		this.abschlussgrad = abschlussgrad;
	}

	public String getAbschlussgradIntern() {
		return abschlussgradIntern;
	}

	public void setAbschlussgradIntern(String abschlussgradIntern) {
		this.abschlussgradIntern = abschlussgradIntern;
	}

	public String getRegelstudienzeit() {
		return regelstudienzeit;
	}

	public void setRegelstudienzeit(String regelstudienzeit) {
		this.regelstudienzeit = regelstudienzeit;
	}

	public Boolean getLehramtsbefaehigung() {
		return lehramtsbefaehigung;
	}

	public void setLehramtsbefaehigung(Boolean lehramtsbefaehigung) {
		this.lehramtsbefaehigung = lehramtsbefaehigung;
	}

	public String getLehramtstyp() {
		return lehramtstyp;
	}

	public void setLehramtstyp(String lehramtstyp) {
		this.lehramtstyp = lehramtstyp;
	}

	public String getUnterrichtssprache() {
		return unterrichtssprache;
	}

	public void setUnterrichtssprache(String unterrichtssprache) {
		this.unterrichtssprache = unterrichtssprache;
	}

	public Boolean getInternationalerDoppelabschluss() {
		return internationalerDoppelabschluss;
	}

	public void setInternationalerDoppelabschluss(Boolean internationalerDoppelabschluss) {
		this.internationalerDoppelabschluss = internationalerDoppelabschluss;
	}

	public void addDualesStudienmodell(String dualesStudienmodell) {
		if (dualesStudienmodell != null) {
			dualeStudienmodelle.add(dualesStudienmodell);
		}
	}
	
	public List<String> getDualeStudienmodelle() {
		return new ArrayList<>(dualeStudienmodelle);
	}
	
	public void addStudienfach(String studienfach) {
		if (studienfach != null) {
			studienfaecher.add(studienfach);
		}
	}

	public List<String> getStudienfaecher() {
		return new ArrayList<>(studienfaecher);
	}
}
