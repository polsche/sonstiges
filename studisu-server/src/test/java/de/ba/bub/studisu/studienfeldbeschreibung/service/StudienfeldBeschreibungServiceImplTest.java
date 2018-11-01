package de.ba.bub.studisu.studienfeldbeschreibung.service;

import java.util.ArrayList;
import java.util.List;

import de.ba.bub.studisu.common.model.BeschreibungsZustand;
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
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.integration.wcc.ContentClient;
import de.ba.bub.studisu.common.model.StudienInformationDTO;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;

/**
 * Tests für {@link StudienfeldBeschreibungServiceImpl}.
 * 
 * @author StraubP
 */
@RunWith(value = MockitoJUnitRunner.class)
public class StudienfeldBeschreibungServiceImplTest {

	/**
	 * Class under Test.
	 */
	private StudienfeldBeschreibungService cut;

	@Mock
	private DKZService dkzService;

	@Mock
	private ContentClient contentClient;

	@Before
	public void setUp() throws Exception {
		cut = new StudienfeldBeschreibungServiceImpl(dkzService, contentClient);
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

		StudienfeldBeschreibungAnfrage anfrage = new StudienfeldBeschreibungAnfrage(1);
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

		StudienfeldBeschreibungAnfrage anfrage = new StudienfeldBeschreibungAnfrage(1);
		StudienfeldBeschreibungErgebnis ergebnis = cut.suche(anfrage);

		Assert.assertEquals("kurzBezeichnungNeutral", ergebnis.getNeutralKurzBezeichnung());
		Assert.assertNotNull("Hier muss immer eine Liste kommen", ergebnis.getStudienfeldbeschreibungen());
		Assert.assertTrue(ergebnis.getStudienfeldbeschreibungen().isEmpty());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSucheBekannteDkzIdWccOkay() throws HtmlContentClientException {

		List<Systematik> semantiken = new ArrayList<>();

		Systematik semantik = new Systematik(1, "HA 1234", "HA 12", "kurzBezeichnungNeutral", "bezeichnungNeutral",
				SystematikZustand.SYSTEMATIKPOSITION, BeschreibungsZustand.BESCHRIEBEN);
		semantiken.add(semantik);

		Mockito.when(dkzService.findSystematik(Mockito.anyInt(), Mockito.anyListOf(String.class))).thenReturn(semantiken);
		List<String> studienfeldbeschreibungen = new ArrayList<String>();
		studienfeldbeschreibungen.add("eins");
		studienfeldbeschreibungen.add("zwei");
		studienfeldbeschreibungen.add("drei");

		StudienInformationDTO studienInformationDTO = new StudienInformationDTO();
		studienInformationDTO.setStudienfeldbeschreibungen(studienfeldbeschreibungen);

		Mockito.when(contentClient.getStudienfeldInformationen(Mockito.anyInt())).thenReturn(studienInformationDTO);

		StudienfeldBeschreibungAnfrage anfrage = new StudienfeldBeschreibungAnfrage(1);
		StudienfeldBeschreibungErgebnis ergebnis = cut.suche(anfrage);

		Assert.assertEquals("kurzBezeichnungNeutral", ergebnis.getNeutralKurzBezeichnung());
		Assert.assertNotNull("Hier muss immer eine Liste kommen", ergebnis.getStudienfeldbeschreibungen());
		Assert.assertArrayEquals(new String[] { "eins", "zwei", "drei" },
				ergebnis.getStudienfeldbeschreibungen().toArray());
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

			StudienfeldBeschreibungAnfrage anfrage = new StudienfeldBeschreibungAnfrage(1);
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
