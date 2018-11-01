package de.ba.bub.studisu.studienfelder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;
import de.ba.bub.studisu.studienfelder.model.SystematikWithChilderen;

/**
 * Lädt Daten für die Studienfeld-Suche.
 * 
 * @author StraubP
 */
@Component
public class StudienfeldSucheDataCollector {

	/**
	 * DKZService.
	 */
	private final DKZService dkzService;

	/**
	 * Konstruktor.
	 * 
	 * @param dkzService
	 *            DKZService
	 */
	public StudienfeldSucheDataCollector(@Qualifier("dkz") DKZService dkzService) {
		this.dkzService = dkzService;
	}

	/**
	 * Lädt alle relevanten Daten zu einer Code-Nummer.
	 * 
	 * @param codeNr
	 *            die Codenummer
	 * @return Collection von {@link SystematikWithChilderen}
	 */
	public Collection<SystematikWithChilderen> collectData(String codeNr) {
		Set<SystematikWithChilderen> data = new HashSet<>();
		List<Systematik> haSystematiken = dkzService.findStudienfeldgruppeSystematiken(codeNr);
		for (Systematik systematik : haSystematiken) {
			if (SystematikZustand.GELOESCHT.equals(systematik.getZustand())) {
				continue;
			}
			List<Systematik> studienbereiche = dkzService.findStudienfeldgruppeSystematiken(systematik.getCodenr());
			data.add(new SystematikWithChilderen(systematik, ohneGeloeschte(studienbereiche)));
		}
		return data;
	}

	/**
	 * Liefert eine Liste, die alle Systematiken aus der übergebenen liste
	 * enthält, die nicht den zustand gelöscht haben.
	 * 
	 * @param systematiken
	 *            Systematiken
	 * @return ungelöschte Systematiken
	 */
	private List<Systematik> ohneGeloeschte(List<Systematik> systematiken) {
		List<Systematik> ret = new ArrayList<Systematik>();
		for (Systematik systematik : systematiken) {
			if (!SystematikZustand.GELOESCHT.equals(systematik.getZustand())) {
				ret.add(systematik);
			}
		}
		return ret;
	}
}
