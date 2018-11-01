package de.ba.bub.studisu.studienfach.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ba.bub.studisu.common.model.Studienfach;

/**
 * TO fuer das Ergebnis einer Studienfachsuche.
 *
 * @author schneidek084 on 2017-06-01
 */
public class StudienfachErgebnis {

	/**
	 * Studienfächer.
	 */
	private List<Studienfach> studienfaecher = new ArrayList<>();

	/**
	 * Konstruktor.
	 * 
	 * @param studienfaecher
	 *            nicht null
	 */
	public StudienfachErgebnis(List<Studienfach> studienfaecher) {
		super();
		this.studienfaecher = new ArrayList<>(studienfaecher);
	}

	/**
	 * Liefert die Studienfächer.
	 * 
	 * @return nicht null.
	 */
	public List<Studienfach> getStudienfaecher() {
		return Collections.unmodifiableList(studienfaecher);
	}
}
