package de.ba.bub.studisu.studienangebote.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.studienangebote.command.StudienangebotsucheCommand;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;

/**
 * Web REST endpoint for studienangebote.
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.STUDIENANGEBOTE)
public class StudienangebotsucheController extends StudisuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudienangebotsucheController.class);

	private final StudienangebotsucheCommand angebotsucheCommand;

	// for future use with paging - configurable through properties by spring magic
	// dont delete code as long we dont have another usecase of the property loading in use
	@SuppressWarnings("unused")
	private final Integer anzahlSuchErgebnisse;

	/**
	 * C-tor
	 *
	 * @param angebotsucheCommand
	 *            Das injizierte Kommando fuer die Angebotssuche.
	 * @param anzahlSuchErgebnisse
	 *            Die aus den Request-Parametern stammende Anzahl der Suchergebnisse.
	 */
	@Autowired
	public StudienangebotsucheController(
			StudienangebotsucheCommand angebotsucheCommand,
			@Value("${studienangebote.ergebnisse.max:12}") Integer anzahlSuchErgebnisse) {
		this.angebotsucheCommand = angebotsucheCommand;
		this.anzahlSuchErgebnisse = anzahlSuchErgebnisse;
		LOGGER.debug("controller constructed");
	}

	/**
	 * Endpoint for Studienangebot suche
	 *
	 * @param studienfelder
	 *            Binds to comma separated List of Studienfelder strings.
	 * @param studienfaecher
	 *            Binds to comma separated List of Studienfächer strings.
	 * @param orte
	 *            Binds to comma separated list of orte with name and coordinates
	 * @param studienformFacette
	 *            binds to studienform filter facet values
	 * @param hochschulartFacette
	 *            binds to hochschulart filter facet values
	 * @param umkreisFacette
	 *            binds to umkreis filter facet values
	 * @param studientypFacette
	 *            binds to studientyp filter facet values
	 * @param fitFuerStudiumFacette
	 * 			  binds to osa/studicheck filter facet values
	 * @param regionenFacette
	 * 			  binds to the facet values for the selection of the bundesland
	 * @param page
	 * 			  binds to page param
	 * @param reload
	 * 			  binds to reload flag
	 * @param request
	 *            Web Request Representation
	 * @param response
	 *            Web Response Representation
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<StudienangebotsucheErgebnis> sucheAngebote(
			@RequestParam(value = URL_PARAM_STUDIENFELDER, required=false) Studienfelder studienfelder,
			@RequestParam(value = URL_PARAM_STUDIENFAECHER, required=false) Studienfaecher studienfaecher,
			@RequestParam(value = URL_PARAM_ORT, required = false) AnfrageOrte orte,
			@RequestParam(value = URL_PARAM_STUDIENFORM, required = false) StudienformFacette studienformFacette,
			@RequestParam(value = URL_PARAM_HOCHSCHULART, required = false) HochschulartFacette hochschulartFacette,
			@RequestParam(value = URL_PARAM_UMKREIS, required=false) UmkreisFacette umkreisFacette,
			@RequestParam(value = URL_PARAM_STUDIENTYP, required = false) StudientypFacette studientypFacette,
			@RequestParam(value = URL_PARAM_FITFUERSTUDIUM, required = false) FitFuerStudiumFacette fitFuerStudiumFacette,
			@RequestParam(value = URL_PARAM_REGION, required = false) RegionenFacette regionenFacette,
			@RequestParam(value = URL_PARAM_PAGE, required = false) Integer page,
			@RequestParam(value = URL_PARAM_RELOAD, required = false) Integer reload,
			WebRequest request,
			HttpServletResponse response) {

    	long startTs = System.currentTimeMillis();

    	// initialisiere Sortierung und Paging
    	Paging sortingPaging = new Paging();
    	// setze Count/Offset, falls Seite angegeben
    	if(page != null && page > 0) {
    		if(reload != null && reload.intValue() == 1) {
        		// ändere Default-Offset für Nachladen von Angeboten
    			// d.h. wir laden nur genau die eine seite
    			// durch setzen des offset vor die seite und nutzen des default count
    			sortingPaging.setOffset(Paging.COUNT_DEFAULT * (page - 1));
    		} else {
        		// erhöhe Count, falls Seite angegeben ohne Nachladen
    			// d.h. wir laden alle angebote bis inklusive zur angefragten seite
    			sortingPaging.setCount(Paging.COUNT_DEFAULT * page);
    		}
    	}

		final StudienangebotsucheAnfrage angebotsucheAnfrage = new StudienangebotsucheAnfrage(
				studienfelder,
				studienfaecher,
				orte,
				studienformFacette,
				hochschulartFacette,
				fitFuerStudiumFacette,
				umkreisFacette,
				sortingPaging,
				studientypFacette,
				regionenFacette);

		final StudienangebotsucheErgebnis angebotSearchResult = angebotsucheCommand.execute(angebotsucheAnfrage);

		if (LOGGER.isDebugEnabled()) {
			final StringBuilder sb = new StringBuilder("sucheAngebote called");
			sb.append(" " + URL_PARAM_STUDIENFELDER + "=" + studienfelder);
			sb.append(" " + URL_PARAM_ORT + "=" + orte);
			if (studienformFacette != null) {
				sb.append(" " + URL_PARAM_STUDIENFORM + "=" + studienformFacette);
			}
			if (hochschulartFacette != null) {
				sb.append(" " + URL_PARAM_HOCHSCHULART + "=" + hochschulartFacette);
			}
			if (regionenFacette != null) {
				sb.append(" " + URL_PARAM_REGION + "=" + regionenFacette);
			}
			if (umkreisFacette != null) {
				sb.append(" " + URL_PARAM_UMKREIS + "=" + umkreisFacette);
			}
			if (studientypFacette != null) {
				sb.append(" " + URL_PARAM_STUDIENTYP + "=" + studientypFacette);
			}
			if (fitFuerStudiumFacette != null) {
				sb.append(" " + URL_PARAM_FITFUERSTUDIUM + "=" + fitFuerStudiumFacette);
			}
			LOGGER.debug(sb.toString());

			LOGGER.debug("*** TIME-INFO *** - Total time: " + (System.currentTimeMillis() - startTs));
		}

		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.DYNAMIC_CONTENT);


		return ResponseEntity.ok(angebotSearchResult);
	}

}
