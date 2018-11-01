package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getsignetfuerbildungsanbieter_v_1.Signet;

public class SignetMapperTest {

	List<Signet> signetListe= null;
	Signet signet1 = new Signet();
	Signet signet2 = new Signet();
	private static String CSS_MIME = "CSS";
	private static String TEXT_MIME = "TEXT";

	@Before
	public void setUp() throws Exception {
		
		signetListe = new ArrayList<Signet>();
		
		signet1.setMimetype(CSS_MIME);
		signet1.setDaten(new byte[1233]);
		
		signet2.setMimetype(TEXT_MIME);
		signet2.setDaten(new byte[999]);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMap() {
		de.ba.bub.studisu.common.model.Signet signet = SignetMapper.map(signet1);
		assertNotNull(signet);
		assertEquals(signet.getMimetype(), CSS_MIME);
		assertFalse(signet.getMimetype().equals(TEXT_MIME));
		assertNotNull(signet.getDaten());
	}

}
