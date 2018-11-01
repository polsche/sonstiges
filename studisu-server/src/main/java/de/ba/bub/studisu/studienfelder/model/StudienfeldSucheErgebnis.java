package de.ba.bub.studisu.studienfelder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ergebnis-Container fuer eine Studienfeldsuche zur Rueckgabe an den Client.
 *
 * @author StraubP
 */
public class StudienfeldSucheErgebnis implements Serializable {

	private static final long serialVersionUID = 5146738612620562716L;

	/**
	 * Liste der Studienfeldgruppen, welche wiederum die entsprechenden
	 * Studienfelder enthalten.
	 */
	private List<Studienfeldgruppe> studienfeldgruppen = new ArrayList<>();

	/**
	 * Kosntruktor.
	 *
	 * @param studienfeldgruppen
	 *            Liste der Studienfeldgruppen, welche wiederum die
	 *            entsprechenden Studienfelder enthalten; nicht null
	 * @throws {@link
	 *             NullPointerException} falls studienfeldgruppe null
	 */
	public StudienfeldSucheErgebnis(List<Studienfeldgruppe> studienfeldgruppen) {
		if (studienfeldgruppen == null) {
			throw new IllegalArgumentException("studienbereiche must not be null");
		}
		this.studienfeldgruppen = new ArrayList<>(studienfeldgruppen);
	}

	/**
	 * Liefert die Liste der Studienfeldgruppen, welche wiederum die
	 * entsprechenden Studienfelder enthalten.
	 *
	 * @return nie null
	 */
	public List<Studienfeldgruppe> getStudienfeldgruppen() {
		return Collections.unmodifiableList(studienfeldgruppen);
	}

	// TODO Builder einbauen?!
}
