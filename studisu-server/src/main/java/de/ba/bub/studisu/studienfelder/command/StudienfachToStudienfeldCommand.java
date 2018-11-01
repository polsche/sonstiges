package de.ba.bub.studisu.studienfelder.command;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfelder.service.StudienfeldSucheService;

/**
 * Command zum Ermitteln von Studienfeldinformationen zu Studienfaechern.
 *
 * @author DauminN
 */
@Component
public class StudienfachToStudienfeldCommand extends AbstractCommand<Integer, Set<Integer>> {

    
    /**
	 * Service zum Ermitteln von Studienfeldinformationen
	 */
    private final StudienfeldSucheService studienfeldSucheService;


    /**
     * Validator für die Anfrage
     */
    private final Validator validator;
    
    /**
     * Exception-Message fuer Validierungsfehler bei Ueberpruefung des Aufrufparameters Dkz-Id
     */
    static final String VALIDATIONMESSAGE_DKZID_INVALID = "DKZ-Id fehlerhaft";



    /**
     * C-tor.
     *
     * @param studienfeldSucheService
     * 			Der injizierte Service zum Ermitteln von Studienfeldinformationen zu Studienfaechern
     * @param validator
     * 			Der zu verwendende Validator
     */
    @Autowired
    public StudienfachToStudienfeldCommand(@Qualifier("studienfelder") StudienfeldSucheService studienfeldSucheService,
    		final Validator validator) {
    	this.studienfeldSucheService = studienfeldSucheService;
        this.validator = validator;
    }
  
    /**
	 * fuehrt die Geschaeftslogik des Commands aus. Gibt die Dkz-Ids der Studienfeldelemente grundstaendig und weiterfuerend zum 
	 * Studienfach mit der uebergebenen Dkz-Id zurueck.
	 * @param studienFachDKZId Dkz-Id des Studienfachs
	 * @return DKZ-IDs Studienfeld-Elemente zum Studienfach als Set<Integer>
	 */
	@Override
    protected Set<Integer> geschaeftslogikAusfuehren(Integer studienfachDKZId) {
    	Set<Integer> studienfelderZuStudienfach = studienfeldSucheService.getStudienfachToStudienfeldMap().get(studienfachDKZId);
    	return studienfelderZuStudienfach;
	}

	/**
	 * prueft die Vorbedingungen fuer die Verarbeitung. Validierung des uebergebenen Parameters.
	 */
	@Override
	protected void pruefeVorbedingungen(Integer studienfachDKZId) {
		final Set<ConstraintViolation<Integer>> violations = validator.validate(studienfachDKZId);
        if (!violations.isEmpty()) {
            throw new ValidationException(VALIDATIONMESSAGE_DKZID_INVALID, violations);
        }
	}

   

}



