package de.ba.bub.studisu.studienangebote.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.ba.bub.studisu.common.model.SuchortAbstand;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.util.GeoAbstandUtil;

/**
 * Ergebnis-Container fuer eine Studienangebotsuche zur Rueckgabe an den Client.
 */
public class StudienangebotsucheErgebnis {

	private List<StudienangebotWrapperMitAbstand> items = new ArrayList<>();
	private long maxErgebnisse;
	private long filteredOutErgebnisse;
	private List<? extends StudienangebotFacette> facetten;
	
	// Comparator für die Sortierung nach den Abständen.
	private final static Comparator<SuchortAbstand> ABSTAND_COMPARATOR = new Comparator<SuchortAbstand>() {
		@Override
		public int compare(SuchortAbstand a, SuchortAbstand b) {
			return Double.compare(a.getAbstand(), b.getAbstand());
		}
	};

	/**
	 * Diese Build-Methode liefert ein {@link StudienangebotsucheErgebnis} auf Basis der ermittelten Studienangebote.
	 *
	 * Außerdem werden auch die Facetten anhand des Suchergebnisses belegt.
	 *
	 * @param items
	 *            Die Liste mit den gefundenen {@link Studienangebot}en als Basis für das Suchergebnis.
	 * @param facetten
	 *            Die benötigten Facetten.
	 * @param filteredOutErgebnisse
	 * 			  Die Anzahl der Ergebnisse, die durch Filter unterdrückt worden sind.           
	 * @return Das Suchergebnis mit den gefundenen Studienangeboten und den ermittelten Facetten.
	 */
	public static StudienangebotsucheErgebnis withItems(List<StudienangebotWrapperMitAbstand> items,
			List<? extends StudienangebotFacette> facetten, long filteredOutErgebnisse) {
		final StudienangebotsucheErgebnis ergebnis = new StudienangebotsucheErgebnis();
		ergebnis.items = new ArrayList<>(items);
		ergebnis.maxErgebnisse = items.size();
		ergebnis.facetten = new ArrayList<>(facetten);
		ergebnis.filteredOutErgebnisse = filteredOutErgebnisse;
		return ergebnis;
	}

	/**
	 * Private c-tor - get an instance with builder above
	 */
	private StudienangebotsucheErgebnis() {

	}

	/**
	 * Beschränkt die Anzahl der Suchergebnisse anhand des gelieferten Parameters.
	 *
	 * @param paging
	 *            Legt Paginierung fest.
	 * @return Das {@link StudienangebotsucheErgebnis} erlaubt die Kaskadierung von Methodenaufrufen.
	 */
	public StudienangebotsucheErgebnis limit(Paging paging) {
		items = items.subList(paging.getOffset(), (int) Math.min(maxErgebnisse, paging.getOffset() + paging.getCount()));
		return this;
	}

	/**
	 * Berechnet die Abstände der Studienergebnisse zu den angegebenen Orten.
	 * 
	 * @param anfrageOrte
	 *            Liste mit Anfrage-Orten, für die die Abstände berechnet werden sollen. 
	 * @return Das {@link StudienangebotsucheErgebnis} erlaubt die Kaskadierung von Methodenaufrufen.
	 */
	public StudienangebotsucheErgebnis berechneAbstaende(List<AnfrageOrt> anfrageOrte) {

		for (StudienangebotWrapperMitAbstand item : this.items) {
			
			List<SuchortAbstand> abstaende = new ArrayList<>();
			for (AnfrageOrt anfrageOrt : anfrageOrte) {
				Double abstand = GeoAbstandUtil.berechneEntfernung(
					anfrageOrt.getKoordinaten(), item.getStudienangebot().getKoordinaten()
				);
				abstaende.add(new SuchortAbstand(anfrageOrt.getOrtsname(), abstand));
			}
			
			Collections.sort(abstaende, ABSTAND_COMPARATOR);
			
			item.setAbstaende(abstaende);
		}
		
		return this;
	}

	public List<StudienangebotWrapperMitAbstand> getItems() {
		return Collections.unmodifiableList(items);
	}

	public long getMaxErgebnisse() {
		return maxErgebnisse;
	}

	public long getFilteredOutErgebnisse() {
		return filteredOutErgebnisse;
	}

	public List<? extends StudienangebotFacette> getFacetten() {
		return Collections.unmodifiableList(facetten);
	}
}
