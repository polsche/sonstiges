package de.ba.bub.studisu.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAGContextTest {


	@Test
	public void testAlias() {
		String alias = "cl";
		OAGContext oagContext = OAGContext.CONFIDENTIAL;
		String retValue = oagContext.alias();
		assertEquals("gesetzter enum-Wert bei alias zurueckerwartet", alias, retValue);
	}
	

}
