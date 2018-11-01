package de.ba.bub.studisu.ort.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;

/**
 * Testklasse fuer {@link OrtsucheServiceMockImpl}
 * @author KunzmannC
 *
 */
@RunWith(value = MockitoJUnitRunner.class)
public class OrtsucheServiceMockImplTest {

	@Test
	public void testSuche() {
		OrtsucheAnfrage anfrage = null;
		OrtsucheServiceMockImpl instance = new OrtsucheServiceMockImpl();
		OrtsucheErgebnis result = instance.sucheOrte(anfrage);
		Assert.assertNotNull("mock sollte ein nichtleeres ergebnis liefern ", result);
		Assert.assertNotNull("mock sollte ein nichtleeres ergebnis liefern ", result.getOrte());
		Assert.assertTrue("mock sollte ein nichtleeres ergebnis liefern ", result.getOrte().size() > 0);
	}
}
