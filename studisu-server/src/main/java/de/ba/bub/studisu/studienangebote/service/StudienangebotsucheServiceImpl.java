package de.ba.bub.studisu.studienangebote.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotComparator;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.ba.bub.studisu.studienangebote.util.StudienangebotsucheErgebnisBuilder;

/**
 * Created by loutzenhj on 03.04.2017.
 */
@Service("studienangebote")
public class StudienangebotsucheServiceImpl implements StudienangebotsucheService {

	/**
	 * Comparator fuer Sortierung / Mergen von Studienangeboten.
	 */
	private static final StudienangebotComparator COMPARATOR = new StudienangebotComparator();
	
	/**
	 * DKZService.
	 */
	private final DKZService dkzService;

	/**
	 * BildungsangebotService
	 */
	private final BildungsangebotService bildungsangebotServiceClient;

	/**
	 * StudienangebotsucheErgebnisBuilder.
	 */
	private final StudienangebotsucheErgebnisBuilder ergebnisBuilder;

	/**
	 * Konstruktor.
	 *
	 * @param dkzService
	 *            DKZService
	 * @param baClient
	 *            BildungsangebotService
	 * @param ergebnisBuilder
	 *            StudienangebotsucheErgebnisBuilder
	 */
	@Autowired
	public StudienangebotsucheServiceImpl(@Qualifier("dkz") DKZService dkzService, BildungsangebotService baClient,
			StudienangebotsucheErgebnisBuilder ergebnisBuilder) {
		this.dkzService = dkzService;
		this.bildungsangebotServiceClient = baClient;
		this.ergebnisBuilder = ergebnisBuilder;
	}

	@Override
	public StudienangebotsucheErgebnis suche(StudienangebotsucheAnfrage anfrage) {

		// Map mit den Suchorten und deren Suchergebnissen füllen.
		Map<AnfrageOrt, List<Studienangebot>> ortResultMap = this.getResultMapForOrte(anfrage);

		// Sortierte Ergebnisliste für alle Orte ermitteln
		List<Studienangebot> studienangebote = this.getStudienangebote(ortResultMap);

		StudienangebotsucheErgebnis ergebnis = ergebnisBuilder.build(studienangebote, anfrage);

		// Limitieren (paging - page size) der Ergebnisse
		ergebnis.limit(anfrage.getPaging());

		// Anreichern der finalen Liste um die Entfernungen von den gewählten
		// Orten.
		ergebnis.berechneAbstaende(anfrage.getAnfrageOrte());

		return ergebnis;
	}

	/**
	 * Liefert eine Map, die jeden angefragten Ort mit seiner Ergebnisliste
	 * verknüpft.
	 * 
	 * Die Ergebnislisten werden vom Service-Aufruf bereits aufsteigend sortiert
	 * nach Abstand vom Suchort geliefert. Diese Ergebnisse können
	 * erfreulicherweise auch aus dem Cache kommen.
	 * 
	 * Wenn "Bundesweit" als Entfernung gewählt und kein Ort angegeben wird,
	 * werden die Ergebnisse nach dem Abstand von der "Mitte von Deutschland"
	 * sortiert geliefert.
	 * 
	 * Um wirklich deterministische Ergebnisse zu erzielen, müssen die
	 * Ortslisten immer in der gleichen Reihenfolge in der Map stehen. Daher
	 * muss eine Map - sortiert nach Ortsnamen - verwendet werden.
	 * 
	 * @param anfrage
	 *            Die Suchanfrage mit allen Parametern.
	 * @return Die Map mit allen gesuchten Orten + deren Ergebnislisten.
	 */

	private Map<AnfrageOrt, List<Studienangebot>> getResultMapForOrte(StudienangebotsucheAnfrage anfrage) {

		// Liste der Orte, wie sie für die Anfrage beim Bildungsangebotservice
		// verwendet wird.
		List<AnfrageOrt> orteForQuery = anfrage.getAnfrageOrte();

		// orteForQuery ist leer, wenn der Benutzer initial auf die Suchseite
		// kommt
		int umkreisKm;
		if (orteForQuery.isEmpty()) {
			orteForQuery = Collections.singletonList(UmkreisFacette.DE_MITTE);
			umkreisKm = UmkreisFacette.BUNDESWEIT.getUmkreisKm();
		} else {
			umkreisKm = anfrage.getUmkreis().getUmkreisKm();
		}

		List<Integer> studienfaecher = getStudienfaecherDkzIds(anfrage);

		// Map mit den Suchorten und deren Suchergebnissen füllen.
		Map<AnfrageOrt, List<Studienangebot>> ortResultMap = new LinkedHashMap<>();
		for (AnfrageOrt aort : orteForQuery) {
			// Wir kopieren das Ergebnis, weil unser Merge-Algorithmus die
			// Listen leert und wir den Cache nicht löschen wollen...
			List<Studienangebot> listForOrt = new LinkedList<>(
					bildungsangebotServiceClient.findStudienangebote(aort, umkreisKm, studienfaecher));
			if (!listForOrt.isEmpty()) {
				ortResultMap.put(aort, listForOrt);
			}
		}

		return ortResultMap;
	}

