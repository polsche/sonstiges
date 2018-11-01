package de.ba.bub.studisu.studienangebote.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;

/**
 * Testklasse fuer {@link StudienangebotsucheErgebnis}
 * @author OettlJ
 *
 */
public class StudienangebotsucheErgebnisTest {
	
	private final static String PREFIX_ANGEBOT = "Studienangebot ";

	/**
	 * Testmethode fuer {@link StudienangebotsucheErgebnis#limit(Paging)}
	 */
	@Test
	public void testLimit() {		
		// Test Abfrage Seite 1 mit 20 Ergebnissen
		StudienangebotsucheErgebnis testClass = StudienangebotsucheErgebnis.withItems(getAngebote(55), new ArrayList<StudienangebotFacette>(), 0);
		Paging sortingPaging = new Paging();
		testClass.limit(sortingPaging);
		
		assertEquals("Ergebnis hat 55 Einträge", 55, testClass.getMaxErgebnisse());
		assertEquals("20 von 55 Angeboten werden geliefert", 20, testClass.getItems().size());
		assertTrue("'Studienangebot 1' bis 'Studienangebot 20' geliefert", 
				(PREFIX_ANGEBOT + "1").equals(testClass.getItems().get(0).getStudienangebot().getStudiBezeichnung())
						&& (PREFIX_ANGEBOT + "20").equals(testClass.getItems().get(testClass.getItems().size() - 1)
								.getStudienangebot().getStudiBezeichnung()));
		
		// Test Abfrage Seite 2 mit 20 Ergebnissen
		testClass = StudienangebotsucheErgebnis.withItems(getAngebote(45), new ArrayList<StudienangebotFacette>(), 0);
		sortingPaging = new Paging();
		sortingPaging.setOffset(Paging.COUNT_DEFAULT);
		testClass.limit(sortingPaging);
		
		assertEquals("Ergebnis hat 45 Einträge", 45, testClass.getMaxErgebnisse());
		assertEquals("20 von 45 Angeboten werden geliefert", 20, testClass.getItems().size());
		assertTrue("'Studienangebot 21' bis 'Studienangebot 40' geliefert", 
				(PREFIX_ANGEBOT + "21").equals(testClass.getItems().get(0).getStudienangebot().getStudiBezeichnung())
						&& (PREFIX_ANGEBOT + "40").equals(testClass.getItems().get(testClass.getItems().size() - 1)
								.getStudienangebot().getStudiBezeichnung()));
		
		// Test Abfrage Seite 2 mit 15 Ergebnissen
		testClass = StudienangebotsucheErgebnis.withItems(getAngebote(35), new ArrayList<StudienangebotFacette>(), 0);
		sortingPaging = new Paging();
		sortingPaging.setOffset(Paging.COUNT_DEFAULT);
		testClass.limit(sortingPaging);
		
		assertEquals("Ergebnis hat 35 Einträge", 35, testClass.getMaxErgebnisse());
		assertEquals("15 von 35 Angeboten werden geliefert", 15, testClass.getItems().size());
		assertTrue("'Studienangebot 21' bis 'Studienangebot 35' geliefert", 
				(PREFIX_ANGEBOT + "21").equals(testClass.getItems().get(0).getStudienangebot().getStudiBezeichnung())
						&& (PREFIX_ANGEBOT + "35").equals(testClass.getItems().get(testClass.getItems().size() - 1)
								.getStudienangebot().getStudiBezeichnung()));
		
		// Test Abfrage Seite 1 und 2 mit insgesamt 40 Ergebnissen
		testClass = StudienangebotsucheErgebnis.withItems(getAngebote(45), new ArrayList<StudienangebotFacette>(), 0);
		sortingPaging = new Paging();
		sortingPaging.setCount(Paging.COUNT_DEFAULT * 2);
		testClass.limit(sortingPaging);
		
		assertEquals("Ergebnis hat 45 Einträge", 45, testClass.getMaxErgebnisse());
		assertEquals("40 von 45 Angeboten werden geliefert", 40, testClass.getItems().size());
		assertTrue("'Studienangebot 1' bis 'Studienangebot 40' geliefert", 
				(PREFIX_ANGEBOT + "1").equals(testClass.getItems().get(0).getStudienangebot().getStudiBezeichnung())
						&& (PREFIX_ANGEBOT + "40").equals(testClass.getItems().get(testClass.getItems().size() - 1)
								.getStudienangebot().getStudiBezeichnung()));
	}
	
	private List<StudienangebotWrapperMitAbstand> getAngebote(int count) {
		List<StudienangebotWrapperMitAbstand> angebote = new ArrayList<StudienangebotWrapperMitAbstand>();
		
		for(int i = 1; i <= count; i ++) {
			Studienangebot angebot = new Studienangebot();
			angebot.setStudiBezeichnung(PREFIX_ANGEBOT + i);
			angebote.add(new StudienangebotWrapperMitAbstand(angebot, null));
		}
		
		return angebote;
	}

}
