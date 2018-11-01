package de.ba.bub.studisu.studienfelder.command;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.service.StudienfeldSucheService;

/**
 * Command zur Durchführung der Studienfeldsuche.
 *
 * @author StraubP
 */
@Component
public class StudienfeldSucheCommand extends AbstractCommand<StudienfeldSucheAnfrage, StudienfeldSucheErgebnis> {

	/**
	 * Service für Studienfeldersuche.
	 */
    private final StudienfeldSucheService studienfelderService;

    /**
     * Validator für die StudienfeldSucheAnfrage.
     */
    private final Validator validator;


    /**
     * C-tor.
     *
     * @param studienfelderService
     * 			Der injizierte Service für die Studienfeldsuche.
     * @param validator
     * 			Der zu verwendende Validator.
     */
    @Autowired
    public StudienfeldSucheCommand(@Qualifier("studienfelder") StudienfeldSucheService studienfelderService, final Validator validator) {
        this.studienfelderService = studienfelderService;
        this.validator = validator;
    }

    @Override
    protected void pruefeVorbedingungen(final StudienfeldSucheAnfrage studienfeldSucheAnfrage) {
    	final Set<ConstraintViolation<StudienfeldSucheAnfrage>> violations = validator.validate(studienfeldSucheAnfrage);
        if (!violations.isEmpty()) {
            throw new ValidationException("Sucheingaben fehlerhaft", violations);
        }
    }

    @Override
    protected StudienfeldSucheErgebnis geschaeftslogikAusfuehren(StudienfeldSucheAnfrage studienfeldSucheAnfrage) {
        return studienfelderService.suche(studienfeldSucheAnfrage);
    }

}
