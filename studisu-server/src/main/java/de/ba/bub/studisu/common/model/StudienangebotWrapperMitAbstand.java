package de.ba.bub.studisu.common.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Ergänzt das Studienangebot um weitere Infromationen (Abstände).
 * 
 * Das Studienangebot wird dabei nicht direkt verändert (ermöglicht Caching und
 * Nebenläufigkeit).
 */
public final class StudienangebotWrapperMitAbstand implements Serializable {

	private static final long serialVersionUID = 7252879536899545404L;

	private Studienangebot studienangebot;

	private List<SuchortAbstand> abstaende;

	/**
	 * Wrappt ein Studienangebot mit den Abständen zu seinen Suchorten.
	 * 
	 * @param studienangebot
	 *            Das anzureichende Studienangebot.
	 * @param abstaende
	 *            Die Liste mit den Abständen des Studienortes von den
	 *            Suchorten.
	 */
	public StudienangebotWrapperMitAbstand(Studienangebot studienangebot, List<SuchortAbstand> abstaende) {
		setAbstaende(abstaende);
		setStudienangebot(studienangebot);
	}

	/**
	 * Liefert das gespeicherte Studienangebot.
	 * 
	 * @return das gespeicherte Studienangebot.
	 */
	public Studienangebot getStudienangebot() {
		return this.studienangebot;
	}

	/**
	 * Speichert ein neues Studienangebot.
	 * 
	 * @param studienangebot
	 *            Das zu speichernde Studienangebot.
	 */
	public void setStudienangebot(Studienangebot studienangebot) {
		this.studienangebot = studienangebot;
	}

	/**
	 * Liefert die gespeicherte Ortliste mit Abständen.
	 * 
	 * @return die gespeicherte Ortliste mit Abständen.
	 */
	public List<SuchortAbstand> getAbstaende() {
		return this.abstaende;
	}

	/**
	 * Speichert eine neue Ortsliste mit Abständen.
	 * 
	 * @param abstaende
	 *            Die zu speichernde Ortsliste mit Abständen.
	 */
	public void setAbstaende(List<SuchortAbstand> abstaende) {
		if (null == abstaende || abstaende.isEmpty()) {
			this.abstaende = Collections.<SuchortAbstand>emptyList();
		} else {
			this.abstaende = Collections.unmodifiableList(abstaende);
		}
	}
}
