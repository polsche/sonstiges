package de.ba.bub.studisu.signet.controller;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ba.bub.studisu.common.controller.StudisuController;
import de.ba.bub.studisu.common.model.Signet;
import de.ba.bub.studisu.configuration.WebConfig;
import de.ba.bub.studisu.signet.command.SignetCommand;
import de.ba.bub.studisu.signet.model.SignetAnfrage;
import de.ba.bub.studisu.signet.model.SignetErgebnis;

/**
 * Service endpoint for Signet.
 *
 * @author pleinesm001
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.SIGNET)
public class SignetController extends StudisuController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SignetController.class);

    /**
     * Command.
     */
    private final SignetCommand signetCommand;

    /**
     * Constructor with command.
     *
     * @param signetCommand Singleton(!) command
     */
    @Autowired
    public SignetController(SignetCommand signetCommand) {
        this.signetCommand = signetCommand;
        LOGGER.debug("controller constructed");
    }

    /**
     * Endpoint fuer GET auf Signets. Man muss eine BAN-ID angeben
     * <p/>
     *
     * @param banids    Erforderlicher Parameter für den Bildungsanbieter.
     * @param response Web Response Representation
     * @return Das Signet (falls vorhanden) für den Bildungsanbieter
     */
    @GetMapping
    public ResponseEntity<InputStreamResource> getSignet(
            @RequestParam(value = URL_PARAM_SIGNET_BANID, required = true) String banids,
            HttpServletResponse response) {
        Integer banid = -1;
        try {
            banid = Integer.parseInt(banids);
        } catch (NumberFormatException nfe) {
            //String msg = String.format("Könnte banid {} für Signet nicht als integer interpretieren. ", banids);
        	String msg = "Konnte die folgende BAN-ID fuer Signet nicht als integer interpretieren:"+banids;
            LOGGER.info(msg);
        }

        if (banid == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        final SignetAnfrage signetAnfrage = new SignetAnfrage(banid);
        // this will throw an EingabeValidierungException if input invalid
        final SignetErgebnis signetErgebnis = signetCommand.execute(signetAnfrage);
        LOGGER.debug("getSignet called with banid " + banid);
        // Setzen des HTTP-Caching-Headers auf Wert 6h
        super.setHttpCacheHeader(response, HTTP_CACHING_TYPE.STATIC_CONTENT);
        Signet signetResult = signetErgebnis.getSignet();
        if (signetResult != null) {
            // Länge, Mimetype in Response setzen und Signet-Bilddaten zurückliefern
            return ResponseEntity.ok()
                    .contentLength(signetResult.getDaten().length)
                    .contentType(MediaType.parseMediaType(signetResult.getMimetype()))
                    .body(new InputStreamResource(new ByteArrayInputStream(signetResult.getDaten())));
        } else {
            //wenn BAN kein Signet hat, was ja fachlich erlaubt ist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
