package de.ba.bub.studisu.versionsinformationen.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;
import de.ba.bub.studisu.versionsinformationen.service.VersionsInformationenService;

/**
 * Testklasse für {@link VersionsInformationenCommand}
 * @author OettlJ
 *
 */
public class VersionsInformationenCommandTest {

	@Test
	public void testConstructorSuccess() {
		VersionsInformationenService versionsInfoService = Mockito.mock(VersionsInformationenService.class);
		new VersionsInformationenCommand(versionsInfoService);
	}
	
	@Test
	public void testSuccess() {
		VersionsInformationenService versionsInfoService = Mockito.mock(VersionsInformationenService.class);
		VersionsInformationenAnfrage anfrage = new VersionsInformationenAnfrage();
		VersionsInformationen result = new VersionsInformationen();
		result.setDatenstand("28.06.2017");
		result.setVersion("1.40");
		Mockito.when(versionsInfoService.getVersionsInfo(anfrage)).thenReturn(result);
		VersionsInformationenCommand command = new VersionsInformationenCommand(versionsInfoService);

		VersionsInformationen ergebnis = command.execute(anfrage);
		
		assertEquals(result, ergebnis);
	}
}
