package de.ba.bub.studisu.studienfeldbeschreibung.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;
import de.ba.bub.studisu.studienfeldbeschreibung.service.StudienfeldBeschreibungService;

public class StudienfeldBeschreibungCommandTest {
	
	StudienfeldBeschreibungCommand instance;
	
	@Mock
	Validator validator;
	
	@Mock
	StudienfeldBeschreibungService studienfeldBeschreibungService;
	
	@Mock 
	ConstraintViolation<StudienfeldBeschreibungAnfrage> violation;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		violation = mock(ConstraintViolation.class);
		validator = mock(Validator.class);
		studienfeldBeschreibungService = mock(StudienfeldBeschreibungService.class);
	    instance = new StudienfeldBeschreibungCommand(studienfeldBeschreibungService, validator);
	}
	
	@Test(expected=ValidationException.class)
	public void testPruefeVorbedingungen() {
		try {
			Set<ConstraintViolation<StudienfeldBeschreibungAnfrage>> violations = new HashSet<>();
			violations.add(violation);
			
			when(validator.validate(any(StudienfeldBeschreibungAnfrage.class))).thenReturn(violations);

			StudienfeldBeschreibungAnfrage studienfeldBeschreibungAnfrage = new StudienfeldBeschreibungAnfrage(1);
			instance.pruefeVorbedingungen(studienfeldBeschreibungAnfrage);
		} catch (ValidationException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("Sucheingaben fehlerhaft"));
			throw e;
		}
	}
	
	@Test
	public void testGeschaeftslogikAusfuehren() {
		List<String> studienfeldbeschreibungen = new ArrayList<>();
		studienfeldbeschreibungen.add("Beschreibung");
		StudienfeldBeschreibungErgebnis studienfeldBeschreibungErgebnis = new StudienfeldBeschreibungErgebnis();
		studienfeldBeschreibungErgebnis.setStudienfeldbeschreibungen(studienfeldbeschreibungen);
		when(studienfeldBeschreibungService.suche(any(StudienfeldBeschreibungAnfrage.class))).thenReturn(studienfeldBeschreibungErgebnis);
		StudienfeldBeschreibungAnfrage studienfeldBeschreibungAnfrage = new StudienfeldBeschreibungAnfrage(8287681);
		StudienfeldBeschreibungErgebnis studienfeldBeschreibungErgebnisAntwort = instance.geschaeftslogikAusfuehren(studienfeldBeschreibungAnfrage);
		assertEquals("Studienfeldbeschreibung Beschreibung wird erwartet", "Beschreibung", studienfeldBeschreibungErgebnisAntwort.getStudienfeldbeschreibungen().get(0));
	}
}