	/**
	 * Ermittelt die DKZ-IDs der Studienfaecher zu den gewaehlten
	 * Studienfeldern.
	 * 
	 * @param anfrage
	 *            Die Suchanfrage mit allen Parametern.
	 * @return Liste von DKZ-IDs fuer Studienfaecher.
	 */
	private List<Integer> getStudienfaecherDkzIds(StudienangebotsucheAnfrage anfrage) {
		List<Integer> studienfaecherDkzIds = new ArrayList<Integer>();
		if (anfrage.getStudienfelderList() != null && !anfrage.getStudienfelderList().isEmpty()) {
			// rufe DKZ-Service fuer jede Studienfeld-ID einzeln auf (wegen
			// Caching)
			for (Integer studfId : anfrage.getStudienfelderList()) {
				// In Java 8 this should be using .map() function!
				for (Systematik fachSystematik : dkzService.findStudienfaecherFuerStudienfeld(studfId)) {
					studienfaecherDkzIds.add(fachSystematik.getId());
				}
			}
		}
		if (anfrage.getStudienfaecherList() != null && !anfrage.getStudienfaecherList().isEmpty()) {
			studienfaecherDkzIds.addAll(anfrage.getStudienfaecherList());
		}
		return studienfaecherDkzIds;
	}

	/**
	 * Ermittelt die sortierte Ergebnisliste.
	 * 
	 * @param ortsMap
	 *            Die Map mit den Orten und deren Ergebnislisten.
	 * @return Die sortierte Liste mit allen Suchergebnissen.
	 */
	private List<Studienangebot> getStudienangebote(Map<AnfrageOrt, List<Studienangebot>> ortsMap) {

		// Liste mit Studienangeboten + Set mit IDs der bereits gefundenen
		// Studienangebote.
		List<Studienangebot> studienangebote = new ArrayList<Studienangebot>();
		Set<String> studienangebotIds = new HashSet<String>();

		// Schleife über alle Einträge in allen Ortslisten
		while (!this.ortsMapIsEmpty(ortsMap)) {

			AnfrageOrt naehesterOrt = null;
			for (Entry<AnfrageOrt, List<Studienangebot>> entry : ortsMap.entrySet()) {
				if (null == naehesterOrt
						|| COMPARATOR.compare(entry.getValue().get(0), ortsMap.get(naehesterOrt).get(0)) < 0 ) {
					naehesterOrt = entry.getKey();
				}
			}

			Studienangebot studienangebot = ortsMap.get(naehesterOrt).get(0);
			ortsMap.get(naehesterOrt).remove(0);
			if (ortsMap.get(naehesterOrt).isEmpty()) {
				ortsMap.remove(naehesterOrt);
			}

			/**
			 * Es werden alle Studienangebote verworfen, für die kein gültiges
			 * Bundesland oder Staat verfügbar ist.
			 */
			boolean hasBundesland = !RegionenFacettenOption.KEINE_ZUORDNUNG_MOEGLICH.equals(studienangebot.getRegion());
			if (hasBundesland && !studienangebotIds.contains(studienangebot.getId())) {
				studienangebote.add(studienangebot);
				studienangebotIds.add(studienangebot.getId());
			}
		}

		return studienangebote;
	}

	/**
	 * Prüft, ob allen Ergebnislisten in der Orts-Map leer sind.
	 * 
	 * @param ortsMap
	 *            Die zu prüfende Map mit den Ergebnislisten für die einzelnen
	 *            Orte.
	 * @return true, wenn alle Listen leer sind, false sonst.
	 */
	private boolean ortsMapIsEmpty(Map<AnfrageOrt, List<Studienangebot>> ortsMap) {
		for (List<Studienangebot> listForOrt : ortsMap.values()) {
			if (!listForOrt.isEmpty()) {
				return false;
			}
		}
		return true;
	}

}
