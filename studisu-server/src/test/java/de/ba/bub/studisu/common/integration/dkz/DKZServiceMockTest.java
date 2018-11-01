package de.ba.bub.studisu.common.integration.dkz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Systematik;

/**
 * Testklasse fuer {@link DKZServiceMock}
 * @author KunzmannC
 *
 */
@RunWith(value = MockitoJUnitRunner.class)
public class DKZServiceMockTest {

	// List<Systematik> findStudienfeldgruppeSystematiken(String obercodenr);
	@Test
	public void testfindStudienfeldgruppeSystematiken() {
		String obercodenr = "HC 21";
		DKZServiceMock instance = new DKZServiceMock();
		List<Systematik> result = instance.findStudienfeldgruppeSystematiken(obercodenr);

		assertNotNull("mock sollte ein nichtleeres ergebnis liefern ", result);
		assertTrue("mock sollte ein nichtleeres ergebnis liefern ", result.size() > 0);
	}

	//@override ExternalServiceStatus getServiceStatus() throws CommonServiceException;
	public void testgetServiceStatus() {
		DKZServiceMock instance = new DKZServiceMock();
		ExternalServiceStatus status = instance.getServiceStatus();
		assertEquals("service mock sollte verfuegbar liefern",
				ExternalServiceStatus.Status.VERFUEGBAR, status.getServiceStatus());
		assertEquals("service mock name falsch",
				"DKZServiceMock", status.getServiceName());
		
	}
	
	// List<Systematik> findSystematik(int dkzId, List<String> codeNummern);
	@Test(expected=IllegalStateException.class)
	public void testfindSystematik() {
		int dkzId = 3626;
		List<String> codeNummern = null;

		DKZServiceMock instance = new DKZServiceMock();
		instance.findSystematik(dkzId, codeNummern);
		// expected exception with message "Mock ist nicht implementiert"
	}

	//List<Systematik> findStudienfaecherFuerStudienfeld(Integer studfId);
	@Test(expected=IllegalStateException.class)
	public void testfindStudienfaecherFuerStudienfeld() {
		Integer studfId = null;
		
		DKZServiceMock instance = new DKZServiceMock();
		instance.findStudienfaecherFuerStudienfeld(studfId);
		// expected exception with message "Mock ist nicht implementiert"
	}
	
	//List<Systematik> findBeschriebeneStudienfaecherFuerStudienfeld(Integer studfId);
	@Test(expected=IllegalStateException.class)
	public void testfindBeschriebeneStudienfaecherFuerStudienfeld() {
		Integer studfId = null;
		
		DKZServiceMock instance = new DKZServiceMock();
		instance.findBeschriebeneStudienfaecherFuerStudienfeld(studfId);
		// expected exception with message "Mock ist nicht implementiert"
	}

	// public List<Studienfach> findStudienfachBySuchwort(String suchbefriff);
	@Test
	public void testfindStudienfachBySuchwort() {
		String suchbefriff = null;
		
		DKZServiceMock instance = new DKZServiceMock();
		try{
			instance.findStudienfachBySuchwort(suchbefriff);
			fail("missing exception");
		} catch (IllegalStateException ise) {
			// expected exception with message "Mock ist nicht implementiert"
			assertEquals("message sollte stimmen", DKZServiceMock.NOT_IMPLEMENTED, ise.getMessage());
		}
	}
	
	// List<Studienfach> findStudienfachById(List<Integer> dkzIds);
	@Test(expected=IllegalStateException.class)
	public void testfindStudienfachById() {
		List<Integer> dkzIds = null;
		
		DKZServiceMock instance = new DKZServiceMock();
		instance.findStudienfachById(dkzIds);
		// expected exception with message "Mock ist nicht implementiert"
	}
}
