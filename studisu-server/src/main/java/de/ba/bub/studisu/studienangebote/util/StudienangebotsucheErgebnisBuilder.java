package de.ba.bub.studisu.studienangebote.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.facetten.AuswahlComparator;
import de.ba.bub.studisu.common.model.facetten.Facette.Auswahl;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;

/**
 * Erzeugt ein StudienangebotsucheErgebnis aus Eingabe-Parametern.
 * 
 * @author StraubP
 */
@Component
public class StudienangebotsucheErgebnisBuilder {

	/**
	 * Sortierung der Auswahl-Listen.
	 */
	private static final AuswahlComparator AUSWAHL_COMPARATOR = new AuswahlComparator();

	/**
	 * Erzeugt ein StudienangebotsucheErgebnis zu einer bestimmten
	 * {@link StudienangebotsucheAnfrage}.
	 * 
	 * @param studienangebote
	 *            zu filternde Studienangebote
	 * @param anfrage
	 *            StudienangebotsucheAnfrage
	 * @return StudienangebotsucheErgebnis
	 */
	public StudienangebotsucheErgebnis build(List<Studienangebot> studienangebote, StudienangebotsucheAnfrage anfrage) {

		FilterParameter filterParameter = new FilterParameter(anfrage);

		// Nach diesen Facetten (mit FacettenOptionen) soll gefiltert werden.
		List<StudienangebotFacette> filterFacetten = new ArrayList<>(filterParameter.getFilterFacetten());

		// Diese FacettenOptionen wurden vom Anwender explizit angehakt.
		Set<FacettenOption> selectedOptions = filterParameter.getSelectedOptions();

		// Filtern
		FilterResult filterResult = new FilterProcessor(filterFacetten).process(studienangebote);

		// Setzen der Auswahl-Listen für die Facetten
		AuswahlSetter auswahlSetter = new AuswahlSetter(selectedOptions);
		auswahlSetter.process(filterFacetten, filterResult.getFacettenOptionCounts());

		filterFacetten.add(anfrage.getUmkreisFacette());

		filterParameter.setFilterFacetten(filterFacetten);

		return StudienangebotsucheErgebnis.withItems(filterResult.getStudienangebote(), filterFacetten, 
				studienangebote.size() - filterResult.getStudienangebote().size());
	}

	/**
	 * Liefert Informationen zur gewünschten Filterung.
	 * 
	 * @author StraubP
	 */
	static class FilterParameter {

		/**
		 * Facetten mit FacettenOptionen nach denen gefiltert werden soll.
		 */
		private List<StudienangebotFacette> filterFacetten = new ArrayList<>();

		/**
		 * FacettenOptionen, die explizit als Filter-Optionen angegeben wurden.
		 */
		private Set<FacettenOption> selectedOptions = new HashSet<>();

		/**
		 * Konstruktor.
		 * 
		 * @param anfrage
		 *            {@link StudienangebotsucheAnfrage}; nicht null
		 */
		public FilterParameter(StudienangebotsucheAnfrage anfrage) {
			super();
			if (anfrage == null) {
				throw new IllegalArgumentException("null für Parameter anfrage ist nicht erlaubt!");
			}
			handleFacette(anfrage.getStudientypFacette(), new StudientypFacette().withAllOptions());
			handleFacette(anfrage.getStudienformFacette(), new StudienformFacette().withAllOptions());
			handleFacette(anfrage.getHochschulartFacette(), new HochschulartFacette().withAllOptions());
			handleFacette(anfrage.getBundeslandFacette(), new RegionenFacette().withAllOptions());
			handleFacette(anfrage.getFitFuerStudiumFacette(), new FitFuerStudiumFacette().withAllOptions());
		}

		/**
		 * Liefert die Facetten mit FacettenOptionen nach denen (implizit)
		 * gefiltert werden soll.
		 * 
		 * @return nie null
		 */
		public List<StudienangebotFacette> getFilterFacetten() {
			return Collections.unmodifiableList(filterFacetten);
		}

		/**
		 * setter fur filterfacetten
		 * 
		 * @param filterFacetten
		 */
		public void setFilterFacetten (List<StudienangebotFacette> filterFacetten) {
			this.filterFacetten = new ArrayList<>(filterFacetten);
		}

