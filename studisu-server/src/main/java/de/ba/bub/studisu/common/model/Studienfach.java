package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Einfaches TO fuer ein Studienfach in STUDISU.
 *
 * @author schneidek084 on 2017-06-01.
 */
public class Studienfach implements Serializable {

	private static final long serialVersionUID = -8820223970903417793L;

	/**
	 * DKZ-ID des Studienfachs.
	 */
	private int dkzId;

	/**
	 * Neutrale Kurzbezeichnung des Studienfachs.
	 */
	private String name;

	/**
	 * Konstruktor.
	 * 
	 * @param dkzId
	 *            DKZ-ID des Studienfachs
	 * @param name
	 *            Neutrale Kurzbezeichnung des Studienfachs
	 */
	public Studienfach(int dkzId, String name) {
		super();
		this.dkzId = dkzId;
		this.name = name;
	}

	/**
	 * Liefert die DKZ-ID des Studienfachs.
	 * 
	 * @return DKZ-ID
	 */
	public int getDkzId() {
		return dkzId;
	}

	/**
	 * Liefert die neutrale Kurzbezeichnung des Studienfachs.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
}
