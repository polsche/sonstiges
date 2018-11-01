package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import de.ba.bub.studisu.common.model.Dauer;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

/**
 * Mapper Veranstaltung zu Dauer-DTO.
 */
public class DauerMapper {

	private ZulassungssemesterMapper zulassungssemesterMapper = new ZulassungssemesterMapper();

	private static final String AUF_ANFRAGE = "Auf Anfrage";

	/**
	 * Mappt Veranstaltung zu Dauer-DTO.
	 * 
	 * @param veranstaltung
	 *            die Veranstaltung
	 * @return die Dauer
	 */
	public Dauer map(Veranstaltung veranstaltung) {
		Dauer dauer = new Dauer();
		dauer.setBeginn(getDateAsString(veranstaltung.getZeitBeginn(), AUF_ANFRAGE));
		dauer.setEnde(getDateAsString(veranstaltung.getZeitEnde(), AUF_ANFRAGE));
		dauer.setZulassungssemester(zulassungssemesterMapper.map(veranstaltung.getZulassungssemester()));
		dauer.setIndividuellerEinstieg(veranstaltung.isIndividuellerEinstieg());
		dauer.setUnterrichtszeiten(veranstaltung.getVorlesungszeiten());
		dauer.setBemerkung(veranstaltung.getBemerkungZeit());
		return dauer;
	}

	/**
	 * Konvertiert XMLGregorianCalendar in einen String in die Form
	 * <code>dd.MM.yyyy</code>.<br>
	 * Falls Calendar nicht vorhanden ist (<code>null</code>), wird notNullValue
	 * zurueckgeliefert.
	 *
	 * @param xmlGregorianCalendar
	 * @param notNullValue
	 * @return
	 */
	private static String getDateAsString(XMLGregorianCalendar xmlGregorianCalendar, String notNullValue) {
		String result = notNullValue;

		if (xmlGregorianCalendar != null) {
			GregorianCalendar calendar = xmlGregorianCalendar.toGregorianCalendar();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
			simpleDateFormat.setCalendar(calendar);
			result = simpleDateFormat.format(calendar.getTime());
		}

		return result;
	}
	
	/**
	 * NUR FÜR TESTS!
	 */
	void setZulassungssemesterMapper(ZulassungssemesterMapper zulassungssemesterMapper) {
		this.zulassungssemesterMapper = zulassungssemesterMapper;
	}
}