		/**
		 * Liefert alle FacettenOptionen, die explizit als Filter-Optionen
		 * angegeben wurden, also z.B. vom Benutzer explizit angehakt wurden.
		 * <p>
		 * Achtung: Diese FacettenOptionen können sich von den FacettenOptionen
		 * aus getFilterFacetten() unterscheiden. So enthalten z.B. die
		 * FacettenOptionen aus getFilterFacetten() alle FilterOptionen einer
		 * bestimmten Facette, wenn zu dieser gar keine FilterOption explizit
		 * angegeben wurde. Für den eigentlichen Filtervorgang sind also immer
		 * die FilterOptionen aus getFilterFacetten() heranzuziehen!
		 * </p>
		 * 
		 * @return nie null
		 */
		public Set<FacettenOption> getSelectedOptions() {
			return Collections.unmodifiableSet(selectedOptions);
		}

		/**
		 * Initialisiert filterFacetten und selectedOptions für eine Facette.
		 * 
		 * @param facette
		 *            Facette mit Optionen, nach der gefiltert werden soll
		 * @param defaultFacette
		 *            Facette mit Optionen, nach der gefiltert werden soll, wenn
		 *            facette null ist
		 */
		private final void handleFacette(StudienangebotFacette facette, StudienangebotFacette defaultFacette) {
			if (facette == null) {
				filterFacetten.add(defaultFacette);
			} else {
				selectedOptions.addAll(facette.getSelectedOptions());
				filterFacetten.add(facette);
			}
		}
	}

	/**
	 * Filter für Studienangebote.
	 * 
	 * @author StraubP
	 */
	static class FilterProcessor {

		/**
		 * FilterFacetten, mit denen gefiltert werden soll.
		 */
		private final List<StudienangebotFacette> filterFacetten;

		/**
		 * Alle selektierten FilterOptionen der FilterFacetten.
		 */
		private final List<Set<FacettenOption>> optionsToFilter;

		/**
		 * Konstruktor.
		 * 
		 * @param filterFacetten
		 *            filterFacetten, mit denen gefiltert werden soll; nicht
		 *            null
		 * @param list
		 */
		public FilterProcessor(List<StudienangebotFacette> filterFacetten) {
			super();
			if (filterFacetten == null) {
				throw new IllegalArgumentException("null für Parameter filterFacetten ist nicht erlaubt!");
			}
			this.filterFacetten = new ArrayList<>(filterFacetten);

			optionsToFilter = new ArrayList<>();
			for (StudienangebotFacette facette : filterFacetten) {
				optionsToFilter.add(new HashSet<FacettenOption>(facette.getSelectedOptions()));
			}
		}

		/**
		 * Filtert die übergebenen Studienangebote.
		 * 
		 * @param studienangebote
		 *            die zu filternden Studienangebote
		 * @return das Filter-Ergebnis
		 */
		public FilterResult process(List<Studienangebot> studienangebote) {

			/** Informationen über die Häufigkeit von FacettenOptionen. */
			Map<FacettenOption, Integer> haeufigkeitFacettenOptionen = new HashMap<>();

			/** Liste der gefilterten Studienangebote. */
			List<StudienangebotWrapperMitAbstand> result = new ArrayList<>();

			for (Studienangebot studienangebot : studienangebote) {

				List<FacetteOptions> optionsOfStudienangebot = new ArrayList<>();

				for (StudienangebotFacette facette : filterFacetten) {
					optionsOfStudienangebot.add(new FacetteOptions(facette, facette.findOptions(studienangebot)));
				}

				boolean include = filter(optionsOfStudienangebot, haeufigkeitFacettenOptionen);
				if (include) {
					StudienangebotWrapperMitAbstand abstandWrapper = new StudienangebotWrapperMitAbstand(
							studienangebot, null);
					result.add(abstandWrapper);
				}
			}

			return new FilterResult(result, haeufigkeitFacettenOptionen);
		}

