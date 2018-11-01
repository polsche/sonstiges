package de.ba.bub.studisu.common.model.facetten;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacettenOption;

/**
 * Darstellung einer generischen Facette im Rahmen einer Facettierten
 * Suche/Filterung.
 * <p/>
 *
 * @author Patrick Siegel (patrick.siegel2@arbeitsagentur.de)
 */
public abstract class Facette {
	public static final String FACETTE_PREFIX = "FCT";

	private final String id;
	private final Typ typ;

	protected List<Auswahl> auswahl = new ArrayList<Auswahl>();

	/**
	 * Constructor.
	 *
	 * @param id
	 *            Id der Facette, fuer eindeutige Identifikation
	 * @param typ
	 *            {@link Typ} der Facette
	 */
	protected Facette(final String id, final Typ typ) {
		this.id = id;
		this.typ = typ;
	}

	/**
	 * getter fuer auswahl
	 * 
	 * @return
	 */
	public List<Auswahl> getAuswahl() {
		return auswahl;
	}

	/**
	 * setter fuer auswahl
	 * 
	 * @param auswahl
	 */
	public void setAuswahl(List<Auswahl> auswahl) {
		this.auswahl = auswahl;
	}

	/**
	 * abstrakter getter fuer facettenoptionen, der von unterklassen
	 * implementiert werden muss
	 * 
	 * @return
	 */
	@JsonIgnore
	public abstract List<? extends FacettenOption> getAllOptions();

	/**
	 * abstrakter getter fuer selektierte facettenoptionen, der von unterklassen
	 * implementiert werden muss
	 * 
	 * @return
	 */
	@JsonIgnore
	public abstract Set<? extends FacettenOption> getSelectedOptions();

	/**
	 * getter fuer id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * getter fuer typ
	 * 
	 * @return
	 */
	public Typ getTyp() {
		return typ;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Facette facette = (Facette) o;
		return Objects.equals(getId(), facette.getId()) && getTyp() == facette.getTyp()
				&& Objects.equals(getAuswahl(), facette.getAuswahl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTyp(), getAuswahl());
	}

	/**
	 * Typ einer Facette.
	 * <p/>
	 */
	public enum Typ {
		// Facette mit Einfach-Auswahl
		SINGLE,
		// Facette mit Mehrfach-Auswahl
		MULTI,
		// Facette mit Mehrfach-Abwahl ohne TrefferAnzahl
		EXCLUDE,
		// Facette mit Eingabe
		INPUT,
		// Switch für Ja/Nein Wert
		SWITCH
	}

	/**
	 * Auswahlmoeglichkeit einer Facette.
	 * <p/>
	 */
	public static class Auswahl {

		private int trefferAnzahl = 0;
		protected boolean preset = false;

		@JsonIgnore
		protected final FacettenOption facettenOption;

		/**
		 * C-tor.
		 *
		 * @param facettenOption
		 *            Die von diesem Objekt behandelte Facetten-Option.
		 */
		public Auswahl(final FacettenOption facettenOption) {
			this.facettenOption = facettenOption;
		}

		/**
		 * getter fuer preset
		 * 
		 * @return
		 */
		public boolean isPreset() {
			return preset;
		}

		/**
		 * setter fuer preset
		 * 
		 * @param preset
		 */
		public void setPreset(final boolean preset) {
			this.preset = preset;
		}

		/**
		 * Getter fuer id der Facettenoption.
		 *
		 * Diese wird als String an den Client übergeben, obwohl sie derzeit bei
		 * allen Facetten ausser den Bundeslaendern eine Zahl ist. Der Client
		 * kann ohnehin mit Strings an dieser Stelle umgehen.
		 *
		 * Bei den Bundeslaendern wird das ISO-Kuerzel des Bundeslandes (z.B.
		 * "BY" fuer Bayern") verwendet.
		 *
		 * @return
		 */
		public String getId() {
			if (facettenOption instanceof RegionenFacettenOption) {
				// Bundesland, dann ISO-Kuerzel aus Key holen.
				return ((RegionenFacettenOption) facettenOption).getKey();
			} else {
				// andernfalls ID verwenden.
				return Integer.toString(facettenOption.getId());
			}
		}

		/**
		 * getter fuer label der facettenoption
		 * 
		 * @return
		 */
		public String getLabel() {
			return facettenOption.getLabel();
		}

		/**
		 * getter fuer facettenoption
		 * 
		 * @return
		 */
		public FacettenOption getFacettenOption() {
			return facettenOption;
		}

		/**
		 * getter fuer trefferanzahl
		 * 
		 * @return
		 */
		public Integer getTrefferAnzahl() {
			return trefferAnzahl;
		}

		/**
		 * setter fuer trefferanzahl mit logik/robustheit: bei Eingabe von null
		 * wird 0 gesetzt
		 * 
		 * Ungluecklich hierbei, dass wir Integer annehmen aber intern int
		 * speichern TODO: sichten und evtl. auf einen typ angleichen.
		 * 
		 * @param trefferAnzahl
		 */
		public void setTrefferAnzahl(final Integer trefferAnzahl) {
			if (trefferAnzahl != null) {
				this.trefferAnzahl = trefferAnzahl;
			} else {
				this.trefferAnzahl = 0;
			}
		}

		@Override
		@SuppressWarnings("all")
		public final boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || !(o instanceof Auswahl)) {
				return false;
			}
			final Auswahl otherAuswahl = (Auswahl) o;
			return Objects.equals(getTrefferAnzahl(), otherAuswahl.getTrefferAnzahl())
					&& Objects.equals(getFacettenOption(), otherAuswahl.getFacettenOption())
					&& isPreset() == otherAuswahl.isPreset();
		}

		@Override
		public final int hashCode() {
			return Objects.hash(getTrefferAnzahl(), getFacettenOption(), isPreset());
		}

	}
}