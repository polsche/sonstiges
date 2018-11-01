package de.ba.bub.studisu.versionsinformationen.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;
import de.ba.bub.studisu.versionsinformationen.service.VersionsInformationenService;

/**
 * Command fuer die Informationen zu Datenstand und Version des Backends.
 * @author OettlJ
 *
 */
@Component
public class VersionsInformationenCommand extends
		AbstractCommand<VersionsInformationenAnfrage, VersionsInformationen> {

	private VersionsInformationenService versionsinformationensucheService;

	/**
	 * Ctor
	 * @param versionsinformationensucheService
	 */
	@Autowired
	public VersionsInformationenCommand(@Qualifier("versionsinformationen") VersionsInformationenService versionsinformationensucheService) {
		this.versionsinformationensucheService = versionsinformationensucheService;
	}

	/**
	 * Prueft ob die Validierung korrekt ist
	 */
	@Override
	protected void pruefeVorbedingungen(final VersionsInformationenAnfrage anfrage) {
		// keine Validierung notwendig
	}

	/**
	 * Fuehrt die Abfrage aus
	 */
	@Override
	protected VersionsInformationen geschaeftslogikAusfuehren(VersionsInformationenAnfrage anfrage) {
		return versionsinformationensucheService.getVersionsInfo(anfrage);
	}
}
