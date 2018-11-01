package de.ba.bub.studisu.studienfeldbeschreibung.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.exception.HtmlContentClientException.Reason;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.integration.wcc.ContentClient;
import de.ba.bub.studisu.common.model.StudienInformationDTO;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.validation.ValidationException;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungAnfrage;
import de.ba.bub.studisu.studienfeldbeschreibung.model.StudienfeldBeschreibungErgebnis;

/**
 * Implementierung des {@link StudienfeldBeschreibungService}.
 */
@Service("studienfeldBeschreibung")
public class StudienfeldBeschreibungServiceImpl implements StudienfeldBeschreibungService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudienfeldBeschreibungServiceImpl.class);

    /**
     * DKZService.
     */
    private final DKZService dkzService;

    private ContentClient contentClient;

    /**
     * Konstruktor.
     *
     * @param dkzService    DKZService
     * @param contentClient ContentClient
     */
    public StudienfeldBeschreibungServiceImpl(@Qualifier("dkz") DKZService dkzService, ContentClient contentClient) {
        this.dkzService = dkzService;
        this.contentClient = contentClient;
    }

    /**
     * Anhand der DKZ-Ids werden Informationen zu den Studienfeldern ermittelt.
     * Vorab findet eine Validierung der DKZ-Ids auf Zugehörigkeit zur HA / HC
     * Gruppe statt.
     *
     * @param anfrage StudienfeldBeschreibungAnfrage
     * @return studienfeldBeschreibungErgebnis StudienfeldBeschreibungErgebnis
     */
    @Override
    public StudienfeldBeschreibungErgebnis suche(StudienfeldBeschreibungAnfrage anfrage) {

        int dkzId = anfrage.getDkzId();

        List<String> codeNummern = new ArrayList<String>();
        codeNummern.add("HA ????");
        codeNummern.add("HC ????");

        List<Systematik> dkzResult = dkzService.findSystematik(dkzId, codeNummern);
        if (dkzResult.isEmpty()) {
            throw new ValidationException("DKZ-ID: " + dkzId + " nicht vorhanden.");
        }

        // Studienfeld anlegen, "Bezeichnung neutral kurz" aus DKZ übernehmen.
        StudienfeldBeschreibungErgebnis studienfeldBeschreibungErgebnis = new StudienfeldBeschreibungErgebnis();
        studienfeldBeschreibungErgebnis.setNeutralKurzBezeichnung(dkzResult.get(0).getKurzBezeichnungNeutral());
        studienfeldBeschreibungErgebnis.setCodeNr(dkzResult.get(0).getCodenr());
        studienfeldBeschreibungErgebnis.setOberCodeNr(dkzResult.get(0).getObercodenr());

        // Wenn Sytematik beschrieben, Beschreibung aus dem WCC holen.
        StudienInformationDTO studienfeldBeschreibungen = new StudienInformationDTO();
        boolean beschreibungValid = dkzResult.get(0).isBeschreibungValid();
        if (beschreibungValid) {
            try {
                studienfeldBeschreibungen = contentClient.getStudienfeldInformationen(dkzId);
            } catch (HtmlContentClientException e) {
                if (!Reason.NO_CONTENT.equals(e.getReason())) {
                    LOGGER.error("Fehler bei der DKZ-ID-Abfrage an WCC. DKZ-ID: " + dkzId, e);
                }
            }
        }else{
            LOGGER.info("Systematik Beschreibung für "+dkzId+" NOT valid..");
        }
        studienfeldBeschreibungErgebnis.setStudienfeldbeschreibungen(studienfeldBeschreibungen.getStudienfeldbeschreibungen());
        return studienfeldBeschreibungErgebnis;
    }
}
