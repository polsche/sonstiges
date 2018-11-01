package de.ba.bub.studisu.studienangebote.util;

import static de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption.KEINES;
import static de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption.OSA;
import static de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption.STUDICHECK;
import static de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption.BERUFSAKADEMIE;
import static de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption.FACHHOCHSCHULE;
import static de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption.UNIVERSITAET;
import static de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption.BLOCKUNTERRICHT;
import static de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption.TEILZEIT;
import static de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption.VOLLZEIT;
import static de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption.GRUNDSTAENDIG;
import static de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption.WEITERFUEHREND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder.FilterProcessor;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder.FilterResult;

/**
 * Tests für {@link FilterProcessor}.
 * 
 * @author StraubP
 */
public class FilterProcessorTest {

	private static final Studienangebot SA_1 = sa(1, GRUNDSTAENDIG, TEILZEIT, FACHHOCHSCHULE, KEINES);
	private static final Studienangebot SA_2 = sa(2, WEITERFUEHREND, VOLLZEIT, FACHHOCHSCHULE, OSA);
	private static final Studienangebot SA_3 = sa(3, WEITERFUEHREND, VOLLZEIT, UNIVERSITAET, STUDICHECK);
	private static final Studienangebot SA_4 = sa(4, WEITERFUEHREND, TEILZEIT, UNIVERSITAET, OSA, STUDICHECK);
	private static final Studienangebot SA_5 = sa(5, WEITERFUEHREND, BLOCKUNTERRICHT, UNIVERSITAET, OSA, STUDICHECK);
	private static final Studienangebot SA_6 = sa(6, WEITERFUEHREND, TEILZEIT, BERUFSAKADEMIE, OSA);
	private static final Studienangebot SA_7 = sa(7, WEITERFUEHREND, BLOCKUNTERRICHT, BERUFSAKADEMIE, KEINES);

	/**
	 * Null-Parameter-Test.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParam() {
		new FilterProcessor(null);
	}


	/**
	 * Bei keinen FilterFacetten wird alles akzeptiert.
	 */
	@Test
	public void testNoFilterFacetten() {
		List<StudienangebotFacette> filterFacetten = new ArrayList<StudienangebotFacette>();

		FilterProcessor filterProcessor = new FilterProcessor(filterFacetten);

		List<Studienangebot> sas = new ArrayList<Studienangebot>();

		FilterResult result = filterProcessor.process(sas);
		expectStudienangebote(result.getStudienangebote());
		expectCounts(result.getFacettenOptionCounts());

		result = filterProcessor.process(l(SA_1, SA_2, SA_3));
		expectStudienangebote(result.getStudienangebote(), saw(SA_1), saw(SA_2), saw(SA_3));
		expectCounts(result.getFacettenOptionCounts());
	}

	/**
	 * Bei FilterFacetten ohne selektierte Optionen wird alles ausgefiltert.
	 */
	@Test
	public void testFacettenWithNoOptions() {
		List<StudienangebotFacette> filterFacetten = new ArrayList<StudienangebotFacette>();
		filterFacetten.add(new StudientypFacette());
		filterFacetten.add(new StudienformFacette());
		filterFacetten.add(new HochschulartFacette());

		FilterProcessor filterProcessor = new FilterProcessor(filterFacetten);

		List<Studienangebot> sas = new ArrayList<Studienangebot>();

		FilterResult result = filterProcessor.process(sas);
		expectStudienangebote(result.getStudienangebote());
		expectCounts(result.getFacettenOptionCounts());

		result = filterProcessor.process(l(SA_1, SA_2, SA_3));
		expectStudienangebote(result.getStudienangebote());
		expectCounts(result.getFacettenOptionCounts());
	}

	/**
	 * Bei FilterFacetten mit selektierten Optionen werden nicht-passende
	 * Angebote ausgefiltert.
	 */
	@Test
	public void testFacettenWithOptions() {
		List<StudienangebotFacette> filterFacetten = new ArrayList<StudienangebotFacette>();

		List<StudientypFacettenOption> options = new ArrayList<>();
		options.add(WEITERFUEHREND);
		StudientypFacette studientypFacette = new StudientypFacette(options);
		filterFacetten.add(studientypFacette);

		List<StudienformFacettenOption> options1 = new ArrayList<>();
		options1.add(VOLLZEIT);
		options1.add(TEILZEIT);
		StudienformFacette studienformFacette = new StudienformFacette(options1);
		filterFacetten.add(studienformFacette);

		List<HochschulartFacettenOption> options2 = new ArrayList<>();
		options2.add(UNIVERSITAET);
		options2.add(FACHHOCHSCHULE);
		HochschulartFacette hochschulartFacette = new HochschulartFacette(options2);
		filterFacetten.add(hochschulartFacette);

		FilterProcessor filterProcessor = new FilterProcessor(filterFacetten);

		List<Studienangebot> sas = new ArrayList<Studienangebot>();

		FilterResult result = filterProcessor.process(sas);
		expectStudienangebote(result.getStudienangebote());
		expectCounts(result.getFacettenOptionCounts());

		result = filterProcessor.process(l(SA_1, SA_2, SA_3, SA_4, SA_5, SA_6, SA_7));
		expectStudienangebote(result.getStudienangebote(), saw(SA_2), saw(SA_3), saw(SA_4));
		expectCounts(result.getFacettenOptionCounts(), c(WEITERFUEHREND, 3), c(VOLLZEIT, 2), c(TEILZEIT, 1),
				c(UNIVERSITAET, 2), c(FACHHOCHSCHULE, 1), c(GRUNDSTAENDIG, +1), c(BLOCKUNTERRICHT, +1),
				c(BERUFSAKADEMIE, +1));
	}

