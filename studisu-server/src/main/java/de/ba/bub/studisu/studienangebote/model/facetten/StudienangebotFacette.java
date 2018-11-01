package de.ba.bub.studisu.studienangebote.model.facetten;

import java.util.Set;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.facetten.Facette;
import de.ba.bub.studisu.common.model.facetten.FacettenOption;

/**
 * Abstracte Basisklasse für Facetten zu den den Studienangeboten bei der Suche nach Studienangeboten im Backend.
 *
 * @author loutzenhj on 06.04.2017.
 */
public abstract class StudienangebotFacette extends Facette {

	/**
	 * Der Separator, mit dem die Werte der Optionen eines Parameters getrennt werden.
	 */
	public final static String VALUE_SEPARATOR =";";

	/**
	 * Constructor.
	 *
	 * @param id  Id der Facette, fuer eindeutige Identifikation
	 * @param typ {@link Typ} der Facette
	 */
	protected StudienangebotFacette(String id, Typ typ) {
		super(id, typ);
	}

	/**
	 * Liefert die FacettenOptionen, die zum übergebenen Studienangebot passen.
	 * 
	 * @param studienangebot
	 *            Studienangebot
	 * @return nie null
	 */
	public abstract Set<FacettenOption> findOptions(Studienangebot studienangebot);

	/**
	 * Should return itself with all options added
	 * @return this with all options added 
     */
	public abstract StudienangebotFacette withAllOptions();

}
