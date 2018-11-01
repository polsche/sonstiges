package de.ba.bub.studisu.studienfelder.command;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfelder.service.StudienfeldSucheService;

public class StudienfachToStudienfeldCommandTest {
	
	@Mock
	private StudienfeldSucheService studienfeldSucheService;
	@Mock
	private Validator validator;
		
	private StudienfachToStudienfeldCommand instance;


	@Before
	public void setUp() throws Exception {
		studienfeldSucheService = Mockito.mock(StudienfeldSucheService.class);
		validator = Mockito.mock(Validator.class);
		instance = new StudienfachToStudienfeldCommand(studienfeldSucheService, validator);
	}

	@After
	public void tearDown() throws Exception {
		instance = null;
	}

	@Test
	public void testConstructorSuccess() {
		new StudienfachToStudienfeldCommand(studienfeldSucheService, validator);
	}

	@Test
	public void testGeschaeftslogikAusfuehrenInteger() {
		int studienfachDKZId = 0;
		int studienfeldDKZIdA = 1;
		int studienfeldDKZIdB = 2;
		Set<Integer> expectedReturn = new HashSet<>();
		expectedReturn.add(studienfeldDKZIdA);
		expectedReturn.add(studienfeldDKZIdB);
		
		Map<Integer, Set<Integer>> studienfelderZuStudienfach = new HashMap<>();
		Set<Integer> studienfelder = new HashSet<>();
		studienfelder.add(studienfeldDKZIdA);
		studienfelder.add(studienfeldDKZIdB);
		studienfelderZuStudienfach.put(studienfachDKZId, studienfelder);
		Mockito.when(studienfeldSucheService.getStudienfachToStudienfeldMap()).thenReturn(studienfelderZuStudienfach);
		
		Set<Integer> returnObject = instance.geschaeftslogikAusfuehren(studienfachDKZId);
		assertTrue("pruefe erwartete Elementezahl im Ergebnisset", returnObject.size() == expectedReturn.size());
		Integer[] integerArray = new Integer[returnObject.size()];
		Integer[] expectedReturnArray = expectedReturn.toArray(integerArray);
		Integer[] returnArray = returnObject.toArray(integerArray);
		assertTrue("pruefe erstes Element im Ergebnisset", returnArray[0] == expectedReturnArray[0]);
		assertTrue("pruefe zweites Element im Ergebnisset", returnArray[1] == expectedReturnArray[1]);
	}

	@Test
	public void testPruefeVorbedingungenIntegerOK() {
		int studienfachDKZId = 0;
		Set<ConstraintViolation<Integer>> valResult = new HashSet<>();
		Mockito.when(validator.validate(studienfachDKZId)).thenReturn(valResult);
		instance.pruefeVorbedingungen(studienfachDKZId);
		// keine ValidationException aufgetreten
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=ValidationException.class)
	public void testPruefeVorbedingungenIntegerFehler() {
		int studienfachDKZId = 0;
		Set<ConstraintViolation<Integer>> valResult = new HashSet<>();
		valResult.add(Mockito.mock(ConstraintViolation.class));
		Mockito.when(validator.validate(studienfachDKZId)).thenReturn(valResult);
		try {
			instance.pruefeVorbedingungen(studienfachDKZId);
		} catch (ValidationException ex) {
			assertEquals("pruefe Fehlemeldung aus Validierung", ex.getMessage(), 
					StudienfachToStudienfeldCommand.VALIDATIONMESSAGE_DKZID_INVALID);
			throw ex;
		}
	}

}
