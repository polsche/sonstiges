package de.ba.bub.studisu.studienangebote.model;

import java.util.Collections;
import java.util.List;

import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacetteOption;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;

/**
 * Eingabeparameter der Studienangebotsuche.
 */
public final class StudienangebotsucheAnfrage {

	// Validierungsergebnis, welches vom Command abgefragt werden kann
	private int validationAngebotResult;
	private int validationOrtResult;
	// Felder der Anfrage
	private Studienfelder studienfelder;
	private Studienfaecher studienfaecher;
	private AnfrageOrte anfrageOrte;
	private Paging paging;

	private StudienformFacette studienformFacette;
	private StudientypFacette studientypFacette;
	private HochschulartFacette hochschulartFacette;
	private FitFuerStudiumFacette fitFuerStudiumFacette;
	private UmkreisFacette umkreisFacette;
	private RegionenFacette bundeslandFacette;

	/**
	 * C-tor with request parameter objects defaulting optional params
	 *
	 * TODO consider builder-pattern to avoid telescoping constructor
	 * 
	 * @param studienfelder
	 *            Die gewaehlten Studienfelder, in denen gesucht werden soll.
	 * @param studienfaecher
	 *            Die gewaehlten Studienfaecher, die (zusaetzlich) gesucht
	 *            werden sollen.
	 * @param anfrageOrte
	 *            Die Orte, an denen (mit dem gegebenen Umkreis) gesucht werden
	 *            soll.
	 * @param studienformFacette
	 *            Die Facette mit den gewuenschten Studienformen.
	 * @param hochschulartFacette
	 *            Die Facette mit den gewuenschten Hochschularten.
	 * @param fitFuerStudiumFacette
	 *            Die Facette mit den gewuenschten fit für Studium Optionen
	 * @param umkreisFacette
	 *            Die Facette mit dem Umkreis um die gewaehlten Orte (oder
	 *            "Bundesweit").
	 * @param paging
	 *            Die Angabe fuer Paginierung.
	 * @param studientypFacette
	 *            Die Facette mit den gewuenschten Studientypen.
	 * @param bundeslandFacette
	 *            Die Facette mit den gewuenschten Bundesländern.
	 * @param internationalFacette
	 *            Die Facette mit den gewuenschten Staaten.
	 */
	public StudienangebotsucheAnfrage(Studienfelder studienfelder, Studienfaecher studienfaecher, AnfrageOrte anfrageOrte,
			StudienformFacette studienformFacette, HochschulartFacette hochschulartFacette, FitFuerStudiumFacette fitFuerStudiumFacette,
			UmkreisFacette umkreisFacette, Paging paging, StudientypFacette studientypFacette,
			RegionenFacette bundeslandFacette) {

		// handle studienfelder and studienfaecher
		// one of both has to be set
		// otherwise validator will return invalid
		StudienangeboteValidator validatorAngebot = new StudienangeboteValidator(studienfelder, studienfaecher);
		validationAngebotResult = validatorAngebot.getResult();

		// if one is set the other is allowed to be not set, we initialize here with empty string
		if (null == studienfelder) {
			this.studienfelder = new Studienfelder("");
		} else {
			this.studienfelder = studienfelder;
		}
		// for both cases
		if ( null == studienfaecher) {
			this.studienfaecher = new Studienfaecher("");
		} else {
			this.studienfaecher = studienfaecher;
		}

		this.anfrageOrte = anfrageOrte;
		this.paging = paging;

		this.studienformFacette = studienformFacette;
		this.hochschulartFacette = hochschulartFacette;
		this.studientypFacette = studientypFacette;
		this.bundeslandFacette = bundeslandFacette;
		this.fitFuerStudiumFacette = fitFuerStudiumFacette;
		//ensure that there is a umkreis which could be default after this code
		if (umkreisFacette == null) {
			if (anfrageOrte == null) {
				this.umkreisFacette = new UmkreisFacette(UmkreisFacette.BUNDESWEIT.getName());
			} else {
				this.umkreisFacette = new UmkreisFacette(UmkreisFacette.FUENFZIG.getName());
			}
		} else {
			this.umkreisFacette = umkreisFacette;
		}
		
		// handle anfrageOrte
		// darf keine Duplikate oder mehr als drei Orte enthalten
		// otherwise validator will return invalid
		OrteValidator validatorOrt = new OrteValidator(this.umkreisFacette,anfrageOrte);
		validationOrtResult =  validatorOrt.getResult();
	}

