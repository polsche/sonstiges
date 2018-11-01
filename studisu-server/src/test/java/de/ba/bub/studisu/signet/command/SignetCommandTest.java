package de.ba.bub.studisu.signet.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.signet.controller.SignetController;
import de.ba.bub.studisu.signet.model.SignetAnfrage;
import de.ba.bub.studisu.signet.model.SignetErgebnis;
import de.ba.bub.studisu.signet.model.SignetValidator;

@RunWith(PowerMockRunner.class)
public class SignetCommandTest {

	private BildungsangebotService bildungsangebotService;
	private  SignetCommand signetCommand;
	
	@Mock
	Validator validator;
	
	@Mock
	SignetAnfrage signetAnfrageMock;
	
	
	
	@Before
	public void setUp() {
	    bildungsangebotService = mock(BildungsangebotService.class, Mockito.CALLS_REAL_METHODS);
	    signetCommand = new SignetCommand(bildungsangebotService);	
	}
	
	@Test(expected = EingabeValidierungException.class)
	public void testPruefeVorbedingungenAnfrageIsNull() {
		try {
			signetCommand.pruefeVorbedingungen(null);
		}
		catch (EingabeValidierungException e) {
			assertEquals("falsche message", "Ungültiger Wert für Parameter '" + SignetController.URL_PARAM_SIGNET_BANID +  "'.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected = EingabeValidierungException.class)
	public void testPruefeVorbedingungenAnfrageIsNotNull() {
		try {
			when(signetAnfrageMock.getValidationResult()).thenReturn(SignetValidator.INVALID);
			signetCommand.pruefeVorbedingungen(signetAnfrageMock);
		}
		catch (EingabeValidierungException e) {
			assertEquals("falsche message", "Ungültiger Wert für Parameter '" + SignetController.URL_PARAM_SIGNET_BANID +  "'.", e.getMessage());
			throw e;
		}
	}
	
	/* 
	 * testGeschaeftslogikAusfuehren 
	 * No Exception Expected
	 */ 
	@Test(expected = Test.None.class )
	public void testGeschaeftslogikAusfuehren() {
		try {
			when(signetAnfrageMock.getValidationResult()).thenReturn(SignetValidator.VALID);
			when(signetAnfrageMock.getBanId()).thenReturn(26383);
			SignetErgebnis ergebnis = signetCommand.geschaeftslogikAusfuehren(signetAnfrageMock);
			assertNotNull(ergebnis);
		}
		catch (EingabeValidierungException e) { }
	}
}
