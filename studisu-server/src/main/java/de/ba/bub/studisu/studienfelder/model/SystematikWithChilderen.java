package de.ba.bub.studisu.studienfelder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ba.bub.studisu.common.model.Systematik;

/**
 * Datenobjekt, das Informationen zu einer Systematik und ihrer Kinder enthält.
 *
 * @author StraubP
 */
public class SystematikWithChilderen {

	/**
	 * Systematik.
	 */
	private Systematik systematik;

	/**
	 * Kind-Systematiken.
	 */
	private List<Systematik> children;

	/**
	 * Konstruktor.
	 *
	 * @param systematik
	 *            Systematik; nicht null
	 * @param children
	 *            Kind-Systematiken; nicht null
	 */
	public SystematikWithChilderen(Systematik systematik, List<Systematik> children) {
		super();
		if (systematik == null) {
			throw new IllegalArgumentException("systematik must not be null");
		}
		if (children == null) {
			throw new IllegalArgumentException("children must not be null");
		}
		this.systematik = systematik;
		this.children = new ArrayList<>(children);
	}

	/**
	 * Liefert die Systematik.
	 *
	 * @return nie null
	 */
	public Systematik getSystematik() {
		return systematik;
	}

	/**
	 * Liefert die Kind-Systematiken.
	 *
	 * @return nie null
	 */
	public List<Systematik> getChildren() {
		return Collections.unmodifiableList(children);
	}

}