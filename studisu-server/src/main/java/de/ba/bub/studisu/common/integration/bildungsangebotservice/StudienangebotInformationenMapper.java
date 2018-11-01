package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import de.ba.bub.studisu.common.model.ExternalLink;
import de.ba.bub.studisu.common.model.StudienangebotInformationen;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Eingabeverfahren;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Studienveranstaltung;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Systematik;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.getstudienveranstaltungbyid_v_1.Veranstaltung;

/**
 * Hilfsklasse / Mapper
 *
 * @author FuchsD013
 */
public class StudienangebotInformationenMapper {
	
    private final static Logger LOGGER = LoggerFactory.getLogger(StudienangebotInformationenMapper.class);
    
    private DauerMapper dauerMapper = new DauerMapper();
    
    private ZugangsinformationenMapper zugangsinformationenMapper = new ZugangsinformationenMapper();
    
    private StudiengangsinformationenMapper studiengangsinformationenMapper = new StudiengangsinformationenMapper();
    
    private StudienanbieterMapper studienanbieterMapper = new StudienanbieterMapper();
    
    private StudienortMapper studienortMapper = new StudienortMapper();
    
    private KontaktMapper kontaktMapper = new KontaktMapper();

    /**
     * Mappt die übergebene Studienveranstaltung und die Veranstaltung zu einem Studienangebot
     *
     * @param studienveranstaltung
     * @param veranstaltung
     * @return das gemappte Studienangebot
     */
    public StudienangebotInformationen map(Studienveranstaltung studienveranstaltung, Veranstaltung veranstaltung) {
        
    	StudienangebotInformationen sa = new StudienangebotInformationen();
        
        sa.setBezeichnung(studienveranstaltung.getTitel());

        //studieninhalte
        sa.setInhalt(studienveranstaltung.getInhalte());
        sa.setVeranstaltungZusatzlink(veranstaltung.getZusatzlink());
        sa.setStudienschwerpunkte(studienveranstaltung.getStudienschwerpunkte());
        sa.setStudiuminformationen(studienveranstaltung.getStudiuminformation());
        
        // dauer und termine
        sa.setDauer(dauerMapper.map(veranstaltung));
        
        // kosten/gebühren/förderung
        sa.setKosten(veranstaltung.getBemerkungKosten());
        
        // zugangsinformationen;
        sa.setZugangsinformationen(zugangsinformationenMapper.map(studienveranstaltung, veranstaltung));
        
        // studiengangsinformationen
        sa.setStudiengangsinformationen(studiengangsinformationenMapper.map(studienveranstaltung, veranstaltung));
        
        // studienanbieter
		sa.setBildungsanbieter(studienanbieterMapper.map(studienveranstaltung.getBildungsanbieter()));

        // studienort setzen
     	sa.setStudienort(studienortMapper.map(veranstaltung));
     	
     	// kontakt
     	sa.setKontakt(kontaktMapper.map(studienveranstaltung.getBildungsanbieter()));
        
        // veröffentlichungsinfos
     	sa.setId(String.valueOf(veranstaltung.getId())); // Nicht die ID eds Studienangebots!
     	sa.setAktualisierungsdatum(getDateAsString(veranstaltung.getAktualisierungsdatum(), null));
        sa.setIsHrkDatensatz(Eingabeverfahren.H_RK.equals(studienveranstaltung.getBildungsanbieter().getEingabeverfahren()));
        String osaUrl = studienveranstaltung.getOsaUrl();
        if (!StringUtils.isEmpty(osaUrl)) {
            ExternalLink osaLink = null;
            try {
                osaLink = new ExternalLink("osa", osaUrl, "Für diesen Studiengang ist ein webbasierter Selbsttest verfügbar.");
            } catch (URISyntaxException e) {
                LOGGER.error("osa url fehlerhaft", e);
            }
            sa.addExternalLink(osaLink);
        }
        String studicheckURL = studienveranstaltung.getStudicheckUrl();
        if (!StringUtils.isEmpty(studicheckURL) ) {
            ExternalLink studicheckLink = null;
            try {
                studicheckLink = new ExternalLink("studicheck", studicheckURL, "Für diesen Studiengang sind zugeschnittene Studicheck-Tests verfügbar.");
            } catch (URISyntaxException e) {
                LOGGER.error("studicheck url fehlerhaft", e);
            }
            sa.addExternalLink(studicheckLink);
        }
        
        // additional infos
        sa.setBildungsanbieterId(studienveranstaltung.getBildungsanbieter().getId());
        sa.setBildungsanbieterHasSignet(studienveranstaltung.getBildungsanbieter().isHasSignet());
        setStudienfaecher(sa, studienveranstaltung.getSystematik());
        
        // Facettenoptionen!
        sa.setStudientyp(StudientypFacettenOption.forSystematiken(convertSystematik(studienveranstaltung.getSystematik())));
        if (veranstaltung.getUnterrichtsform() != null) {
        	sa.setStudienform(StudienformFacettenOption.forName(veranstaltung.getUnterrichtsform().value()));
        } else {
        	sa.setStudienform(StudienformFacettenOption.SONSTIGE);
        }
        if (studienveranstaltung.getHochschultyp() != null) {
        	sa.setHochschulart(HochschulartFacettenOption.forName(studienveranstaltung.getHochschultyp().value()));
        } else {
        	sa.setHochschulart(HochschulartFacettenOption.KEINE_ZUORDNUNG_MOEGLICH);
        }
        
        return sa;
    }

