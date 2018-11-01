package de.ba.bub.studisu.studienfach.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.ba.bub.studisu.common.model.Studienfach;

/**
 * Testklasse für {@link StudienfachSuchwortComparator}
 * 
 * @author OettlJ
 *
 */
public class StudienfachSuchwortComparatorTest {

	/**
	 * Testmethode für @link {@link StudienfachSuchwortComparator#compare(Studienfach, Studienfach)}
	 */
	@Test
	public void testCompare() {
		Studienfach studfErneuerbareEnergienGrst = new Studienfach(1, "Erneuerbare Energien (grundständig)");
		Studienfach studfErneuerbareEnergienWtf = new Studienfach(2, "Erneuerbare Energien (weiterführend)");
		Studienfach studfDiaetetikGrst = new Studienfach(3, "Diätetik (grundständig)");
		
		StudienfachSuchwortComparator testClass = new StudienfachSuchwortComparator("ern");
		
		// Vergleich 'Erneuerbare Energien (grundständig)' mit 'Erneuerbare Energien (weiterführend)'
		assertTrue(studfErneuerbareEnergienGrst.getName() + " liegt vor " + studfErneuerbareEnergienWtf.getName(), 
				testClass.compare(studfErneuerbareEnergienGrst, studfErneuerbareEnergienWtf) < 0);
		assertTrue(studfErneuerbareEnergienGrst.getName() + " liegt vor " + studfErneuerbareEnergienWtf.getName(), 
				testClass.compare(studfErneuerbareEnergienWtf, studfErneuerbareEnergienGrst) > 0);
		
		// Vergleich 'Erneuerbare Energien (grundständig)' mit 'Diätetik (grundständig)'
		assertTrue(studfErneuerbareEnergienGrst.getName() + " liegt vor " + studfDiaetetikGrst.getName(), 
				testClass.compare(studfErneuerbareEnergienGrst, studfDiaetetikGrst) < 0);
		assertTrue(studfErneuerbareEnergienGrst.getName() + " liegt vor " + studfDiaetetikGrst.getName(), 
				testClass.compare(studfDiaetetikGrst, studfErneuerbareEnergienGrst) > 0);
		
		// Vergleich 'Erneuerbare Energien (weiterführend)' mit 'Diätetik (grundständig)'
		assertTrue(studfErneuerbareEnergienWtf.getName() + " liegt vor " + studfDiaetetikGrst.getName(), 
				testClass.compare(studfErneuerbareEnergienWtf, studfDiaetetikGrst) < 0);
		assertTrue(studfErneuerbareEnergienWtf.getName() + " liegt vor " + studfDiaetetikGrst.getName(), 
				testClass.compare(studfDiaetetikGrst, studfErneuerbareEnergienWtf) > 0);
	}
}
