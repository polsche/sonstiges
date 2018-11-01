package de.ba.bub.studisu.studienangebote.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;

/**
 * Testklasse fuer {@link StudienangebotsucheServiceMockImpl}
 * @author KunzmannC
 *
 */
@RunWith(value = MockitoJUnitRunner.class)
public class StudienangebotsucheServiceMockImplTest {

	@Test
	public void testSuche() {
		StudienangebotsucheAnfrage anfrage = null;
		StudienangebotsucheServiceMockImpl instance = new StudienangebotsucheServiceMockImpl();
		StudienangebotsucheErgebnis result = instance.suche(anfrage);
		Assert.assertNotNull("mock sollte ein nichtleeres ergebnis liefern ", result);
		Assert.assertNotNull("mock sollte ein nichtleeres ergebnis liefern ", result.getItems());
		Assert.assertTrue("mock sollte ein nichtleeres ergebnis liefern ", result.getItems().size() > 0);
	}
}
