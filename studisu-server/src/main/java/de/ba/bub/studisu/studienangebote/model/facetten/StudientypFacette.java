package de.ba.bub.studisu.studienangebote.model.facetten;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;

/**
 * Facette fuer Studientyp in Studienangebot Suche.
 *
 * Kann aus Request Parameter erzeugt werden und dann als Response zurueckgegeben werden mit Info zu Trefferanzahl per
 * Facetten-Auspraegung.
 *
 * @author olschewsp
 */
public class StudientypFacette extends StudienangebotFacette {

	@JsonIgnore
	private static final String ID = "st";

	@JsonIgnore
	private static final List<StudientypFacettenOption> ALL_FACETTS = Collections
			.unmodifiableList(Arrays.asList(StudientypFacettenOption.ALL_OPTIONS));

	/**
	 * Requested filter options coming from client
	 */
	@JsonIgnore
	private final Set<StudientypFacettenOption> selectedOptions = new HashSet<>();

	/**
	 * Default C-tor ohne Auswahl einer Option.
	 */
	public StudientypFacette() {
		super(ID, Typ.MULTI);
	}

	/**
	 * C-tor mit Übergabe des Requests zur Auswahl einer Option.
	 *
	 * @param requestString
	 *            Die Anfrageparameter des Requests aus dem API-Aufruf.
	 */
	public StudientypFacette(String requestString) {
		this();
		if (StringUtils.isEmpty(requestString)) {
			throw new EingabeValidierungException("bad request string");
		}
		final String[] studientypen = requestString.split(StudientypFacette.VALUE_SEPARATOR);
		for (final String st : studientypen) {
			StudientypFacettenOption opt = null;
			try {
				opt = StudientypFacettenOption.forId(Integer.valueOf(st));
			} catch (final NumberFormatException e) {
				throw new EingabeValidierungException("invalid facet option (not a number)");
			}
			if (opt == null) {
				throw new EingabeValidierungException("invalid facet option");
			}
			this.selectedOptions.add(opt);
		}
	}

	/**
	 * C-tor with options (for unit tests)
	 * 
	 * @param options
	 */
	public StudientypFacette(List<StudientypFacettenOption> options) {
		this();
		selectedOptions.addAll(options);
	}

	/**
	 * Liefert die Facette fuer den Studientyp mit allen Auspraegungen.
	 *
	 * @return die Facette fuer den Studientyp mit allen Auspraegungen.
	 */
	public StudientypFacette withAllOptions() {
		this.selectedOptions.clear();
		this.selectedOptions.addAll(ALL_FACETTS);
		return this;
	}

	/**
	 * Get selected or requested filter options
	 *
	 * @return selected or requested filter options
	 */
	public Set<StudientypFacettenOption> getSelectedOptions() {
		return Collections.unmodifiableSet(this.selectedOptions);
	}

	@Override
	public List<StudientypFacettenOption> getAllOptions() {
		return ALL_FACETTS;
	}

	@Override
	public Set<FacettenOption> findOptions(Studienangebot studienangebot) {
		Set<FacettenOption> ret = new HashSet<FacettenOption>(1);
		ret.add(studienangebot.getStudientyp());
		return ret;
	}

}
