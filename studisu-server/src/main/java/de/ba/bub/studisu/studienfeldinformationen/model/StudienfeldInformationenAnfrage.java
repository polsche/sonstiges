package de.ba.bub.studisu.studienfeldinformationen.model;

/**
 * Eingabeparameter der StudienfeldInformationen.
 */
public final class StudienfeldInformationenAnfrage {

	/**
	 * DKZ-ID.
	 */
	private final int dkzId;

	/**
	 * Konstruktor.
	 * 
	 * @param dkzId
	 *            DKZ-ID
	 */
	public StudienfeldInformationenAnfrage(int dkzId) {
		super();
		this.dkzId = dkzId;
	}

	/**
	 * Liefert die DKZ-ID.
	 * 
	 * @return DKZ-ID
	 */
	public int getDkzId() {
		return dkzId;
	}

}
