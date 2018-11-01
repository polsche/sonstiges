package de.ba.bub.studisu.studienangebote.model.requestparams;

import org.springframework.util.StringUtils;

import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;

import java.util.ArrayList;
import java.util.List;

/**
 * List of studienfelder dkz ids Created by loutzenhj on 05.04.2017.
 */
public class Studienfelder {
	List<Integer> studienfelderIds = new ArrayList<Integer>();

	/**
	 * C-tor with semikolon separated list of dkzids
	 *
	 * @param studienfelderCsv
	 */
	public Studienfelder(String studienfelderCsv) {
		if (!StringUtils.isEmpty(studienfelderCsv)) {
			final String[] studienfelder = studienfelderCsv.split(StudienangebotFacette.VALUE_SEPARATOR);
			for (final String sf : studienfelder) {
				this.studienfelderIds.add(Integer.parseInt(sf));
			}
		}
	}

	/**
	 * Liefert die DKZ-IDs der gefundenen Studienfelder.
	 *
	 * @return Die Liste DKZ-IDs der gefundenen Studienfelder.
	 */
	public List<Integer> getStudienfelderIds() {
		return this.studienfelderIds;
	}
}
