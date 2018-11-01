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
 * Backend für die Facette zu den Hochschularten bei der Suche nach Studienangeboten.
 *
 * @author loutzenhj on 10.04.2017.
 */
public class HochschulartFacette extends StudienangebotFacette {

	/**
	 * ID der Facette für die Abfrage über die Frontend-API.
	 */
	public static final String ID = "hsa";

	@JsonIgnore
	private static final List<HochschulartFacettenOption> ALL_FACETTS = Collections.unmodifiableList(Arrays
			.asList(HochschulartFacettenOption.ALL_OPTIONS));

	/**
	 * Requested filter options coming from client
	 */
	@JsonIgnore
	private final Set<HochschulartFacettenOption> selectedOptions = new HashSet<>();

	/**
	 * C-tor ohne Auswahl einer Option.
	 */
	public HochschulartFacette() {
		super(ID, Typ.MULTI);
	}
	
    /**
     * C-tor for Junit
     * @param options
     */
	public HochschulartFacette(List<HochschulartFacettenOption> options) {
		this();
		selectedOptions.addAll(options);
	}

	/**
	 * C-tor mit Auswahl einer Option.
	 *
	 * @param requestString
	 *            Die Anfrageparameter des Requests aus dem API-Aufruf.
	 */
	public HochschulartFacette(String requestString) {
		this();
		if (StringUtils.isEmpty(requestString)) {
			throw new EingabeValidierungException("bad request string");
		}
		final String[] hochschularten = requestString.split(StudienangebotFacette.VALUE_SEPARATOR);
		for (final String hsa : hochschularten) {
			HochschulartFacettenOption opt = null;
			try {
				opt = HochschulartFacettenOption.forId(Integer.valueOf(hsa));
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
	public HochschulartFacette withAllOptions() {
		this.selectedOptions.clear();
		this.selectedOptions.addAll(ALL_FACETTS);
		return this;
	}

	@Override
	public Set<HochschulartFacettenOption> getSelectedOptions() {
		return Collections.unmodifiableSet(this.selectedOptions);
	}

	@Override
	public List<HochschulartFacettenOption> getAllOptions() {
		return ALL_FACETTS;
	}

	@Override
	public Set<FacettenOption> findOptions(Studienangebot studienangebot) {
		Set<FacettenOption> ret = new HashSet<FacettenOption>(1);
		ret.add(studienangebot.getHochschulart());
		return ret;
	}
}
