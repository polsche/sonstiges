package de.ba.bub.studisu.studienfelder.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyCollectionOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.SystematikConstants;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.studienfelder.model.Studienfeld;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.model.Studienfeldgruppe;
import de.ba.bub.studisu.studienfelder.model.SystematikWithChilderen;

/**
 * Tests für den {@link StudienfeldSucheServiceImpl}.
 * 
 * @author StraubP
 */
@RunWith(MockitoJUnitRunner.class)
public class StudienfeldSucheServiceImplTest {

	@Mock
	private StudienfeldSucheDataCollector dataCollector;

	@Mock
	private StudienfeldSucheErgebnisBuilder builder;
	
	@Mock
	private DKZService dkzService;

	/**
	 * Class under Test.
	 */
	private StudienfeldSucheService cut;
	
	@Mock
	private StudienfeldSucheService mockedInstance;

	@Before
	public void setUp() throws Exception {
		cut = new StudienfeldSucheServiceImpl(dkzService, dataCollector) {
			@Override
			StudienfeldSucheErgebnisBuilder createBuilder() {
				return builder;
			}
		};
	}

	@After
	public void tearDown() throws Exception {
		cut = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSucheWithNullParam() {
		cut.suche(null);
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@Test
	public void testSuche() {

		Collection<SystematikWithChilderen> ha = createList();
		Collection<SystematikWithChilderen> hc = createList();
		StudienfeldSucheErgebnis ergebnis = new StudienfeldSucheErgebnis(new ArrayList<Studienfeldgruppe>());

		when(dataCollector.collectData(SystematikConstants.CODENR_STUDIUM_GRUNDSTAENDIG)).thenReturn(ha);
		when(dataCollector.collectData(SystematikConstants.CODENR_STUDIUM_WEITERFUEHREND)).thenReturn(hc);
		when(builder.add(anyCollectionOf(SystematikWithChilderen.class))).thenReturn(builder);
		when(builder.create()).thenReturn(ergebnis);

		StudienfeldSucheErgebnis sucheErgebnis = cut.suche(new StudienfeldSucheAnfrage());

		InOrder inOrder = Mockito.inOrder(builder);

		// HA muss vor HC hinzugefügt werden, damit die Namen von HA verwendet
		// werden!
		ArgumentCaptor<Collection> argument = ArgumentCaptor.forClass(Collection.class);
		inOrder.verify(builder, Mockito.calls(1)).add(argument.capture());
		assertEquals(ha, argument.getValue());
		inOrder.verify(builder, Mockito.calls(1)).add(argument.capture());
		assertEquals(hc, argument.getValue());
		
		assertEquals("Methode muss die Ausgabe des builders zurückgeben!", ergebnis, sucheErgebnis);
	}

	private Collection<SystematikWithChilderen> createList() {
		List<SystematikWithChilderen> list = new ArrayList<>();
		list.add(new SystematikWithChilderen(new Systematik(0, "HA 09", "HA", "kurzBezeichnungNeutral",
				"bezeichnungNeutral", SystematikZustand.SYSTEMATIKPOSITION), new ArrayList<Systematik>()));
		return list;
	}
	

	@SuppressWarnings("deprecation")
	@Test
	public void testGetStudienfachToStudienfeldMap() {
		int studienfachDKZId = 0;
		int studienfeldDKZIdA = 1;
		int studienfeldDKZIdB = 2;
			
		// Rueckgabeobjekt mit erwarteten Werten
		Map<Integer, Set<Integer>> expectedReturn = new HashMap<>();
		Set<Integer> expectedStudienfelder = new HashSet<>();
		expectedStudienfelder.add(studienfeldDKZIdA);
		expectedStudienfelder.add(studienfeldDKZIdB);
		expectedReturn.put(studienfachDKZId, expectedStudienfelder);
				
		ArrayList<SystematikWithChilderen> emptyList = new ArrayList<>();
		when(dataCollector.collectData(SystematikConstants.CODENR_STUDIUM_GRUNDSTAENDIG)).thenReturn(emptyList);
		when(dataCollector.collectData(SystematikConstants.CODENR_STUDIUM_WEITERFUEHREND)).thenReturn(emptyList);
		when(builder.add(Mockito.anyCollectionOf(SystematikWithChilderen.class))).thenReturn(builder);
		
		List<Studienfeldgruppe> studienfeldgruppen = new ArrayList<Studienfeldgruppe>();
		Studienfeldgruppe studienfeldgruppe = new Studienfeldgruppe("GRP 1", "Testgruppe");
		Studienfeld studienfeld = new Studienfeld("STU 1", "Studisu-Studienfeld");

		Set<Integer> feldDkzIds = new HashSet<>(studienfeld.getDkzIds());
		feldDkzIds.add(studienfeldDKZIdA);
		feldDkzIds.add(studienfeldDKZIdB);
		studienfeld.setDkzIds(feldDkzIds);

		List<Studienfeld> gruppenSFelder = new ArrayList<>(studienfeldgruppe.getStudienfelder());
		gruppenSFelder.add(studienfeld);
		studienfeldgruppe.setStudienfelder(gruppenSFelder);

		studienfeldgruppen.add(studienfeldgruppe);
		StudienfeldSucheErgebnis ergebnis = new StudienfeldSucheErgebnis(studienfeldgruppen);
		
		when(builder.create()).thenReturn(ergebnis);
		
		List<Systematik> beschriebeneStudienfaecherFuerStudienfeld = new ArrayList<Systematik>();
		beschriebeneStudienfaecherFuerStudienfeld.add(new Systematik(0, "HH 1-2", "HH", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION));
		
		when(dkzService.findBeschriebeneStudienfaecherFuerStudienfeld(studienfeldDKZIdA)).thenReturn(beschriebeneStudienfaecherFuerStudienfeld);
		when(dkzService.findBeschriebeneStudienfaecherFuerStudienfeld(studienfeldDKZIdB)).thenReturn(beschriebeneStudienfaecherFuerStudienfeld);
		
		Map<Integer, Set<Integer>> result = cut.getStudienfachToStudienfeldMap();
		int mapSize = result.size();
		assertTrue("pruefe Map-size: " + mapSize, mapSize == expectedReturn.size());
		int mapKey = result.keySet().iterator().next();
		assertTrue("pruefe Map-Key: " + mapKey, mapKey == expectedReturn.keySet().iterator().next());
		int valuesSize = result.values().size();
		assertTrue("pruefe Map-Values-size: " + valuesSize, valuesSize  == expectedReturn.size());
		Iterator<Integer> iteratorResult = result.values().iterator().next().iterator();
		Iterator<Integer> iteratorExpected = expectedReturn.values().iterator().next().iterator();
		int erstesElement = iteratorResult.next();
		int zweitesElement = iteratorResult.next();
		assertTrue("pruefe Map-Values-erstes Element: " + erstesElement, erstesElement == iteratorExpected.next());
		assertTrue("pruefe Map-Values-zweites Element: " + zweitesElement, zweitesElement == iteratorExpected.next());
	}
	
	
}
