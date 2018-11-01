package de.ba.bub.studisu.studienfeldbeschreibung.controller;

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
import de.ba.bub.studisu.studienfeldbeschreibung.command.StudienfeldBeschreibungCommand;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;

/**
 * REST-Endpunkt für Studienfeldbeschreibungen.
 *
 * Die Beschreibung zu einem Studienfeld wird anhand einer DKZ-ID aus dem WCC
 * geholt.
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.STUDIENFELDBESCHREIBUNG)
public class StudienfeldBeschreibungController extends StudisuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudienfeldBeschreibungController.class);
	
	private final StudienfeldBeschreibungCommand studienfeldBeschreibungCommand;

	/**
	 * C-tor.
	 *
	 * @param studienfeldBeschreibungCommand
	 *            Das vom Controller zu verwendende Command.
	 */
	@Autowired
	public StudienfeldBeschreibungController(StudienfeldBeschreibungCommand studienfeldBeschreibungCommand) {
		this.studienfeldBeschreibungCommand = studienfeldBeschreibungCommand;
	}

	/**
	 * Endpoint für GET. Delegiert die WCC-Anfrage an den ContentClient.
	 *
	 * @param dkzId
	 *            DKZ-ID des Studienfeldes aus dem Request.
	 * @param request
	 *            Web Request Representation
	 * @param response
	 *            Web Response Representation
	 * @return Eine zur DKZ-ID passende StudienfeldBeschreibung oder ein
	 *         HTML-Fehlercode
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<StudienfeldBeschreibungErgebnis> sucheAngebote(
			@RequestParam(value = "dkz", required = true) int dkzId, 
			WebRequest request,
			HttpServletResponse response) {

		final StudienfeldBeschreibungAnfrage studienfeldBeschreibungAnfrage = new StudienfeldBeschreibungAnfrage(dkzId);

		StudienfeldBeschreibungErgebnis studienfeldBeschreibungErgebnis = null;
		try {
			studienfeldBeschreibungErgebnis = this.studienfeldBeschreibungCommand
					.execute(studienfeldBeschreibungAnfrage);
		} catch (final ValidationException e) {
			LOGGER.error("Validierung der DKZ-IDs fehlgeschlagen.", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

		return ResponseEntity.ok(studienfeldBeschreibungErgebnis);
	}

}
