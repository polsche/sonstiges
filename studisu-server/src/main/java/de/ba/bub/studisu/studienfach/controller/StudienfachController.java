package de.ba.bub.studisu.studienfach.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienfach.command.StudienfachSucheCommand;
import de.ba.bub.studisu.studienfach.model.StudienfachAnfrage;
import de.ba.bub.studisu.studienfach.model.StudienfachErgebnis;

/**
 * Service endpoint for Studienfachsuche.
 *
 * @author schneidek084 on 2017-06-01.
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.STUDIENFAECHER)
public class StudienfachController extends StudisuController {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StudienfachController.class);

	/**
	 * Command.
	 */
	private final StudienfachSucheCommand studienfachCommand;

	/**
	 * Constructor with command.
	 *
	 * @param studienfachCommand
	 *            Singleton(!) command
	 */
	@Autowired
	public StudienfachController(StudienfachSucheCommand studienfachCommand) {
		this.studienfachCommand = studienfachCommand;
		LOGGER.debug("controller constructed");
	}

	/**
	 * Endpoint fuer GET auf Studienfachsuche. Man muss entweder suchstring oder
	 * studienfaecher liefern, aber nicht beides
	 * <p/>
	 *
	 * @param suchstring
	 *            Erforderlicher Parameter für den Eingabestring in den
	 *            Suchschlitz für das Studienfach.
	 * @param studienfaecher
	 *            Studienfach-DKZ-IDs
	 * @param response
	 *            Web Response Representation
	 * @return Eine Liste mit gefundenen passenden Studienfaechern.
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Studienfach>> sucheStudienfach(
			@RequestParam(value = URL_PARAM_STUDIENFACH_SUCHWORT, required = false) String suchstring,
			@RequestParam(value = URL_PARAM_STUDIENFACH_IDS, required = false) Studienfaecher studienfaecher,
			HttpServletResponse response) {

		final StudienfachAnfrage studienfachAnfrage = new StudienfachAnfrage(suchstring, studienfaecher);
		// this will throw an EingabeValidierungException if input invalid
		final StudienfachErgebnis studienfachErgebnis = studienfachCommand.execute(studienfachAnfrage);
		LOGGER.debug("sucheStudienfach called with search string " + suchstring);
		List<Studienfach> studienfachResultList = new ArrayList<Studienfach>(studienfachErgebnis.getStudienfaecher());

		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

		return ResponseEntity.ok(studienfachResultList);
	}

}
