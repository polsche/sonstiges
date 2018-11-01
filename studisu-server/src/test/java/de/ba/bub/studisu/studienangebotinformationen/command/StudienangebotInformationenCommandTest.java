package de.ba.bub.studisu.studienangebotinformationen.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.common.model.SuchortAbstand;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.service.StudienangebotsucheService;
import de.ba.bub.studisu.studienangebotinformationen.model.StudienangebotInformationenAnfrage;

public class StudienangebotInformationenCommandTest {
	
	StudienangebotInformationenCommand instance;
	
	@Mock
	BildungsangebotService bildungsangebotServiceClient;
	
	@Mock
	Validator validator;
	
	@Mock
	StudienangebotsucheService studienangebotsucheService;
	
	@Mock 
	ConstraintViolation<StudienangebotInformationenAnfrage> violation;
	
	@Mock
	StudienangebotsucheErgebnis studienangebotsucheErgebnis;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		violation = mock(ConstraintViolation.class);
		validator = mock(Validator.class);
		bildungsangebotServiceClient = mock(BildungsangebotService.class);
		studienangebotsucheService = mock(StudienangebotsucheService.class);
		studienangebotsucheErgebnis = mock(StudienangebotsucheErgebnis.class);
	    instance = new StudienangebotInformationenCommand(bildungsangebotServiceClient, validator, studienangebotsucheService);
	}

	@Test(expected=ValidationException.class)
	public void testPruefeVorbedingungen() {
		try {
			Set<ConstraintViolation<StudienangebotInformationenAnfrage>> violations = new HashSet<>();
			violations.add(violation);
			
			when(validator.validate(any(StudienangebotInformationenAnfrage.class))).thenReturn(violations);

			StudienangebotInformationenAnfrage studienangebotInformationenAnfrage = new StudienangebotInformationenAnfrage(1);
			instance.pruefeVorbedingungen(studienangebotInformationenAnfrage);
		} catch (ValidationException e) {
			assertTrue("message sollte in exception message enthalten sein", e.getMessage().contains("Sucheingaben fehlerhaft"));
			throw e;
		}
	}

	@Test
	public void testGeschaeftslogikAusfuehren() {
		StudienangebotInformationenAnfrage studienangebotInformationenAnfrage = new StudienangebotInformationenAnfrage(1);
		
		StudienangebotInformationen studienangebotInformationen = new StudienangebotInformationen();
		studienangebotInformationen.setBezeichnung("Bachelor of Science");
		when(bildungsangebotServiceClient.holeStudienangebotInformationen(anyInt())).thenReturn(studienangebotInformationen);
		StudienangebotInformationen informationenAntwort = instance.geschaeftslogikAusfuehren(studienangebotInformationenAnfrage);
		assertEquals("Bezeichnung Bachelor of Science wird erwartet", "Bachelor of Science", informationenAntwort.getBezeichnung());
	}

	@Test
	public void testSucheStudienangebote() {
		// antwort vorbereiten
		SuchortAbstand suchortAbstand = new SuchortAbstand("Berlin", 0.0);
		List<SuchortAbstand> abstaende = new ArrayList<>();
		abstaende.add(suchortAbstand);
		
		Studienangebot studienangebot = new Studienangebot();
		studienangebot.setAbstand(0.0);
		studienangebot.setBildungsanbieterName("Technische Universität Berlin");
		studienangebot.setId("8287681");
	
		StudienangebotWrapperMitAbstand studienangebotWrapperMitAbstand = new StudienangebotWrapperMitAbstand(studienangebot, abstaende);

		List<StudienangebotWrapperMitAbstand> studienangebotWrapperMitAbstands = new ArrayList<>();
		studienangebotWrapperMitAbstands.add(studienangebotWrapperMitAbstand);
		
		when(studienangebotsucheService.suche(any(StudienangebotsucheAnfrage.class))).thenReturn(studienangebotsucheErgebnis);
		when(studienangebotsucheErgebnis.getItems()).thenReturn(studienangebotWrapperMitAbstands);
		StudienangebotsucheAnfrage angebotsucheAnfrage = new StudienangebotsucheAnfrage(null, null, null, null, null,
				null, null, null, null, null);

		// anfrage
		List<StudienangebotWrapperMitAbstand> studienangebotWrapperMitAbstandsAntwort = instance.sucheStudienangebote(angebotsucheAnfrage);

		// pruefe angebot
		Studienangebot angebot = studienangebotWrapperMitAbstandsAntwort.get(0).getStudienangebot();
		assertEquals("Id 8287681 wird erwartet", "8287681", angebot.getId());
		assertEquals("angebot sollte übernommen sein", studienangebot, angebot);

		// pruefe abstaende
		SuchortAbstand actualAbstand = studienangebotWrapperMitAbstandsAntwort.get(0).getAbstaende().get(0);
		assertEquals("abstaende sollten übernommen sein", suchortAbstand, actualAbstand);
	}
}