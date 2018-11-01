package de.ba.bub.studisu.studienangebote.model.facetten;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik;



public class StudientypFacettenOptionTest {

	
	Systematik VALID_HA_SYSTEMATIK = null;
	Systematik VALID_HC_SYSTEMATIK = null;
	
	Systematik INVALID_HB_SYSTEMATIK = null;
	
	StudientypFacettenOption HA_SFO = null;
	StudientypFacettenOption HC_SFO = null;
	
	StudientypFacettenOption HB_SFO = null;
	List<Systematik> systematikenList = null;
	
	@Before
	public void setUp() throws Exception {
		
		VALID_HA_SYSTEMATIK = new Systematik();
		VALID_HA_SYSTEMATIK.setDkzCodenr("HA_12345");
		
		// Invalide, da eine HB-Systematik nicht existiert (Mai17: nur HA/HC vorhanden und valide)
		INVALID_HB_SYSTEMATIK = new Systematik();
		INVALID_HB_SYSTEMATIK.setDkzCodenr("HB_12345");
		
		VALID_HC_SYSTEMATIK = new Systematik();
		VALID_HC_SYSTEMATIK.setDkzCodenr("HC_12345");
		
		systematikenList = new ArrayList<Systematik>();
	}

	/**
	 * Testet die validen Systematiken HA/HC
	 */
	@Test
	public void testValidSystematiken() {
		List<Systematik> systematikenList = new ArrayList<Systematik>();
		
		systematikenList.add(VALID_HA_SYSTEMATIK);
		HA_SFO = StudientypFacettenOption.forSystematiken(systematikenList);
		assertEquals(StudientypFacettenOption.GRUNDSTAENDIG, HA_SFO);
		
		systematikenList.clear();
		
		systematikenList.add(VALID_HC_SYSTEMATIK);
		HC_SFO = StudientypFacettenOption.forSystematiken(systematikenList);
		assertEquals(StudientypFacettenOption.WEITERFUEHREND, HC_SFO);
		
		systematikenList.clear();
		
		systematikenList.add(VALID_HA_SYSTEMATIK);
		systematikenList.add(VALID_HC_SYSTEMATIK);
		
		HA_SFO = StudientypFacettenOption.forSystematiken(systematikenList);
		assertEquals(StudientypFacettenOption.GRUNDSTAENDIG, HA_SFO);
		
		
	}
	
	@Test
	public void testInvalidSystematiken() {
		systematikenList.clear();
		
		systematikenList.add(INVALID_HB_SYSTEMATIK);
		
		HB_SFO = StudientypFacettenOption.forSystematiken(systematikenList);
		assertEquals(StudientypFacettenOption.SONSTIGE, HB_SFO);
	}

	
}
