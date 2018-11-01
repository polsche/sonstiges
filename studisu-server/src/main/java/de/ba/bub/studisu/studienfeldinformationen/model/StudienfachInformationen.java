package de.ba.bub.studisu.studienfeldinformationen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ergebnis-Container für die Studienfach-Informationen zur Rückgabe an den
 * Client. Die Studienfach-Informationen werden aus der HTML-Response des WCC
 * geparst.
 */
public class StudienfachInformationen {

	private int id;
	private String neutralBezeichnung;
	private List<String> studienfachbeschreibungen;
	private List<String> studiengangsbezeichnungen;
	private int studienfachFilmId;
	private int count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNeutralBezeichnung() {
		return neutralBezeichnung;
	}

	public void setNeutralBezeichnung(String neutralBezeichnung) {
		this.neutralBezeichnung = neutralBezeichnung;
	}

	public List<String> getStudienfachbeschreibungen() {
		return Collections.unmodifiableList(studienfachbeschreibungen);
	}

	public void setStudienfachbeschreibungen(List<String> studienfachbeschreibungen) {
		this.studienfachbeschreibungen = new ArrayList<>(studienfachbeschreibungen);
	}
	
	public List<String> getStudiengangsbezeichnungen() {
		return Collections.unmodifiableList(studiengangsbezeichnungen);
	}
	
	public void setStudiengangsbezeichnungen(List<String> studiengangsbezeichnungen) {
		this.studiengangsbezeichnungen = new ArrayList<>(studiengangsbezeichnungen);
	}
	
	public int getStudienfachFilmId() {
		return studienfachFilmId;
	}
	
	public void setStudienfachFilmId(int studienfachFilmId) {
		this.studienfachFilmId = studienfachFilmId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}