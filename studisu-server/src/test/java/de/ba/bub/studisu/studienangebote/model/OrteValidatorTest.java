package de.ba.bub.studisu.studienangebote.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ba.bub.studisu.studienangebote.model.OrteValidator;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
/**
 * Tests für {@link OrteValidator}.
 * 
 * @author Bulbul geklaut von StraubP
 *
 */
public class OrteValidatorTest {

	/**
	 * Testet die Validierung in verschiedenen Varianten.
	 */
	@Test
	public void test() {
		check(null, null, OrteValidator.INVALID);
		check(null, new UmkreisFacette(null), OrteValidator.INVALID);
		check(null, new UmkreisFacette(""), OrteValidator.INVALID);
		check(new AnfrageOrte("München_11.5753_48.1369;Nümbrecht_7.5406_50.9"), new UmkreisFacette("50"), OrteValidator.VALID);
		check(new AnfrageOrte("München_11.5753_48.1369;Nümbrecht_7.5406_50.;Berlin_13.4058_52.51839;Nürnberg Mittelfranken_11.075_49.4508"), new UmkreisFacette("Bundesweit"), OrteValidator.INVALID);
		check(new AnfrageOrte("München_11.5753_48.1369;Nümbrecht_7.5406_50.;Berlin_13.4058_52.51839"), new UmkreisFacette("50"), OrteValidator.VALID);
		check(new AnfrageOrte("München_11.5753_48.1369;Nümbrecht_7.5406_50.9"), null, OrteValidator.INVALID);
		check(new AnfrageOrte("München_11.5753_48.1369;Nümbrecht_7.5406_50.9;Nümbrecht_7.5406_50.9"), new UmkreisFacette("Bundesweit"), OrteValidator.VALID);
		check(new AnfrageOrte("München_11.5753_48.1369;Nümbrecht_7.5406_50.9"), null, OrteValidator.INVALID);
		check(null, new UmkreisFacette("Bundesweit"), OrteValidator.VALID);
	}

	/**
	 * Methode, die eine Validierung ausführt.
	 * 
	 * @param suchbegriff
	 *            Suchbegriff
	 * @param studienfaecher
	 *            Studienfächer
	 * @param expected
	 *            erwartetes Validierungsergebnis
	 */
	private void check(AnfrageOrte ortRequestParams,UmkreisFacette umkreisFacette , int expected) {
		OrteValidator val = new OrteValidator(umkreisFacette, ortRequestParams);
		assertEquals(expected, val.getResult());
	}

}
