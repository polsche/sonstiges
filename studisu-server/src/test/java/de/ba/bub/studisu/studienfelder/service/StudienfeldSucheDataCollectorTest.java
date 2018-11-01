package de.ba.bub.studisu.studienfelder.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.studienfelder.model.SystematikWithChilderen;

/**
 * Tests für den {@link StudienfeldSucheDataCollector}.
 * 
 * @author StraubP
 */
@RunWith(MockitoJUnitRunner.class)
public class StudienfeldSucheDataCollectorTest {

	/**
	 * Class under Test.
	 */
	private StudienfeldSucheDataCollector cut;
	
	@Mock
	private DKZService dkzService;
	
	@Before
	public void setUp() throws Exception {
		cut = new StudienfeldSucheDataCollector(dkzService);
	}

	@After
	public void tearDown() throws Exception {
		cut = null;
	}

	/**
	 * Testet auch, dass Systematiken, die den Zustand "gelöscht" haben, aussortiert werden.
	 */
	@Test
	public void testCollectData() {
		
		Systematik x1 = new Systematik(1, "X 1", "X", "bez1", "bez1", SystematikZustand.SYSTEMATIKPOSITION);
		Systematik x2 = new Systematik(2, "X 2", "X", "bez2", "bez2", SystematikZustand.GELOESCHT);
		Systematik x3 = new Systematik(3, "X 3", "X", "bez3", "bez3", SystematikZustand.SYSTEMATIKPOSITION);
		
		Systematik x11 = new Systematik(4, "X 11", "X 1", "bez11", "bez11", SystematikZustand.GELOESCHT);
		Systematik x12 = new Systematik(5, "X 12", "X 1", "bez12", "bez12", SystematikZustand.SYSTEMATIKPOSITION);
		Systematik x13 = new Systematik(6, "X 13", "X 1", "bez13", "bez13", SystematikZustand.SYSTEMATIKPOSITION);
		
		// untersystematiken fuer x2 unnoetig da geloescht
		
		Systematik x31 = new Systematik(10, "X 31", "X 3", "bez31", "bez31", SystematikZustand.SYSTEMATIKPOSITION);
		Systematik x32 = new Systematik(11, "X 32", "X 3", "bez32", "bez32", SystematikZustand.SYSTEMATIKPOSITION);
		Systematik x33 = new Systematik(12, "X 33", "X 3", "bez33", "bez33", SystematikZustand.GELOESCHT);
		
		Mockito.when(dkzService.findStudienfeldgruppeSystematiken("X")).thenReturn(
			Arrays.asList(x1, x2, x3)
		);
		
		Mockito.when(dkzService.findStudienfeldgruppeSystematiken("X 1")).thenReturn(
			Arrays.asList(x11, x12, x13)
		);
		
		// kein mock fuer X 2 sondern unten pruefung, dass nicht aufgerufen
		
		Mockito.when(dkzService.findStudienfeldgruppeSystematiken("X 3")).thenReturn(
			Arrays.asList(x31, x32, x33)
		);
		
		// anfrage
		Collection<SystematikWithChilderen> data = cut.collectData("X");
		
		// pruefungen
		assertEquals("die gelöschte systematik X 2 darf nicht dabei sein", 2, data.size());
		// service nicht mit geloeschter systematik angefragt
		verify(dkzService, never()).findStudienfeldgruppeSystematiken(Mockito.contains("X 2"));
		
		boolean containsX1 = false;
		boolean containsX3 = false;
		for (SystematikWithChilderen systematikWithChilderen : data) {
			Systematik systematik = systematikWithChilderen.getSystematik();
			List<Systematik> children = systematikWithChilderen.getChildren();
			if (systematik.equals(x1)) {
				assertEquals("die gelöschte systematik X 11 darf nicht dabei sein", 2, data.size());
				assertEquals(x12, children.get(0));
				assertEquals(x13, children.get(1));				
				containsX1 = true;
			} else if (systematik.equals(x3)) {
				assertEquals("die gelöschte systematik X 33 darf nicht dabei sein", 2, data.size());
				assertEquals(x31, children.get(0));
				assertEquals(x32, children.get(1));
				containsX3 = true;
			}
		}
		assertTrue(containsX1);
		assertTrue(containsX3);
	}

}
