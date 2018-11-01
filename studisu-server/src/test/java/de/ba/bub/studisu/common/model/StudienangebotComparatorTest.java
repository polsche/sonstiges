package de.ba.bub.studisu.common.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotComparator;

/**
 * Tests für {@link StudienangebotComparator}.
 * 
 * Prueft, ob der Vergleich korrekt erst nach Abstand (zum naechsten gewaehlten Ort) und dann nach der Veranstaltungs-ID des Studienangebots erfolgt.
 * 
 * @author KSC
 *
 */
public class StudienangebotComparatorTest {

	/**
	 * Der zu testende Comparator fuer Studienangebote.
	 */
	private static final StudienangebotComparator COMPARATOR = new StudienangebotComparator(); 
	
	/**
	 * Testet den Comparator fuer Studienangebote.
	 */
	@Test
	public void test() {
		Studienangebot a = new Studienangebot();
		a.setId("1");
		a.setAbstand(10.0);
		Studienangebot b = new Studienangebot();
		b.setId("2");
		a.setAbstand(10.0);
		Studienangebot c = new Studienangebot();
		a.setId("3");
		a.setAbstand(12.0);
		
		check(a, a, 0);
		check(a, b, -1);
		check(b, a, 1);
		check(a, c, -1);
		check(c, a, 1);
	}

	/**
	 * Methode, die eine Validierung ausführt.
	 * 
	 * @param s1
	 *            Erstes Studienangebot
	 * @param s2
	 *            Zweites Studienangebot
	 * @param expected
	 *            erwartetes Comparator-Ergebnis
	 */
	private void check(Studienangebot s1, Studienangebot s2, int expected) {
		int compareResult = COMPARATOR.compare(s1, s2);
		assertEquals(expected, compareResult);
	}

}
