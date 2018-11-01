package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import de.ba.bub.studisu.common.model.OhneAbiZugangsbedingung;
import de.ba.bub.studisu.common.model.Zugangsinformationen;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.ZugangsbedingungStudierenOhneAbi;

/**
 * Mapper für Zugangsinformationen.
 */
public class ZugangsinformationenMapper {

	private ZulassungsmodusMapper zulassungsmodusMapper = new ZulassungsmodusMapper();
	
	private ZugangsbedingungMapper zugangsbedingungMapper = new ZugangsbedingungMapper();
	
	private AkkreditierungsMapper akkreditierungsMapper = new AkkreditierungsMapper();

	/**
	 * Mappt auf ein Zugangsinformationen.
	 * 
	 * @param studienveranstaltung
	 *            die Studienveranstaltung
	 * @param veranstaltung
	 *            die Veranstaltung
	 * @return Zugang
	 */
	public Zugangsinformationen map(Studienveranstaltung studienveranstaltung, Veranstaltung veranstaltung) {

		Zugangsinformationen zugang = new Zugangsinformationen();
		zugang.setZulassungsmodus(zulassungsmodusMapper.map(veranstaltung.getZulassungsmodus()));
		zugang.setZulassungsmodusInfo(veranstaltung.getZulassungsmodusInfo());
		zugang.setVoraussetzungen(studienveranstaltung.getZugang());
		zugang.setOhneAbiMoeglich(studienveranstaltung.isStudierenOhneAbi());
		List<ZugangsbedingungStudierenOhneAbi> zugangsbedingungenStudierenOhneAbi = studienveranstaltung
				.getZugangsbedingungStudierenOhneAbi();
		for (ZugangsbedingungStudierenOhneAbi zugangsbedingungStudierenOhneAbi : zugangsbedingungenStudierenOhneAbi) {
			OhneAbiZugangsbedingung ohneAbiZugangsbedingung = new OhneAbiZugangsbedingung();
			ohneAbiZugangsbedingung
					.setBedingung(zugangsbedingungMapper.map(zugangsbedingungStudierenOhneAbi.getZugangsbedingung()));
			ohneAbiZugangsbedingung.setBemerkung(zugangsbedingungStudierenOhneAbi.getBemerkungen());
			zugang.addOhneAbiZugangsbedingung(ohneAbiZugangsbedingung);
		}
		zugang.setAkkreditierung(akkreditierungsMapper.map(studienveranstaltung.getAkkreditierung()));
		zugang.setAkkreditierungVon(getDateAsString(studienveranstaltung.getAkkreditierungVon()));
		zugang.setAkkreditierungBis(getDateAsString(studienveranstaltung.getAkkreditierungBis()));
		zugang.setAkkreditierungsbedingungen(studienveranstaltung.getAkkreditierungBedingung());
		return zugang;
	}

	private String getDateAsString(XMLGregorianCalendar xmlGregorianCalendar) {
		String result = null;
		if (xmlGregorianCalendar != null) {
			GregorianCalendar calendar = xmlGregorianCalendar.toGregorianCalendar();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
			simpleDateFormat.setCalendar(calendar);
			result = simpleDateFormat.format(calendar.getTime());
		}
		return result;
	}
	
	/**
	 * NUR FUER TESTS!
	 */
	void setAkkreditierungsMapper(AkkreditierungsMapper akkreditierungsMapper) {
		this.akkreditierungsMapper = akkreditierungsMapper;
	}
	
	/**
	 * NUR FUER TESTS!
	 */
	void setZugangsbedingungMapper(ZugangsbedingungMapper zugangsbedingungMapper) {
		this.zugangsbedingungMapper = zugangsbedingungMapper;
	}
	
	/**
	 * NUR FUER TESTS!
	 */
	void setZulassungsmodusMapper(ZulassungsmodusMapper zulassungsmodusMapper) {
		this.zulassungsmodusMapper = zulassungsmodusMapper;
	}
}
