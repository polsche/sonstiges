package de.ba.bub.studisu.ort.controller;

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
import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.ort.command.OrtsucheCommand;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;

/**
 * Service endpoint for Ortsuche
 * Created by loutzenhj on 29.03.2017.
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.ORTE)
public class OrtsucheController extends StudisuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrtsucheController.class);

    private final OrtsucheCommand ortsucheCommand;

    /**
     * Constructor with command
     *
     * @param ortsucheCommand Singleton(!) command
     */
    @Autowired
    public OrtsucheController(OrtsucheCommand ortsucheCommand) {
        this.ortsucheCommand = ortsucheCommand;
        LOGGER.debug("controller constructed");
    }

    /**
     * Endpoint fuer GET auf Ortsuche.<p/>
     *
     * @param ortsuche required param - such string aus ort Suchschlitz
     * @param response Web Response Representation
     * @return Ein {@link Ort} mit den gefundenenen Angeboten
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ort>> sucheOrte(
            @RequestParam(value = URL_PARAM_ORTSUCHE, required = true) String ortsuche,
            HttpServletResponse response) {
        final OrtsucheAnfrage ortsucheAnfrage = new OrtsucheAnfrage(ortsuche);
        //this will throw an EingabeValidierungException if input invalid
        final OrtsucheErgebnis ortsucheErgebnis = ortsucheCommand.execute(ortsucheAnfrage);
        LOGGER.debug("sucheOrte called with such string " + ortsuche);
        List<Ort> ortResultList = new ArrayList<Ort>(ortsucheErgebnis.getOrte());

        // Setzen des HTTP-Caching-Headers auf Wert 6h
     	super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);

        return ResponseEntity.ok(ortResultList);
    }

}
