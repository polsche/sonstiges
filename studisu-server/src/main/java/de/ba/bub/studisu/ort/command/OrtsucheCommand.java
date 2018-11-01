package de.ba.bub.studisu.ort.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.exception.EingabeZuVieleZeichenException;
import de.ba.bub.studisu.common.service.command.AbstractCommand;
import de.ba.bub.studisu.ort.controller.OrtsucheController;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;
import de.ba.bub.studisu.ort.model.OrtsucheValidator;
import de.ba.bub.studisu.ort.service.OrtsucheService;

/**
 * Ort suche command
 * Achtung! diese ist ein Singleton und darf daher keinen State haben!
 * Created by loutzenhj on 29.03.2017.
 */
@Component
public class OrtsucheCommand extends AbstractCommand<OrtsucheAnfrage, OrtsucheErgebnis> {

    /**
     * Service impl dependency
     */
    private final OrtsucheService ortsucheService;

    /**
     * C-tor with autowired service
     * @param ortsucheService --
     */
    @Autowired
    public OrtsucheCommand(@Qualifier("orte") OrtsucheService ortsucheService) {
        this.ortsucheService = ortsucheService;
    }

    @Override
    protected void pruefeVorbedingungen(OrtsucheAnfrage anfrage) {
        if(anfrage==null || OrtsucheValidator.INVALID ==anfrage.getValidationResult()){
            throw new EingabeValidierungException(OrtsucheController.URL_PARAM_ORTSUCHE);
        }
        else if(OrtsucheValidator.INVALID_ORT_ZU_LANG ==anfrage.getValidationResult()){
            throw new EingabeZuVieleZeichenException(OrtsucheController.URL_PARAM_ORTSUCHE_ZU_LANG);
        }
    }

    @Override
    protected OrtsucheErgebnis geschaeftslogikAusfuehren(OrtsucheAnfrage anfrage) {
        OrtsucheErgebnis ergebnis = ortsucheService.sucheOrte(anfrage);
        return ergebnis;
    }
}