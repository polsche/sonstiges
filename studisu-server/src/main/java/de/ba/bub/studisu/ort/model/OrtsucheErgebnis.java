package de.ba.bub.studisu.ort.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.ba.bub.studisu.common.model.Ort;

/**
 * Ort suchergebnis TO Created by loutzenhj on 29.03.2017.
 */
public class OrtsucheErgebnis {

	/**
	 * nur top 10 ergebnisse zunächst mal...
	 */
	public static final int MAX_ERGEBNISSE = 10;

	private List<Ort> orte = new ArrayList<>();

	/**
	 * C-tor with list of bildungsangebotservice response orte Also does sorting and limiting of results
	 *
	 * @param orte
	 *            Liste mit Ortsangaben aus der WSDL.
	 */
	public OrtsucheErgebnis(List<Ort> orte) {
		if (orte != null) {
			// map
			this.orte = new ArrayList<>(orte);
			// remove duplicates
			this.removeDuplicates();
			// sort
			Collections.sort(this.orte);
			// limit
			if (!this.orte.isEmpty()) {
				final int max = this.orte.size() >= MAX_ERGEBNISSE ? MAX_ERGEBNISSE : this.orte.size();
				this.orte = this.orte.subList(0, max);
			}
		}
	}

	/**
	 * Removes duplicates from list.
	 */
	private void removeDuplicates() {
		final List<Ort> minifiedOrteList = new ArrayList<Ort>();
		for (int it = 0; it < this.orte.size(); ++it) {
			if (!minifiedOrteList.contains(this.orte.get(it))) {
				minifiedOrteList.add(this.orte.get(it));
			}
		}

		this.orte = minifiedOrteList;
	}

	/**
	 * Default C-tor
	 */
	public OrtsucheErgebnis() {
	}

	/**
	 * Return collection of results
	 *
	 * @return copy of list of Orte
	 */
	public Collection<Ort> getOrte() {
		return new ArrayList<>(this.orte);
	}

	/**
	 * add ort
	 *
	 * @param ort
	 */
	public void addOrt(Ort ort) {
		if (this.orte.size() < MAX_ERGEBNISSE) {
			this.orte.add(ort);
		}
	}
}
