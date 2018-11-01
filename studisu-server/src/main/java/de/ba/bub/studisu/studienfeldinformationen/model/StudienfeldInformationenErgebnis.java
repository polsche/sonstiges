package de.ba.bub.studisu.studienfeldinformationen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ergebnis-Container für die StudienfeldInformationen zur Rückgabe an den
 * Client.
 * 
 * Nach Implementierung von STUDISU-205 enthält er nur noch die Liste mit den
 * Informationen zu den Studienfächern.
 */
public class StudienfeldInformationenErgebnis {

	private List<StudienfachInformationen> studienfachInformationen;

	public List<StudienfachInformationen> getStudienfachInformationen() {
		return Collections.unmodifiableList(studienfachInformationen);
	}

	public void setStudienfachInformationen(List<StudienfachInformationen> studienfachInformationen) {
		this.studienfachInformationen = new ArrayList<>(studienfachInformationen);
	}

}
