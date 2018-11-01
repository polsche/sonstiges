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
 * Backend für die Facette zur Studienform bei der Suche nach Studienangeboten.
 *
 * Kann aus Request-Parameter erzeugt und dann als Response zurückgegeben werden mit Info zur Trefferanzahl per
 * Facetten-Option.
 *
 * @author loutzenhj on 05.04.2017.
 */
public class StudienformFacette extends StudienangebotFacette {

	/**
	 * ID der Facette für die Abfrage über die Frontend-API.
	 */
	@JsonIgnore
	private static final String ID = "sfo";

	@JsonIgnore
	private static final List<StudienformFacettenOption> ALL_FACETTS = Collections.unmodifiableList(Arrays
			.asList(StudienformFacettenOption.ALL_OPTIONS));

	/**
	 * Requested filter options coming from client
	 */
	@JsonIgnore
	private final Set<StudienformFacettenOption> selectedOptions = new HashSet<>();

	/**
	 * C-tor ohne Auswahl einer Option.
	 */
	public StudienformFacette() {
		super(ID, Typ.MULTI);
	}

	/**
	 * C-tor for junit
	 * @param options
     */
	public  StudienformFacette(List<StudienformFacettenOption> options){
		this();
		selectedOptions.addAll(options);
	}

	/**
	 * C-tor mit Auswahl einer Option.
	 *
	 * @param requestString
	 *            Die Anfrageparameter des Requests aus dem API-Aufruf.
	 */
	public StudienformFacette(String requestString) {
		this();
		if (StringUtils.isEmpty(requestString)) {
			throw new EingabeValidierungException("bad request string");
		}
		final String[] studienformen = requestString.split(StudienangebotFacette.VALUE_SEPARATOR);
		for (final String sf : studienformen) {
			StudienformFacettenOption opt = null;
			try {
				opt = StudienformFacettenOption.forId(Integer.valueOf(sf));
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
	 * Auswahl aller vorhandenen Optionen für diese Facette.
	 *
	 * @return Die {@link HochschulartFacette}, in der alle Optionen ausgewählt sind.
	 */
	public StudienformFacette withAllOptions() {
		this.selectedOptions.clear();
		this.selectedOptions.addAll(ALL_FACETTS);
		return this;
	}

	@Override
	public Set<StudienformFacettenOption> getSelectedOptions() {
		return Collections.unmodifiableSet(this.selectedOptions);
	}

	@Override
	public List<StudienformFacettenOption> getAllOptions() {
		return ALL_FACETTS;
	}
	
	@Override
	public Set<FacettenOption> findOptions(Studienangebot studienangebot) {
		Set<FacettenOption> ret = new HashSet<FacettenOption>(1);
		ret.add(studienangebot.getStudienform());
		return ret;
	}
}
