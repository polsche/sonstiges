package de.ba.bub.studisu.versionsinformationen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.versionsinformationen.command.VersionsInformationenCommand;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;

/**
 * Web REST Endpoint für VersionsInformationen.
 *
 * @author OettlJ
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.VERSIONSINFOS)
public class VersionsInformationenController extends StudisuController {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(VersionsInformationenController.class);

	/**
	 * VersionsInformationenCommand
	 */
	private final VersionsInformationenCommand versionsInformationenCommand;

	/**
	 * Ctor
	 *
	 * @param command
	 */
	@Autowired
	public VersionsInformationenController(VersionsInformationenCommand command) {
		this.versionsInformationenCommand = command;
	}

	/**
	 * Rest-Endpoint. Liefert Informationen zu Datenstand und Version des Backends.
	 *
	 * @return {@link ResponseEntity}
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<VersionsInformationen> getVersionsInfo() {
		final VersionsInformationenAnfrage versionsInformationenAnfrage = new VersionsInformationenAnfrage();
		final VersionsInformationen version = this.versionsInformationenCommand.execute(versionsInformationenAnfrage);

		if (LOGGER.isDebugEnabled()) {
			final StringBuilder sb = new StringBuilder("getVersionsInfo called");
			LOGGER.debug(sb.toString());
		}

		return ResponseEntity.ok(version);
	}

}