		/**
		 * Prüft, ob die FacettenOptionen eines Studienangebots zu den
		 * Filter-FacettenOptionen passt und updated Informationen über die
		 * Häufigkeit von FacettenOptionen.
		 * 
		 * @param optionsOfStudienangebot
		 *            FacettenOptionen eines Studienangebots
		 * @param haeufigkeitFacettenOptionen
		 *            Informationen über die Häufigkeit von FacettenOptionen
		 * @return true, falls Passung
		 */
		private boolean filter(List<FacetteOptions> optionsOfStudienangebot,
				Map<FacettenOption, Integer> haeufigkeitFacettenOptionen) {

			int idxOfNotContains = -1;

			for (int i = 0; i < optionsToFilter.size(); i++) {
				Set<FacettenOption> selectedOptionen = optionsToFilter.get(i);
				Set<FacettenOption> optionenOfStudienangebot = optionsOfStudienangebot.get(i).getOptions();
				
				if (!CollectionUtils.containsAny(selectedOptionen, optionenOfStudienangebot)) {
					if (idxOfNotContains >= 0) {
						// Wenn 2 Facetten nicht passen, passt das
						// Studienangebot sicher gar nicht.
						return false;
					}
					idxOfNotContains = i;
				}
			}

			// An dieser Stelle wissen wir, dass das entsprechende
			// Studienangebot entweder zum Ergebnis gehört (idxOfNotContains <
			// 0) oder es zum Ergebnis dazu käme, wenn eine der FacettenOptionen
			// der i-ten Facette selektiert wird (idxOfNotContains = i).

			for (int i = 0; i < optionsOfStudienangebot.size(); i++) {
				FacetteOptions optionenOfStudienangebot = optionsOfStudienangebot.get(i);

				if (idxOfNotContains < 0) {
					// Wenn alle Facetten passen, zählen wir alle
					// FacettenOptionen aller Facetten.
					updateHaeufigkeitFacettenOptionen(optionenOfStudienangebot, haeufigkeitFacettenOptionen, true);
				} else if (idxOfNotContains == i) {
					// Wenn eine Facette nicht passt, zählen wir nur die
					// FacettenOptionen dieser Facette - denn erst wenn eine
					// dieser Optionen selektiert wird, landet das
					// Studienangebot im Ergebnis. Hier zählen wir also die
					// Fälle, die im Frontend mit einem + angezeigt werden.
					updateHaeufigkeitFacettenOptionen(optionenOfStudienangebot, haeufigkeitFacettenOptionen, false);
					return false;
				}
			}
			return true;
		}

		/**
		 * Updatet die Häufigkeit der FacettenOptionen. Für alle übergebenen
		 * FacettenOptionen, wir deren Anzahl um 1 erhöht.
		 * 
		 * @param facettenOptionen
		 *            FacettenOptionen
		 * @param haeufigkeitFacettenOptionen
		 *            Informationen über die Häufigkeit von FacettenOptionen
		 * @param onlySelected
		 *            Häufigkeit nur für selektierte Facetten aktualisieren
		 */
		private void updateHaeufigkeitFacettenOptionen(FacetteOptions facettenOptionen,
				Map<FacettenOption, Integer> haeufigkeitFacettenOptionen, boolean onlySelected) {
			
			Set<FacettenOption> options = facettenOptionen.getOptions();
			for (FacettenOption facettenOption : options) {
				
				if (onlySelected) {
					boolean selected = facettenOptionen.getFacette().getSelectedOptions().contains(facettenOption);
					
					if (!selected) {
						continue;
					}
				}
				
				Integer anz = haeufigkeitFacettenOptionen.get(facettenOption);
				if (anz == null) {
					anz = 0;
				}
				haeufigkeitFacettenOptionen.put(facettenOption, anz + 1);
			}
		}
	}

	/**
	 * Ergebnis einer Filterung von Studienangeboten.
	 * 
	 * @author StraubP
	 */
	static class FilterResult {

		/**
		 * Liste der gefilterten Studienangebote.
		 */
		private final List<StudienangebotWrapperMitAbstand> studienangebote;

		/**
		 * Informationen über die Häufigkeit von FacettenOptionen.
		 */
		private final Map<FacettenOption, Integer> facettenOptionCounts;

