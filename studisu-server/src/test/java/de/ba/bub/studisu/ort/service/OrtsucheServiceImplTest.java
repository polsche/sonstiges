package de.ba.bub.studisu.ort.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.model.Ort;
import de.ba.bub.studisu.ort.model.OrtsucheAnfrage;
import de.ba.bub.studisu.ort.model.OrtsucheErgebnis;

public class OrtsucheServiceImplTest {
	
	OrtsucheServiceImpl instance;
	
	@Mock
	BildungsangebotService baClient;
	
	@Before
	public void setUp() {
		baClient = mock(BildungsangebotService.class);
	    instance = new OrtsucheServiceImpl(baClient);
	}
	
	@Test
	public void sucheOrte() {
		Ort ort = new Ort("Ansbach", "99900", 49.3006, 10.5714, "Mittelfranken");
		List<Ort> orte = new ArrayList<>();
		orte.add(ort);
		OrtsucheAnfrage anfrage = new OrtsucheAnfrage("Ansbach");
		when(baClient.findOrte(nullable(String.class), any(String.class))).thenReturn(orte);

		OrtsucheErgebnis ergebnis = instance.sucheOrte(anfrage);

		ArrayList<Ort> ortList = (ArrayList<Ort>) ergebnis.getOrte();
		assertEquals("Stadt Ansbach wird erwartet", "Ansbach", ortList.get(0).getName());
		assertEquals("gleicher ort wird erwartet", ort, ortList.get(0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void sucheOrteAnfrageIsNull() {
		try{
			instance.sucheOrte(null);
		} catch(IllegalArgumentException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("anfrage null"));
			throw e;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void sucheOrteSuchStringIsNull() {
		try{
			OrtsucheAnfrage anfrage = new OrtsucheAnfrage(null);
			instance.sucheOrte(anfrage);
		} catch(IllegalArgumentException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("anfrage string empty"));
			throw e;
		}
	}
}
