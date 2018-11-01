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
 * Backend für die Facette zu den Regionen (Bundesländer und Nachbarstaaten)
 * bei der Suche nach Studienangeboten.
 *
 */
public class RegionenFacette extends StudienangebotFacette {

	/**
	 * ID der Facette für die Abfrage über die Frontend-API.
	 */
	@JsonIgnore
	private static final String ID = "re";

	/**
	 * C-tor ohne Auswahl einer Option.
	 */
	public RegionenFacette() {
		super(ID, Typ.MULTI);
	}

	/**
	 * C-tor for Junit
	 * 
	 * @param options
	 *            Vorausgewählte aktivierte Optionen (für JUnit).
	 */
	public RegionenFacette(List<RegionenFacettenOption> options) {
		this();
		selectedOptions.addAll(options);
	}

	/**
	 * C-tor mit Auswahl einer Option.
	 *
	 * @param requestString
	 *            Die Anfrageparameter des Requests aus dem API-Aufruf.
	 */
	public RegionenFacette(String requestString) {
		this();
		if (StringUtils.isEmpty(requestString)) {
			throw new EingabeValidierungException("bad request string");
		}
		final String[] bundeslaender = requestString.split(StudienangebotFacette.VALUE_SEPARATOR);
		for (final String bundl : bundeslaender) {
			RegionenFacettenOption opt = null;
			try {
				opt = RegionenFacettenOption.forRegionISOName(bundl);
			} catch (final NumberFormatException e) {
				throw new EingabeValidierungException("invalid facet option (no value for searched bundesland)");
			}
			if (opt == null) {
				throw new EingabeValidierungException("invalid facet option");
			}
			this.selectedOptions.add(opt);
		}
	}

	/**
	 * Requested filter options coming from client
	 */
	@JsonIgnore
	private final Set<RegionenFacettenOption> selectedOptions = new HashSet<>();

	@JsonIgnore
	private static final List<RegionenFacettenOption> ALL_FACETTS = Collections
			.unmodifiableList(Arrays.asList(RegionenFacettenOption.ALL_OPTIONS));

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<FacettenOption> findOptions(Studienangebot studienangebot) {
		Set<FacettenOption> ret = new HashSet<>(1);
		ret.add(studienangebot.getRegion());
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RegionenFacettenOption> getAllOptions() {
		return ALL_FACETTS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<RegionenFacettenOption> getSelectedOptions() {
		return Collections.unmodifiableSet(this.selectedOptions);
	}

	/**
	 * Auswahl aller vorhandenen Optionen für diese Facette.
	 *
	 * @return Die {@link RegionenFacette}, in der alle Optionen ausgewählt
	 *         sind.
	 */
	public RegionenFacette withAllOptions() {
		this.selectedOptions.clear();
		this.selectedOptions.addAll(ALL_FACETTS);
		return this;
	}

}
