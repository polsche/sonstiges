package de.ba.bub.studisu.studienangebote.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder.FilterParameter;

/**
 * Tests für {@link FilterParameter}.
 * 
 * @author StraubP
 */
public class FilterParameterTest {

	/**
	 * Test mit null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFilterParameterNull() {
		new FilterParameter(null);
	}

	/**
	 * Test mit Facetten-Werten null.
	 */
	@Test
	public void testFilterParameterAllNullFacetten() {

		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte anfrageOrte = null;
		StudienformFacette studienformFacette = null;
		HochschulartFacette hochschulartFacette = null;
		UmkreisFacette umkreisFacette = null;
		Paging sortingPaging = null;
		StudientypFacette studientypFacette = null;
		RegionenFacette bundeslandFacette = null;
		FitFuerStudiumFacette fitFuerStudiumFacette = null;

		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging,
				studientypFacette, bundeslandFacette);

		FilterParameter filterParameter = new FilterParameter(anfrage);

		List<StudienangebotFacette> filterFacetten = filterParameter.getFilterFacetten();

		// Liefert Facetten in der Reigenfolge Typ, Form, Art mit jeweils allen
		// Optionen
		assertEquals("5 Facetten erwartet", 5, filterFacetten.size());
		StudientypFacette expectedStudientypFacette = new StudientypFacette().withAllOptions();
		assertFacette(expectedStudientypFacette, filterFacetten.get(0));
		StudienformFacette expectedStudienformFacette = new StudienformFacette().withAllOptions();
		assertFacette(expectedStudienformFacette, filterFacetten.get(1));
		HochschulartFacette expectedHochschulartFacette = new HochschulartFacette().withAllOptions();
		assertFacette(expectedHochschulartFacette, filterFacetten.get(2));
	}

	/**
	 * Test mit leeren Facetten.
	 */
	@Test
	public void testFilterParameterEmptyFacetten() {

		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte anfrageOrte = null;
		StudienformFacette studienformFacette = new StudienformFacette();
		HochschulartFacette hochschulartFacette = new HochschulartFacette();
		UmkreisFacette umkreisFacette = null;
		Paging sortingPaging = null;
		StudientypFacette studientypFacette = new StudientypFacette();
		RegionenFacette bundeslandFacette = new RegionenFacette();
		FitFuerStudiumFacette fitFuerStudiumFacette = null;

		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging,
				studientypFacette, bundeslandFacette);



		FilterParameter filterParameter = new FilterParameter(anfrage);

		List<StudienangebotFacette> filterFacetten = filterParameter.getFilterFacetten();

		// Liefert Facetten in der Reigenfolge Typ, Form, Art mit jeweils allen
		// Optionen
		assertEquals("5 Facetten erwartet", 5, filterFacetten.size());
		assertFacette(new StudientypFacette(), filterFacetten.get(0));
		assertFacette(new StudienformFacette(), filterFacetten.get(1));
		assertFacette(new HochschulartFacette(), filterFacetten.get(2));
	}

	/**
	 * Test mit nicht-leeren Facetten-Werten.
	 */
	@Test
	public void testFilterParameterNonEmptyFacetten() {

		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte anfrageOrte = null;
		List<StudienformFacettenOption> opts = new ArrayList<>();
		opts.add(StudienformFacettenOption.BLOCKUNTERRICHT);
		opts.add(StudienformFacettenOption.SONSTIGE);
		StudienformFacette studienformFacette = new StudienformFacette(opts);

		List<HochschulartFacettenOption> options = new ArrayList<>();
		options.add(HochschulartFacettenOption.BERUFSAKADEMIE);
		options.add(HochschulartFacettenOption.EINRICHTUNG_DER_BERUFLICHEN_WEITERBILDUNG);
		HochschulartFacette hochschulartFacette = new HochschulartFacette(options);

		UmkreisFacette umkreisFacette = null;
		Paging sortingPaging = null;

		List<StudientypFacettenOption> options1 = new ArrayList<>();
		options1.add(StudientypFacettenOption.GRUNDSTAENDIG);
		options1.add(StudientypFacettenOption.WEITERFUEHREND);
		StudientypFacette studientypFacette = new StudientypFacette(options1);

		RegionenFacette bundeslandFacette = null;
		FitFuerStudiumFacette fitFuerStudiumFacette = null;

		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging,
				studientypFacette, bundeslandFacette);

		FilterParameter filterParameter = new FilterParameter(anfrage);

		List<StudienangebotFacette> filterFacetten = filterParameter.getFilterFacetten();

		// Liefert Facetten in der Reigenfolge Typ, Form, Art mit jeweils allen
		// Optionen
		assertEquals("5 Facetten erwartet", 5, filterFacetten.size());
		assertFacette(studientypFacette, filterFacetten.get(0));
		assertFacette(studienformFacette, filterFacetten.get(1));
		assertFacette(hochschulartFacette, filterFacetten.get(2));
	}

	/**
	 * Test der getSelectedOptions-Methode.
	 */
	@Test
	public void testFilterParameterGetSelectedOptions1() {

		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte anfrageOrte = null;
		List<StudienformFacettenOption> options = new ArrayList<StudienformFacettenOption>();
		options.add(StudienformFacettenOption.BLOCKUNTERRICHT);
		options.add(StudienformFacettenOption.SONSTIGE);
		StudienformFacette studienformFacette = new StudienformFacette(options);

		List<HochschulartFacettenOption> options1 = new ArrayList<HochschulartFacettenOption>();
		options1.add(HochschulartFacettenOption.BERUFSAKADEMIE);
		options1.add(HochschulartFacettenOption.EINRICHTUNG_DER_BERUFLICHEN_WEITERBILDUNG);
		HochschulartFacette hochschulartFacette = new HochschulartFacette(options1);

		UmkreisFacette umkreisFacette = null;
		Paging sortingPaging = null;

		List<StudientypFacettenOption> options2 = new ArrayList<>();
		options2.add(StudientypFacettenOption.GRUNDSTAENDIG);
		options2.add(StudientypFacettenOption.WEITERFUEHREND);
		StudientypFacette studientypFacette = new StudientypFacette(options2);

		RegionenFacette bundeslandFacette = new RegionenFacette();
		FitFuerStudiumFacette fitFuerStudiumFacette = null;

		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging,
				studientypFacette, bundeslandFacette);

		FilterParameter filterParameter = new FilterParameter(anfrage);

		assetSelectedOptionen(filterParameter.getSelectedOptions(), StudienformFacettenOption.BLOCKUNTERRICHT,
				StudienformFacettenOption.SONSTIGE, HochschulartFacettenOption.BERUFSAKADEMIE,
				HochschulartFacettenOption.EINRICHTUNG_DER_BERUFLICHEN_WEITERBILDUNG,
				StudientypFacettenOption.GRUNDSTAENDIG, StudientypFacettenOption.WEITERFUEHREND);
	}

	/**
	 * Test der getSelectedOptions-Methode.
	 */
	@Test
	public void testFilterParameterGetSelectedOptions2() {

		Studienfelder studienfelder = null;
		Studienfaecher studienfaecher = null;
		AnfrageOrte anfrageOrte = null;
		StudienformFacette studienformFacette = new StudienformFacette();

		HochschulartFacette hochschulartFacette = new HochschulartFacette();

		UmkreisFacette umkreisFacette = null;
		Paging sortingPaging = null;
		StudientypFacette studientypFacette = new StudientypFacette();
		RegionenFacette bundeslandFacette = new RegionenFacette();
		FitFuerStudiumFacette fitFuerStudiumFacette = null;

		StudienangebotsucheAnfrage anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging,
				studientypFacette, bundeslandFacette);

		FilterParameter filterParameter = new FilterParameter(anfrage);

		assetSelectedOptionen(filterParameter.getSelectedOptions());
	}

	/**
	 * Prüf-Methode für SelectedOptions.
	 * 
	 * @param actual
	 * @param expecteds
	 */
	private void assetSelectedOptionen(Set<? extends FacettenOption> actual, FacettenOption... expecteds) {

		Set<FacettenOption> expected = new HashSet<>();
		for (FacettenOption e : expecteds) {
			expected.add(e);
		}

		assertOptions(expected, actual);

	}

	/**
	 * Prüf-Methode für Facetten.
	 * 
	 * @param expected
	 * @param actual
	 */
	private void assertFacette(StudienangebotFacette expected, StudienangebotFacette actual) {
		
		assertEquals(expected.getClass(), actual.getClass());
		assertOptions(expected.getSelectedOptions(), actual.getSelectedOptions());

	}

	/**
	 * Prüf-Methode für FacettenOptions.
	 * 
	 * @param expected
	 * @param actual
	 */
	private void assertOptions(Collection<? extends FacettenOption> expected,
			Collection<? extends FacettenOption> actual) {

		assertEquals(expected.size(), actual.size());
		for (FacettenOption facettenOption : actual) {
			assertEquals(true, expected.contains(facettenOption));
		}
	}
}
