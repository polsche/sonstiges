package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.List;

import de.ba.bub.studisu.common.model.Studiengangsinformationen;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Studienmodell;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Systematik;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

/**
 * Mapper für Studiengangsinformationen.
 */
public class StudiengangsinformationenMapper {

	private BildungsartMapper bildungsartMapper = new BildungsartMapper();
	
	private AbschlusstypMapper abschlusstypMapper = new AbschlusstypMapper();
	
	private UnterrichtsformMapper unterrichtsformMapper = new UnterrichtsformMapper();
	
	private HochschultypMapper hochschultypMapper = new HochschultypMapper();
	
	private AbschlussgradMapper abschlussgradMapper = new AbschlussgradMapper();
	
	private LehramtstypMapper lehramtstypMapper = new LehramtstypMapper();
	
	private UnterrichtsspracheMapper unterrichtsspracheMapper = new UnterrichtsspracheMapper();
	
	private StudienmodellMapper studienmodellMapper = new StudienmodellMapper();
	
	/**
	 * Mappt auf Studiengangsinformationen.
	 * 
	 * @param studienveranstaltung
	 *            die Studienveranstaltung
	 * @param veranstaltung
	 *            die Veranstaltung
	 * @return Studiengangsinformationen
	 */
	public Studiengangsinformationen map(Studienveranstaltung studienveranstaltung, Veranstaltung veranstaltung) {

		Studiengangsinformationen infos = new Studiengangsinformationen();
		
		infos.setBildungsart(bildungsartMapper.map(studienveranstaltung.getBildungsart()));
		infos.setAbschlusstyp(abschlusstypMapper.map(studienveranstaltung.getAbschlusstyp()));
		infos.setStudienform(unterrichtsformMapper.map(veranstaltung.getUnterrichtsform()));
		infos.setSchulart(hochschultypMapper.map(studienveranstaltung.getHochschultyp()));
		infos.setAbschlussgrad(abschlussgradMapper.map(studienveranstaltung.getAbschlussgrad()));
		infos.setAbschlussgradIntern(studienveranstaltung.getAbschlussgradIntern());
		if (studienveranstaltung.getRegelstudienzeitWert() != null && studienveranstaltung.getRegelstudienzeitEinheit() != null) {
			infos.setRegelstudienzeit(studienveranstaltung.getRegelstudienzeitWert() + " " + studienveranstaltung.getRegelstudienzeitEinheit().value());
		}
		infos.setLehramtsbefaehigung(studienveranstaltung.isLehramtsbefaehigung());
		infos.setLehramtstyp(lehramtstypMapper.map(studienveranstaltung.getLehramtstypen()));
		infos.setUnterrichtssprache(unterrichtsspracheMapper.map(veranstaltung.getHauptunterrichtssprache()));
		infos.setInternationalerDoppelabschluss(studienveranstaltung.isInternationalerDoppelabschluss());
		List<Studienmodell> studienmodelle = studienveranstaltung.getStudienmodell();
		for (Studienmodell studienmodell : studienmodelle) {
			infos.addDualesStudienmodell(studienmodellMapper.map(studienmodell));
		}
		for (Systematik systematik : studienveranstaltung.getSystematik()) {
			infos.addStudienfach(systematik.getDkzBezeichnung());
		}
		
		return infos;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setBildungsartMapper(BildungsartMapper bildungsartMapper) {
		this.bildungsartMapper = bildungsartMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setAbschlusstypMapper(AbschlusstypMapper abschlusstypMapper) {
		this.abschlusstypMapper = abschlusstypMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setUnterrichtsformMapper(UnterrichtsformMapper unterrichtsformMapper) {
		this.unterrichtsformMapper = unterrichtsformMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setHochschultypMapper(HochschultypMapper hochschultypMapper) {
		this.hochschultypMapper = hochschultypMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setAbschlussgradMapper(AbschlussgradMapper abschlussgradMapper) {
		this.abschlussgradMapper = abschlussgradMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setLehramtstypMapper(LehramtstypMapper lehramtstypMapper) {
		this.lehramtstypMapper = lehramtstypMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setUnterrichtsspracheMapper(UnterrichtsspracheMapper unterrichtsspracheMapper) {
		this.unterrichtsspracheMapper = unterrichtsspracheMapper;
	}

	/**
	 * NUR FÜR TESTS! 
	 */
	void setStudienmodellMapper(StudienmodellMapper studienmodellMapper) {
		this.studienmodellMapper = studienmodellMapper;
	}
	
}
