package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Lehramtstyp;

public class LehramtstypMapperTest {

	@Test
	public void testMapAll() {
		LehramtstypMapper mapper = new LehramtstypMapper();
		for (Lehramtstyp value : Lehramtstyp.values()) {
			assertNotNull("Alle Lehramtstypen müssen ein Mapping haben!", mapper.map(value));
		}
	}

	@Test
	public void testMapNull() {
		LehramtstypMapper mapper = new LehramtstypMapper();
		assertNull(mapper.map(null));
	}

	@Test
	public void testMapKnown() {
		LehramtstypMapper mapper = new LehramtstypMapper();
		assertEquals("andere", mapper.map(Lehramtstyp.ANDERE));
		assertEquals("Lehrämter der Grundschule bzw. Primarstufe",
				mapper.map(Lehramtstyp.LEHRAEMTER_DER_GRUNDSCHULE_BZW_PRIMARSTUFE));
		assertEquals("Lehrämter der Sekundarstufe II [allgemeinbildende Fächer] oder für das Gymnasium", mapper
				.map(Lehramtstyp.LEHRAEMTER_DER_SEKUNDARSTUFE_II_ALLGEMEINBILDENDE_FAECHER_ODER_FUER_DAS_GYMNASIUM));
		assertEquals("Lehrämter der Sekundarstufe II [berufliche Fächer] oder für die beruflichen Schulen", mapper
				.map(Lehramtstyp.LEHRAEMTER_DER_SEKUNDARSTUFE_II_BERUFLICHE_FAECHER_ODER_FUER_DIE_BERUFLICHEN_SCHULEN));
		assertEquals("Lehrämter für alle oder einzelne Schularten der Sekundarstufe I",
				mapper.map(Lehramtstyp.LEHRAEMTER_FUER_ALLE_ODER_EINZELNE_SCHULARTEN_DER_SEKUNDARSTUFE_I));
		assertEquals("Sonderpädagogische Lehrämter", mapper.map(Lehramtstyp.SONDERPAEDAGOGISCHE_LEHRAEMTER));
		assertEquals("Übergreifende Lehrämter der Primarstufe und aller oder einzelner Schularten der Sekundarstufe I",
				mapper.map(
						Lehramtstyp.UEBERGREIFENDE_LEHRAEMTER_DER_PRIMARSTUFE_UND_ALLER_ODER_EINZELNER_SCHULARTEN_DER_SEKUNDARSTUFE_I));
	}

}
