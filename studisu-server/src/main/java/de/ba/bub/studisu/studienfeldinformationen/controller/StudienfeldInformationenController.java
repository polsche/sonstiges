package de.ba.bub.studisu.studienfeldinformationen.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.studienfeldinformationen.command.StudienfeldInformationenCommand;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenAnfrage;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;

/**
 * REST-Endpunkt für Studienfeldinformationen.
 *
 * Die Studieninformationen werden anhand einer DKZ-ID aus dem WCC geholt.
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.STUDIENFELDINFOS)
public class StudienfeldInformationenController extends StudisuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudienfeldInformationenController.class);

	private final StudienfeldInformationenCommand studienfeldInformationenCommand;

	/**
	 * C-tor.
	 *
	 * @param studienfeldInformationenCommand
	 * 			Das vom Controller zu verwendende Command.
	 */
	@Autowired
	public StudienfeldInformationenController(StudienfeldInformationenCommand studienfeldInformationenCommand) {
		this.studienfeldInformationenCommand = studienfeldInformationenCommand;
	}

	/**
	 * Endpoint für GET. Delegiert die WCC-Anfrage an den ContentClient.
	 *
	 * @param dkzId
	 * 			  DKZ-ID des gesuchten Objektes aus dem Request.
	 * @param request
	 *            Web Request Representation
	 * @param response
	 * 			  Web Response Representation
	 * @return Eine zur DKZ-ID passende StudienfeldInformation oder einen HTML-Fehlercode
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<StudienfeldInformationenErgebnis> sucheAngebote(
			@RequestParam(value = "dkz", required = true) String dkzId, WebRequest request,
			HttpServletResponse response) {

		int dkz;
		try {
			dkz = Integer.valueOf(dkzId);
		} catch (NumberFormatException e) {
			LOGGER.info("DKZ-ID ist nicht numerisch.", e);
			throw new ValidationException("DKZ-ID hat ungültiges Format");
			//return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}

		final StudienfeldInformationenAnfrage studienfeldInformationenAnfrage = new StudienfeldInformationenAnfrage(dkz);

		StudienfeldInformationenErgebnis studienfeldInformationenErgebnis = null;
		try {
			studienfeldInformationenErgebnis = this.studienfeldInformationenCommand
					.execute(studienfeldInformationenAnfrage);
		} catch (final ValidationException e) {
			LOGGER.error("Validierung der DKZ-ID fehlgeschlagen.", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

		return ResponseEntity.ok(studienfeldInformationenErgebnis);
	}

}
