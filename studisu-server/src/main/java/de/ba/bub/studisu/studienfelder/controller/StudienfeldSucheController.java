package de.ba.bub.studisu.studienfelder.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.studienfelder.command.StudienfachToStudienfeldCommand;
import de.ba.bub.studisu.studienfelder.command.StudienfeldSucheCommand;

import java.util.Set;

import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;


/**
 * Web Rest Endpoint für die Studienfeldsuche.
 *
 * @author StraubP
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.STUDIENFELDER)
public class StudienfeldSucheController extends StudisuController {
	/**
	 * Command zur Durchführung der Studienfeldsuche.
	 */
	private final StudienfeldSucheCommand studienfeldSucheCommand;
	
	/**
	 * Command zur Ermittlung von DKZ-Studienfeldinformationen zu Studienfaechern
	 */
	private final StudienfachToStudienfeldCommand studienfachToStudienfeldCommand;

	/**
	 * Konstruktor.
	 *
	 * @param studienfeldSucheCommand
	 *            Command zur Durchführung der Studienfeldsuche
	 * @param studienfachToStudienfeldCommand 
	 *            Command zur Ermittlung einer Map mit allen Studienfächern, die von DKZ geliefert wurden, 
	 *            und den zugehoerigen DKZ-Studienfeldern
	 */
	@Autowired
	public StudienfeldSucheController(StudienfeldSucheCommand studienfeldSucheCommand, 
			StudienfachToStudienfeldCommand studienfachToStudienfeldCommand) {
		this.studienfeldSucheCommand = studienfeldSucheCommand;
		this.studienfachToStudienfeldCommand = studienfachToStudienfeldCommand;
	}

	/**
	 * GET liefert die Liste der Studienfelder, gruppiert nach Studienbereichen.
	 *
	 * @param response Web Response Representation
	 * @return Liste der Studienfelder, gruppiert nach Studienbereichen
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<StudienfeldSucheErgebnis> sucheStudienfelder(HttpServletResponse response) {
		final StudienfeldSucheAnfrage studienfeldSucheAnfrage = new StudienfeldSucheAnfrage();
		final StudienfeldSucheErgebnis studienfeldSucheErgebnis = studienfeldSucheCommand
				.execute(studienfeldSucheAnfrage);

		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

		return ResponseEntity.ok(studienfeldSucheErgebnis);
	}


	/**
	 * GET liefert die Dkz-Ids der Dkz-Studienfelder grundstaendig und weiterfuehrend zum Studienfach mit der 
	 * uebergebenen Dkz-Id, gruppiert nach Studienbereichen.
	 *
	 * @param studienfachDKZId Dkz-Id des Studienfachs, fuer das die zugehoerigen DKZ-Studienfelder geliefert werden sollen 
	 * @param response Web Response Representation
	 * @return Liste der Dkz-Ids der Dkz-Studienfelder grundstaendig und weiterfuehrend zum Studienfach, 
	 * gruppiert nach Studienbereichen
	 */
	@RequestMapping(path = WebConfig.Path.Constants.STUDIENFELDER_STUDIENFACH, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Set<Integer>> getStudienfeldByStudienfach(
			@RequestParam(value = URL_PARAM_STUDIENFACH, required = true) int studienfachDKZId,
			HttpServletResponse response) {
		
		final Set<Integer> dkzStudienfelderZuStudienfach = studienfachToStudienfeldCommand.execute(studienfachDKZId);
		
		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

		return ResponseEntity.ok(dkzStudienfelderZuStudienfach);
	}
}
