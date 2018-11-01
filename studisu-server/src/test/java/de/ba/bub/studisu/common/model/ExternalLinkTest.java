package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import org.junit.Test;

public class ExternalLinkTest {

	
	@Test
	public void testExternalLink() throws URISyntaxException {
		String linkname = "linkname";
		String linkdest = "http://arbeitsagentur.de";
		String tooltip = "tooltip";
		ExternalLink cut = new ExternalLink(linkname, linkdest, tooltip);
		assertEquals(linkname, cut.getName());
		assertEquals(linkdest, cut.getLink().toString());
		assertEquals(tooltip, cut.getTooltip());
		
		String tooltip2 = "tooltip2";
		cut.setTooltip(tooltip2);
		assertEquals(tooltip2, cut.getTooltip());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNullLinkname() throws URISyntaxException {
		String linkname = null;
		String linkdest = "http://arbeitsagentur.de";
		String tooltip = "tooltip";
		new ExternalLink(linkname, linkdest, tooltip);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyLinkname() throws URISyntaxException {
		String linkname = "";
		String linkdest = "http://arbeitsagentur.de";
		String tooltip = "tooltip";
		new ExternalLink(linkname, linkdest, tooltip);
	}

}
