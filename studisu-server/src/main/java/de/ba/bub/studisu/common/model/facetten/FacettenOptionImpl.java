package de.ba.bub.studisu.common.model.facetten;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstrakte Basisklasse für die Facetten-Optionen.
 * 
 * Sie enthält eine Implementierung des Interfaces {@link FacettenOption} und stellt für die Unterklassen außerdem den
 * für das Caching benötigten Hashwert und die equals()-Methode bereit.
 * 
 * @author SchneideK084
 */
abstract public class FacettenOptionImpl implements FacettenOption, Serializable {

	private static final long serialVersionUID = -3870905996851624248L;

	@JsonIgnore
	private String name;

	private int id;

	@JsonIgnore
	private int displayOrder;

	private String label;

	@JsonIgnore
	private boolean show;

	/**
	 * Konstruktor der generischen Implementierung für Facetten-Optionen.
	 *
	 * @param name
	 *            Der Name der Facetten-Option.
	 * @param id
	 *            Die ID der Facetten-Option.
	 * @param displayOrder
	 *            Sortierreihenfolge für die Facetten-Option, Anzeige aufsteigend nach Wert.
	 * @param label
	 *            Das anzuzeigende Label (Bezeichnung) für die Facetten-Option.
	 * @param show
	 *            Flag, das angibt, ob diese Facetten-Option in's JSON für die Oberfläche exportiert wird.
	 */
	protected FacettenOptionImpl(String name, int id, int displayOrder, String label, boolean show) {
		this.name = (null == name) ? "" : name;
		this.id = id;
		this.displayOrder = displayOrder;
		this.label = (null == label) ? "" : label;
		this.show = show;
	}

	/**
	 * Leerer Konstruktor für eh-Cache
	 */
	public FacettenOptionImpl() {
		this("", 0, 0, "", true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean show() {
		return show;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final FacettenOption that = (FacettenOption) o;

		boolean isEqual = this.id == that.getId();
		isEqual &= this.displayOrder == that.getDisplayOrder();
		isEqual &= this.name.equals(that.getName());
		isEqual &= this.label.equals(that.getLabel());

		return isEqual;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + id;
		result = 31 * result + displayOrder;
		result = 31 * result + label.hashCode();
		return result;
	}

}
