package de.ba.bub.studisu.studienangebote.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder.FilterResult;

/**
 * Tests für {@link FilterResult}.
 * 
 * @author StraubP
 */
public class FilterResultTest {

	/**
	 * Test mit null-Parameter studienangebote.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParam1() {
		Map<FacettenOption, Integer> facettenOptionCounts = new HashMap<>();

		new FilterResult(null, facettenOptionCounts);
	}

	/**
	 * Test mit null-Parameter facettenOptionCounts.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParam2() {
		List<StudienangebotWrapperMitAbstand> studienangebote = new ArrayList<>();

		new FilterResult(studienangebote, null);
	}

	/**
	 * Test mit gültigen Parametern.
	 */
	@Test
	public void testNonNullParams() {
		List<StudienangebotWrapperMitAbstand> studienangebote = new ArrayList<>();
		Map<FacettenOption, Integer> facettenOptionCounts = new HashMap<>();

		FilterResult filterResult = new FilterResult(studienangebote, facettenOptionCounts);

		assertEquals(studienangebote, filterResult.getStudienangebote());
		assertEquals(facettenOptionCounts, filterResult.getFacettenOptionCounts());
	}
}
