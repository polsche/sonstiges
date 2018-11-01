package de.ba.bub.studisu.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.ba.bub.studisu.common.model.ExternalServiceStatus.Status;

public class ExternalServiceStatusTest {

	@Test
	public void test() {
		
		String serviceName = "serviceName";
		Status serviceStatus = Status.VERFUEGBAR;
		
		ExternalServiceStatus cut = new ExternalServiceStatus(serviceName, serviceStatus);
		
		assertEquals(serviceName, cut.getServiceName());
		assertEquals(serviceStatus, cut.getServiceStatus());
	}

}
