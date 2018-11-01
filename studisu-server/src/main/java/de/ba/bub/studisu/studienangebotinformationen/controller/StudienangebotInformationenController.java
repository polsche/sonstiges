package de.ba.bub.studisu.studienangebotinformationen.controller;

import java.util.ArrayList;
import java.util.List;

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

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;
import de.ba.bub.studisu.studienangebotinformationen.command.StudienangebotInformationenCommand;
import de.ba.bub.studisu.studienangebotinformationen.model.StudienangebotInformationenAnfrage;

/**
 * Web REST Endpoint für StudienangebotInformationen.
 *
 * @author FuchsD013
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.STUDIENANGEBOTINFOS)
public class StudienangebotInformationenController extends StudisuController {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StudienangebotInformationenController.class);

	/**
	 * StudienangebotInformationenCommand
	 */
	private final StudienangebotInformationenCommand studienangebotInformationenCommand;

	/**
	 * Ctor
	 *
	 * @param command
	 */
	@Autowired
	public StudienangebotInformationenController(StudienangebotInformationenCommand command) {
		this.studienangebotInformationenCommand = command;
	}

	/**
	 * Rest-Endpoint. Liefert ein Studienangebot, welches mittels der ID vom
	 * Bildungsservice kommt.
	 *
	 * @param id
	 *            Die ID des Bildungsangebotes
	 * @param studienfelder
	 *            Binds to comma separated List of Studienfelder strings.
	 * @param studienfaecher
	 *            Binds to comma separated List of Studienfächer strings.
	 * @param orte
	 *            Binds to comma separated list of orte with name and
	 *            coordinates
	 * @param studienformFacette
	 *            binds to studienform filter facet values
	 * @param hochschulartFacette
	 *            binds to hochschulart filter facet values
	 * @param umkreisFacette
	 *            binds to umkreis filter facet values
	 * @param studientypFacette
	 *            binds to studientyp filter facet values
	 * @param fitFuerStudiumFacette
	 *            binds to osa/studicheck filter facet values
	 * @param regionenFacette
	 *            binds to the facet values for the selection of the bundesland
	 * @param response
	 *            Web Response Representation
	 * @return {@link ResponseEntity}
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<StudienangebotInformationen> sucheStudienangebot(
			@RequestParam(value = URL_PARAM_STUDIENANGEBOT_ID, required = true) int id,
			@RequestParam(value = URL_PARAM_STUDIENFELDER, required = false) Studienfelder studienfelder,
			@RequestParam(value = URL_PARAM_STUDIENFAECHER, required = false) Studienfaecher studienfaecher,
			@RequestParam(value = URL_PARAM_ORT, required = false) AnfrageOrte orte,
			@RequestParam(value = URL_PARAM_STUDIENFORM, required = false) StudienformFacette studienformFacette,
			@RequestParam(value = URL_PARAM_HOCHSCHULART, required = false) HochschulartFacette hochschulartFacette,
			@RequestParam(value = URL_PARAM_UMKREIS, required = false) UmkreisFacette umkreisFacette,
			@RequestParam(value = URL_PARAM_STUDIENTYP, required = false) StudientypFacette studientypFacette,
			@RequestParam(value = URL_PARAM_FITFUERSTUDIUM, required = false) FitFuerStudiumFacette fitFuerStudiumFacette,
			@RequestParam(value = URL_PARAM_REGION, required = false) RegionenFacette regionenFacette,
			HttpServletResponse response) {

		final StudienangebotInformationenAnfrage studienfeldSucheAnfrage = new StudienangebotInformationenAnfrage(id);
		final StudienangebotInformationen studienAngebot = this.studienangebotInformationenCommand
				.execute(studienfeldSucheAnfrage);
		
		if (studienAngebot == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Studienfaecher studienfaecherNeu = studienfaecher;
		StudienformFacette studienformFacetteNeu = studienformFacette;
		HochschulartFacette hochschulartFacetteNeu = hochschulartFacette;
		StudientypFacette studientypFacetteNeu = studientypFacette;
		
		// Wenn es keine Studienfelder und keine Studienfaecher gibt, dann muss
		// der Aufruf "direkt" erfolgt sein.
		// In diesem Fall erzeugen wir moeglichst "enge" Parameter, um eine
		// Ergebnisliste erzeugen zu koennen.
		if (null == studienfelder && null == studienfaecher) {
			studienfaecherNeu = new Studienfaecher(studienAngebot.getStudienfaecherCsv());
			studienformFacetteNeu = getStudienformFacetteDefault(studienAngebot, studienformFacette);
			hochschulartFacetteNeu = getHochschulartFacetteDefault(studienAngebot, hochschulartFacette);
			studientypFacetteNeu = getStudientypFacetteDefault(studienAngebot, studientypFacette);
		}

		Paging paging = new Paging();
		paging.setCount(10000000); // alle Ergebnisse benoetigt!
		
		final StudienangebotsucheAnfrage angebotsucheAnfrage = new StudienangebotsucheAnfrage(studienfelder,
				studienfaecherNeu, orte, studienformFacetteNeu, hochschulartFacetteNeu, fitFuerStudiumFacette, umkreisFacette,
				paging, studientypFacetteNeu, regionenFacette);

		this.addNavigationInfoToStudienAngebot(studienAngebot, angebotsucheAnfrage);

		if (LOGGER.isDebugEnabled()) {
			final StringBuilder sb = new StringBuilder("sucheStudienangebot called");
			sb.append(" " + URL_PARAM_STUDIENANGEBOT_ID + "=" + id);
			LOGGER.debug(sb.toString());
		}		

		// Setzen des HTTP-Caching-Headers auf Wert 6h
		super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

		return ResponseEntity.ok(studienAngebot);
	}

	/**
	 * Fuegt die Informationen zur Navigation zu den Studienangebotsinformationen hinzu.
	 * 
	 * @param studienAngebot
	 *            Das Studienangebot, dem die Informationen hinzugefuegt werden
	 *            sollen.
	 * @param angebotsucheAnfrage
	 * 			  Die Angebotsucheanfrage, mit der die Liste der Suchergebnisse gefunden 
	 * 			  werden kann.
	 */
	private void addNavigationInfoToStudienAngebot(StudienangebotInformationen studienAngebot,
			                                       StudienangebotsucheAnfrage angebotsucheAnfrage) {

		List<StudienangebotWrapperMitAbstand> items = this.studienangebotInformationenCommand
				.sucheStudienangebote(angebotsucheAnfrage);
		String currentId = studienAngebot.getId();

		int currentPos;
		for (currentPos = 0; currentPos < items.size(); currentPos++) {
			if (currentId.equals(items.get(currentPos).getStudienangebot().getId())) {
				break;
			}
		}

		// Math.max() macht Sonar gluecklich, auch wenn's derzeit total ueberfluessig ist (Konstante!)
		studienAngebot.setCurrentPage(1 + (int) Math.floor(currentPos / Math.max(1.0, 0.0 + Paging.COUNT_DEFAULT)));

		studienAngebot.setNumPrevElements(currentPos);
		if (currentPos > 0) {
			studienAngebot.setPrevElementId(items.get(currentPos - 1).getStudienangebot().getId());
		}
		
		studienAngebot.setNumNextElements(Math.max(0, items.size() - currentPos - 1));
		if (currentPos < items.size() - 1) {
			studienAngebot.setNextElementId(items.get(currentPos + 1).getStudienangebot().getId());
		}
	}

	/**
	 * Ermittle einen Default-Wert fuer die Studienform aus dem Angebot wenn keine Facette angegeben wurde.
	 * 
	 * @param studienAngebot Das Studienangenot, aus dem der Default-Wert ermittelt werden soll.
	 * @param studienformFacette Die Studienform-Facette, für die ggf. ein Defaultwert ermittelt werden soll.
	 * @return Eine gültige belegte Studienform-Facette.
	 */
	private StudienformFacette getStudienformFacetteDefault(StudienangebotInformationen studienAngebot, 
			                                                StudienformFacette studienformFacette) {
		if (null == studienformFacette) {
			List<StudienformFacettenOption> studienformFacettenOptionen = new ArrayList<>();
			studienformFacettenOptionen.add(studienAngebot.getStudienform());
			return new StudienformFacette(studienformFacettenOptionen);
		} else {
			return studienformFacette;
		}
	}

	/**
	 * Ermittle einen Default-Wert fuer die Hochschulart aus dem Angebot wenn keine Facette angegeben wurde.
	 * 
	 * @param studienAngebot Das Studienangenot, aus dem der Default-Wert ermittelt werden soll.
	 * @param hochschulartFacette Die Hochschulart-Facette, für die ggf. ein Defaultwert ermittelt werden soll.
	 * @return Eine gültige belegte Hochschulart-Facette.
	 */
	private HochschulartFacette getHochschulartFacetteDefault(StudienangebotInformationen studienAngebot, 
															  HochschulartFacette hochschulartFacette) {
		if (null == hochschulartFacette) {
			List<HochschulartFacettenOption> hochschulartFacettenOptionen = new ArrayList<>();
			hochschulartFacettenOptionen.add(studienAngebot.getHochschulart());
			return new HochschulartFacette(hochschulartFacettenOptionen);
		} else {
			return hochschulartFacette;
		}
	}

	/**
	 * Ermittle einen Default-Wert fuer den Studientyp aus dem Angebot wenn keine Facette angegeben wurde.
	 * 
	 * @param studienAngebot Das Studienangenot, aus dem der Default-Wert ermittelt werden soll.
	 * @param studientypFacette Die Studientyp-Facette, für die ggf. ein Defaultwert ermittelt werden soll.
	 * @return Eine gültige belegte Studientyp-Facette.
	 */
	private StudientypFacette getStudientypFacetteDefault(StudienangebotInformationen studienAngebot, 
														  StudientypFacette studientypFacette) {
		if (null == studientypFacette) {
			List<StudientypFacettenOption> studientypFacettenOptionen = new ArrayList<>();
			studientypFacettenOptionen.add(studienAngebot.getStudientyp());
			return new StudientypFacette(studientypFacettenOptionen);
		} else {
			return studientypFacette;
		}
	}
	
}
