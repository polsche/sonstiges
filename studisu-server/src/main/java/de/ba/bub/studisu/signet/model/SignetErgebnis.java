package de.ba.bub.studisu.signet.model;

import de.ba.bub.studisu.common.model.Signet;

/**
 * TO fuer das Signet.
 *
 * @author pleinesm001
 */
public class SignetErgebnis {

	private Signet signet;

	/**
	 * Konstruktor.
	 * 
	 * @param signet
	 *            nicht null
	 */
	public SignetErgebnis(Signet signet) {
		super();
		this.signet = signet;
	}

	/**
	 * Liefert das Signet.
	 * 
	 * @return nicht null.
	 */
	public Signet getSignet() {
		return signet;
	}
}
