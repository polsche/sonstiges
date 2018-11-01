package de.ba.bub.studisu.studienfelder.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import de.ba.bub.studisu.studienfelder.model.Studienfeld;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheAnfrage;
import de.ba.bub.studisu.studienfelder.model.StudienfeldSucheErgebnis;
import de.ba.bub.studisu.studienfelder.model.Studienfeldgruppe;
import de.ba.bub.studisu.common.SystematikConstants;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Systematik;

/**
 * Implementierung des {@link StudienfeldSucheService}.
 * 
 * @author StraubP, DauminN
 */
@Service("studienfelder")
public class StudienfeldSucheServiceImpl implements StudienfeldSucheService {
	
	/**
	 * DKZ-Fassade
	 */
	private final DKZService dkzService;
	
	/**
	 * Cache. Es werden zu einem Studienfach jeweils zwei DKZ-Studienfelder (grundständig, weiterführend) gemapped.
	 */
	private final Map<Integer, Set<Integer>> studienfachToStudienfeldMap = new HashMap<>();

	/**
	 * Data-Collector.
	 */
	private final StudienfeldSucheDataCollector dataCollector;

	/**
	 * Konstruktor.
	 * 
	 * @param dkzService DKZ-Fassade
	 * @param dataCollector
	 *            Data-Collector
	 */
	@Autowired
	public StudienfeldSucheServiceImpl(@Qualifier("dkz") DKZService dkzService, StudienfeldSucheDataCollector dataCollector) {
		super();
		this.dkzService = dkzService;
		this.dataCollector = dataCollector;
	}

	/**
	 * Sucht Studienfelder und liefert sie gruppiert nach Studienbereichen zurück
	 * 
	 * @param anfrage
	 *            Anfrage-Parameter
	 * @return Studienfelder gruppiert nach Studienbereichen
	 * @throws NullPointerException
	 *             falls anfrage null ist
	 */
	@Override
	@Cacheable(cacheNames="studienfelder", key="{ #root.methodName }")
	public StudienfeldSucheErgebnis suche(StudienfeldSucheAnfrage anfrage) {
		if (anfrage == null) {
			throw new IllegalArgumentException("anfrage darf nicht null sein");
		}
		StudienfeldSucheErgebnisBuilder builder = createBuilder();
		// grundstaendig (HA) vor weiterfuehrend (HC) hinzufügen, damit die Namen von HA bevorzugt verwendet werden!
		builder.add(dataCollector.collectData(SystematikConstants.CODENR_STUDIUM_GRUNDSTAENDIG))
		    .add(dataCollector.collectData(SystematikConstants.CODENR_STUDIUM_WEITERFUEHREND));
		StudienfeldSucheErgebnis ergebnis = builder.create();
		return ergebnis;
	}

	/**
	 * Liefert den passenden Builder (überschreibbar von JUnit-Tests).
	 * 
	 * @return StudienfeldSucheErgebnisBuilder
	 */
	StudienfeldSucheErgebnisBuilder createBuilder() {
		return new StudienfeldSucheErgebnisBuilder();
	}
	
	
	/**
	 * Erstellt die Zuordnung Studienfach- zu DKZ-Studienfeldern. Zunaechst werden vom Studienfeld-Service alle 
	 * DKZ-Studienfeld-IDs abgerufen. Dann werden zu jedem DKZ-Studienfeld ueber den DKZ-Service die zugehoerigen 
	 * Studienfaecher abgefragt. Pro Studienfach erfolgt ein Map-Eintrag mit den zugeordneten DKZ-Studienfeldern.
	 */
	private void erstelleStudienfachToStudienfeldMap() {
		final StudienfeldSucheErgebnis studienfeldSucheErgebnis = this.suche(new StudienfeldSucheAnfrage());
		// fuer alle Studienfeldgruppen
		for (Studienfeldgruppe studienfeldgruppe : studienfeldSucheErgebnis.getStudienfeldgruppen()) {
			// fuer alle Studisu-Studienfelder der Studienfeldgruppe
			for (Studienfeld studienfeld : studienfeldgruppe.getStudienfelder()) {
				this.erstelleZuordnungFuerStudienfeld(studienfeld);
			}
		}
	}	
		
    /**
	 * ermittelt ueber DKZ-Servicezugriff fuer das uebergebene Studisu-Studienfeld alle zugehoerigen Studienfaecher und
	 * setzt diese mit Zuordnung zu ihren DKZ-Studienfeldern grundstaendig und weiterfuehrend in die studienfachToFeldMap.
	 * Als Map-Keys und -Values wird jeweils die DKZ-Id des jeweiligen Elements gesetzt.
	 * @param studienfeld Studisu-Studienfeld
	 */
	private void erstelleZuordnungFuerStudienfeld(Studienfeld studienfeld) {
		// fuer alle DKZ-Studienfelder zum Studisu-Studienfeld (in der Regel immer zwei: grundstaendig und weiterfuehrend)
		for (Integer studifeldDkzId : studienfeld.getDkzIds()) {
			// fuer alle Studienfaecher zum DKZ-Studienfeld
			for (Systematik systematik : dkzService.findBeschriebeneStudienfaecherFuerStudienfeld(studifeldDkzId)) {
				// setze Zuordnung Studienfach - DKZ-Studienfelder
				studienfachToStudienfeldMap.put(systematik.getId(), studienfeld.getDkzIds());
			}
		}
	}
	
	/**
	 * gibt fuer die Studienfaecher aller Studienfeldbereiche eine Zuordnung zu den DKZ-Studienfeldern grundstaendig und weiterfuehrend
     * zurueck. Jeder Eintrag der zurueckgegebenen Map enthaelt:
	 * <ul>
     * <li>Key: DKZ-ID des Studienfachs</li>
     * <li>Value: DKZ-IDs der beiden DKZ-Studienfelder grundstaendig und weiterfuehrend zum Studienfach</li>
     * </ul>
	 * @return Zuordnung Studienfach - DKZ-Ids der zugehoerigen DKZ-Studienfelder grundstaendig und weiterfuehrend
	 */
	 @Cacheable(cacheNames="studienfachToStudienfeld", key="{ #root.methodName }")
	 @Override
	 public Map<Integer, Set<Integer>> getStudienfachToStudienfeldMap() {
		 this.erstelleStudienfachToStudienfeldMap();
		 return this.studienfachToStudienfeldMap;
	 }


}
