package de.ba.bub.studisu.studienfeldinformationen.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.integration.wcc.ContentClient;
import de.ba.bub.studisu.common.model.StudienInformationDTO;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenAnfrage;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;

/**
 * Tests für {@link StudienfeldInformationenServiceImpl}.
 * 
 * @author StraubP
 */
@RunWith(value = MockitoJUnitRunner.class)
public class StudienfeldInformationenServiceImplTest {

	/**
	 * Class under Test.
	 */
	private StudienfeldInformationenService cut;

	@Mock
	private DKZService dkzService;
	
	@Mock
	private BildungsangebotService bildungsangebotServiceClient;

	@Mock
	private ContentClient contentClient;

	@Before
	public void setUp() throws Exception {
		cut = new StudienfeldInformationenServiceImpl(dkzService, bildungsangebotServiceClient, contentClient);
	}

	@After
	public void tearDown() throws Exception {
		cut = null;
	}

	@Test(expected = RuntimeException.class)
	public void testSucheMitNull() {
		cut.suche(null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ValidationException.class)
	public void testSucheUnbekannteDkzId() {

		Mockito.when(dkzService.findSystematik(Mockito.anyInt(), Mockito.anyListOf(String.class)))
				.thenReturn(new ArrayList<Systematik>());

		StudienfeldInformationenAnfrage anfrage = new StudienfeldInformationenAnfrage(1);
		cut.suche(anfrage);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSucheBekannteDkzIdWccFehler() throws HtmlContentClientException {

		List<Systematik> semantiken = new ArrayList<>();

		Systematik semantik = new Systematik(1, "HA 1234", "HA 12", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
		semantiken.add(semantik);

		List<String> studienfeldbeschreibungen = new ArrayList<String>();
		StudienInformationDTO studienInformationDTO = new StudienInformationDTO();
		studienInformationDTO.setStudienfeldbeschreibungen(studienfeldbeschreibungen);
		
		Mockito.when(dkzService.findSystematik(Mockito.anyInt(), Mockito.anyListOf(String.class))).thenReturn(semantiken);
		
		StudienfeldInformationenAnfrage anfrage = new StudienfeldInformationenAnfrage(1);
		StudienfeldInformationenErgebnis ergebnis = cut.suche(anfrage);

		Assert.assertNotNull("Hier muss immer eine Liste kommen", ergebnis.getStudienfachInformationen());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSucheBekannteDkzIdWccOkay() throws HtmlContentClientException {

		List<Systematik> semantiken = new ArrayList<>();

		Systematik semantik = new Systematik(1, "HA 1234", "HA 12", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION);
		semantiken.add(semantik);

		Mockito.when(dkzService.findSystematik(Mockito.anyInt(), Mockito.anyListOf(String.class))).thenReturn(semantiken);
		List<String> studienfeldbeschreibungen = new ArrayList<String>();
		studienfeldbeschreibungen.add("eins");
		studienfeldbeschreibungen.add("zwei");
		studienfeldbeschreibungen.add("drei");
		
		StudienInformationDTO studienInformationDTO = new StudienInformationDTO();
		studienInformationDTO.setStudienfeldbeschreibungen(studienfeldbeschreibungen);
		
		StudienfeldInformationenAnfrage anfrage = new StudienfeldInformationenAnfrage(1);
		StudienfeldInformationenErgebnis ergebnis = cut.suche(anfrage);

		Assert.assertNotNull("Hier muss immer eine Liste kommen", ergebnis.getStudienfachInformationen());
	}

	/**
	 * Prüft, dass nur Systematiken gesucht werden, die ein HA oder HC
	 * Studienfeld repräsentieren.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSucheHaHc() {

		@SuppressWarnings("rawtypes")
		ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

		try {
			Mockito.when(dkzService.findSystematik(Mockito.anyInt(), captor.capture()))
					.thenReturn(new ArrayList<Systematik>());

			StudienfeldInformationenAnfrage anfrage = new StudienfeldInformationenAnfrage(1);
			cut.suche(anfrage);

		} catch (ValidationException e) {
			// das ist hier egal
		} finally {
			// findSystematik muss eingeschränkt werden auf Studienfelder aus HA
			// und HC!
			List<String> list = captor.getValue();
			String soll = "Der Aufruf zu findSystematik muss eingeschränkt werden auf Studienfelder aus HA und HC!";
			Assert.assertEquals(soll, 2, list.size());
			Assert.assertTrue(soll, list.contains("HA ????"));
			Assert.assertTrue(soll, list.contains("HC ????"));
		}

	}

}
