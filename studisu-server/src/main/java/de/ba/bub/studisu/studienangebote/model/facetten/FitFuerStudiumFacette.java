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
 * Created by loutzenhj on 18.09.2017.
 */
public class FitFuerStudiumFacette extends StudienangebotFacette {

	@JsonIgnore
	private static final List<FitFuerStudiumFacettenOption> ALL_OPTIONS = Collections
			.unmodifiableList(Arrays.asList(FitFuerStudiumFacettenOption.ALL_OPTIONS));

	/**
	 * Requested filter options coming from client
	 */
	@JsonIgnore
	private final Set<FitFuerStudiumFacettenOption> selectedOptions = new HashSet<>();

	private static final String ID = "ffst";

	/**
	 * Default C-tor ohne Auswahl einer Option.
	 */
	public FitFuerStudiumFacette() {
		super(ID, Typ.MULTI);
	}

	/**
	 * C-tor mit Übergabe des Requests zur Auswahl einer Option.
	 * 
	 * @param requestString
	 *            Die Anfrageparameter des Requests aus dem API-Aufruf.
	 */
	public FitFuerStudiumFacette(String requestString) {
		this();
		if (StringUtils.isEmpty(requestString)) {
			throw new EingabeValidierungException("bad request string");
		}
		final String[] ffss = requestString.split(StudienangebotFacette.VALUE_SEPARATOR);
		for (final String ffs : ffss) {
			FitFuerStudiumFacettenOption opt = null;
			try {
				opt = FitFuerStudiumFacettenOption.forId(Integer.valueOf(ffs));
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
	 * C-tor for Junit
	 * 
	 * @param options
	 */
	public FitFuerStudiumFacette(List<FitFuerStudiumFacettenOption> options) {
		this();
		selectedOptions.addAll(options);
	}

	@Override
	public Set<FacettenOption> findOptions(Studienangebot studienangebot) {
		return studienangebot.getFitFuerStudiumFacettenOptions();
	}

	@Override
	public StudienangebotFacette withAllOptions() {
		this.selectedOptions.clear();
		this.selectedOptions.addAll(ALL_OPTIONS);
		return this;
	}

	@Override
	public List<FitFuerStudiumFacettenOption> getAllOptions() {
		return ALL_OPTIONS;
	}

	@Override
	public Set<FitFuerStudiumFacettenOption> getSelectedOptions() {
		return Collections.unmodifiableSet(this.selectedOptions);
	}
}
