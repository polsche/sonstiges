package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import de.ba.bub.studisu.common.model.Signet;

/**
 * map bildungsangebotservice signet to our signet
 */
public final class SignetMapper {

	private SignetMapper() {		
	}

    /**
     * Mappt Signet zu Studisu-Signet-DTO
     * @param signet zu mappendes Signet vom BildungsangebotService
     * @return Studisu-Typ Signet
     */
    public static Signet map(de.baintern.dst.services.wsdl.operativ.unterstuetzung.getsignetfuerbildungsanbieter_v_1.Signet signet){
    	Signet signetDTO = null;
    	
    	if (signet != null) {
    		signetDTO = new Signet();
    		signetDTO.setDaten(signet.getDaten());
    		signetDTO.setMimetype(signet.getMimetype());
    	}
    	
    	return signetDTO;    	
    }
}
