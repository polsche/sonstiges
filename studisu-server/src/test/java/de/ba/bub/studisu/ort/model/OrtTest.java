package de.ba.bub.studisu.ort.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.ba.bub.studisu.common.model.Ort;

/**
 * Tests für {@link Systematik}.
 * 
 * @author KunzmannC
 */
public class OrtTest {

	String name, plz, bundesland;
	Double breitengrad, laengengrad;
	Ort pb, xanten, berlin; 
	
	@Before
	public void setUp() throws Exception {
		name        = "Paderborn";
		plz         = "33100";
		breitengrad = 50.0;
		laengengrad = 18.0;
		bundesland  = "NRW";
		
		pb     = new Ort(name, plz, breitengrad, laengengrad, bundesland);
		berlin = new Ort("Berlin", "12487", breitengrad, laengengrad, bundesland);
		xanten = new Ort("Xanten,", "46509", breitengrad, laengengrad, bundesland);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testCompareToNull() {
		//"compare to null should throw exception"
		pb.compareTo(null);
	}
	
	@Test
	public void testCompareToName() {
		assertEquals("should be sorted alphabetically", true, pb.compareTo(berlin) > 0);
		assertEquals("should be sorted alphabetically", true, pb.compareTo(xanten) < 0);
	}
	
	@Test
	public void testCompareToPlz() {
		Ort suedberlin = new Ort("Berlin", "12000", breitengrad, laengengrad, bundesland);
		Ort nordberlin = new Ort("Berlin", "12999", breitengrad, laengengrad, bundesland);
		
		assertEquals("same name with different plz should be on same position", 0, berlin.compareTo(nordberlin));
		assertEquals("same name with different plz should be on same position", 0, berlin.compareTo(suedberlin));
	}

	@Test
	public void testCompareToEmptyFields() {
		Ort bielefeld = new Ort(null, plz, breitengrad, laengengrad, bundesland);
		assertEquals("empty name with same plz should be on same position", 0, pb.compareTo(bielefeld));
		assertEquals("empty name with same plz should be on same position", 0, bielefeld.compareTo(pb));
		
		Ort ragnaroek = new Ort("Ragnaroek", null, breitengrad, laengengrad, bundesland);
		assertEquals("different name with missing plz should be sorted alphabetically", true, pb.compareTo(ragnaroek) < 0);
		assertEquals("different name with missing plz should be sorted alphabetically", true, ragnaroek.compareTo(pb) > 0);
		
		Ort neverland = new Ort(null, null, breitengrad, laengengrad, bundesland);
		assertEquals(-1, pb.compareTo(neverland));
	}

	@Test
	public void testCompareToEqual() {
		assertEquals("comparison with identity should be 0", 0, pb.compareTo(pb));
	}

	@Test
	public void testCompareEquals() {
		assertEquals("comparison with identity should be true", true, pb.equals(pb));
		Ort copiedPB = new Ort(name, plz, breitengrad, laengengrad, bundesland);
		assertEquals("comparison with copy should be true", true, pb.equals(copiedPB));
		
		assertEquals("comparison with different stuff should be false", false, pb.equals("Baum"));
		assertEquals("comparison with null should be false", false, pb.equals(null));
	}

	@Test
	public void testToString() {
		String pbString = pb.toString();
		assertEquals("tostring should contain name, separator and plz", true, pbString.contains(name) && pbString.contains(plz));
	}
	
	@Test
	public void testhashCode() {
		Ort copiedPB = new Ort(name, plz, breitengrad, laengengrad, bundesland);
		assertEquals("hash on copy should be the same", pb.hashCode(), copiedPB.hashCode());
		
		List<Ort> ortsliste = new ArrayList<>();
		ortsliste.add(pb);
		assertEquals("copy should be found in list by implementing equals", true, ortsliste.contains(copiedPB));

		Ort bielefeld = new Ort(null, plz, breitengrad, laengengrad, bundesland);
		assertEquals("hashcode of empty named city should be 0", 0, bielefeld.hashCode());
	}

	@Test
	public void testGetters() {
		assertEquals("getter should deliver what the constructor had put in ", name, pb.getName());
		assertEquals("getter should deliver what the constructor had put in ", plz, pb.getPostleitzahl());
		assertEquals("getter should deliver what the constructor had put in ", breitengrad, pb.getBreitengrad());
		assertEquals("getter should deliver what the constructor had put in ", laengengrad, pb.getLaengengrad());
		assertEquals("getter should deliver what the constructor had put in ", bundesland, pb.getBundesland());
	}	
}
