package de.ba.bub.studisu.studienfelder.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.ba.bub.studisu.studienfelder.command.StudienfachToStudienfeldCommand;
import de.ba.bub.studisu.studienfelder.command.StudienfeldSucheCommand;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.model.Studienfeldgruppe;

public class StudienfeldSucheControllerTest {

	StudienfeldSucheController instance;
	
	@Mock
	StudienfeldSucheCommand studienfeldSucheCommand;
	
	@Mock
	StudienfachToStudienfeldCommand studienfachToStudienfeldCommand;
	
	@Mock
	HttpServletResponse response;
	
	@Before
	public void setUp() {
		studienfeldSucheCommand = mock(StudienfeldSucheCommand.class);
		studienfachToStudienfeldCommand = mock(StudienfachToStudienfeldCommand.class);
		response = mock(HttpServletResponse.class);
		
	    instance = new StudienfeldSucheController(studienfeldSucheCommand, studienfachToStudienfeldCommand);
	}
	
	@Test
	public void testSucheStudienfelder() {
		Set<Integer> dkzIds = new HashSet<>();
		dkzIds.add(1);
		Studienfeldgruppe studienfeldgruppe = new Studienfeldgruppe("key", "name");
		studienfeldgruppe.setDkzIds(dkzIds);
		List<Studienfeldgruppe> studienfeldgruppen = new ArrayList<>();
		studienfeldgruppen.add(studienfeldgruppe);
		StudienfeldSucheErgebnis studienfeldSucheErgebnis = new StudienfeldSucheErgebnis(studienfeldgruppen);
		when(studienfeldSucheCommand.execute(any(StudienfeldSucheAnfrage.class))).thenReturn(studienfeldSucheErgebnis);
		ResponseEntity<StudienfeldSucheErgebnis> responseEntity = instance.sucheStudienfelder(response);
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		Studienfeldgruppe studienfeldgruppeResponse = responseEntity.getBody().getStudienfeldgruppen().get(0);
		assertEquals("Key key wird erwartet", "key", studienfeldgruppeResponse.getKey());
		assertEquals("Name name wird erwartet", "name", studienfeldgruppeResponse.getName());
		assertEquals("Dkz Id 1 wird erwartet", 1, studienfeldgruppeResponse.getDkzIds().toArray()[0]);
		
	}
	
	@Test
	public void testGetStudienfeldByStudienfach() {
		int studienfachDKZId = 1;
		Set<Integer> dkzStudienfelderZuStudienfach = new HashSet<>();
		dkzStudienfelderZuStudienfach.add(1);
		when(studienfachToStudienfeldCommand.execute(anyInt())).thenReturn(dkzStudienfelderZuStudienfach);
		ResponseEntity<Set<Integer>> responseEntity = instance.getStudienfeldByStudienfach(studienfachDKZId, response);
		assertEquals("Status Code 200 wird erwartet", HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Dkz Id 1 wird erwartet", 1, responseEntity.getBody().toArray()[0]);
	}
}
