package de.ba.bub.studisu.studienfeldinformationen.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.exception.HtmlContentClientException.Reason;
import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.integration.wcc.ContentClient;
import de.ba.bub.studisu.common.model.StudienInformationDTO;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfachInformationen;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenAnfrage;
import de.ba.bub.studisu.studienfeldinformationen.model.StudienfeldInformationenErgebnis;

/**
 * Implementierung des {@link StudienfeldInformationenService}.
 */
@Service("studienfeldInformationen")
public class StudienfeldInformationenServiceImpl implements StudienfeldInformationenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudienfeldInformationenServiceImpl.class);

	/**
	 * DKZService.
	 */
	private final DKZService dkzService;

	/**
	 * BildungsangebotService.
	 */
	private final BildungsangebotService bildungsangebotServiceClient;
	
	private ContentClient contentClient;

	/**
	 * Konstruktor.
	 *
	 * @param dkzService
	 *            DKZService
	 * @param bildungsangebotServiceClient
	 * 			  BildungsangebotService
	 * @param contentClient
	 *            ContentClient
	 */
	public StudienfeldInformationenServiceImpl(@Qualifier("dkz") DKZService dkzService, BildungsangebotService bildungsangebotServiceClient, ContentClient contentClient) {
		this.dkzService = dkzService;
		this.contentClient = contentClient;
		this.bildungsangebotServiceClient = bildungsangebotServiceClient;
	}

	/**
	 * Anhand der DKZ-Id werden Informationen zu dem Studienfeld ermittelt.
	 * Vorab findet eine Validierung der DKZ-Id auf Zugehörigkeit zur HA / HC
	 * Gruppe statt.
	 *
	 * @param anfrage
	 *            StudienfeldInformationenAnfrage
	 * @return studienfeldInformationenErgebnis StudienfeldInformationenErgebnis
	 */
	@Override
	public StudienfeldInformationenErgebnis suche(StudienfeldInformationenAnfrage anfrage) {

		int dkzId = anfrage.getDkzId();

		List<String> codeNummern = new ArrayList<String>();
		codeNummern.add("HA ????");
		codeNummern.add("HC ????");
		List<Systematik> dkzResult = dkzService.findSystematik(dkzId, codeNummern);
		if (dkzResult.isEmpty()) {
			throw new ValidationException("DKZ-ID: " + dkzId + " nicht vorhanden.");
		}

		// Studienfeld anlegen und Bezeichnung neutral kurz aus DKZ übernehmen.
		StudienfeldInformationenErgebnis studienfeldInformationenErgebnis = new StudienfeldInformationenErgebnis();

		// Liste der Studienfächer zum Studienfeld - wenn verfügbar - aus dem DKZ holen.
		List<StudienfachInformationen> studienfachInformationen = getStudienfachInformationen(dkzId);

		studienfeldInformationenErgebnis.setStudienfachInformationen(studienfachInformationen);

		return studienfeldInformationenErgebnis;
	}

	/**
	 * Liefert die Liste mit den Informationen zu allen Studienfächern des
	 * Studienfeldes.
	 *
	 * @param dkzId
	 *            Die DKZ-ID des Studienfeldes.
	 * @return Die Ergebnisliste
	 */
	private List<StudienfachInformationen> getStudienfachInformationen(int dkzId) {

		List<StudienfachInformationen> studienfachInformationen = new ArrayList<StudienfachInformationen>();

		for (Systematik fachSystematik : getStudienfachSystematiken(dkzId)) {
			StudienfachInformationen studienfachInfo = new StudienfachInformationen();

			StudienInformationDTO studienInformationDTO = new StudienInformationDTO();

			studienfachInfo.setId(fachSystematik.getId());
			studienfachInfo.setNeutralBezeichnung(fachSystematik.getBezeichnungNeutral());
			try {
				studienInformationDTO = getWccContentForStudienfach(fachSystematik.getId());
			} catch (HtmlContentClientException e) {
				if (!Reason.NO_CONTENT.equals(e.getReason())) {
					LOGGER.error("Fehler bei der Studienfachabfrage an WCC. DKZ-ID: " + fachSystematik.getId(), e);
				}
			}

			studienfachInfo.setStudienfachbeschreibungen(studienInformationDTO.getStudienfeldbeschreibungen());
			studienfachInfo.setStudiengangsbezeichnungen(studienInformationDTO.getStudiengangsbezeichnungen());
			studienfachInfo.setStudienfachFilmId(studienInformationDTO.getStudienfachFilmId());

			int count = bildungsangebotServiceClient.holeAnzahlBildungsangebote(fachSystematik.getId());
			studienfachInfo.setCount(count);

			studienfachInformationen.add(studienfachInfo);
		}

		return studienfachInformationen;
	}

	/**
	 * Liefert die Liste aller beschriebenen Studienfach-Systematikeinträge zum über die
	 * DKZ-ID definierten Studienfeld.
	 *
	 * @param studfId
	 *            Die DKZ-ID des Studienfeldes.
	 * @return Die Liste der Systematik-Einträge.
	 */
	private List<Systematik> getStudienfachSystematiken(int studfId) {
		return dkzService.findBeschriebeneStudienfaecherFuerStudienfeld(studfId);
	}

	/**
	 * Liefert den WCC-Eintrag zu dem Studienfach mit der übergebenen ID.
	 *
	 * @param studienfachId
	 *            Die DKZ-ID des Studienfaches
	 * @return Die Liste mit den Beschreibungen des Studienfaches.
	 * @throws HtmlContentClientException
	 */
	private StudienInformationDTO getWccContentForStudienfach(int studienfachId) throws HtmlContentClientException {
		return contentClient.getStudienfachInformationen(studienfachId);
	}

}
