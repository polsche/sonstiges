package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object
 * 
 * @author csl
 */
public class StudienInformationDTO implements Serializable {

	private static final long serialVersionUID = -888027710626147481L;

	private List<String> studienfeldbeschreibungen;
	private List<String> studiengangsbezeichnungen;
	private int studienfachFilmId;

	/**
	 * Constructor initializing Lists to avoid null where getter would run into
	 * nullpointer due to Collections.unmodifiableList
	 */
	public StudienInformationDTO() {
		this.studienfeldbeschreibungen = new ArrayList<>();
		this.studiengangsbezeichnungen = new ArrayList<>();
	}

	public List<String> getStudienfeldbeschreibungen() {
		return Collections.unmodifiableList(studienfeldbeschreibungen);
	}

	public void setStudienfeldbeschreibungen(List<String> studienfeldbeschreibungen) {
		this.studienfeldbeschreibungen = new ArrayList<>(studienfeldbeschreibungen);
	}

	public List<String> getStudiengangsbezeichnungen() {
		return Collections.unmodifiableList(studiengangsbezeichnungen);
	}

	public void setStudiengangsbezeichnungen(List<String> studiengangsbezeichnungen) {
		this.studiengangsbezeichnungen = new ArrayList<>(studiengangsbezeichnungen);
	}

	public int getStudienfachFilmId() {
		return studienfachFilmId;
	}

	public void setStudienfachFilmId(int studienfachFilmId) {
		this.studienfachFilmId = studienfachFilmId;
	}

}
