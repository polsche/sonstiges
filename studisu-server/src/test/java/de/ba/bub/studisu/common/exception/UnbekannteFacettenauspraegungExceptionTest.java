package de.ba.bub.studisu.common.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;

/**
 * Test analog zu EingabeValidierungExceptionTest
 * 
 * @author KunzmannC
 */
public class UnbekannteFacettenauspraegungExceptionTest {

	private class MyFacettenOption implements FacettenOption {
		@Override 
		public String getName() {
			return "myfacetteoption";
		}
		@Override 
		public int getId() {
			return 33100;
		}
		@Override 
		public int getDisplayOrder() {
			return 99;
		}
		@Override 
		public String getLabel() {
			return "My f option: ";
		}
		@Override 
		public boolean show() {
			return true;
		}
	}
	
	private String uriParameterWert = "nix_param";

	private String causeMessage = "nix da";
	private Exception cause = new NullPointerException(causeMessage);

	@Test
	public void testUnbekannteFacettenauspraegungExceptionMessage() {
		UnbekannteFacettenauspraegungException instance = new UnbekannteFacettenauspraegungException(MyFacettenOption.class, uriParameterWert, cause);

		assertTrue("statischer message anteil fehlt", instance.getMessage().contains(UnbekannteFacettenauspraegungException.ERRORMESSAGE_START));
		assertTrue("statischer message anteil fehlt", instance.getMessage().contains(UnbekannteFacettenauspraegungException.ERRORMESSAGE_MID));
		assertTrue("statischer message anteil fehlt", instance.getMessage().contains(UnbekannteFacettenauspraegungException.ERRORMESSAGE_END));
		
		assertTrue("dynamischer message anteil fehlt", instance.getMessage().contains(uriParameterWert));
		assertTrue("dynamischer message anteil fehlt", instance.getMessage().contains(MyFacettenOption.class.getSimpleName()));
	}

	@Test
	public void testEingabeZuVieleZeichenExceptionCause() {
		UnbekannteFacettenauspraegungException instance = new UnbekannteFacettenauspraegungException(MyFacettenOption.class, uriParameterWert, cause);

		assertEquals("pruefe cause nach Aufruf Konstruktor mit Parameter und Cause",  cause.getClass().getName(), instance.getCause().getClass().getName());
		assertEquals("pruefe cause.getMessage nach Aufruf Konstruktor mit Meldung und Cause", causeMessage, instance.getCause().getMessage());
	}
}
