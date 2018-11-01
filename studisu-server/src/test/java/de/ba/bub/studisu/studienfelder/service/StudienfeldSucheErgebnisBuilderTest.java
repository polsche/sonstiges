package de.ba.bub.studisu.studienfelder.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.studienfelder.model.Studienfeld;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.model.Studienfeldgruppe;
import de.ba.bub.studisu.studienfelder.model.SystematikWithChilderen;

/**
 * Tests für den {@link StudienfeldSucheErgebnisBuilder}.
 * 
 * @author StraubP
 */
public class StudienfeldSucheErgebnisBuilderTest {

	/**
	 * Class under Test.
	 */
	private StudienfeldSucheErgebnisBuilder cut;

	@Before
	public void setUp() throws Exception {
		cut = new StudienfeldSucheErgebnisBuilder();
	}

	@After
	public void tearDown() throws Exception {
		cut = null;
	}

	@Test
	public void testNoAddition() {
		StudienfeldSucheErgebnis ergebnis = cut.create();
		assertTrue("liste der studienbereiche muss leer sein", ergebnis.getStudienfeldgruppen().isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void testAddFailsOnNull() {
		cut.add(null);
	}

	@Test
	public void testWithEmptyAddition() {
		cut.add(new ArrayList<SystematikWithChilderen>());
		StudienfeldSucheErgebnis ergebnis = cut.create();
		assertTrue("liste der studienbereiche muss leer sein", ergebnis.getStudienfeldgruppen().isEmpty());
	}

	@Test
	public void testWithOneStudienbereichWithNoStudienfelder() {
		List<SystematikWithChilderen> list = new ArrayList<>();
		list.add(create(sy(1, "HA 21", "HA", "bez1")));
		cut.add(list);
		StudienfeldSucheErgebnis ergebnis = cut.create();
		assertEquals("liste muss einen studienbereich enthalten", 1, ergebnis.getStudienfeldgruppen().size());
		assertStudienfeldgruppeValues(ergebnis.getStudienfeldgruppen().get(0), "21", "bez1", 1);
		assertTrue("studienbereich darf keine studienfelder haben",
				ergebnis.getStudienfeldgruppen().get(0).getStudienfelder().isEmpty());
	}

	@Test
	public void testWithOneStudienbereichWithTwoStudienfelder() {
		List<SystematikWithChilderen> list = new ArrayList<>();
		list.add(create(sy(1, "HA 21", "HA", "bez1"), sy(2, "HA 2112", "HA 21", "bez2"),
				sy(3, "HA 2111", "HA 21", "bez3")));
		cut.add(list);
		StudienfeldSucheErgebnis ergebnis = cut.create();
		assertEquals("liste muss einen studienbereich enthalten", 1, ergebnis.getStudienfeldgruppen().size());
		assertStudienfeldgruppeValues(ergebnis.getStudienfeldgruppen().get(0), "21", "bez1", 1);
		List<Studienfeld> studienfelder = ergebnis.getStudienfeldgruppen().get(0).getStudienfelder();
		assertEquals("studienbereich muss 2 studienfelder haben", 2, studienfelder.size());
		// Sortiert nach key!
		assertStudienfeldValues(studienfelder.get(0), "2111", "bez3", 3);
		assertStudienfeldValues(studienfelder.get(1), "2112", "bez2", 2);
	}

	@Test
	public void testWithTwoStudienbereicheWithTwoStudienfelder() {
		List<SystematikWithChilderen> list = new ArrayList<>();
		list.add(create(sy(1, "HA 21", "HA", "bez1"), sy(2, "HA 2112", "HA 21", "bez2"),
				sy(3, "HA 2111", "HA 21", "bez3")));
		list.add(create(sy(4, "HA 20", "HA", "bez4"), sy(5, "HA 2012", "HA 20", "bez5"),
				sy(6, "HA 2011", "HA 20", "bez6")));
		cut.add(list);
		StudienfeldSucheErgebnis ergebnis = cut.create();
		assertEquals("liste muss zwei studienbereiche enthalten", 2, ergebnis.getStudienfeldgruppen().size());
		// Sortiert nach key!
		assertStudienfeldgruppeValues(ergebnis.getStudienfeldgruppen().get(0), "20", "bez4", 4);
		assertStudienfeldgruppeValues(ergebnis.getStudienfeldgruppen().get(1), "21", "bez1", 1);

		List<Studienfeld> studienfelder1 = ergebnis.getStudienfeldgruppen().get(0).getStudienfelder();
		assertEquals("studienbereich muss 2 studienfelder haben", 2, studienfelder1.size());
		// Sortiert nach key!
		assertStudienfeldValues(studienfelder1.get(0), "2011", "bez6", 6);
		assertStudienfeldValues(studienfelder1.get(1), "2012", "bez5", 5);

		List<Studienfeld> studienfelder2 = ergebnis.getStudienfeldgruppen().get(1).getStudienfelder();
		assertEquals("studienbereich muss 2 studienfelder haben", 2, studienfelder2.size());
		// Sortiert nach key!
		assertStudienfeldValues(studienfelder2.get(0), "2111", "bez3", 3);
		assertStudienfeldValues(studienfelder2.get(1), "2112", "bez2", 2);
	}

	@Test
	public void testAllesMoeglich() {
		List<SystematikWithChilderen> listHA = new ArrayList<>();
		listHA.add(create(sy(1, "HA 21", "HA", "bez1a"), sy(2, "HA 2112", "HA 21", "bez2a"),
				sy(3, "HA 2111", "HA 21", "bez3a")));
		listHA.add(create(sy(4, "HA 20", "HA", "bez4a"), sy(5, "HA 2012", "HA 20", "bez5a"),
				sy(6, "HA 2011", "HA 20", "bez6a"), sy(14, "HA 2013", "HA 20", "bez14a")));
		cut.add(listHA);
		List<SystematikWithChilderen> listHC = new ArrayList<>();
		listHC.add(create(sy(7, "HC 21", "HC", "bez1c"), sy(8, "HC 2112", "HC 21", "bez2c"),
				sy(9, "HC 2111", "HC 21", "bez3c")));
		listHC.add(create(sy(10, "HC 20", "HC", "bez4c"), sy(11, "HC 2012", "HC 20", "bez5c"),
				sy(12, "HC 2011", "HC 20", "bez6c"), sy(13, "HC 2010", "HC 20", "bez7c")));
		cut.add(listHC);
		StudienfeldSucheErgebnis ergebnis = cut.create();
		assertEquals("liste muss zwei studienbereiche enthalten", 2, ergebnis.getStudienfeldgruppen().size());
		// Sortiert nach key!
		assertStudienfeldgruppeValues(ergebnis.getStudienfeldgruppen().get(0), "20", "bez4a", 4, 10);
		assertStudienfeldgruppeValues(ergebnis.getStudienfeldgruppen().get(1), "21", "bez1a", 1, 7);

		List<Studienfeld> studienfelder1 = ergebnis.getStudienfeldgruppen().get(0).getStudienfelder();
		assertEquals("studienbereich muss 4 studienfelder haben", 4, studienfelder1.size());
		// Sortiert nach key!
		assertStudienfeldValues(studienfelder1.get(0), "2010", "bez7c", 13);
		assertStudienfeldValues(studienfelder1.get(1), "2011", "bez6a", 6, 12);
		assertStudienfeldValues(studienfelder1.get(2), "2012", "bez5a", 5, 11);
		assertStudienfeldValues(studienfelder1.get(3), "2013", "bez14a", 14);

		List<Studienfeld> studienfelder2 = ergebnis.getStudienfeldgruppen().get(1).getStudienfelder();
		assertEquals("studienbereich muss 2 studienfelder haben", 2, studienfelder2.size());
		// Sortiert nach key!
		assertStudienfeldValues(studienfelder2.get(0), "2111", "bez3a", 3, 9);
		assertStudienfeldValues(studienfelder2.get(1), "2112", "bez2a", 2, 8);
	}

	private void assertStudienfeldgruppeValues(Studienfeldgruppe studienfeldgruppe, String key, String name,
			int... dkzIds) {
		assertEquals(key, studienfeldgruppe.getKey());
		assertEquals(name, studienfeldgruppe.getName());
		assertEquals(dkzIds.length, studienfeldgruppe.getDkzIds().size());
		for (int dkzId : dkzIds) {
			assertTrue(studienfeldgruppe.getDkzIds().contains(dkzId));
		}
	}

	private void assertStudienfeldValues(Studienfeld studienfeld, String key, String name, int... dkzIds) {
		assertEquals(key, studienfeld.getKey());
		assertEquals(name, studienfeld.getName());
		assertEquals(dkzIds.length, studienfeld.getDkzIds().size());
		for (int dkzId : dkzIds) {
			assertTrue(studienfeld.getDkzIds().contains(dkzId));
		}
	}

	private SystematikWithChilderen create(Systematik systematik, Systematik... children) {
		return new SystematikWithChilderen(systematik, Arrays.asList(children));
	}

	private Systematik sy(int id, String codeNr, String oberCodeNr, String bez) {
		return sy(id, codeNr, oberCodeNr, bez, SystematikZustand.SYSTEMATIKPOSITION);
	}

	private Systematik sy(int id, String codeNr, String oberCodeNr, String bez, SystematikZustand zustand) {
		return new Systematik(id, codeNr, oberCodeNr, bez, bez, zustand);
	}

}
