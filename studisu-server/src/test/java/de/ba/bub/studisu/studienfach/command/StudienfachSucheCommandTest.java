package de.ba.bub.studisu.studienfach.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.exception.EingabeZuVieleZeichenException;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienfach.command.StudienfachSucheCommand;
import de.ba.bub.studisu.studienfach.controller.StudienfachController;
import de.ba.bub.studisu.studienfach.model.StudienfachAnfrage;
import de.ba.bub.studisu.studienfach.model.StudienfachErgebnis;

public class StudienfachSucheCommandTest {
	StudienfachSucheCommand instance;
	
	@Mock
	Validator validator;
	
	@Mock
	DKZService dkzService;
	
	@Before
	public void setUp() {
		dkzService = mock(DKZService.class);
	    instance = new StudienfachSucheCommand(dkzService);
	}
	
	@Test(expected=EingabeValidierungException.class)
	public void testPruefeVorbedingungenAnfrageValidationResultInvalid() {
		try {
			Studienfaecher studienfaecher = new Studienfaecher("8287681;8287682;8287683");
			StudienfachAnfrage studienfachAnfrage = new StudienfachAnfrage("Koch", studienfaecher);
			instance.pruefeVorbedingungen(studienfachAnfrage);
		} catch (EingabeValidierungException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains(
					StudienfachController.URL_PARAM_STUDIENFACH_SUCHWORT + "/"
					+ StudienfachController.URL_PARAM_STUDIENFACH_IDS));
			throw e;
		}
	}
	
	@Test(expected=EingabeValidierungException.class)
	public void testPruefeVorbedingungenAnfrageIsNull() {
		try {
			instance.pruefeVorbedingungen(null);
		} catch (EingabeValidierungException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains(
					StudienfachController.URL_PARAM_STUDIENFACH_SUCHWORT + "/"
					+ StudienfachController.URL_PARAM_STUDIENFACH_IDS));
			throw e;
		}
	}
	
	@Test(expected=EingabeZuVieleZeichenException.class)
	public void testPruefeVorbedingungenAnfrageSuchbegriffZuLang() {
		try {
			StudienfachAnfrage studienfachAnfrage = new StudienfachAnfrage(
					"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
					+ "Aenean commodo ligula eget dolor. Aenean massa. "
					+ "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. "
					+ "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.", null);
			instance.pruefeVorbedingungen(studienfachAnfrage);
		} catch (EingabeZuVieleZeichenException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains(
					StudienfachController.URL_PARAM_STUDIENFACH_SUCHWORT_ZU_LANG));
			throw e;
		}
	}
	
	@Test
	public void testGeschaeftslogikAusfuehrenValidSuchbegriff() {
		Studienfach studienfach = new Studienfach(8287681, "suche0");
		List<Studienfach> studienfaecherList = new ArrayList<>();
		studienfaecherList.add(studienfach);
		when(dkzService.findStudienfachBySuchwort(any(String.class))).thenReturn(studienfaecherList);
		StudienfachAnfrage anfrage = new StudienfachAnfrage("Koch", null);
		StudienfachErgebnis studienfachErgebnis = instance.geschaeftslogikAusfuehren(anfrage);
		assertEquals("Id 8287681 wird erwartet", 8287681, studienfachErgebnis.getStudienfaecher().get(0).getDkzId());
		assertEquals("name sollte uebernommen werden", "suche0", studienfachErgebnis.getStudienfaecher().get(0).getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGeschaeftslogikAusfuehrenValidIds() {
		Studienfach studienfach = new Studienfach(8287681, "suche0");
		List<Studienfach> studienfaecherList = new ArrayList<>();
		studienfaecherList.add(studienfach);
		when(dkzService.findStudienfachById(any(List.class))).thenReturn(studienfaecherList);
		Studienfaecher studienfaecher = new Studienfaecher("8287681;8287682;8287683");
		StudienfachAnfrage anfrage = new StudienfachAnfrage(null, studienfaecher);
		StudienfachErgebnis studienfachErgebnis = instance.geschaeftslogikAusfuehren(anfrage);
		assertEquals("Id 8287681 wird erwartet", 8287681, studienfachErgebnis.getStudienfaecher().get(0).getDkzId());
	}

	@Test(expected=IllegalStateException.class)
	public void testGeschaeftslogikAusfuehrenSuchbegriffZuLang() {
		try {
			Studienfach studienfach = new Studienfach(8287681, "suche0");
			List<Studienfach> studienfaecherList = new ArrayList<>();
			studienfaecherList.add(studienfach);
			when(dkzService.findStudienfachBySuchwort(any(String.class))).thenReturn(studienfaecherList);
			StudienfachAnfrage anfrage = new StudienfachAnfrage("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
						+ "Aenean commodo ligula eget dolor. Aenean massa. "
						+ "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. "
						+ "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.", null);
			instance.geschaeftslogikAusfuehren(anfrage);
		} catch(IllegalStateException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("Unknown validation result"));
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateException.class)
	public void testGeschaeftslogikAusfuehrenInvalid() {
		try {
			Studienfach studienfach = new Studienfach(8287681, "suche0");
			List<Studienfach> studienfaecherList = new ArrayList<>();
			studienfaecherList.add(studienfach);
			when(dkzService.findStudienfachById(any(List.class))).thenReturn(studienfaecherList);
			Studienfaecher studienfaecher = new Studienfaecher("8287681;8287682;8287683");
			StudienfachAnfrage anfrage = new StudienfachAnfrage("8287681", studienfaecher);
			instance.geschaeftslogikAusfuehren(anfrage);
		} catch(IllegalStateException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("Unknown validation result"));
			throw e;
		}
	}
}
