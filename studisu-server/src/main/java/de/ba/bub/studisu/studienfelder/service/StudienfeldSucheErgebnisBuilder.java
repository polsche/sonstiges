package de.ba.bub.studisu.studienfelder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.studienfelder.model.Studienfeld;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.model.Studienfeldgruppe;
import de.ba.bub.studisu.studienfelder.model.SystematikWithChilderen;

/**
 * Builder für die Liste für {@link StudienfeldSucheErgebnis}.
 * 
 * @author StraubP
 */
class StudienfeldSucheErgebnisBuilder {

	/**
	 * Speichert Daten zu allen Studienfeldgruppen anhand deren eindeutigen
	 * Keys.
	 */
	private Map<String, Studienfeldgruppe> studienfeldgruppenData = new HashMap<>();

	/**
	 * Speichert Daten zu allen Studienfeldern anhand deren eindeutigen Keys.
	 */
	private Map<String, Studienfeld> studienfeldData = new HashMap<>();

	/**
	 * Fügt eine Collection von SystematikWithChilderen dem Builder hinzu.
	 * Werden einzelne Studienfeldgruppen oder -felder als schon vorhanden
	 * erkannt, wird der entsprechende, bereits vorhandene Name nicht
	 * überschrieben.
	 * 
	 * @param systematikenWithChilderen
	 *            Collection von {@link SystematikWithChilderen}
	 * @return self
	 */
	public StudienfeldSucheErgebnisBuilder add(Collection<SystematikWithChilderen> systematikenWithChilderen) {
		for (SystematikWithChilderen systematikWithChilderen : systematikenWithChilderen) {
			add(systematikWithChilderen);
		}
		return this;
	}

	/**
	 * Fügt eine SystematikWithChilderen dem Builder hinzu. Werden einzelne
	 * Studienfeldgruppen oder -felder als schon vorhanden erkannt, wird der
	 * entsprechende, bereits vorhandene Name nicht überschrieben.
	 * 
	 * @param systematikWithChilderen
	 *            {@link SystematikWithChilderen}
	 */
	private void add(SystematikWithChilderen systematikWithChilderen) {
		Systematik systematik = systematikWithChilderen.getSystematik();

		String key = getKey(systematik);
		Studienfeldgruppe studienfeldgruppe = studienfeldgruppenData.get(key);
		if (studienfeldgruppe == null) {
			studienfeldgruppe = new Studienfeldgruppe(key, systematik.getKurzBezeichnungNeutral());
			studienfeldgruppenData.put(key, studienfeldgruppe);
		}

		Set<Integer> gruppenDkzIds = new HashSet<>(studienfeldgruppe.getDkzIds());
		gruppenDkzIds.add(systematik.getId());
		studienfeldgruppe.setDkzIds(gruppenDkzIds);

		Collection<Systematik> children = systematikWithChilderen.getChildren();
		for (Systematik child : children) {
			String childsKey = getKey(child);
			Studienfeld studienfeld = studienfeldData.get(childsKey);
			if (studienfeld == null) {
				studienfeld = new Studienfeld(childsKey, child.getKurzBezeichnungNeutral());

				List<Studienfeld> gruppenSFelder = new ArrayList<>(studienfeldgruppe.getStudienfelder());
				gruppenSFelder.add(studienfeld);
				studienfeldgruppe.setStudienfelder(gruppenSFelder);
				
				studienfeldData.put(childsKey, studienfeld);
			}

			Set<Integer> feldDkzIds = new HashSet<>(studienfeld.getDkzIds());
			feldDkzIds.add(child.getId());
			studienfeld.setDkzIds(feldDkzIds);
		}
	}

	/**
	 * Liefert einen Key, der eine Studienfeldgruppe bzw. Studienfeld eindeutig
	 * identifiziert.
	 * 
	 * @param systematik
	 *            {@link Systematik}
	 * @return der Key
	 */
	private String getKey(Systematik systematik) {
		String codenr = systematik.getCodenr();
		String[] vals = codenr.split(" ");
		return vals.length > 1 ? vals[1] : vals[0];
	}

	/**
	 * Baut und liefert die Liste der Studienfeldgruppen.
	 * 
	 * @return StudienfeldSucheErgebnis mit Liste von Studienfeldgruppen
	 *         aufsteigend sortiert nach dem Key. Die Studienfelder in den
	 *         Studienfeldgruppen sind ebenso sortiert.
	 */
	public StudienfeldSucheErgebnis create() {

		List<Studienfeldgruppe> list = new ArrayList<Studienfeldgruppe>(studienfeldgruppenData.values());

		Collections.sort(list, new Comparator<Studienfeldgruppe>() {
			@Override
			public int compare(Studienfeldgruppe o1, Studienfeldgruppe o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		for (Studienfeldgruppe studienfeldgruppe : list) {
			List<Studienfeld> gruppenSFelder = new ArrayList<>(studienfeldgruppe.getStudienfelder());
			Collections.sort(gruppenSFelder, new Comparator<Studienfeld>() {
				@Override
				public int compare(Studienfeld o1, Studienfeld o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});
			studienfeldgruppe.setStudienfelder(gruppenSFelder);
		}

		return new StudienfeldSucheErgebnis(list);
	}
}