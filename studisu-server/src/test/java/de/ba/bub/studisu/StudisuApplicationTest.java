package de.ba.bub.studisu;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * struggled with testMain verification as PowerMockito.verifyStatic seems to change from 1.6.4 to version 2
 * 
 * PowerMockIgnore("javax.management.*") is there to get rid of logs like
 * java.lang.LinkageError: loader constraint violation
 * ERROR StatusLogger Unable to unregister MBeans: java.lang.LinkageError: javax/management/MBeanServer
 * 
 * @author KunzmannC
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringApplication.class, LoggerFactory.class})
@PowerMockIgnore("javax.management.*")
public class StudisuApplicationTest {

	@Mock
	private static Logger logger;

	@BeforeClass
	public static void init() {
		// logging mock vereinfacht ohne before und after, weil hier nur ein test
		// mockstatic vor den zugriffen aus unittests, um logger zu ersetzen
	    mockStatic(LoggerFactory.class);
	    logger = mock(Logger.class);
	    when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);
	}

	@Test
	public void testConfigureSpringApplicationBuilder() {
		PowerMockito.mockStatic(SpringApplication.class);
		SpringApplicationBuilder springAppBuilderMock = Mockito.mock(SpringApplicationBuilder.class);
		StudisuApplication instance = Mockito.mock(StudisuApplication.class);
		Mockito.doCallRealMethod().when(instance).configure(springAppBuilderMock);
		Mockito.when(springAppBuilderMock.sources(StudisuApplication.class)).thenReturn(springAppBuilderMock);
		instance.configure(springAppBuilderMock);
		Mockito.verify(springAppBuilderMock).sources(StudisuApplication.class);
	}

	@Test
	public void testMain() throws Exception {
		PowerMockito.mockStatic(SpringApplication.class);
		String[] args = {"A", "B"};
		StudisuApplication.main(args);
		verify(logger, Mockito.times(1)).info(Mockito.eq("StudisuApplication main"));
	}
}
