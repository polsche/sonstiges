package de.ba.bub.studisu.ort.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;
import de.ba.bub.studisu.ort.model.OrtsucheValidator;

/**
 * Orts-service using bildungsangebot webservice schnittstelle
 * Created by loutzenhj on 30.03.2017.
 */
@Service("orte")
public class OrtsucheServiceImpl implements OrtsucheService {

    private BildungsangebotService bildungsangebotServiceClient;

    /**
     * C-tor gets initialized service port as autowired dependency
     *
     * @param baClient
     * 			Der zu verwendete {@link BildungsangebotService}.
     */
    @Autowired
    public OrtsucheServiceImpl(BildungsangebotService baClient){
        this.bildungsangebotServiceClient = baClient;
    }

    @Override
    public OrtsucheErgebnis sucheOrte(OrtsucheAnfrage anfrage) {
        if(anfrage==null){
            throw new IllegalArgumentException("anfrage null");
        }
        String suchstring = anfrage.getSuchString();
        if(StringUtils.isEmpty(suchstring)){
            throw new IllegalArgumentException("anfrage string empty...");
        }
        String plz = null;
        String ort = null;
        if(anfrage.getValidationResult()== OrtsucheValidator.VALID_PLZ){
            plz = anfrage.getSuchString();
        }else if(anfrage.getValidationResult()==OrtsucheValidator.VALID_NAME){
            //add wildcard!
           ort = new StringBuilder(suchstring).append("*").toString();
        }
        OrtsucheErgebnis ergebnis = new OrtsucheErgebnis(bildungsangebotServiceClient.findOrte(plz, ort));
        return ergebnis;
    }
}