	/**
	 * Validation Angebot result
	 *
	 * @return validation Angebot result
	 */
	public int getAngebotValidationResult() {
		return validationAngebotResult;
	}
	
	/**
	 * Validation Ort result
	 *
	 * @return validation Ort result
	 */
	public int getOrtValidationResult() {
		return validationOrtResult;
	}
	
	/**
	 * Liefert die Liste mit den IDs aller gesuchten Studienfelder aus der Suchfacette.
	 *
	 * @return Liste mit DKZ-IDs der Studienfelder.
	 */
	public List<Integer> getStudienfelderList() {
		if (studienfelder != null) {
			return studienfelder.getStudienfelderIds();
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * Liefert die Liste mit den IDs aller gesuchten Studienfächer aus der Suchfacette.
	 *
	 * @return Liste mit DKZ-IDs der Studienfächer.
	 */
	public List<Integer> getStudienfaecherList() {
		if (studienfaecher != null) {
			return studienfaecher.getStudienfaecherIds();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Liefert die Liste mit den angefragten Orten aus der Suchfacette.
	 *
	 * @return Liste mit den Orten.
	 */
	public List<AnfrageOrt> getAnfrageOrte() {
		if (anfrageOrte != null) {
			return anfrageOrte.getOrte();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Liefert die Parameter für die Paginierung.
	 *
	 * @return Die Parameter für's Paginieren.
	 */
	public Paging getPaging() {
		return paging;
	}

	/**
	 * Liefert die Anzahl der maximal gewünschten Suchergebnisse aus dem Pagienierungsparameter.
	 *
	 * @return page size
	 */
	public Integer getMaxErgebnisse() {
		if (paging != null) {
			return paging.getCount();
		} else {
			return Paging.COUNT_DEFAULT;
		}
	}

	/**
	 * Liefert den Offset für die Suchergebnisse aus dem Pagienierungsparameter.
	 *
	 * @return paging offset
	 */
	public int getOffset() {
		if (paging != null) {
			return paging.getOffset();
		} else {
			return 0;
		}
	}

	/**
	 * Liefert die gewählten Umkreisoption aus der Suchfacette.
	 *
	 * @return Die Option für die Bestimmung des Suchumkreises.
	 */
	public UmkreisFacetteOption getUmkreis() {
		if (umkreisFacette != null) {
			return umkreisFacette.getSelectedOption();
		}
		return UmkreisFacette.FUENFZIG;
	}

	/**
	 * Liefert die Studienform-Facette mit den zu fiternden Optionen.
	 * 
	 * @return null, falls ohne Einschränkung
	 */
	public StudienformFacette getStudienformFacette() {
		return studienformFacette;
	}
	
	/**
	 * Liefert die Studientyp-Facette mit den zu fiternden Optionen.
	 * 
	 * @return null, falls ohne Einschränkung
	 */
	public StudientypFacette getStudientypFacette() {
		return studientypFacette;
	}
	
	/**
	 * Liefert die FFS-Facette mit den zu fiternden Optionen.
	 * 
	 * @return null, falls ohne Einschränkung
	 */
	public FitFuerStudiumFacette getFitFuerStudiumFacette() {
		return fitFuerStudiumFacette;
	}

	/**
	 * Liefert die Hochschulart-Facette mit den zu fiternden Optionen.
	 *
	 * @return null, falls ohne Einschränkung
	 */
	public HochschulartFacette getHochschulartFacette() {
		return hochschulartFacette;
	}


	/**
	 * Liefert die Umkreis Facette der Anfrage zurueck
	 *
	 * @return Umkreis Facette
	 */
	public UmkreisFacette getUmkreisFacette() {
		return umkreisFacette;
	}
	
	/**
	 * Liefert die Bundesland Facette der Anfrage zurueck
	 *
	 * @return Bundesland Facette
	 */
	public RegionenFacette getBundeslandFacette() {
		return bundeslandFacette;
	}

}
