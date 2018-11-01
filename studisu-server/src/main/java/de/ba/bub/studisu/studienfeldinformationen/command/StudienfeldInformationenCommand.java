package de.ba.bub.studisu.studienfeldinformationen.command;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenAnfrage;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;
import de.ba.bub.studisu.studienfeldinformationen.service.StudienfeldInformationenService;

/**
 * Command zur Abfrage von StudienfeldInformationen.
 *
 */
@Component
public class StudienfeldInformationenCommand
		extends AbstractCommand<StudienfeldInformationenAnfrage, StudienfeldInformationenErgebnis> {

	/**
	 * Service für StudienfeldInformationen.
	 */
	private final StudienfeldInformationenService studienfeldInformationenService;

    /**
	 * Validator für die StudienfeldInformationenAnfrage.
	 */
    private final Validator validator;

    /**
     * C-tor.
     *
     * @param studienfeldInformationenService
     * 			Der zu verwendenden {@link StudienfeldInformationenService}.
     * @param validator
     * 			Der zu verwendende {@link Validator}.
     */
    @Autowired
	public StudienfeldInformationenCommand(
			@Qualifier("studienfeldInformationen") StudienfeldInformationenService studienfeldInformationenService,
			final Validator validator) {
		this.studienfeldInformationenService = studienfeldInformationenService;
        this.validator = validator;
    }

    @Override
	protected void pruefeVorbedingungen(final StudienfeldInformationenAnfrage studienfeldInformationenAnfrage) {
		final Set<ConstraintViolation<StudienfeldInformationenAnfrage>> violations = validator
				.validate(studienfeldInformationenAnfrage);
        if (!violations.isEmpty()) {
            throw new ValidationException("Sucheingaben fehlerhaft", violations);
        }
    }

    @Override
	protected StudienfeldInformationenErgebnis geschaeftslogikAusfuehren(
			StudienfeldInformationenAnfrage studienfeldInformationenAnfrage) {
		return studienfeldInformationenService.suche(studienfeldInformationenAnfrage);
    }

}
