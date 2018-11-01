package de.ba.bub.studisu.studienangebote.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.ba.bub.studisu.common.model.facetten.Facette.Auswahl;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder.AuswahlSetter;

/**
 * Tests für den {@link AuswahlSetter}.
 * 
 * @author StraubP
 */
public class AuswahlSetterTest {

	/**
	 * Testet mit null-Parameter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParam() {
		new AuswahlSetter(null);
	}

	/**
	 * Testet den einfachsten Fall.
	 */
	@Test
	public void testWorksWithEmtpyData() {
		Set<FacettenOption> selectedOptions = new HashSet<>();

		AuswahlSetter auswahlSetter = new AuswahlSetter(selectedOptions);

		List<StudienangebotFacette> filterFacetten = new ArrayList<>();
		Map<FacettenOption, Integer> facettenOptionCounts = new HashMap<>();

		auswahlSetter.process(filterFacetten, facettenOptionCounts);
	}

	/**
	 * Testet einen Fall, in dem alles vorkommt.
	 */
	@Test
	public void testNormalCase() {
		Set<FacettenOption> selectedOptions = new HashSet<>();
		selectedOptions.add(StudientypFacettenOption.GRUNDSTAENDIG);
		selectedOptions.add(StudienformFacettenOption.VOLLZEIT);
		selectedOptions.add(StudienformFacettenOption.AUF_ANFRAGE);
		selectedOptions.add(HochschulartFacettenOption.FACHHOCHSCHULE);

		AuswahlSetter auswahlSetter = new AuswahlSetter(selectedOptions);

		StudientypFacette studientypFacette = new StudientypFacette().withAllOptions();
		StudienformFacette studienformFacette = new StudienformFacette().withAllOptions();
		HochschulartFacette hochschulartFacette = new HochschulartFacette().withAllOptions();

		List<StudienangebotFacette> filterFacetten = new ArrayList<>();
		filterFacetten.add(studientypFacette);
		filterFacetten.add(studienformFacette);
		filterFacetten.add(hochschulartFacette);

		Map<FacettenOption, Integer> facettenOptionCounts = new HashMap<>();
		facettenOptionCounts.put(StudientypFacettenOption.GRUNDSTAENDIG, 1);
		facettenOptionCounts.put(StudienformFacettenOption.VOLLZEIT, 2);
		facettenOptionCounts.put(StudienformFacettenOption.TEILZEIT, 3);
		facettenOptionCounts.put(HochschulartFacettenOption.UNIVERSITAET, 5);
		facettenOptionCounts.put(HochschulartFacettenOption.FACHHOCHSCHULE, 0);
		facettenOptionCounts.put(HochschulartFacettenOption.BERUFSAKADEMIE, 0);

		auswahlSetter.process(filterFacetten, facettenOptionCounts);

		List<Auswahl> studientypFacetteAuswahl = studientypFacette.getAuswahl();
		assertEquals(1, studientypFacetteAuswahl.size());
		assertAuswahl(studientypFacetteAuswahl.get(0), StudientypFacettenOption.GRUNDSTAENDIG, 1);

		List<Auswahl> studienformFacetteAuswahl = studienformFacette.getAuswahl();
		assertEquals(3, studienformFacetteAuswahl.size());
		assertAuswahl(studienformFacetteAuswahl.get(0), StudienformFacettenOption.VOLLZEIT, 2);
		assertAuswahl(studienformFacetteAuswahl.get(1), StudienformFacettenOption.TEILZEIT, 3);
		assertAuswahl(studienformFacetteAuswahl.get(2), StudienformFacettenOption.AUF_ANFRAGE, 0); // enthalten,
																									// weil
																									// selektiert

		List<Auswahl> hochschulartFacetteAuswahl = hochschulartFacette.getAuswahl();
		assertEquals(2, hochschulartFacetteAuswahl.size());
		assertAuswahl(hochschulartFacetteAuswahl.get(0), HochschulartFacettenOption.UNIVERSITAET, 5);
		assertAuswahl(hochschulartFacetteAuswahl.get(1), HochschulartFacettenOption.FACHHOCHSCHULE, 0); // enthalten,
																										// weil
																										// selektiert
	}

	/**
	 * Prüfmethode für Auswahl.
	 * 
	 * @param auswahl
	 * @param option
	 * @param anzahl
	 */
	private void assertAuswahl(Auswahl auswahl, FacettenOption option, int anzahl) {
		assertEquals(option, auswahl.getFacettenOption());
		assertEquals(anzahl, auswahl.getTrefferAnzahl().intValue());
	}

}
