package de.ba.bub.studisu.studienangebote.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.studienangebote.model.OrteValidator;
import de.ba.bub.studisu.studienangebote.model.StudienangeboteValidator;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.service.StudienangebotsucheService;

/**
 * Dieses Kommand führt die eigentliche Suche nach Studienangeboten mit Hilfe des {@linkStudienangebotsucheService} aus.
 */
@Component
public class StudienangebotsucheCommand
		extends AbstractCommand<StudienangebotsucheAnfrage, StudienangebotsucheErgebnis> {

	private final StudienangebotsucheService studienangebotsucheService;

	/**
	 * C-tor.
	 *
	 * @param studienangebotsucheService
	 *            Der injizierte {@link StudienangebotsucheService}, an den die eigentliche Arbeit delegiert wird.
	 */
	@Autowired
	public StudienangebotsucheCommand(
			@Qualifier("studienangebote") StudienangebotsucheService studienangebotsucheService) {
		this.studienangebotsucheService = studienangebotsucheService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void pruefeVorbedingungen(final StudienangebotsucheAnfrage studienangebotsucheAnfrage) {
		String errorMessage = null;
		if (studienangebotsucheAnfrage == null
				|| StudienangeboteValidator.INVALID == studienangebotsucheAnfrage.getAngebotValidationResult()) {
			errorMessage = StudienangeboteValidator.MISSING_PARAM_MESSAGE_S_AND_S;
		}
		if (studienangebotsucheAnfrage != null
				&& OrteValidator.INVALID == studienangebotsucheAnfrage.getOrtValidationResult()) {
			// TODO messages verbessern, ueber backlogitem abgebildet
			errorMessage = (errorMessage == null) ? OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES
					: errorMessage + " " + OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES;
		}
		if (errorMessage != null) {
			throw new EingabeValidierungException(OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES,
					new Exception(errorMessage));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StudienangebotsucheErgebnis geschaeftslogikAusfuehren(StudienangebotsucheAnfrage angebotsucheAnfrage) {
		return this.studienangebotsucheService.suche(angebotsucheAnfrage);
	}

}
