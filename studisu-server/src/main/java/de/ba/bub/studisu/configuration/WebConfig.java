package de.ba.bub.studisu.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Konfigurationsklasse für das Setzen von
 * CORS-Headern, falls ein entsprechendes Property
 * auf true gesetzt ist. Die CORS-Header sollten
 * nur für die unteren Umgebungen (lokal, SPU, STU)
 * gesetzt werden. In den höheren Umgebungen erfolgt
 * das seitens Betrieb durch den OAG.
 * @author OettlJ
 *
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	
	/** Sub-Pfade innerhalb von Controllern */
	public enum Path {
		
		STUDIENFELDER_STUDIENFACH(Constants.STUDIENFELDER_STUDIENFACH);
		
		private String value;
		
		private Path(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		/**
		 * Hilfsklasse für Konstanten zu Sub-Pfaden innerhalb von Controllern
		 * @author OettlJ
		 *
		 */
		public final static class Constants {
			
			private Constants() {}
			
			public final static String STUDIENFELDER_STUDIENFACH = "/bystudienfach";
		}
	}
	
	/**
	 * Request-Mappings von Controllern
	 * 
	 * Neue Request-Mappings müssen in enum ergänzt werden, damit
	 * CORS-Mapping automatisch gesetzt wird, falls für Umgebung aktiv.
	 */
	public enum RequestMapping {
		
		HEALTHCHECK(Constants.HEALTHCHECK),
		ORTE(Constants.ORTE),
		STUDIENANGEBOTE(Constants.STUDIENANGEBOTE),
		STUDIENANGEBOTINFOS(Constants.STUDIENANGEBOTINFOS),
		STUDIENFAECHER(Constants.STUDIENFAECHER),
		STUDIENFELDBESCHREIBUNG(Constants.STUDIENFELDBESCHREIBUNG),
		SIGNET(Constants.SIGNET),
		STUDIENFELDER(Constants.STUDIENFELDER),
		STUDIENFELDER_STUDIENFACH(Constants.STUDIENFELDER_STUDIENFACH),
		STUDIENFELDINFOS(Constants.STUDIENFELDINFOS),
		VERSIONSINFOS(Constants.VERSIONSINFOS);		
		
		private String path;
		
		private RequestMapping(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return this.path;
		}
		
		/**
		 * Hilfsklasse für Konstanten zu Request-Mappings von Controllern
		 * @author OettlJ
		 *
		 */
		public final static class Constants {
			
			private Constants() {}
			
			// Content for public consumption WITH token protection
			public final static String HEALTHCHECK = "/pc/v1/mt/healthcheck_internal";
			public final static String ORTE = "/pc/v1/orte";
			public final static String STUDIENANGEBOTE = "/pc/v1/studienangebote";
			public final static String STUDIENANGEBOTINFOS = "/pc/v1/studienangebotinformationen";
			public final static String STUDIENFAECHER = "/pc/v1/studienfaecher";
			public final static String STUDIENFELDBESCHREIBUNG = "/pc/v1/studienfeldbeschreibung";
			public final static String STUDIENFELDER = "/pc/v1/studienfelder";
			public final static String STUDIENFELDER_STUDIENFACH = STUDIENFELDER + Path.STUDIENFELDER_STUDIENFACH.getValue();
			public final static String STUDIENFELDINFOS = "/pc/v1/studienfeldinformationen";
			public final static String VERSIONSINFOS = "/pc/v1/versionsinformationen";
			
			// Content for public consumption WITHOUT token protection
			public final static String SIGNET = "/ct/v1/signet";
		}
	}

	@Value("${studisu.ui.url}")
	private String studisuUiUrl;
	
	@Value("${studisu.cors.active}")
	private boolean isCorsActive;
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	if (isCorsActive) {
    		for(RequestMapping mapping : RequestMapping.values()) {
    			registry.addMapping(mapping.getPath())
    				.allowedOrigins(studisuUiUrl)
    				.allowCredentials(true);
    		}
    	}
    }
}
