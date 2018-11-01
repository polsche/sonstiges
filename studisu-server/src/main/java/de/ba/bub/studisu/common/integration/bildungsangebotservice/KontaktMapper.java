package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import de.ba.bub.studisu.common.model.Kontakt;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Bildungsanbieter;

/**
 * Mapper für Kontakt.
 */
public class KontaktMapper {

	/**
	 * Mappt auf einen Kontakt.
	 * 
	 * @param anbieter
	 *            der Bildungsanbieter
	 * @return Kontakt
	 */
	public Kontakt map(Bildungsanbieter anbieter) {

		Kontakt kontakt = new Kontakt();
		kontakt.setTelefonVorwahl(anbieter.getTelefonvorwahl());
		kontakt.setTelefonNummer(anbieter.getTelefonnummer());
		kontakt.setTelefaxVorwahl(anbieter.getFaxvorwahl());
		kontakt.setTelefaxNummer(anbieter.getFaxnummer());
		kontakt.setInternet(anbieter.getHomepage());
		kontakt.setEmail(anbieter.getEMail());

		return kontakt;
	}

}
