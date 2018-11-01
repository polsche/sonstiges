package de.ba.bub.studisu.common.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ConstraintViolationErrorDataTest {

	ConstraintViolationErrorData instance;
	
	@Mock 
	ConstraintViolation<String> violation;
	
	@Mock
	Path path;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		violation = mock(ConstraintViolation.class);
		path = mock(Path.class);
	}
	
	@Test
	public void testConstructor() {
		Set<ConstraintViolation<String>> violations = new HashSet<>();
		violations.add(violation);
		when(violation.getPropertyPath()).thenReturn(path);
		when(violation.getMessage()).thenReturn("message");
		when(path.toString()).thenReturn("path");
	    instance = new ConstraintViolationErrorData(violations);
	    assertEquals("Version - wird erwartet", 1, instance.getViolations().size());
	    assertTrue("message sollte Violations enthalten sein",instance.getZusatzinfos().contains("Violations:"));
	}
	
	@Test
	public void testConstructorViolationsAreNull() {
		Set<ConstraintViolation<String>> violations = null;
	    instance = new ConstraintViolationErrorData(violations);
	    assertEquals("keine menge angegeben, dann sollte liste auch leer sein", 0, instance.getViolations().size());
	}

	@Test
	public void testConstructorViolationsAreEmpty() {
		Set<ConstraintViolation<String>> violations = new HashSet<>();
	    instance = new ConstraintViolationErrorData(violations);
	    assertEquals("keine violation angegeben, dann sollte liste auch leer sein", 0, instance.getViolations().size());
	}
}
