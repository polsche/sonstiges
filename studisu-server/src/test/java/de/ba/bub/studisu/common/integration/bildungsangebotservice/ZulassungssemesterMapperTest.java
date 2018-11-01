package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Zulassungssemester;

public class ZulassungssemesterMapperTest {

	@Test
	public void testMapAll() {
		ZulassungssemesterMapper mapper = new ZulassungssemesterMapper();
		for (Zulassungssemester value : Zulassungssemester.values()) {
			assertNotNull("Alle Zulassungssemester müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		ZulassungssemesterMapper mapper = new ZulassungssemesterMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		ZulassungssemesterMapper mapper = new ZulassungssemesterMapper();
		assertEquals("Frühjahrssemester", mapper.map(Zulassungssemester.FRUEHJAHRSSEMESTER));
		assertEquals("Frühjahrstrimester", mapper.map(Zulassungssemester.FRUEHJAHRSTRIMESTER));
		assertEquals("Herbstsemester", mapper.map(Zulassungssemester.HERBSTSEMESTER));
		assertEquals("Herbsttrimester", mapper.map(Zulassungssemester.HERBSTTRIMESTER));
		assertEquals("keine Angabe", mapper.map(Zulassungssemester.KEINE_ANGABE));
		assertEquals("Sommersemester", mapper.map(Zulassungssemester.SOMMERSEMESTER));
		assertEquals("Sommertrimester", mapper.map(Zulassungssemester.SOMMERTRIMESTER));
		assertEquals("Wintersemester", mapper.map(Zulassungssemester.WINTERSEMESTER));
		assertEquals("Wintertrimester", mapper.map(Zulassungssemester.WINTERTRIMESTER));
	}

}
