package de.ba.bub.studisu.ort.service;

import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;

import org.springframework.stereotype.Service;

/**
 * Created by loutzenhj on 29.03.2017.
 */
@Service("mock-orte")
public class OrtsucheServiceMockImpl implements OrtsucheService{
    @Override
    public OrtsucheErgebnis sucheOrte(OrtsucheAnfrage anfrage) {
        String[]ortnames = {"Minga","DickesB","Neumarkt i.d. Haus","Oberweitendorf"};
        OrtsucheErgebnis ergebnis = new OrtsucheErgebnis();
        // mock data
        for(String name : ortnames){
            ergebnis.addOrt(Ort.withNameAndPlz(name, String.valueOf(ergebnis.getOrte().size())));
        }
        return ergebnis;
    }
}
