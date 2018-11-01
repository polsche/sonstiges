package de.ba.bub.studisu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;

/**
 * Initialisiert den SpringBoot-Applikationsrahmen für die STUDISU-Application.
 * 
 * Hinweis: Die Cache-Configuration wurde in eine eigene Config-Klasse {@link CachingConfig} ausgelagert.
 */
@SpringBootApplication
@EnableCaching
@PropertySource("classpath:infosysbub-studisu-server.properties")
// Damit Filter HttpMethodsFilter in Spring Boot registriert wird,
// bitte wieder einkommentieren wenn wir auf Container migrieren.
// @ServletComponentScan
public class StudisuApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(StudisuApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		LOG.info("StudisuApplication configure");
		return application.sources(StudisuApplication.class);
	}

	/**
	 * Der Einstiegspunkt für die Applikation.
	 *
	 * Die Applikation kann eine allgemeine {@link Exception} werfen. Da Sonar
	 * dies als kritischen Fehler sieht, wir das aber für den Betrieb von
	 * SprintBoot brauchen, wird es hier mittes "false positive" unterdrückt...
	 *
	 * @param args
	 *            Die an die Applikation übergebenen Parameter.
	 * @throws Exception
	 *             Fehler bei der Verarbeitung
	 */
	public static void main(String[] args) throws Exception {
		LOG.info("StudisuApplication main");
		SpringApplication.run(StudisuApplication.class, args);
	}
}