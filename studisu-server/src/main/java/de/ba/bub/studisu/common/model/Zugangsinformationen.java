package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Zugangsinformationen implements Serializable {

	private static final long serialVersionUID = 1L;

	private String zulassungsmodus;
	
	private String zulassungsmodusInfo;
	
	private String voraussetzungen;
	
	private Boolean ohneAbiMoeglich;
	
	private final List<OhneAbiZugangsbedingung> ohneAbiZugangsbedingungen = new ArrayList<OhneAbiZugangsbedingung>();
	
	private String akkreditierung;
	
	private String akkreditierungVon;
	
	private String akkreditierungBis;
	
	private String akkreditierungsbedingungen;

	public String getZulassungsmodus() {
		return zulassungsmodus;
	}

	public void setZulassungsmodus(String zulassungsmodus) {
		this.zulassungsmodus = zulassungsmodus;
	}

	public String getZulassungsmodusInfo() {
		return zulassungsmodusInfo;
	}

	public void setZulassungsmodusInfo(String zulassungsmodusInfo) {
		this.zulassungsmodusInfo = zulassungsmodusInfo;
	}

	public String getVoraussetzungen() {
		return voraussetzungen;
	}

	public void setVoraussetzungen(String voraussetzungen) {
		this.voraussetzungen = voraussetzungen;
	}

	public void addOhneAbiZugangsbedingung(OhneAbiZugangsbedingung ohneAbiZugangsbedingung) {
		ohneAbiZugangsbedingungen.add(ohneAbiZugangsbedingung);
	}
	
	public List<OhneAbiZugangsbedingung> getOhneAbiZugangsbedingungen() {
		return new ArrayList<>(ohneAbiZugangsbedingungen);
	}

	public Boolean getOhneAbiMoeglich() {
		return ohneAbiMoeglich;
	}

	public void setOhneAbiMoeglich(Boolean ohneAbiMoeglich) {
		this.ohneAbiMoeglich = ohneAbiMoeglich;
	}

	public String getAkkreditierung() {
		return akkreditierung;
	}

	public void setAkkreditierung(String akkreditierung) {
		this.akkreditierung = akkreditierung;
	}

	public String getAkkreditierungVon() {
		return akkreditierungVon;
	}

	public void setAkkreditierungVon(String akkreditierungVon) {
		this.akkreditierungVon = akkreditierungVon;
	}

	public String getAkkreditierungBis() {
		return akkreditierungBis;
	}

	public void setAkkreditierungBis(String akkreditierungBis) {
		this.akkreditierungBis = akkreditierungBis;
	}

	public String getAkkreditierungsbedingungen() {
		return akkreditierungsbedingungen;
	}

	public void setAkkreditierungsbedingungen(String akkreditierungsbedingungen) {
		this.akkreditierungsbedingungen = akkreditierungsbedingungen;
	}
	
}