		/**
		 * Konstruktor.
		 * 
		 * @param studienangebote
		 *            Liste der gefilterten Studienangebote
		 * @param facettenOptionCounts
		 *            Informationen über die Häufigkeit von FacettenOptionen
		 */
		public FilterResult(List<StudienangebotWrapperMitAbstand> studienangebote,
				Map<FacettenOption, Integer> facettenOptionCounts) {
			super();
			if (studienangebote == null) {
				throw new IllegalArgumentException("null für Parameter studienangebote ist nicht erlaubt!");
			}
			if (facettenOptionCounts == null) {
				throw new IllegalArgumentException("null für Parameter facettenOptionCounts ist nicht erlaubt!");
			}
			this.studienangebote = new ArrayList<>(studienangebote);
			this.facettenOptionCounts = new HashMap<>(facettenOptionCounts);
		}

		/**
		 * Liefert Informationen über die Häufigkeit von FacettenOptionen. Gibt
		 * es zu einer bestimmten Facettenoption keinen Wert, bedeutet das, dass
		 * er 0 Mal vorkommt.
		 * 
		 * @return nie null
		 */
		public Map<FacettenOption, Integer> getFacettenOptionCounts() {
			return facettenOptionCounts;
		}

		/**
		 * Liefert die gefilterten Studienangebote.
		 * 
		 * @return Liste von Studienangeboten
		 */
		public List<StudienangebotWrapperMitAbstand> getStudienangebote() {
			return Collections.unmodifiableList(studienangebote);
		}
	}

	/**
	 * Berechnet und setzt die Auswahl-Felder der Facetten.
	 * 
	 * @author StraubP
	 */
	static class AuswahlSetter {

		/**
		 * FacettenOptionen, die explizit als Filter-Optionen angegeben wurden.
		 */
		private final Set<FacettenOption> selectedOptions;

		/**
		 * Konstruktor.
		 * 
		 * @param selectedOptions
		 *            FacettenOptionen, die explizit als Filter-Optionen
		 *            angegeben wurden; nicht null
		 */
		public AuswahlSetter(Set<FacettenOption> selectedOptions) {
			super();
			if (selectedOptions == null) {
				throw new IllegalArgumentException("null für Parameter anfrageOptions ist nicht erlaubt!");
			}
			this.selectedOptions = new HashSet<>(selectedOptions);
		}

		/**
		 * Berechnet und füllt die Felder "auswahl" in den übergebenen Facetten.
		 * 
		 * @param filterFacetten
		 *            die FilterFacetten
		 * @param facettenOptionCounts
		 *            Informationen über die Häufigkeit von FacettenOptionen
		 */
		public void process(List<StudienangebotFacette> filterFacetten,
				Map<FacettenOption, Integer> facettenOptionCounts) {
			for (StudienangebotFacette facette : filterFacetten) {
				List<Auswahl> auswahlen = new ArrayList<>();
				for (FacettenOption option : facette.getAllOptions()) {
					Integer c = facettenOptionCounts.get(option);
					int anzahl = c == null ? 0 : c;
					boolean selected = selectedOptions.contains(option);
					if (option.show() && (anzahl > 0 || selected)) {
						Auswahl auswahl = new Auswahl(option);
						auswahl.setTrefferAnzahl(anzahl);
						auswahl.setPreset(selected);
						auswahlen.add(auswahl);
					}
				}
				Collections.sort(auswahlen, AUSWAHL_COMPARATOR);

				facette.setAuswahl(auswahlen);
			}
		}
	}
	
	/**
	 * Holder für Facette mit Optionen.
	 */
	private static class FacetteOptions {

		/**
		 * Facette.
		 */
		private StudienangebotFacette facette;

		/**
		 * Optionen.
		 */
		private Set<FacettenOption> options;

		/**
		 * Konstruktor.
		 * 
		 * @param facette
		 *            Facette
		 * @param options
		 *            Optionen
		 */
		public FacetteOptions(StudienangebotFacette facette, Set<FacettenOption> options) {
			super();
			this.facette = facette;
			this.options = new HashSet<FacettenOption>(options);
		}

		/**
		 * Liefert die Facette.
		 * 
		 * @return StudienangebotFacette
		 */
		public StudienangebotFacette getFacette() {
			return facette;
		}

		/**
		 * Liefert die Optionen.
		 * 
		 * @return Set
		 */
		public Set<FacettenOption> getOptions() {
			return new HashSet<FacettenOption>(options);
		}
	}
}
