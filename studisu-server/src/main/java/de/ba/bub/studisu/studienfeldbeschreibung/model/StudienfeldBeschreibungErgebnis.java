package de.ba.bub.studisu.studienfeldbeschreibung.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ergebnis-Container für eine StudienfeldBeschreibung zur Rückgabe an den
 * Client.
 */
public class StudienfeldBeschreibungErgebnis {

	private String neutralKurzBezeichnung;
	private List<String> studienfeldbeschreibungen;
	private String codeNr;
	private String oberCodeNr;

	public String getNeutralKurzBezeichnung() {
		return neutralKurzBezeichnung;
	}

	public void setNeutralKurzBezeichnung(String neutralKurzBezeichnung) {
		this.neutralKurzBezeichnung = neutralKurzBezeichnung;
	}

	public List<String> getStudienfeldbeschreibungen() {
		return Collections.unmodifiableList(studienfeldbeschreibungen);
	}

	public void setStudienfeldbeschreibungen(List<String> studienfeldbeschreibungen) {
		this.studienfeldbeschreibungen = new ArrayList<>(studienfeldbeschreibungen);
	}

	public String getCodeNr() {
		return codeNr;
	}

	public void setCodeNr(String codeNr) {
		this.codeNr = codeNr;
	}

	public String getOberCodeNr() {
		return oberCodeNr;
	}

	public void setOberCodeNr(String oberCodeNr) {
		this.oberCodeNr = oberCodeNr;
	}
}
