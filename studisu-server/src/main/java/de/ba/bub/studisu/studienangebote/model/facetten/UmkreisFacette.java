package de.ba.bub.studisu.studienangebote.model.facetten;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;

/**
 * Backend für die Facette zur Umkreissuche bei der Suche nach Studienangeboten.
 *
 * @author loutzenhj on 07.04.2017.
 */
public class UmkreisFacette extends StudienangebotFacette {

	/**
	 * ID der Facette für die Abfrage über die Frontend-API.
	 */
	public static final String ID = "uk";

	/**
	 * UmkreisFacetteOption für Einträge, die 50km oder weniger vom Suchort entfernt sind.
	 */
	public static final UmkreisFacetteOption FUENFZIG = new UmkreisFacetteOption("50", 0);

	/**
	 * UmkreisFacetteOption für Einträge, die 100km oder weniger vom Suchort entfernt sind.
	 */
	public static final UmkreisFacetteOption HUNDERT = new UmkreisFacetteOption("100", 1);

	/**
	 * UmkreisFacetteOption für Einträge, die 200km oder weniger vom Suchort entfernt sind.
	 */
	public static final UmkreisFacetteOption ZWEIHUNDERT = new UmkreisFacetteOption("200", 2);

	/**
	 * UmkreisFacetteOption für beliebige Einträge (bundesweite Suche).
	 */
	public static final UmkreisFacetteOption BUNDESWEIT = new UmkreisFacetteOption("Bundesweit", 3);

	/**
	 * Liste aller verfügbaren UmkreisFacetteOptionen.
	 */
	static final UmkreisFacetteOption[] ALL_OPTIONS = { FUENFZIG, HUNDERT, ZWEIHUNDERT, BUNDESWEIT };

	/**
	 * Abfrageort für die "Mitte von Deutschland" für die bundesweite Suche.
	 */
	public static final AnfrageOrt DE_MITTE = new AnfrageOrt("Langula_10.4194_51.1508");

	/**
	 * Default-Umkreis ist 50km.
	 */
	private UmkreisFacetteOption selectedOption = FUENFZIG;

	/**
	 * C-tor mit Auswahl einer Option.
	 *
	 * @param umkreis
	 *            Der Name der UmkreisFacetteOption (zugleich Entfernung, oder "Bundesweit").
	 */
	public UmkreisFacette(String umkreis) {
		super(ID, Typ.INPUT);
		if (!StringUtils.isEmpty(umkreis)) {
			this.selectedOption = forName(umkreis);
		}
	}

	/**
	 * C-tor ohne Auswahl einer Option.
	 */
	public UmkreisFacette() {
		super(ID, Typ.INPUT);
	}

	/**
	 * Accessor für die UmkreisFacetteOption auf Basis des angegebenen Optionsnamens.
	 *
	 * @param name
	 *            Der Name der gesuchten Option ("50", "100", "200" oder "Bundesweit").
	 * @return Die zum Namen passende UmkreisFacetteOption.
	 */
	public static UmkreisFacetteOption forName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		for (final UmkreisFacetteOption opt : ALL_OPTIONS) {
			if (name.equals(opt.getName())) {
				return opt;
			}
		}
		return new UmkreisFacetteOption(name, -1);
	}

	/**
	 * Liefert die aktuell selektierte Option.
	 *
	 * @return Die selektierte Option.
	 */
	public UmkreisFacetteOption getSelectedOption() {
				return this.selectedOption;
	}

	@Override
	public List<? extends FacettenOption> getAllOptions() {
		return Collections.unmodifiableList(Arrays.asList(ALL_OPTIONS));
	}
	
	@Override
	public Set<? extends FacettenOption> getSelectedOptions() {
		Set<FacettenOption> ret = new HashSet<>(1);
		if (selectedOption != null) {
			ret.add(selectedOption);
		}
		return ret;
	}
	
	@Override
	public Set<FacettenOption> findOptions(Studienangebot studienangebot) {
		Set<FacettenOption> ret = new HashSet<>(1);
		ret.add(selectedOption);
		return ret;
	}

	/**
	 * empty impl
	 * @return
     */
	@Override
	public StudienangebotFacette withAllOptions() {
		return null;
	}

}
