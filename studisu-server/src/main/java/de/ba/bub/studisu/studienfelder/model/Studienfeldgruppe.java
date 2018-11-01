package de.ba.bub.studisu.studienfelder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Eine Studienfeldgruppe gruppiert mehrere Studienfelder zu einer "Obergruppe"
 * 
 * @author StraubP
 */
public class Studienfeldgruppe implements Serializable {

	private static final long serialVersionUID = 7531577011288033971L;

	/**
	 * Key, der die Studienfeldgruppe eindeutig identifiziert.
	 */
	private String key;

	/**
	 * Name der Studienfeldgruppe.
	 */
	private String name;

	/**
	 * Die mit der Studienfeldgruppe verknüpften DKZ-IDs. In der Regel sind dies
	 * immer zwei: Eine für "grundständig" und eine für "weiterführend".
	 */
	private Set<Integer> dkzIds = new HashSet<Integer>();

	/**
	 * Liste der Studienfelder in der Studienfeldgruppe.
	 */
	private List<Studienfeld> studienfelder = new ArrayList<Studienfeld>();

	/**
	 * Konstruktor.
	 * 
	 * @param key
	 *            Key, der die Studienfeldgruppe eindeutig identifiziert; nicht
	 *            leer!
	 * @param name
	 *            Name der Studienfeldgruppe; nicht leer!
	 * @throws {@link
	 *             IllegalArgumentException} falls name oder key null oder leer
	 */
	public Studienfeldgruppe(String key, String name) {
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
	 * Liefert den Namen der Studienfeldgruppe.
	 * 
	 * @return nie null
	 */
	public String getName() {
		return name;
	}

	/**
	 * Liefert die Liste der Studienfelder in der Studienfeldgruppe.
	 * 
	 * @return nie null
	 */
	public List<Studienfeld> getStudienfelder() {
		return Collections.unmodifiableList(studienfelder);
	}

    /**
     * Setter f&uuml; Liste von Studienfeldern der Gruppe
     * @param studienfelder Liste von Studienfeldern der Gruppe.
     */
	public void setStudienfelder(List<Studienfeld> studienfelder) {
		this.studienfelder = new ArrayList<Studienfeld>(studienfelder);
	}
	
	/**
	 * Liefert die mit der Studienfeldgruppe verknüpften DKZ-IDs. In der Regel
	 * sind dies immer zwei: eine für "grundständig" und eine für
	 * "weiterführend".
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
	 * Liefert den Key, der die Studienfeldgruppe eindeutig identifiziert.
	 * 
	 * @return nie null
	 */
	public String getKey() {
		return key;
	}

}
