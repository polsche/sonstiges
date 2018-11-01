package de.ba.bub.studisu.monitoring.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ba.bub.studisu.configuration.WebConfig;

/**
 * uebernommen von de.ba.ben.common.healthcheck.servlet
 * 
 * Controller für internen Healthcheck Rendered plain html mit Laufzeit-Informationen zu Server und Anwendung
 *
 * Wenn die Klasse hier und da mal unleserlich wird, oder die ganzen Konstanten zuviel erscheinen, der Dank gilt
 * Sonar...
 * 
 * @author RHA CKU
 */
@RestController
@RequestMapping(WebConfig.RequestMapping.Constants.HEALTHCHECK)
public class HealthcheckInternalController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthcheckInternalController.class);

	private static final String STRIPED_BOOTSTRAP_CLASSES = "table table-striped table-bordered";
	private static final String CSS_FULL_WIDTH = "width:100%";
	private static final String NAME = "STUDISU";
	private static final String EXCEPTION_MESSAGE = "Exception: ";
	private static final String HR_SEPARATOR = "<hr style=\"margin: 8px 0; border: 2px solid #337ab7;\">";

	private String applicationName;
	private StringBuilder builder;

	@Autowired
	// inject the cache manager
	private CacheManager cacheManager;

	/**
	 * REST endpoint für GET liefert das UI (html)
	 * 
	 * @return html repraesentation des internen healthcheck
	 * @throws IOException
	 *             moeglicher Fehler z.B. beim Einlesen der properties
	 */
	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<?> doGet() throws IOException {
		builder = new StringBuilder();
		BodyBuilder bodybuilder = ResponseEntity.status(200);

		// suppress client caching by http headers
		HttpHeaders headers = new HttpHeaders();
		// Set standard HTTP/1.1 no-cache headers.
		headers.add("Cache-Control", "private, no-store, no-cache, must-revalidate");
		// Set standard HTTP/1.0 no-cache header.
		headers.add("Pragma", "no-cache");
		bodybuilder.headers(headers);

		// String contextPath = request.getContextPath();
		String contextPath = "/studisu";
		applicationName = findApplicationName(contextPath);

		builder.append(HTMLUtil.constructOpeningTag("html", null, null));
		builder.append(HTMLUtil.constructBootstrapHeader());
		builder.append(HTMLUtil.constructOpeningTag("body", null, null));
		builder.append(
				HTMLUtil.constructElement("h1", "Internal Healthcheck " + String.valueOf(applicationName), null, null));

		Map<String, String> accordionMap = new HashMap<String, String>();

		String[] columnHeader = new String[] { "Key", "Value" };
		accordionMap.put(applicationName + " Properties", HTMLUtil.constructTable(Arrays.asList(columnHeader),
				readProperties(applicationName), STRIPED_BOOTSTRAP_CLASSES, CSS_FULL_WIDTH));

		accordionMap.put(applicationName + "Feature Properties", HTMLUtil.constructTable(Arrays.asList(columnHeader),
				readFeatureProperties(applicationName), STRIPED_BOOTSTRAP_CLASSES, CSS_FULL_WIDTH));

		accordionMap.put("JVM Data", HTMLUtil.constructTable(Arrays.asList(columnHeader), readJVMData(),
				STRIPED_BOOTSTRAP_CLASSES, CSS_FULL_WIDTH));

		// omitted weblogic specific code from Ben healthcheck internal

		columnHeader = new String[] { "Element Key", "Element Value (size)" };
		accordionMap.put("EHCache Keys", HTMLUtil.constructTable(Arrays.asList(columnHeader), readEHCacheData(),
				STRIPED_BOOTSTRAP_CLASSES, CSS_FULL_WIDTH));

		/*
		 * columnHeader = new String[] { "Component Name", "Component Version" };
		 * accordionMap.put("WCC Components installed", HTMLUtil.constructTable(Arrays.asList(columnHeader),
		 * readWccComponents(), STRIPED_BOOTSTRAP_CLASSES, CSS_FULL_WIDTH));
		 */

		builder.append(HTMLUtil.constructAccordion(accordionMap.keySet(), accordionMap.values()));

		builder.append(HTMLUtil.constructClosingTag("body"));
		builder.append(HTMLUtil.constructClosingTag("html"));

		ResponseEntity<String> response = bodybuilder.body(builder.toString());
		builder.setLength(0);
		return response;
	}

	/**
	 * Designed as Convenience-Method to reflect over RuntimeMXBean, but due to reasons, which remain unknown, the
	 * Methods are not accessible through Reflection. Even though they are indeed public Methods.
	 *
	 * @return List of FunctionName-Result Mappings
	 */
	private Map<String, String> readJVMData() {
		Map<String, String> rows = new HashMap<String, String>();
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

		// A long time ago, in a Galaxy far, far away, I ran out of fucks to give

		// Why this barrage of calling getters manually, you may ask.
		// Because reflecting over it results in a wave of IllegalAccessErrors...
		// Seriously, this is kinda BS.
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(runtimeMxBean.getUptime());

		String dateFormatted = cal.getTime().toString();
		rows.put("Uptime", dateFormatted);
		rows.put("BootClassPath", runtimeMxBean.getBootClassPath());
		rows.put("Class Path", runtimeMxBean.getClassPath());
		rows.put("Library Path", runtimeMxBean.getLibraryPath());
		rows.put("Spec Name", runtimeMxBean.getSpecName());
		rows.put("Spec Version", runtimeMxBean.getSpecVersion());
		rows.put("VM Name", runtimeMxBean.getVmName());
		rows.put("VM Vendor", runtimeMxBean.getVmVendor());
		rows.put("VM Version", runtimeMxBean.getVmVersion());
		return rows;
	}

	// omitted weblogic specific code from Ben healthcheck internal

	/**
     * Convenience Method to collect EHCache Runtime data
     */
    @SuppressWarnings("unchecked")
	private List<TableRow> readEHCacheData() {
        List<TableRow> rows = new ArrayList<TableRow>();

        rows.add(new TableRow("for Details see", "http://www.ehcache.org/apidocs/3.4.0/index.html"));
        
        Collection<String> cacheNames = cacheManager.getCacheNames();

        for (String cacheName : cacheNames) {
            rows.add(new TableRow("", HR_SEPARATOR));
            //as HashMap.put updates duplicates instead of adding another we work around this fact
            rows.add(new TableRow("Cache Name", "<b>" + cacheName + "</b>"));
            Cache cache = cacheManager.getCache(cacheName);

            javax.cache.Cache<Object, Object> nativeCache = (javax.cache.Cache<Object,Object>) cache.getNativeCache();

            if (!nativeCache.isClosed()) {
                rows.add(new TableRow("Cache Status", " aktiv"));
                
                int count = 0;
                for (Iterator<?> iterator = nativeCache.iterator(); iterator.hasNext(); iterator.next()) {
                	count++;
                }
                rows.add(new TableRow("Cache Size [count]", String.valueOf(count)));

                //cut out for simlicity / sonar
                rows = appendDetailsForCache(rows, nativeCache);

            } else {
                rows.add(new TableRow("Cache Status", " SKIPPING more details"));
            }
        }

        return rows;
    }

	/**
	 * Convenience Method to append details for cache
	 * 
	 * @param rows
	 *            to append the details to
	 * @param cache
	 *            from which the details should be read
	 * @return rows with appended details
	 */
	@SuppressWarnings("unchecked")
	private List<TableRow> appendDetailsForCache(List<TableRow> rows, javax.cache.Cache<Object, Object> cache) {

		// add cached data structures to our lists
		List<AbstractMap.SimpleEntry<Object, Collection<Object>>> collectionValues = new ArrayList<>();

		List<AbstractMap.SimpleEntry<Object, Object>> objectValues = new ArrayList<>();

		for (javax.cache.Cache.Entry<Object, Object> entry : cache) {
			if (null == entry) {
				continue;
			}
			
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Collection) {
				// we need to store Lists and Maps to inspect them later on
				collectionValues.add(new AbstractMap.SimpleEntry<>(key, (Collection<Object>) value));
			} else {
				// adding some info on non collection values as well
				objectValues.add(new AbstractMap.SimpleEntry<>(key, value));
			}
		}

		// sort our list of lists
		if (!collectionValues.isEmpty()) {
			// if list is not empty, sort it biggest to smallest
			Collections.sort(collectionValues, new Comparator<AbstractMap.SimpleEntry<Object, Collection<Object>>>() {
				@Override
				public int compare(AbstractMap.SimpleEntry<Object, Collection<Object>> se1,
						AbstractMap.SimpleEntry<Object, Collection<Object>> se2) {
					return se2.getValue().size() - se1.getValue().size();
				}
			});
		}
		// print first entries of our list of lists
		for (int i = 0; i < Math.min(collectionValues.size(), 40); i++) {
			rows.add(new TableRow("[" + i + "] " + String.valueOf(collectionValues.get(i).getKey()),

					"Size: " + String.valueOf(collectionValues.get(i).getValue().size())
							+ " System.identityHashCode(): "
							+ System.identityHashCode(collectionValues.get(i).getValue())));
		}

		// print first entries of our list of object values
		for (int i = 0; i < Math.min(objectValues.size(), 40); i++) {
			rows.add(new TableRow("[" + i + "] " + String.valueOf(objectValues.get(i).getKey()),

					"toString: " + String.valueOf(objectValues.get(i).getValue()) + " System.identityHashCode(): "
							+ System.identityHashCode(objectValues.get(i).getValue())));
		}

		return rows;
	}

	// omitted weblogic specific code from Ben healthcheck internal

	/**
	 * Convenience-Method to load and read Application-Specific properties and exclude Entries that contain passwords
	 * 
	 * @param applicationName
	 *            Name of the current Application
	 */
	private Map<String, String> readProperties(String applicationName) throws IOException {
		String propertyName = "";
		if (NAME.equals(applicationName)) {
			propertyName = "/infosysbub-studisu-server.properties";
		}
		return readPropertyFile(propertyName);
	}

	/**
	 * Convenience-Method to load and read Application-Specific properties and exclude Entries that contain passwords
	 * 
	 * @param applicationName
	 *            Name of the current Application
	 */
	private Map<String, String> readFeatureProperties(String applicationName) throws IOException {
		String propertyName = "";
		if (NAME.equals(applicationName)) {
			propertyName = "/studisu_features.properties";
		}
		return readPropertyFile(propertyName);
	}

	/**
	 * Convenience-Method to load and read Application-Specific properties and exclude Entries that contain passwords
	 */
	private Map<String, String> readPropertyFile(String propertyName) throws IOException {
		Properties props = new Properties();
		InputStream asStream = this.getClass().getResourceAsStream(propertyName);

		if (asStream != null) {
			// as we checked the file existance, we shouldnt hit FileNotFound Exceptions
			// the GUI wont show any entries in this case
			try {
				props.load(asStream);
			} catch (IOException e) {
				LOGGER.error(EXCEPTION_MESSAGE + e.getMessage(), e);
			} finally {
				asStream.close();
			}
		}

		return HTMLUtil.getPropertiesAsMap(props, "password");
	}

	/**
	 * Convenience Method to identify the application by it's context path
	 * 
	 * @param contextPath
	 */
	private String findApplicationName(String contextPath) {
		String appName = "";
		if (contextPath.toUpperCase().contains(NAME)) {
			appName = NAME;
		}
		return appName;
	}

	/**
	 * Format file size as MB, GB etc found here:
	 * http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	 * 
	 * @param size
	 * @return
	 */
	public static String readableFileSize(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * Last chance exception handler
	 * 
	 * @param e
	 * @return http 500 as unexpected error occured
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<?> behandleFehler(final Exception e) {
		if (e.getMessage() != null && !"java.io.IOException: Broken pipe".equals(e.getMessage())) {
			// logging reduziert, null bringt keinen mehrwert und brokenpipe erhaltene wir von mesos-healthchecks zu
			// haeufig
			LOGGER.error("Exception during Monitoring! " + e.getMessage());
		} else {
			LOGGER.debug("Exception during Monitoring! get rid off this: " + e);
		}
		return ResponseEntity.status(500).body(null);
	}
}
