package de.ba.bub.studisu.studienfeldinformationen.command;

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
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfachInformationen;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenAnfrage;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;
import de.ba.bub.studisu.studienfeldinformationen.service.StudienfeldInformationenService;

public class StudienfeldInformationenCommandTest {

	StudienfeldInformationenCommand instance;
	
	@Mock
	Validator validator;
	
	@Mock
	StudienfeldInformationenService studienfeldInformationenService;
	
	@Mock 
	ConstraintViolation<StudienfeldInformationenAnfrage> violation;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		violation = mock(ConstraintViolation.class);
		validator = mock(Validator.class);
		studienfeldInformationenService = mock(StudienfeldInformationenService.class);
	    instance = new StudienfeldInformationenCommand(studienfeldInformationenService, validator);
	}
	
	@Test(expected=ValidationException.class)
	public void testPruefeVorbedingungen() {
		try {
			Set<ConstraintViolation<StudienfeldInformationenAnfrage>> violations = new HashSet<>();
			violations.add(violation);
			
			when(validator.validate(any(StudienfeldInformationenAnfrage.class))).thenReturn(violations);

			StudienfeldInformationenAnfrage studienfeldInformationenAnfrage = new StudienfeldInformationenAnfrage(1);
			instance.pruefeVorbedingungen(studienfeldInformationenAnfrage);
		} catch (ValidationException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("Sucheingaben fehlerhaft"));
			throw e;
		}
	}
	
	@Test
	public void testGeschaeftslogikAusfuehren() {
		//aufbau umfangreicher mock
		StudienfachInformationen studienfachInformationen = new StudienfachInformationen();
		studienfachInformationen.setId(8287681);
		studienfachInformationen.setNeutralBezeichnung("neutrale Bezeichnung");
		List<String> studienfachbeschreibungen = new ArrayList<>();
		studienfachbeschreibungen.add("Studienfachbeschreibung1");
		studienfachInformationen.setStudienfachbeschreibungen(studienfachbeschreibungen);
		List<String> studiengangsbezeichnungen = new ArrayList<>();
		studiengangsbezeichnungen.add("Studiengangsbezeichnung1");
		studienfachInformationen.setStudiengangsbezeichnungen(studiengangsbezeichnungen);
		studienfachInformationen.setStudienfachFilmId(777);
		studienfachInformationen.setCount(1);
		List<StudienfachInformationen> studienfachInformationenList = new ArrayList<>();
		studienfachInformationenList.add(studienfachInformationen);
		StudienfeldInformationenErgebnis studienfeldInformationenErgebnis = new StudienfeldInformationenErgebnis();
		studienfeldInformationenErgebnis.setStudienfachInformationen(studienfachInformationenList);
		when(studienfeldInformationenService.suche(any(StudienfeldInformationenAnfrage.class))).thenReturn(studienfeldInformationenErgebnis);
		StudienfeldInformationenAnfrage studienfeldInformationenAnfrage = new StudienfeldInformationenAnfrage(8287681);

		//aufruf
		StudienfeldInformationenErgebnis studienfeldInformationenErgebnisAntwort = instance.geschaeftslogikAusfuehren(studienfeldInformationenAnfrage);
		
		assertEquals("Id 8287681 wird erwartet", 8287681, studienfeldInformationenErgebnisAntwort.getStudienfachInformationen().get(0).getId());
		assertEquals("neutralBezeichnung neutrale Bezeichnung wird erwartet", "neutrale Bezeichnung", studienfeldInformationenErgebnisAntwort.getStudienfachInformationen().get(0).getNeutralBezeichnung());
		assertEquals("Studienfachbeschreibung Studienfachbeschreibung1 wird erwartet", "Studienfachbeschreibung1", studienfeldInformationenErgebnisAntwort.getStudienfachInformationen().get(0).getStudienfachbeschreibungen().get(0));
		assertEquals("Studiengangsbezeichnung Studiengangsbezeichnung1 wird erwartet", "Studiengangsbezeichnung1", studienfeldInformationenErgebnisAntwort.getStudienfachInformationen().get(0).getStudiengangsbezeichnungen().get(0));
		assertEquals("FilmId 777 wird erwartet", 777, studienfeldInformationenErgebnisAntwort.getStudienfachInformationen().get(0).getStudienfachFilmId());
		assertEquals("Count 1 wird erwartet", 1, studienfeldInformationenErgebnisAntwort.getStudienfachInformationen().get(0).getCount());
	}
}
