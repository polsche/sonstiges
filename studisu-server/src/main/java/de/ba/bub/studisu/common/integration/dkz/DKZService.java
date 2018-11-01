package de.ba.bub.studisu.common.integration.dkz;

import java.util.List;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.integration.ExternalService;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.common.model.Systematik;

/**
 * DKZ-Service-Fassade.
 *
 * @author StraubP
 */
public interface DKZService extends ExternalService{

	/**
	 * Liefert alle Systematiken, die Studienfelder repräsentieren und zur
	 * angegebenen Ober-Codenummer gehören.
	 *
	 * @param obercodenr
	 *            Ober-Codenummer; nicht null
	 * @return Liste von Systematiken
	 */
	List<Systematik> findStudienfeldgruppeSystematiken(String obercodenr);

	/**
	 * Liefert alle Systematiken passend zu den angegebenen Parametern.
	 *
	 * @param dkzId
	 *            DKZ-ID
	 * @param codeNummern
	 *            Liste von Codenummern zum Filtern des Requests.
	 * @return Liste aller gefundenen Systematiken
	 */
	public List<Systematik> findSystematik(int dkzId, List<String> codeNummern);

	/**
	 * Liefert alle Studienfaecher zur angegebenen Studienfeld-ID.
	 *
	 * @param studfId
	 *            Studienfeld-ID; nicht null
	 *
	 * @return Liste der Sytematik-Informationen aller gefundenen Studienfaecher
	 */
	List<Systematik> findStudienfaecherFuerStudienfeld(Integer studfId);

	/**
	 * Liefert alle beschriebenen Studienfaecher zur angegebenen Studienfeld-ID.
	 *
	 * @param studfId
	 *            Studienfeld-ID; nicht null
	 *
	 * @return Liste von DKZ-IDs aller gefundenen und beschriebenen
	 *         Studienfaecher
	 */
	List<Systematik> findBeschriebeneStudienfaecherFuerStudienfeld(Integer studfId);

	/**
	 * Liefert alle beschriebenen Studienfaecher passend zum angegebenen
	 * Suchbegriff.
	 *
	 * @param suchbefriff
	 *            Der Suchbegriff, nicht null oder leer
	 * @return Liste von Studienfächern
	 */
	public List<Studienfach> findStudienfachBySuchwort(String suchbefriff);

	/**
	 * Liefert alle Studienfaecher zu den angegebenen IDs.
	 *
	 * @param dkzIds
	 *            DKZ-ID, nicht null
	 * @return Liste von Studienfächern
	 */
	public List<Studienfach> findStudienfachById(List<Integer> dkzIds);

	/**
	 * Service status for monitoring/healthcheck
	 * @return service status
	 * @throws CommonServiceException
     */
	@Override
	ExternalServiceStatus getServiceStatus() throws CommonServiceException;

}
