package de.ba.bub.studisu.studienfelder.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.studienfelder.model.SystematikWithChilderen;

/**
 * Tests für {@link SystematikWithChilderen}.
 * 
 * @author StraubP
 */
public class SystematikWithChilderenTest {

	/**
	 * Testdaten.
	 */
	private static final Systematik SYSTEMATIK_1 = new Systematik(1, "X 1", "X", "kurzBezeichnungNeutral1", "bezeichnungNeutral1", SystematikZustand.SYSTEMATIKPOSITION);
	
	/**
	 * Testdaten.
	 */
	private static final Systematik SYSTEMATIK_2 = new Systematik(2, "X 2", "X", "kurzBezeichnungNeutral2", "bezeichnungNeutral2", SystematikZustand.SYSTEMATIKPOSITION);
	
	/**
	 * Testdaten.
	 */
	private static final List<Systematik> CHILDREN = Collections.singletonList(SYSTEMATIK_2);
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullSystematik() {
		new SystematikWithChilderen(null, new ArrayList<Systematik>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullChildren() {
		new SystematikWithChilderen(SYSTEMATIK_1, null);
	}
	
	@Test
	public void testGetters() {
		SystematikWithChilderen systematikWithChilderen = new SystematikWithChilderen(SYSTEMATIK_1, CHILDREN);
		assertEquals(SYSTEMATIK_1, systematikWithChilderen.getSystematik());
		assertEquals(CHILDREN, systematikWithChilderen.getChildren());
	}
}
