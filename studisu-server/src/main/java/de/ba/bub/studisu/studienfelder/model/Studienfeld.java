package de.ba.bub.studisu.studienfelder.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Studienfeld.
 * 
 * @author StraubP
 */
public class Studienfeld implements Serializable {

	private static final long serialVersionUID = 1472197774142386562L;

	/**
	 * Key, der das Studienfeld eindeutig identifiziert.
	 */
	private String key;

	/**
	 * Name des Studienfelds.
	 */
	private String name;

	/**
	 * Die mit dem Studienfeld verknüpften DKZ-IDs. In der Regel sind dies immer
	 * zwei: Eine für "grundständig" und eine für "weiterführend".
	 */
	private Set<Integer> dkzIds = new HashSet<Integer>();

	/**
	 * Konstruktor.
	 * 
	 * @param key
	 *            Key, der das Studienfeld eindeutig identifiziert; nicht leer!
	 * @param name
	 *            Name des Studienfelds; nicht leer
	 * @throws {@link
	 *             IllegalArgumentException} falls name oder key null oder leer
	 */
	public Studienfeld(String key, String name) {
		super();
		if (key == null || key.trim().isEmpty()) {
			throw new IllegalArgumentException("key must not be blank");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be blank");
		}
		this.key = key;
		this.name = name;
	}

	/**
	 * Liefert den Namen des Studienfelds.
	 * 
	 * @return nie null
	 */
	public String getName() {
		return name;
	}

	/**
	 * Liefert die mit dem Studienfeld verknüpften DKZ-IDs. In der Regel sind
	 * dies immer zwei: eine für "grundständig" und eine für "weiterführend".
	 * 
	 * @return nie null
	 */
	public Set<Integer> getDkzIds() {
		return Collections.unmodifiableSet(dkzIds);
	}

    /**
     * Setter f&uuml; Liste von DKZ IDs.
     * @param dkzIds  Liste von DKZ IDs.
     */
	public void setDkzIds(Set<Integer> dkzIds) {
		this.dkzIds = new HashSet<>(dkzIds);
	}

	/**
	 * Liefert den Key, der das Studienfeld eindeutig identifiziert.
	 * 
	 * @return nie null
	 */
	public String getKey() {
		return key;
	}
}
