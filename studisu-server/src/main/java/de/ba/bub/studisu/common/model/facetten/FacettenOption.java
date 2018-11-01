package de.ba.bub.studisu.common.model.facetten;

/**
 * Interface für den Zugriff auf den konkreten Wert einer Facette, der
 * ausgewählt werden kann.
 *
 * Dies kann ggf. in den Implementierungen als Enum realisiert werden.
 */
public interface FacettenOption {

	/**
	 * Liefert den Namen der Facetten-Option für Anfrage im
	 * Bildungsangebotsservice.
	 *
	 * @return Name der Option.
	 */
	String getName();

	/**
	 * Liefert die ID der Facetten-Option für den Zugriff aus dem Frontend.
	 *
	 * Dies ist i.d.R. identisch mit der Datenbank-ID der Option im
	 * Bildungsangebotsservice.
	 *
	 * @return Die ID für die Option.
	 */
	int getId();

	/**
	 * Liefert die Ordnungszahl für die Sortierreihenfolge (aufsteigend!) der
	 * Option.
	 *
	 * @return Die Ordnungszahl für die Sortierung.
	 */
	int getDisplayOrder();

	/**
	 * Liefert das anzuzeigende Label der Option.
	 *
	 * @return Das Label für die Darstellung im Frontend.
	 */
	String getLabel();

	/**
	 * Flag to control if this option should be shown on the OF (json rendered)
	 * 
	 * @return Flag to control if this option should be shown on the OF (json
	 *         rendered)
	 */
	boolean show();
}
