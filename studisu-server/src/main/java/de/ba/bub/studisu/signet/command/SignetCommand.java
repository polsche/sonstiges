package de.ba.bub.studisu.signet.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.model.Signet;
import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.signet.controller.SignetController;
import de.ba.bub.studisu.signet.model.SignetAnfrage;
import de.ba.bub.studisu.signet.model.SignetErgebnis;
import de.ba.bub.studisu.signet.model.SignetValidator;

/**
 * Command zur Abfrage eines Signets.
 *
 * Achtung! Stateless Singleton.
 *
 * @author pleinesm001
 */
@Component
public class SignetCommand extends AbstractCommand<SignetAnfrage, SignetErgebnis> {

	/**
	 * Service-Interface, wird ueber DI mit Implementierung verbunden.
	 */
	private BildungsangebotService bildungsangebotServiceClient;

	/**
	 * C-tor with autowired service
	 * 
	 * @param dkzService 
	 *            --
	 */
	@Autowired
	public SignetCommand(BildungsangebotService bildungsangebotServiceClient) {
		this.bildungsangebotServiceClient = bildungsangebotServiceClient;
	}

	/**
	 * Prueft ob die Validierung korrekt ist
	 */
	@Override
	protected void pruefeVorbedingungen(
			final SignetAnfrage anfrage) {
		if (anfrage == null || SignetValidator.INVALID == anfrage.getValidationResult()) {
			throw new EingabeValidierungException(SignetController.URL_PARAM_SIGNET_BANID);
		}
	}

	@Override
	protected SignetErgebnis geschaeftslogikAusfuehren(SignetAnfrage anfrage) {

		int validationResult = anfrage.getValidationResult();

		Signet signet;

		if (SignetValidator.VALID == validationResult) {
			signet = bildungsangebotServiceClient.getSignet(anfrage.getBanId());
		} else {
			throw new IllegalStateException("Unknown validation result: " + validationResult);
		}

		SignetErgebnis ergebnis = new SignetErgebnis(signet);
		return ergebnis;
	}
}