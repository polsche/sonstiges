package de.ba.bub.studisu.studienangebote.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.SuchortAbstand;
import de.ba.bub.studisu.studienangebote.model.OrteValidator;
import de.ba.bub.studisu.studienangebote.model.StudienangeboteValidator;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;
import de.ba.bub.studisu.studienangebote.service.StudienangebotsucheService;

public class StudienangebotsucheCommandTest {

	
	@Mock
	private StudienangebotsucheService studienangebotsucheService;
		
	@Mock
	private StudienangebotsucheErgebnis ergebnis;

	private StudienangebotsucheCommand instance;
	private StudienangebotsucheAnfrage anfrage;
	
	// anfrage parameter (aehnlich sucheparametern im controller)
	Studienfelder studienfelder;
	Studienfaecher studienfaecher;
	AnfrageOrte anfrageOrte;
	StudienformFacette studienformFacette;
	HochschulartFacette hochschulartFacette;
	UmkreisFacette umkreisFacette;
	StudientypFacette studientypFacette;
	FitFuerStudiumFacette fitFuerStudiumFacette;
	RegionenFacette bundeslandFacette;
	Paging paging;

	
	@Before
	public void setUp() throws Exception {
		studienangebotsucheService = mock(StudienangebotsucheService.class);
		instance = new StudienangebotsucheCommand(studienangebotsucheService);
		
		ergebnis = mock(StudienangebotsucheErgebnis.class);

		// Felder für ergebnis - uebernommen von controllertest
		List<StudienangebotWrapperMitAbstand> angeboteWrapped = new ArrayList<>();
		Studienangebot studienangebot = new Studienangebot();
		studienangebot.setStudiInhalt("test inhalt");
		SuchortAbstand suchortAbstand = new SuchortAbstand("Paderborn", Double.valueOf(66));
		List<SuchortAbstand> abstaende = new ArrayList<>();
		abstaende.add(suchortAbstand);
		StudienangebotWrapperMitAbstand item = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);
		angeboteWrapped.add(item);
		List<? extends StudienangebotFacette> facetten = new ArrayList<>();
		long filteredOutErgebnisse = 0;
		ergebnis = StudienangebotsucheErgebnis.withItems(angeboteWrapped, facetten, filteredOutErgebnisse);

		//anfrage parameter
		studienfelder = new Studienfelder("93796;93701;93802;93986;94014;94163");
		studienfaecher = new Studienfaecher("93615;94028;94080");
		
		String pbParam = "Paderborn_8.7572_51.7194";
		@SuppressWarnings("unused")
		AnfrageOrt pb = new AnfrageOrt(pbParam);
		String nParam = "Nürnberg%2C%20Mittelfranken_11.075_49.4508";
		anfrageOrte = new AnfrageOrte(pbParam + StudienangebotFacette.VALUE_SEPARATOR + nParam);

		studienformFacette = new StudienformFacette("1;2;4");

		hochschulartFacette = new HochschulartFacette();
		umkreisFacette = new UmkreisFacette(UmkreisFacette.BUNDESWEIT.getName());
		studientypFacette = new StudientypFacette("0;1");
		fitFuerStudiumFacette = new FitFuerStudiumFacette();//FitFuerStudiumFacettenOption.OSA.name()
		bundeslandFacette = new RegionenFacette(RegionenFacettenOption.BRANDENBURG.getName());
		
		paging = new Paging();
		//paging.setCount(30);
		//paging.setOffset(40);

		// weil weder mit mockito noch mit Powermockito die final class mocked werden konnte,
		// anfrage = PowerMockito.mock(StudienangebotsucheAnfrage.class);
		// instanziieren wir selbst :)
		anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
						studienformFacette, hochschulartFacette, fitFuerStudiumFacette,
				umkreisFacette, paging, studientypFacette, bundeslandFacette);
	}

	@After
	public void tearDown() throws Exception {
		instance = null;
	}

	@Test
	public void testConstructorSuccess() {
		new StudienangebotsucheCommand(studienangebotsucheService);
	}

	@Test
	public void testGeschaeftslogikAusfuehrenInteger() {
		when(studienangebotsucheService.suche(Mockito.any(StudienangebotsucheAnfrage.class))).thenReturn(ergebnis);
		StudienangebotsucheErgebnis returnObject = instance.geschaeftslogikAusfuehren(anfrage);

		//assertTrue("pruefe erwartete Elementezahl im Ergebnisset", returnObject.size() == ergebnis.size());
		String beispielInhalt = returnObject.getItems().get(0).getStudienangebot().getStudiInhalt();
		assertEquals("ergebnis sollte aus gemockten service durchschlagen", "test inhalt", beispielInhalt);
	}

	public void testPruefeVorbedingungenSuccess() {
		instance.pruefeVorbedingungen(anfrage);
		// keine ValidationException aufgetreten
		assertTrue("validierung sollte erfolgreich sein, jedoch ist eine exception aufgetreten", true);
	}

	@Test(expected=EingabeValidierungException.class)
	public void testPruefeVorbedingungenNull() {
		try {
			instance.pruefeVorbedingungen(null);
		} catch (EingabeValidierungException ex) {
			// message wird noch gewrappt, deswegen nur contains, nicht equals
			assertTrue("pruefe Fehlemeldung aus Validierung", 
					ex.getMessage().contains(OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES));
			
			assertTrue(ex.getCause() != null);
			assertEquals("errormessage bei null", 
					StudienangeboteValidator.MISSING_PARAM_MESSAGE_S_AND_S, ex.getCause().getMessage());
			throw ex;
		}
	}
	
  	@Test(expected=EingabeValidierungException.class)
	public void testPruefeVorbedingungenAngebotInvalid() {
  		studienfelder = null;
  		studienfaecher = null;
		anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette,
				umkreisFacette, paging, studientypFacette, bundeslandFacette);
  		try {
			instance.pruefeVorbedingungen(anfrage);
		} catch (EingabeValidierungException ex) {
			// message wird noch gewrappt, deswegen nur contains, nicht equals
			assertTrue("pruefe Fehlemeldung aus Validierung", 
					ex.getMessage().contains(OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES));
			
			assertTrue(ex.getCause() != null);
			assertEquals("in cause eingepackte errormessage stimmt nicht", 
					StudienangeboteValidator.MISSING_PARAM_MESSAGE_S_AND_S, ex.getCause().getMessage());
			throw ex;
		}
	}

  	@Test(expected=EingabeValidierungException.class)
	public void testPruefeVorbedingungenOrtInvalid() {
  		anfrageOrte = null;
  		umkreisFacette = new UmkreisFacette();
		anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette,
				umkreisFacette, paging, studientypFacette, bundeslandFacette);
  		try {
			instance.pruefeVorbedingungen(anfrage);
		} catch (EingabeValidierungException ex) {
			// message wird noch gewrappt, deswegen nur contains, nicht equals
			assertTrue("pruefe Fehlemeldung aus Validierung", 
					ex.getMessage().contains(OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES));
			
			assertTrue(ex.getCause() != null);
			assertEquals("in cause eingepackte errormessage stimmt nicht", 
					OrteValidator.TOO_MANY_ORT_VALUES_OR_DUPLICATES, ex.getCause().getMessage());
			throw ex;
		}
	}
}