	/**
	 * FitFuerStudiumFacette ist eine Facette, bei der ein Studienangebot mehr
	 * als eine Option haben kann. Hier gilt eine spezielle Zähllogik, die wir
	 * hier testen. (STUDISU-239)
	 */
	@Test
	public void testNonExcusiveOptions() {
		FilterProcessor filterProcessor = filterProzessor(STUDICHECK);
		FilterResult result = filterProcessor.process(l(SA_1, SA_2, SA_3, SA_4, SA_5, SA_6, SA_7));
		expectStudienangebote(result.getStudienangebote(), saw(SA_3), saw(SA_4), saw(SA_5));
		expectCounts(result.getFacettenOptionCounts(), c(STUDICHECK, 3), c(OSA, +2), c(KEINES, +2));

		filterProcessor = filterProzessor(OSA);
		result = filterProcessor.process(l(SA_1, SA_2, SA_3, SA_4, SA_5, SA_6, SA_7));
		expectStudienangebote(result.getStudienangebote(), saw(SA_2), saw(SA_4), saw(SA_5), saw(SA_6));
		expectCounts(result.getFacettenOptionCounts(), c(STUDICHECK, +1), c(OSA, 4), c(KEINES, +2));

		filterProcessor = filterProzessor(STUDICHECK, OSA);
		result = filterProcessor.process(l(SA_1, SA_2, SA_3, SA_4, SA_5, SA_6, SA_7));
		expectStudienangebote(result.getStudienangebote(), saw(SA_2), saw(SA_3), saw(SA_4), saw(SA_5), saw(SA_6));
		expectCounts(result.getFacettenOptionCounts(), c(STUDICHECK, 3), c(OSA, 4), c(KEINES, +2));
	}

	/**
	 * Baut einen FilterProcessor
	 * 
	 * @param options
	 *            die selektierten Optionen
	 * @return FilterProcessor
	 */
	private FilterProcessor filterProzessor(FitFuerStudiumFacettenOption... options) {

		List<FitFuerStudiumFacettenOption> selectedOptions = new ArrayList<FitFuerStudiumFacettenOption>();
		for (FitFuerStudiumFacettenOption option : options) {
			selectedOptions.add(option);
		}

		FitFuerStudiumFacette fitFuerStudiumFacette = new FitFuerStudiumFacette(selectedOptions);

		List<StudienangebotFacette> filterFacetten = new ArrayList<StudienangebotFacette>();
		filterFacetten.add(fitFuerStudiumFacette);

		return new FilterProcessor(filterFacetten);
	}

	/**
	 * Prüfmethode für die FacettenOptionCounts.
	 * 
	 * @param facettenOptionCounts
	 * @param expecteds
	 */
	private void expectCounts(Map<FacettenOption, Integer> facettenOptionCounts, C... expecteds) {

		Set<FacettenOption> options = new HashSet<>();
		for (C expected : expecteds) {
			Integer anzahl = facettenOptionCounts.get(expected.option);
			if (anzahl == null) {
				anzahl = 0;
			}
			options.add(expected.option);
			assertEquals(expected.anzahl, anzahl.intValue());
		}

		for (FacettenOption o : facettenOptionCounts.keySet()) {
			Integer count = facettenOptionCounts.get(o);
			if (count != null) {
				assertTrue(o.getLabel() + " fehlt!", options.contains(o));
			}
		}
	}

	/**
	 * Erzeugt eine Liste aus Studienangeboten.
	 * 
	 * @param expecteds
	 * @return
	 */
	private List<Studienangebot> l(Studienangebot... expecteds) {
		return Arrays.asList(expecteds);
	}

	/**
	 * Prüfmethode für Studienangebote.
	 * 
	 * @param studienangebote
	 * @param expecteds
	 */
	private void expectStudienangebote(List<StudienangebotWrapperMitAbstand> studienangebote,
			StudienangebotWrapperMitAbstand... expecteds) {
		List<String> studienangebotIds = new ArrayList<String>();
		for (StudienangebotWrapperMitAbstand studienangeboteWrapper : studienangebote) {
			studienangebotIds.add(studienangeboteWrapper.getStudienangebot().getId());
		}
		assertEquals(expecteds.length, studienangebote.size());
		for (StudienangebotWrapperMitAbstand expected : expecteds) {
			assertTrue(studienangebotIds.contains(expected.getStudienangebot().getId()));
		}
	}

	/**
	 * Erzeugt ein C.
	 * 
	 * @param option
	 * @param anzahl
	 * @return
	 */
	private static C c(FacettenOption option, int anzahl) {
		C c = new C();
		c.option = option;
		c.anzahl = anzahl;
		return c;
	}

	/**
	 * Klasse für Optionen-Anzahlen.
	 */
	private static class C {
		FacettenOption option;
		int anzahl;
	}

	/**
	 * Erzeugt ein Studienangebot.
	 * 
	 * @param id
	 * @param studientyp
	 * @param studienform
	 * @param hochschulart
	 * @return
	 */
	private static Studienangebot sa(int id, StudientypFacettenOption studientyp, StudienformFacettenOption studienform,
			HochschulartFacettenOption hochschulart, FitFuerStudiumFacettenOption... fitFuerStudim) {
		Studienangebot s = new Studienangebot();
		s.setId(String.valueOf(id));
		s.setStudientyp(studientyp);
		s.setStudienform(studienform);
		s.setHochschulart(hochschulart);
		for (FitFuerStudiumFacettenOption option : fitFuerStudim) {
			s.addFitFuerStudiumFacettenOption(option);
		}
		return s;
	}

	private static StudienangebotWrapperMitAbstand saw(Studienangebot angebot) {
		return new StudienangebotWrapperMitAbstand(angebot, null);
	}
}
