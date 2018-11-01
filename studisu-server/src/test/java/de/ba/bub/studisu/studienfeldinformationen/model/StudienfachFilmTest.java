package de.ba.bub.studisu.studienfeldinformationen.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Tests für {@link StudienfachFilm}.
 * Mapping von JSON auf DTO geprueft
 * 
 * @author KunzmannC
 */
public class StudienfachFilmTest {
	
	private static final String JSON = 
			"{ \"LocalData\": "
				+ "{ "
					+ "\"IsJson\":1, "
					+ "\"METAFLDS\": \"metafields\", "
					+ "\"changedMonikers\": \"changedMonikers\", "
					+ "\"changedSubjects\": \"changedSubjects\", "
					+ "\"dDocName\": \"dDocName\", "
					+ "\"dUser\": \"dUser\", "
					+ "\"idcToken\": \"idcToken\", "
					+ "\"localizedForResponse\": 99, "
					+ "\"refreshMonikers\": \"refreshMonikers\", "
					+ "\"refreshSubMonikers\": \"refreshSubMonikers\", "
					+ "\"refreshSubjects\": \"refreshSubjects\", "
					+ "\"xBenBpBatchBerufeTvID\": 2, "
					+ "\"IdcService\": \"IdcService\""
				+ "} "
			+ "}";
	
	@Test
	public void testJsonMapping() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		StudienfachFilm sfFilm = mapper.readValue(JSON, StudienfachFilm.class);

		LocalData localData = sfFilm.getLocalData();
		assertEquals(1, localData.getIsJson());
		assertEquals("metafields", localData.getMetaflds());
		assertEquals("changedMonikers", localData.getChangedMonikers());
		assertEquals("changedSubjects", localData.getChangedSubjects());
		assertEquals("dDocName", localData.getDDocName());
		assertEquals("dUser", localData.getDUser());
		assertEquals("idcToken", localData.getIdcToken());
		assertEquals(99, localData.getLocalizedForResponse());
		assertEquals("refreshMonikers", localData.getRefreshMonikers());
		assertEquals("refreshSubMonikers", localData.getRefreshSubMonikers());
		assertEquals("refreshSubjects", localData.getRefreshSubjects());
		assertEquals(2, localData.getXBenBpBatchBerufeTvID());
		assertEquals("IdcService", localData.getIdcService());
	}
}