	/**
     * Konvertiert die übergebenen Systematiken der Methode getVeranstaltungById()
     * zu Systematiken der Methode findStudienveranstaltungen().
     * In {@link StudientypFacettenOption} gibt es bereits eine
     * Methode, die den Studientyp aus den Systematiken ermittelt.
     * Da diese Methode eine Liste der Klasse Systematik der Methode
     * findStudienveranstaltungen() erwartet, müssen die Systematiken
     * der Methode getVeranstaltungById() konvertiert werden.
     * @param systematiken Systematiken der Methode getVeranstaltungById().
     * @return Systematiken der Methode findStudienveranstaltungen().
     */
    private List<de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik> 
    		convertSystematik(List<Systematik> systematiken) {
    	
    	List<de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik> result = new ArrayList<>();
    	
    	for(Systematik sysOld : systematiken) {
    		de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik sysNew =
    				new de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik();
    		
    		sysNew.setDkzId(sysOld.getDkzId());
    		sysNew.setDkzCodenr(sysOld.getDkzCodenr());
    		sysNew.setDkzBezeichnung(sysOld.getDkzBezeichnung());
    		
    		result.add(sysNew);
    	}
    	
    	return result;
    }

	/**
	 * Ergänzt das übergebene StudienangebotInformationen-Objekt
	 * um die Informationen zu Studienfaecher, falls vorhanden.
	 *
	 * @param sa das zu ergänzende Studienangebot.
	 * @param systematik die Systematiken/Studienfaecher.
	 */
	private static void setStudienfaecher(StudienangebotInformationen sa, List<Systematik> systematik) {
		StringBuilder studienFaecherCsvSb= new StringBuilder();
		
		for(Systematik sys : systematik) {
			if (!studienFaecherCsvSb.toString().isEmpty()) {
				studienFaecherCsvSb.append(";");
			}
			studienFaecherCsvSb.append(sys.getDkzId());  
		}
		
		sa.setStudienfaecherCsv(studienFaecherCsvSb.toString());
	}

    /**
     * Konvertiert XMLGregorianCalendar in einen String in die Form <code>dd.MM.yyyy</code>.<br>
     * Falls Calendar nicht vorhanden ist (<code>null</code>), wird notNullValue zurueckgeliefert.
     *
     * @param xmlGregorianCalendar
     * @param notNullValue
     * @return
     */
    private String getDateAsString(XMLGregorianCalendar xmlGregorianCalendar, String notNullValue) {
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
	void setDauerMapper(DauerMapper dauerMapper) {
		this.dauerMapper = dauerMapper;
	}

	/**
	 * NUR FÜR TESTS!
	 */
	void setZugangsinformationenMapper(ZugangsinformationenMapper zugangsinformationenMapper) {
		this.zugangsinformationenMapper = zugangsinformationenMapper;
	}

	/**
	 * NUR FÜR TESTS!
	 */
	void setStudiengangsinformationenMapper(StudiengangsinformationenMapper studiengangsinformationenMapper) {
		this.studiengangsinformationenMapper = studiengangsinformationenMapper;
	}

	/**
	 * NUR FÜR TESTS!
	 */
	void setStudienanbieterMapper(StudienanbieterMapper studienanbieterMapper) {
		this.studienanbieterMapper = studienanbieterMapper;
	}

	/**
	 * NUR FÜR TESTS!
	 */
	void setStudienortMapper(StudienortMapper studienortMapper) {
		this.studienortMapper = studienortMapper;
	}

	/**
	 * NUR FÜR TESTS!
	 */
	void setKontaktMapper(KontaktMapper kontaktMapper) {
		this.kontaktMapper = kontaktMapper;
	}
    
    

}
