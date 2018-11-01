package de.ba.bub.studisu.ort.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.exception.EingabeZuVieleZeichenException;
import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.ort.controller.OrtsucheController;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;
import de.ba.bub.studisu.ort.model.OrtsucheValidator;
import de.ba.bub.studisu.ort.service.OrtsucheService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrtsucheService.class})
public class OrtsucheCommandTest {

	OrtsucheCommand instance;
	
	OrtsucheService ortsucheService;
	
	@Mock
	OrtsucheValidator validator;
	
	@Mock
	OrtsucheAnfrage anfrageMock;
	
	@Before
	public void setUp() {
	    mockStatic(OrtsucheService.class);
	    ortsucheService = mock(OrtsucheService.class, Mockito.CALLS_REAL_METHODS);
		
	    instance = new OrtsucheCommand(ortsucheService);
	}
	
	@Test(expected = EingabeValidierungException.class)
	public void testPruefeVorbedingungenAnfrageIsNull() {
		try {
			OrtsucheAnfrage anfrageLocal = null;
			instance.pruefeVorbedingungen(anfrageLocal);
		} catch (EingabeValidierungException e) {
			assertEquals("falsche message", "Ungültiger Wert für Parameter '" + OrtsucheController.URL_PARAM_ORTSUCHE +  "'.", e.getMessage());
			throw e;
		}
	}

	@Test(expected = EingabeValidierungException.class)
	public void testPruefeVorbedingungenOrtsucheValidatorIsInvalid() {
		try {
			when(anfrageMock.getValidationResult()).thenReturn(OrtsucheValidator.INVALID);
			instance.pruefeVorbedingungen(anfrageMock);
		} catch (EingabeValidierungException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains(OrtsucheController.URL_PARAM_ORTSUCHE));
			throw e;
		}
	}
	
	@Test(expected = EingabeZuVieleZeichenException.class)
	public void testPruefeVorbedingungenOrtsucheValidatorIsInvalidOrtZuLang() {
		try {
			when(anfrageMock.getValidationResult()).thenReturn(OrtsucheValidator.INVALID_ORT_ZU_LANG);
			instance.pruefeVorbedingungen(anfrageMock);
		} catch (EingabeZuVieleZeichenException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains(OrtsucheController.URL_PARAM_ORTSUCHE_ZU_LANG));
			throw e;
		}
	}
	
	@Test
	public void testGeschaeftslogikAusfuehren() {
		OrtsucheAnfrage anfrage = new OrtsucheAnfrage("Berlin");
		List<Ort> orte = new ArrayList<>();
		Double breitengrad = 52.5183;
		Double laengengrad = 13.4058;
		String bundesland = "Auenland";
		orte.add(new Ort("Berlin", "14197", breitengrad, laengengrad, bundesland));
		OrtsucheErgebnis ortsucheErgebnis = new OrtsucheErgebnis(orte);
		when(ortsucheService.sucheOrte(any(OrtsucheAnfrage.class))).thenReturn(ortsucheErgebnis);

		OrtsucheErgebnis ortsucheErgebnisResult = instance.geschaeftslogikAusfuehren(anfrage);

		// inhalt pruefen
		List<Ort> responseEntityOrte = (List<Ort>) ortsucheErgebnisResult.getOrte();
		Ort testOrt = responseEntityOrte.get(0);
		assertEquals("Ort Berlin wird erwartet", "Berlin", testOrt.getName());
		assertEquals("Ort PLZ wird erwartet", "14197", testOrt.getPostleitzahl());
		assertEquals("Ort Breitengrad wird erwartet", breitengrad, testOrt.getBreitengrad());
		assertEquals("Ort Laengengrad wird erwartet", laengengrad, testOrt.getLaengengrad());
		assertEquals("Ort Breitengrad wird erwartet", bundesland, testOrt.getBundesland());

	}
}
