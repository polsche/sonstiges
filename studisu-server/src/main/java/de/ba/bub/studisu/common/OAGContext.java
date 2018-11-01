package de.ba.bub.studisu.common;

/**
 * Created by SiegelP003 on 05.07.2016.
 */
public enum OAGContext {
	PUBLIC("pc"), PROTECTED("pd"), CONFIDENTIAL("cl");

	private final String alias;

	/**
	 * C-Tor.
	 *
	 * @param value
	 *            Das zu verwendende Alias.
	 */
	OAGContext(final String value) {
		this.alias = value;
	}

	/**
	 * Liefert das im Kontruktor gesetzte Alias.
	 *
	 * @return Das gesetzt Alias.
	 */
	public String alias() {
		return alias;
	}
}
