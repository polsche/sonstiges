package de.ba.bub.studisu.studienangebote.model.requestparams;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;

/**
 * List of anfrage orte Created by loutzenhj on 05.04.2017.
 */
public class AnfrageOrte {

	List<AnfrageOrt> orte = new ArrayList<>();

	/**
	 * Construct a list of anfrageorte out of a request string like
	 * "Berlin_58.1234_10.3333,Nuernberg_58.1234_10.3333,Koeln_58.1234_10.3333"
	 *
	 * @param requestString
	 */
	public AnfrageOrte(String requestString) {
		if (StringUtils.isEmpty(requestString)) {
			throw new EingabeValidierungException("ortstring empty");
		}

		final String[] orteAusAnfrage = requestString.split(StudienangebotFacette.VALUE_SEPARATOR);
		for (final String ort : orteAusAnfrage) {
			this.orte.add(new AnfrageOrt(ort));
		}
	}

	/**
	 * getter fuer liste von anfrageorten
	 * @return
	 */
	public List<AnfrageOrt> getOrte() {
		return this.orte;
	}

}
