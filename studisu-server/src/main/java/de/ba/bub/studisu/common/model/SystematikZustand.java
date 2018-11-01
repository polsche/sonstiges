package de.ba.bub.studisu.common.model;

/**
 * Zustand einer {@link Systematik}.
 * 
 * @author StraubP
 */
public enum SystematikZustand {

	ENDPUNKT("E"),
	METAEINHEIT("M"),
	ALTE_AUSBILDUNG("A"),
	ALTE_TAETIGKEIT("R"),
	GELOESCHT("L"),
	SYSTEMATIKPOSITION("S"),
	WEITERE_TAETIGKEITSBEZEICHNUNG("T");

	/**
	 * String-Wert, der den Zusatand repräsentiert.
	 */
	private final String value;

	/**
	 * Konstruktor.
	 * 
	 * @param v
	 *            String-Wert, der den Zusatand repräsentiert
	 */
	SystematikZustand(String v) {
		value = v;
	}

	/**
	 * Liefert den String-Wert, der den Zusatand repräsentiert.
	 * 
	 * @return
	 */
	public String value() {
		return value;
	}

	/**
	 * Liefert einen SystematikZustand entsprechend seines String-Wertes.
	 * 
	 * @param v
	 *            String-Wert, der den Zusatand repräsentiert
	 * @return der Zustand
	 * @throws IllegalArgumentException,
	 *             falls String-Wert nicht bekannt ist
	 */
	public static SystematikZustand fromValue(String v) {
		for (SystematikZustand c : SystematikZustand.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
