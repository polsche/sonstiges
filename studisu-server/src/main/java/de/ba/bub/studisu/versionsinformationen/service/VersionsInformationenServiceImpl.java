package de.ba.bub.studisu.versionsinformationen.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.ba.bub.studisu.common.exception.HtmlContentClientException;
import de.ba.bub.studisu.common.integration.wcc.ContentClient;
import de.ba.bub.studisu.common.service.ManifestService;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationen;
import de.ba.bub.studisu.versionsinformationen.model.VersionsInformationenAnfrage;

/**
 * Implementation des Interface {@link VersionsInformationenService}.
 * @author OettlJ
 *
 */
@Service(value = "versionsinformationen")
public class VersionsInformationenServiceImpl implements VersionsInformationenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionsInformationenServiceImpl.class);

	private ContentClient contentClient;
	private ManifestService manifestService;

	/**
	 * Ctor
	 *
	 * @param contentClient
	 * @param manifestService
	 */
	@Autowired
	public VersionsInformationenServiceImpl(ContentClient contentClient, @Qualifier("manifest") ManifestService manifestService) {
		this.contentClient = contentClient;
		this.manifestService = manifestService;
	}

	/**
     * Liefert Informationen zu Datenstand und Version des Backends.
     * @param anfrage
     * @return Informationen zu Datenstand und Version des Backends.
     */
	@Override
	public VersionsInformationen getVersionsInfo(VersionsInformationenAnfrage anfrage){
		VersionsInformationen versionsInfo = new VersionsInformationen();

		// Datenstand, wenn verfügbar, aus dem WCC holen
		String datenstand = "";
		try {
			datenstand = contentClient.getDatenstand();
		} catch (HtmlContentClientException e) {
			LOGGER.error("Fehler bei der Datenstand-Abfrage an WCC: ", e);
		}
		versionsInfo.setDatenstand(datenstand);

		// Version des Backends, wenn verfügbar, aus Manifest lesen
		String version = null;
		try {
			version = this.manifestService.getImplementationVersion();
		} catch (Exception e) {
			LOGGER.error("Fehler beim Auslesen der Version aus dem Manifest: ", e);
		}
		// konnte keine Version gefunden werden bzw. ist ein Fehler aufgetreten, setze '-' für Version,
		// andernfalls entferne ggf. vorhandenes '-snapshot' bzw. '-SNAPSHOT'
		if(version == null) {
			version = "-";
		} else {
			version = version.replace("-snapshot", "").replace("-SNAPSHOT", "");
		}
		versionsInfo.setVersion(version);

		return versionsInfo;
	}
}
