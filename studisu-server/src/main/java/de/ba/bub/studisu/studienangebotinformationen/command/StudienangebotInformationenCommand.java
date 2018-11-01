package de.ba.bub.studisu.studienangebotinformationen.command;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.service.StudienangebotsucheService;
import de.ba.bub.studisu.studienangebotinformationen.model.StudienangebotInformationenAnfrage;

/**
 * Command fuer die StudienangebotInformationen
 * @author FuchsD013
 *
 */
@Component
public class StudienangebotInformationenCommand extends
		AbstractCommand<StudienangebotInformationenAnfrage, StudienangebotInformationen> {

	private final Validator validator;
	private StudienangebotsucheService studienangebotsucheService;
	private BildungsangebotService bildungsangebotServiceClient;

	/**
	 * Ctor
	 * @param bildungsangebotServiceClient
	 * @param validator
	 */
	@Autowired
	public StudienangebotInformationenCommand(BildungsangebotService bildungsangebotServiceClient, 
			final Validator validator, 
			@Qualifier("studienangebote") StudienangebotsucheService studienangebotsucheService) {
		this.validator = validator;
		this.bildungsangebotServiceClient = bildungsangebotServiceClient;
		this.studienangebotsucheService = studienangebotsucheService;
	}

	/**
	 * Prueft ob die Validierung korrekt ist
	 */
	@Override
	protected void pruefeVorbedingungen(
			final StudienangebotInformationenAnfrage anfrage) {
		final Set<ConstraintViolation<StudienangebotInformationenAnfrage>> violations = validator
				.validate(anfrage);
		if (!violations.isEmpty()) {
			throw new ValidationException("Sucheingaben fehlerhaft",
					violations);
		}
	}

	/**
	 * Holt die Informationen fuer ein einzelnes Studienangebot.
	 */
	@Override
	protected StudienangebotInformationen geschaeftslogikAusfuehren(
			StudienangebotInformationenAnfrage anfrage) {
		return bildungsangebotServiceClient.holeStudienangebotInformationen(anfrage.getStudienangebotID());
	}
	
	/**
	 * Ermittelt eine Liste mit Studienangeboten; benoetigt fuer Vor/Zurueck-Navigation auf der Detailseite.
	 * 
	 * @param angebotsucheAnfrage
	 * 			Die zu verwendende Suchanfrage
	 * @return Liste mit Studienangeboten.
	 */
	public List<StudienangebotWrapperMitAbstand> sucheStudienangebote(StudienangebotsucheAnfrage angebotsucheAnfrage) {
		return this.studienangebotsucheService.suche(angebotsucheAnfrage).getItems();
	}

}
