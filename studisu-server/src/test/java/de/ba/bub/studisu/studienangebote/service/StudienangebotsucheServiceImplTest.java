package de.ba.bub.studisu.studienangebote.service;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder;

/**
 * Testklasse fuer {@link StudienangebotsucheServiceImpl}
 * @author OettlJ
 *
 */
@RunWith(value = MockitoJUnitRunner.class)
public class StudienangebotsucheServiceImplTest {

	@Mock
	private DKZService dkzService;
	@Mock
	private BildungsangebotService bildungsangebotServiceClient;
	@Mock
	private StudienangebotsucheErgebnisBuilder ergebnisBuilder;
	
	private StudienangebotsucheService studienangebotsucheService;

	@Before
	public void setUp() throws Exception {
		studienangebotsucheService = new StudienangebotsucheServiceImpl(dkzService, bildungsangebotServiceClient, ergebnisBuilder);
	}

	@After
	public void tearDown() throws Exception {
		studienangebotsucheService = null;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSuche() {
		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(
				new Studienfelder("94114"),
				new Studienfaecher(""),
				new AnfrageOrte("Berlin_58.1234_10.3333;Nuernberg_58.1234_10.3333"),
				null,
				null,
				null, new UmkreisFacette("Bundesweit"),
				new Paging(),
				null,
				new RegionenFacette("BW"));
		
		StudienangebotsucheErgebnis ergebnis = StudienangebotsucheErgebnis
				.withItems(new ArrayList<StudienangebotWrapperMitAbstand>(), new ArrayList<StudienangebotFacette>(), 0);
		Mockito.when(ergebnisBuilder.build(Mockito.anyListOf(Studienangebot.class), Mockito.any(StudienangebotsucheAnfrage.class))).thenReturn(ergebnis);

		StudienangebotsucheErgebnis result = studienangebotsucheService.suche(anfrage);

		Assert.assertEquals(ergebnis, result);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSucheAnfrageOrteIsEmpty() {
		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(
				new Studienfelder("94114"),
				new Studienfaecher(""),
				null,
				null,
				null,
				null, new UmkreisFacette("Bundesweit"),
				new Paging(),
				null,
				new RegionenFacette("BW"));
		
		StudienangebotsucheErgebnis ergebnis = StudienangebotsucheErgebnis
				.withItems(new ArrayList<StudienangebotWrapperMitAbstand>(), new ArrayList<StudienangebotFacette>(), 0);
		Mockito.when(ergebnisBuilder.build(Mockito.anyListOf(Studienangebot.class), Mockito.any(StudienangebotsucheAnfrage.class))).thenReturn(ergebnis);

		StudienangebotsucheErgebnis result = studienangebotsucheService.suche(anfrage);

		Assert.assertEquals(ergebnis, result);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSucheListForOrtIsEmpty() {
		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(
				new Studienfelder("94114"),
				new Studienfaecher(""),
				new AnfrageOrte("Berlin_58.1234_10.3333;Nuernberg_58.1234_10.3333"),
				null,
				null,
				null, new UmkreisFacette("Bundesweit"),
				new Paging(),
				null,
				new RegionenFacette("BW"));
		
		StudienangebotsucheErgebnis ergebnis = StudienangebotsucheErgebnis
				.withItems(new ArrayList<StudienangebotWrapperMitAbstand>(), new ArrayList<StudienangebotFacette>(), 0);
		Mockito.when(ergebnisBuilder.build(Mockito.anyListOf(Studienangebot.class), Mockito.any(StudienangebotsucheAnfrage.class))).thenReturn(ergebnis);

		StudienangebotsucheErgebnis result = studienangebotsucheService.suche(anfrage);

		Assert.assertEquals(ergebnis, result);
	}
}
