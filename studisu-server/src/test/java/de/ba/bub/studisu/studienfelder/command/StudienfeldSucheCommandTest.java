package de.ba.bub.studisu.studienfelder.command;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.mockito.Mockito;

import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.model.Studienfeldgruppe;
import de.ba.bub.studisu.studienfelder.service.StudienfeldSucheService;

/**
 * Tests für das {@link StudienfeldSucheCommand}.
 * 
 * @author StraubP
 */
public class StudienfeldSucheCommandTest {

	@Test
	public void testConstructorSuccess() {
		StudienfeldSucheService studienfelderService = Mockito.mock(StudienfeldSucheService.class);
		Validator validator = Mockito.mock(Validator.class);
		new StudienfeldSucheCommand(studienfelderService, validator);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=ValidationException.class)
	public void testValidationFails() {
		StudienfeldSucheService studienfelderService = Mockito.mock(StudienfeldSucheService.class);
		Validator validator = Mockito.mock(Validator.class);
		StudienfeldSucheAnfrage anfrage = new StudienfeldSucheAnfrage();
		Set<ConstraintViolation<StudienfeldSucheAnfrage>> valResult = new HashSet<>();
		valResult.add(Mockito.mock(ConstraintViolation.class));
		Mockito.when(validator.validate(anfrage)).thenReturn(valResult);
		StudienfeldSucheCommand command = new StudienfeldSucheCommand(studienfelderService, validator);
		command.execute(anfrage);
	}
	
	@Test
	public void testSuccess() {
		StudienfeldSucheService studienfelderService = Mockito.mock(StudienfeldSucheService.class);
		Validator validator = Mockito.mock(Validator.class);
		StudienfeldSucheAnfrage anfrage = new StudienfeldSucheAnfrage();
		List<Studienfeldgruppe> obergruppen = new ArrayList<Studienfeldgruppe>();
		StudienfeldSucheErgebnis result = new StudienfeldSucheErgebnis(obergruppen);
		Mockito.when(studienfelderService.suche(anfrage)).thenReturn(result);
		StudienfeldSucheCommand command = new StudienfeldSucheCommand(studienfelderService, validator);
		StudienfeldSucheErgebnis ergebnis = command.execute(anfrage);
		assertEquals(result, ergebnis);
	}

}
