package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bundesland;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findorte_v_1.Ort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * map bildungsangebotservice ort to our ort
 * Created by loutzenhj on 30.03.2017.
 */
public final class OrtMapper {

	private OrtMapper() {		
	}

    /**
     * Mappt eine Liste vom BildungsangebotService XML-Typ Ort
     * zu einer Liste vom Studisu-Typ Ort.
     * @param orte zu mappende Liste vom BildungsangebotService
     * XML-Typ Ort.
     * @return Liste vom Studisu-Typ Ort für übergebene Liste vom
     * XML-Typ Ort.
     */
    public static List<de.ba.bub.studisu.common.model.Ort> map(Collection<Ort> orte){
        List<de.ba.bub.studisu.common.model.Ort> mappedOrte = new ArrayList<de.ba.bub.studisu.common.model.Ort>();
        for(Ort ort : orte) {
            String name = ort.getOrtsname();
            String plz = ort.getPostleitzahl();
            Double lgrad = ort.getLaengengrad();
            Double bgrad = ort.getBreitengrad();

            Bundesland bundeslandEnum = ort.getBundesland();
            //robustheit gegen null erhoeht
            String bundesland = "";
            if (bundeslandEnum != null) {
            	bundesland = bundeslandEnum.value();
            }
            de.ba.bub.studisu.common.model.Ort mapped = new de.ba.bub.studisu.common.model.Ort(name,plz,bgrad,lgrad,bundesland);
            mappedOrte.add(mapped);
        }
        return mappedOrte;
    }
}
