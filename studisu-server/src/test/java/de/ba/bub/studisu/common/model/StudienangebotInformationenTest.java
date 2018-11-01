package de.ba.bub.studisu.common.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;

import org.junit.Test;

import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;

public class StudienangebotInformationenTest {

	@Test
	public void test() {
		
		StudienangebotInformationen cut = new StudienangebotInformationen();
		
		assertNull(cut.getAktualisierungsdatum());
		assertNull(cut.getBezeichnung());
		assertNull(cut.getBildungsanbieter());
		assertEquals(0, cut.getBildungsanbieterId());
		assertEquals(1, cut.getCurrentPage());
		assertNull(cut.getDauer());
		assertNull(cut.getHochschulart());
		assertNull(cut.getId());
		assertNull(cut.getInhalt());
		assertFalse(cut.getIsHrkDatensatz());
		assertNull(cut.getKontakt());
		assertNull(cut.getNextElementId());
		assertEquals(0, cut.getNumNextElements());
		assertEquals(0, cut.getNumPrevElements());
		assertNull(cut.getPrevElementId());
		assertNull(cut.getStudienfaecherCsv());
		assertNull(cut.getStudienform());
		assertNull(cut.getStudienort());
		assertNull(cut.getStudientyp());
		assertNull(cut.getVeranstaltungZusatzlink());
		assertFalse(cut.getBildungsanbieterHasSignet());
		
		String aktualisierungsdatum = "aktualisierungsdatum";
		String bezeichnung = "bezeichnung";
		AdresseKurz bildungsanbieter = new AdresseKurz();
		int bildungsanbieterId = 12;
		int currentPage = 2;
		Dauer dauer = new Dauer();
		HochschulartFacettenOption hochschulart = HochschulartFacettenOption.BERUFSBILDUNGSWERK;
		String veranstaltungsid = "veranstaltungsid";
		String inhalt = "inhalt";
		boolean hrkDatensatz = true;
		Kontakt kontakt = new Kontakt();
		String nextElementId = "nextElementId";
		int numNextElements = 5;
		int numPrevElements = 6;
		String prevElementId = "prevElementId";
		String studienfaecherCsv = "studienfaecherCsv";
		StudienformFacettenOption studienform = StudienformFacettenOption.BLOCKUNTERRICHT;
		AdresseKurz studienort = new AdresseKurz();
		StudientypFacettenOption studientyp = StudientypFacettenOption.WEITERFUEHREND;
		String veranstaltungZusatzlink = "veranstaltungZusatzlink";

		cut.setAktualisierungsdatum(aktualisierungsdatum);
		cut.setBezeichnung(bezeichnung);
		cut.setBildungsanbieter(bildungsanbieter);
		cut.setBildungsanbieterId(bildungsanbieterId);
		cut.setCurrentPage(currentPage);
		cut.setDauer(dauer);
		cut.setHochschulart(hochschulart);
		cut.setId(veranstaltungsid);
		cut.setInhalt(inhalt);
		cut.setIsHrkDatensatz(hrkDatensatz);
		cut.setKontakt(kontakt);
		cut.setNextElementId(nextElementId);
		cut.setNumNextElements(numNextElements);
		cut.setNumPrevElements(numPrevElements);
		cut.setPrevElementId(prevElementId);
		cut.setStudienfaecherCsv(studienfaecherCsv);
		cut.setStudienform(studienform);
		cut.setStudienort(studienort);
		cut.setStudientyp(studientyp);
		cut.setVeranstaltungZusatzlink(veranstaltungZusatzlink);
		cut.setBildungsanbieterHasSignet(true);
		
		assertEquals(aktualisierungsdatum, cut.getAktualisierungsdatum());
		assertEquals(bezeichnung, cut.getBezeichnung());
		assertEquals(bildungsanbieter, cut.getBildungsanbieter());
		assertEquals(bildungsanbieterId, cut.getBildungsanbieterId());
		assertEquals(currentPage, cut.getCurrentPage());
		assertEquals(dauer, cut.getDauer());
		assertEquals(hochschulart, cut.getHochschulart());
		assertEquals(veranstaltungsid, cut.getId());
		assertEquals(inhalt, cut.getInhalt());
		assertEquals(hrkDatensatz, cut.getIsHrkDatensatz());
		assertEquals(kontakt, cut.getKontakt());
		assertEquals(nextElementId, cut.getNextElementId());
		assertEquals(numNextElements, cut.getNumNextElements());
		assertEquals(numPrevElements, cut.getNumPrevElements());
		assertEquals(prevElementId, cut.getPrevElementId());
		assertEquals(studienfaecherCsv, cut.getStudienfaecherCsv());
		assertEquals(studienform, cut.getStudienform());
		assertEquals(studienort, cut.getStudienort());
		assertEquals(studientyp, cut.getStudientyp());
		assertEquals(veranstaltungZusatzlink, cut.getVeranstaltungZusatzlink());
		assertEquals(true, cut.getBildungsanbieterHasSignet());
	}
	
	@Test
	public void testAddExternalLink() throws URISyntaxException {
		
		StudienangebotInformationen cut = new StudienangebotInformationen();
		assertNotNull(cut.getExternalLinks());
		assertEquals(0, cut.getExternalLinks().size());
		
		ExternalLink externalLink1 = new ExternalLink("linkname1", "http://arbeitsagentur.de", "tooltip1");
		cut.addExternalLink(externalLink1);
		
		assertEquals(1, cut.getExternalLinks().size());
		assertEquals(externalLink1, cut.getExternalLinks().get(0));
		
		ExternalLink externalLink2 = new ExternalLink("linkname2", "http://www.arbeitsagentur.de", "tooltip2");
		cut.addExternalLink(externalLink2);
		
		assertEquals(2, cut.getExternalLinks().size());
		assertEquals(externalLink1, cut.getExternalLinks().get(0));
		assertEquals(externalLink2, cut.getExternalLinks().get(1));
	}

}
