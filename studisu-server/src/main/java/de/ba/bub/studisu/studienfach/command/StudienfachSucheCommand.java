package de.ba.bub.studisu.studienfach.command;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.exception.EingabeZuVieleZeichenException;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.studienfach.controller.StudienfachController;
import de.ba.bub.studisu.studienfach.model.StudienfachAnfrage;
import de.ba.bub.studisu.studienfach.model.StudienfachErgebnis;
import de.ba.bub.studisu.studienfach.model.StudienfachSucheValidator;
import de.ba.bub.studisu.studienfach.model.StudienfachSuchwortComparator;

/**
 * Command zur Suche nach einem Studienfach.
 *
 * Achtung! Stateless Singleton.
 *
 * @author schneidek084 on 2017-06-01.
 */
@Component
public class StudienfachSucheCommand extends AbstractCommand<StudienfachAnfrage, StudienfachErgebnis> {

	/**
	 * Service-Interface, wird ueber DI mit Implementierung verbunden.
	 */
	private final DKZService dkzService;

	/**
	 * C-tor with autowired service
	 * 
	 * @param dkzService 
	 *            --
	 */
	@Autowired
	public StudienfachSucheCommand(@Qualifier("dkz") DKZService dkzService) {
		this.dkzService = dkzService;
	}

	@Override
	protected void pruefeVorbedingungen(StudienfachAnfrage anfrage) {
		if (anfrage == null || StudienfachSucheValidator.INVALID == anfrage.getValidationResult()) {
			throw new EingabeValidierungException(StudienfachController.URL_PARAM_STUDIENFACH_SUCHWORT + "/"
					+ StudienfachController.URL_PARAM_STUDIENFACH_IDS);
		}
		else if (StudienfachSucheValidator.INVALID_SUCHBEGRIFF_ZU_LANG == anfrage.getValidationResult()) {
			throw new EingabeZuVieleZeichenException(StudienfachController.URL_PARAM_STUDIENFACH_SUCHWORT_ZU_LANG);
		}
	}

	@Override
	protected StudienfachErgebnis geschaeftslogikAusfuehren(StudienfachAnfrage anfrage) {

		int validationResult = anfrage.getValidationResult();

		List<Studienfach> studienfaecher;

		if (StudienfachSucheValidator.VALID_SUCHBEGRIFF == validationResult) {
			studienfaecher = dkzService.findStudienfachBySuchwort(anfrage.getSuchString() + "*");
			// sortiere gefundene Einträge nach Gewichtung
			Collections.sort(studienfaecher, new StudienfachSuchwortComparator(anfrage.getSuchString()));
		} else if (StudienfachSucheValidator.VALID_IDS == validationResult) {
			studienfaecher = dkzService.findStudienfachById(anfrage.getStudienfaecher().getStudienfaecherIds());
		} else {
			throw new IllegalStateException("Unknown validation result: " + validationResult);
		}

		StudienfachErgebnis ergebnis = new StudienfachErgebnis(studienfaecher);
		return ergebnis;
	}
}