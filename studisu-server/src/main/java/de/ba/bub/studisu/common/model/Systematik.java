package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Systematik.
 * 
 * @author StraubP
 */
public class Systematik implements Serializable {

	private static final long serialVersionUID = -8323434693242302452L;

	/**
	 * Pattern des Codes.
	 */
	private static final Pattern CODE_NR_PATTERN = Pattern.compile("[A-Z]+( [0-9]+\\-*[0-9]*)?");

	/**
	 * DKZ-ID.
	 */
	private int id;

	/**
	 * Codenummer der Form "[A-Z]+( [0-9]+)?".
	 */
	private String codenr;

	/**
	 * Ober-Codenummer.
	 */
	private String obercodenr;

	/**
	 * Kurzbezeichnung neutral.
	 */
	private String kurzBezeichnungNeutral;

	/**
	 * Bezeichnung neutral.
	 */
	private String bezeichnungNeutral;

	/**
	 * Zustand der Systematik.
	 */
	private SystematikZustand zustand;

	/**
	 * optional beschreibungszustand
	 */
	private BeschreibungsZustand beschreibungsZustand;

	/**
	 * 
	 * @param id
	 *            DKZ-ID
	 * @param codenr
	 *            Codenummer der Form "[A-Z]+( [0-9]+\\-*[0-9]*)?"
	 * @param obercodenr
	 *            Ober-Codenummer
	 * @param kurzBezeichnungNeutral
	 *            Kurzbezeichnung neutral
	 * @param bezeichnungNeutral
	 *            Bezeichnung neutral
	 * @param zustand
	 *            Zustand der Systematik
	 * @param beschreibungsZustand
	 * 			  Zustand der Sytematik Beschreibung
	 * @throws NullPointerException
	 *             falls codenr null ist
	 * @throws IllegalArgumentException
	 *             falls codenr nicht dem Pattern "[A-Z]+( [0-9]+\\-*[0-9]*)?"
	 *             entspricht
	 */
	public Systematik(int id, String codenr, String obercodenr, String kurzBezeichnungNeutral,
			String bezeichnungNeutral, SystematikZustand zustand,BeschreibungsZustand beschreibungsZustand) {
		super();
		if (codenr == null || !CODE_NR_PATTERN.matcher(codenr).matches()) {
			throw new IllegalArgumentException("codenr '" + codenr + "' does not match " + CODE_NR_PATTERN.pattern());
		}
		this.id = id;
		this.codenr = codenr;
		this.obercodenr = obercodenr;
		this.kurzBezeichnungNeutral = kurzBezeichnungNeutral;
		this.bezeichnungNeutral = bezeichnungNeutral;
		this.zustand = zustand;
		this.beschreibungsZustand = beschreibungsZustand;
	}

	/**
	 *
	 * @param id
	 *            DKZ-ID
	 * @param codenr
	 *            Codenummer der Form "[A-Z]+( [0-9]+\\-*[0-9]*)?"
	 * @param obercodenr
	 *            Ober-Codenummer
	 * @param kurzBezeichnungNeutral
	 *            Kurzbezeichnung neutral
	 * @param bezeichnungNeutral
	 *            Bezeichnung neutral
	 * @param zustand
	 *            Zustand der Systematik
	 * @throws NullPointerException
	 *             falls codenr null ist
	 * @throws IllegalArgumentException
	 *             falls codenr nicht dem Pattern "[A-Z]+( [0-9]+\\-*[0-9]*)?"
	 *             entspricht
	 */
	public Systematik(int id, String codenr, String obercodenr, String kurzBezeichnungNeutral,
					  String bezeichnungNeutral, SystematikZustand zustand) {
		this(id,codenr,obercodenr,kurzBezeichnungNeutral,bezeichnungNeutral,zustand,null);
	}


	/**
	 * Liefert die DKZ-ID.
	 * 
	 * @return DKZ-ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Liefert die Codenummer der Form "[A-Z]+( [0-9]+\\-*[0-9]*)?".
	 * 
	 * @return Codenummer
	 */
	public String getCodenr() {
		return codenr;
	}

	/**
	 * Liefert die Ober-Codenummer.
	 * 
	 * @return Ober-Codenummer
	 */
	public String getObercodenr() {
		return obercodenr;
	}

	/**
	 * Liefert die Kurzbezeichnung neutral.
	 * 
	 * @return
	 */
	public String getKurzBezeichnungNeutral() {
		return kurzBezeichnungNeutral;
	}

	/**
	 * Liefert Bezeichnung neutral
	 * 
	 * @return Bezeichnung neutral
	 */
	public String getBezeichnungNeutral() {
		return bezeichnungNeutral;
	}

	/**
	 * Liefert den Zustand der Systematik
	 * 
	 * @return Zustand der Systematik
	 */
	public SystematikZustand getZustand() {
		return zustand;
	}

	/**
	 * beschreibungsZustand getter
	 * @return beschreibungszustand
     */
	public BeschreibungsZustand getBeschreibungsZustand() {
		return beschreibungsZustand;
	}


	/**
	 *
	 * @return
     */
	public boolean isBeschreibungValid() {
		if(beschreibungsZustand==null){
			return false;
		}
		return beschreibungsZustand.isValid();
	}
}
