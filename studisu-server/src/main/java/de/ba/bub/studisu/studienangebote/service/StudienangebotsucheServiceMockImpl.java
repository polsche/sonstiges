package de.ba.bub.studisu.studienangebote.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.ba.bub.studisu.common.model.AdresseKurz;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.common.model.StudienangebotWrapperMitAbstand;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienangebotFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;


/**
 * Mock implementierung für studienangebote für SET
 *
 * @Author HBO
 */
@Service("mock-studienangebote")
public class StudienangebotsucheServiceMockImpl implements StudienangebotsucheService {

    @Override
    public StudienangebotsucheErgebnis suche(final StudienangebotsucheAnfrage anfrage) {

		List<StudienangebotWrapperMitAbstand> mockedStudienangebote = new ArrayList<>();

        Studienangebot a1 = new Studienangebot();
        a1.setId("1");
        a1.setBildungsanbieterName("See und Meeres Universität Helgoland");
        a1.setStudiBeginn("flexibel");
        a1.setStudiBezeichnung("High perfomance anaerobic fishing");
        a1.setStudiInhalt("Navigation, Meeresbiologie\nUmeltschutz");
        a1.setStudienform(StudienformFacettenOption.VOLLZEIT);
        a1.setStudientyp(StudientypFacettenOption.GRUNDSTAENDIG);
        AdresseKurz adr1 = new AdresseKurz();
        adr1.setOrt("Helgoland");
        adr1.setPostleitzahl("00000");
        adr1.setStrasse("Die eine Strasse 0");
        a1.setStudienort(adr1);
		mockedStudienangebote.add(new StudienangebotWrapperMitAbstand(a1, null));

        Studienangebot a2 = new Studienangebot();
        a2.setId("2");
        a2.setBildungsanbieterName("See und Meeres Universität Hanau");
        a2.setStudiBeginn("flexibel");
        a2.setStudiBezeichnung("High speed vehicle maneuvering");
        a2.setStudiInhalt("Navigation, Vechicle Engineering, Fitness,");
        a2.setStudienform(StudienformFacettenOption.VOLLZEIT);
        a2.setStudientyp(StudientypFacettenOption.WEITERFUEHREND);
        AdresseKurz adr2 = new AdresseKurz();
        adr2.setOrt("Hanau");
        adr2.setPostleitzahl("10123");
        adr2.setStrasse("Dritte Straße von Links 42");
        a2.setStudienort(adr2);
		mockedStudienangebote.add(new StudienangebotWrapperMitAbstand(a2, null));

        final StudienangebotsucheErgebnis ergebnis = StudienangebotsucheErgebnis.withItems(mockedStudienangebote, new ArrayList<StudienangebotFacette>(), 0);

        for (int i = 3; i < 200 + 3; i++) {

            Studienangebot a = new Studienangebot();
            a.setId(String.valueOf(i));
            a.setBildungsanbieterName("Universität " + i % 44);
            a.setStudiBeginn("flexibel");
            a.setStudiBezeichnung("Studium " + i % 18);
            a.setStudiInhalt("Studiumsinhalt " + i % 97);
            a.setStudienform(StudienformFacettenOption.VOLLZEIT);
            AdresseKurz adr = new AdresseKurz();
            adr.setOrt("Stadt " + i % 44);
            adr.setPostleitzahl(String.valueOf(10000 + i % 44));
            adr.setStrasse("Straße " + i % 44);
            a.setStudienort(adr);
			mockedStudienangebote.add(new StudienangebotWrapperMitAbstand(a, null));
        }
        return ergebnis;
    }
}
