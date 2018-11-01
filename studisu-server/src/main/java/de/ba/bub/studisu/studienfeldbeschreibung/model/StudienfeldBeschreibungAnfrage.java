package de.ba.bub.studisu.studienfeldbeschreibung.model;

/**
 * Eingabeparameter der StudienfeldBeschreibung.
 */
public final class StudienfeldBeschreibungAnfrage {

	/**
	 * DKZ-ID.
	 */
	private final int dkzId;

	/**
	 * Konstruktor.
	 * 
	 * @param dkzId
	 *            Die DKZ-ID des Studienfeldes.
	 */
	public StudienfeldBeschreibungAnfrage(int dkzId) {
		super();
		this.dkzId = dkzId;
	}

	/**
	 * Liefert die DKZ-ID des Studienfeldes.
	 * 
	 * @return DKZ-ID
	 */
	public int getDkzId() {
		return dkzId;
	}

}
