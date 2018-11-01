package de.ba.bub.studisu.studienfeldbeschreibung.command;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;
import de.ba.bub.studisu.studienfeldbeschreibung.service.StudienfeldBeschreibungService;

/**
 * Command zur Abfrage einer Studienfeldbeschreibung.
 */
@Component
public class StudienfeldBeschreibungCommand
		extends AbstractCommand<StudienfeldBeschreibungAnfrage, StudienfeldBeschreibungErgebnis> {

	/**
	 * Service für die StudienfeldBeschreibung.
	 */
	private final StudienfeldBeschreibungService studienfeldBeschreibungService;

	/**
	 * Validator für die StudienfeldBeschreibungAnfrage.
	 */
	private final Validator validator;

	/**
	 * C-tor für das Command.
	 *
	 * @param studienfeldBeschreibungService
	 *            Der zu verwendenden {@link StudienfeldBeschreibungService}.
	 * @param validator
	 *            Der zu verwendende {@link Validator}.
	 */
	@Autowired
	public StudienfeldBeschreibungCommand(
			@Qualifier("studienfeldBeschreibung") StudienfeldBeschreibungService studienfeldBeschreibungService,
			final Validator validator) {
		this.studienfeldBeschreibungService = studienfeldBeschreibungService;
		this.validator = validator;
	}

	@Override
	protected void pruefeVorbedingungen(final StudienfeldBeschreibungAnfrage studienfeldBeschreibungAnfrage) {
		final Set<ConstraintViolation<StudienfeldBeschreibungAnfrage>> violations = validator
				.validate(studienfeldBeschreibungAnfrage);
		if (!violations.isEmpty()) {
			throw new ValidationException("Sucheingaben fehlerhaft", violations);
		}
	}

	@Override
	protected StudienfeldBeschreibungErgebnis geschaeftslogikAusfuehren(
			StudienfeldBeschreibungAnfrage studienfeldBeschreibungAnfrage) {
		return studienfeldBeschreibungService.suche(studienfeldBeschreibungAnfrage);
	}

}
