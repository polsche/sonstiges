package de.ba.bub.studisu.studienangebote.model.requestparams;

import org.springframework.util.StringUtils;

import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;

import java.util.ArrayList;
import java.util.List;

/**
 * List of studienfaecher dkz ids
 *
 * @author schneidek084, 2017-06-01
 */
public class Studienfaecher {
	List<Integer> studienfaecherIds = new ArrayList<Integer>();

	/**
	 * C-tor with semikolon separated list of dkzids
	 *
	 * @param studienfaecherCsv
	 */
	public Studienfaecher(String studienfaecherCsv) {
		if (!StringUtils.isEmpty(studienfaecherCsv)) {
			for (final String sf : studienfaecherCsv.split(StudienangebotFacette.VALUE_SEPARATOR)) {
				this.studienfaecherIds.add(Integer.parseInt(sf));
			}
		}
	}

	/**
	 * Liefert die DKZ-IDs der gefundenen Studienfaecher.
	 *
	 * @return Die Liste DKZ-IDs der gefundenen Studienfaecher.
	 */
	public List<Integer> getStudienfaecherIds() {
		return this.studienfaecherIds;
	}
}
