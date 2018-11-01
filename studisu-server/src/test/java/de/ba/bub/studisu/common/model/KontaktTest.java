package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class KontaktTest {

	@Test
	public void test() {
		
		Kontakt cut = new Kontakt();
		
		assertNull(cut.getEmail());
		assertNull(cut.getInternet());
		assertNull(cut.getTelefaxNummer());
		assertNull(cut.getTelefaxVorwahl());
		assertNull(cut.getTelefonNummer());
		assertNull(cut.getTelefonVorwahl());
		
		String email = "email";
		String internet = "internet";
		String telefaxNummer = "telefaxNummer";
		String telefaxVorwahl = "telefaxVorwahl";
		String telefonNummer = "telefonNummer";
		String telefonVorwahl = "telefonVorwahl";

		cut.setEmail(email);
		cut.setInternet(internet);
		cut.setTelefaxNummer(telefaxNummer);
		cut.setTelefaxVorwahl(telefaxVorwahl);
		cut.setTelefonNummer(telefonNummer);
		cut.setTelefonVorwahl(telefonVorwahl);
		
		assertEquals(email, cut.getEmail());
		assertEquals(internet, cut.getInternet());
		assertEquals(telefaxNummer, cut.getTelefaxNummer());
		assertEquals(telefaxVorwahl, cut.getTelefaxVorwahl());
		assertEquals(telefonNummer, cut.getTelefonNummer());
		assertEquals(telefonVorwahl, cut.getTelefonVorwahl());
	}

}
