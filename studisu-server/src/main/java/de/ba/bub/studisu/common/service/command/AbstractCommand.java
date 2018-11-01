package de.ba.bub.studisu.common.service.command;

import org.springframework.context.annotation.Scope;

/**
 * An abstract command defintion defining a default execution order.
 * <p/>
 *
 * <emphasize> A command needs to be have scope prototype if it uses instance scope (see {@link Scope @Scope} example
 * below. </emphasize>
 *
 * @author SchubertT006
 *
 * @param <S>
 *            Generic parameter defining the parameter type of {@link #execute}
 * @param <T>
 *            Generic parameter defining the return type of {@link #execute}
 */
public abstract class AbstractCommand<S, T> {

	/**
	 * Methode zum Ausführen des Kommandos.
	 *
	 * @param anfrage
	 *            Ein Anfrageparameter {@link S}.
	 * @return Ein {@link T} welche das Ergebnis der Geschäftslogik enthält.
	 */
	public T execute(S anfrage) {
		this.pruefeVorbedingungen(anfrage);
		return this.geschaeftslogikAusfuehren(anfrage);
	}

	/**
	 * Methode zum Validieren der zur Ausführung der Geschäftslogik benötigten Vorbedingungen.
	 *
	 * Die Default Implementierung enthält keine Validierungen.
	 *
	 * @param anfrage
	 *            Ein Anfrageparameter {@link S}.
	 */
	protected abstract void pruefeVorbedingungen(S anfrage);

	/**
	 * Methode zum Ausführen der Geschäftslogik
	 *
	 * @param anfrage
	 *            Ein Anfrageparameter {@link S}.
	 * @return Ein {@link T} welche das Ergebnis der Geschäftslogik enthält.
	 */
	protected abstract T geschaeftslogikAusfuehren(S anfrage);
}
