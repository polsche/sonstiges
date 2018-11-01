package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Adresse;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;

/**
 * Mapper für Studienanbieter.
 */
public class StudienanbieterMapper {

	/**
	 * Mappt auf ein Zugang.
	 * 
	 * @param anbieter
	 *            der Bildungsanbieter
	 * @return AdresseKurz
	 */
	public AdresseKurz map(Bildungsanbieter anbieter) {

		AdresseKurz ret = new AdresseKurz();
		
		ret.setName(anbieter.getName());

		Adresse adresse = anbieter.getAdresse();
		if (adresse != null) {
			ret.setStrasse(adresse.getStrasse());
			ret.setOrt(adresse.getOrt().getOrtsname());
			ret.setPostleitzahl(adresse.getOrt().getPostleitzahl());
		}

		return ret;
	}
}
