package de.ba.bub.studisu.studienangebote.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheAnfrage;
import de.ba.bub.studisu.studienangebote.model.StudienangebotsucheErgebnis;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.RegionenFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.FitFuerStudiumFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.HochschulartFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacette;
import de.ba.bub.studisu.studienangebote.model.facetten.StudientypFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;
import de.ba.bub.studisu.studienangebote.model.requestparams.Paging;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;
import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfelder;

/**
 * Tests füpr den {@link StudienangebotsucheErgebnisBuilder}.
 * 
 * @author StraubP
 */
public class StudienangebotsucheErgebnisBuilderTest {

	List<Studienangebot> studienangebote = new ArrayList<Studienangebot>();
	StudienangebotsucheAnfrage anfrage;
	StudienangebotsucheAnfrage anfrageMitAllenFilter;

	@Before
	public void prepare() {
		Studienangebot sa1 = sa(1, StudientypFacettenOption.GRUNDSTAENDIG, StudienformFacettenOption.VOLLZEIT,
				HochschulartFacettenOption.UNIVERSITAET, FitFuerStudiumFacettenOption.STUDICHECK,
				RegionenFacettenOption.BAYERN);
		Studienangebot sa2 = sa(1, StudientypFacettenOption.WEITERFUEHREND, StudienformFacettenOption.VOLLZEIT,
				HochschulartFacettenOption.FACHHOCHSCHULE, FitFuerStudiumFacettenOption.OSA,
				RegionenFacettenOption.BERLIN);
		Studienangebot sa3 = sa(1, StudientypFacettenOption.WEITERFUEHREND, StudienformFacettenOption.TEILZEIT,
				HochschulartFacettenOption.BERUFSAKADEMIE, FitFuerStudiumFacettenOption.OSA,
				RegionenFacettenOption.SACHSEN);
		Studienangebot sa4 = sa(1, StudientypFacettenOption.GRUNDSTAENDIG, StudienformFacettenOption.SONSTIGE,
				HochschulartFacettenOption.BERUFSAKADEMIE, FitFuerStudiumFacettenOption.OSA,
				RegionenFacettenOption.SACHSEN);
		Studienangebot sa5 = sa(1, StudientypFacettenOption.WEITERFUEHREND, StudienformFacettenOption.TEILZEIT,
				HochschulartFacettenOption.FACHHOCHSCHULE, FitFuerStudiumFacettenOption.STUDICHECK,
				RegionenFacettenOption.NORDRHEIN_WESTFALEN);
		
		studienangebote.add(sa1);
		studienangebote.add(sa2);
		studienangebote.add(sa3);
		studienangebote.add(sa4);
		studienangebote.add(sa5);
		
		List<StudientypFacettenOption> optionsStudientyp = new ArrayList<>();
		List<FitFuerStudiumFacettenOption> optionsFitFuerStudium = new ArrayList<>();
		List<StudienformFacettenOption> optionsStudienform = new ArrayList<>();
		List<RegionenFacettenOption> optionsBundesland = new ArrayList<>();
		List<HochschulartFacettenOption> optionsHochschulart = new ArrayList<>();

		optionsStudientyp.clear();
		optionsStudientyp.add(StudientypFacettenOption.GRUNDSTAENDIG);
		optionsStudientyp.add(StudientypFacettenOption.WEITERFUEHREND);
		StudientypFacette studientypFacette = new StudientypFacette(optionsStudientyp);

		optionsStudienform.clear();
		optionsStudienform.add(StudienformFacettenOption.VOLLZEIT);
		optionsStudienform.add(StudienformFacettenOption.SONSTIGE);
		StudienformFacette studienformFacette = new StudienformFacette(optionsStudienform);


		HochschulartFacette hochschulartFacette = null; // entspricht: alle Optionen
		Studienfelder studienfelder = null; // brauchen wir hier nicht
		Studienfaecher studienfaecher = null; // brauchen wir hier nicht
		AnfrageOrte anfrageOrte = null; // brauchen wir hier nicht
		UmkreisFacette umkreisFacette = null; // brauchen wir hier nicht
		Paging sortingPaging = null; // brauchen wir hier nicht
		RegionenFacette bundeslandFacette = null; // entspricht: alle Optionen
		FitFuerStudiumFacette fitFuerStudiumFacette = null;

		anfrage = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte, studienformFacette,
				hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging, studientypFacette,
				bundeslandFacette);
		
		optionsStudientyp.clear();
		optionsStudientyp.add(StudientypFacettenOption.WEITERFUEHREND);
		studientypFacette = new StudientypFacette(optionsStudientyp);

		optionsStudienform.clear();
		optionsStudienform.add(StudienformFacettenOption.TEILZEIT);
		studienformFacette = new StudienformFacette(optionsStudienform);

		optionsFitFuerStudium.clear();
		optionsFitFuerStudium.add(FitFuerStudiumFacettenOption.OSA);
		optionsFitFuerStudium.add(FitFuerStudiumFacettenOption.STUDICHECK);
		fitFuerStudiumFacette = new FitFuerStudiumFacette(optionsFitFuerStudium);

		optionsBundesland.clear();
		optionsBundesland.add(RegionenFacettenOption.SACHSEN);
		optionsBundesland.add(RegionenFacettenOption.BAYERN);
		optionsBundesland.add(RegionenFacettenOption.NORDRHEIN_WESTFALEN);
		bundeslandFacette = new RegionenFacette(optionsBundesland);

		optionsHochschulart.clear();
		optionsHochschulart.add(HochschulartFacettenOption.UNIVERSITAET);
		optionsHochschulart.add(HochschulartFacettenOption.BERUFSAKADEMIE);
		optionsHochschulart.add(HochschulartFacettenOption.FACHHOCHSCHULE);
		hochschulartFacette = new HochschulartFacette(optionsHochschulart);

		studienfelder = new Studienfelder("94008;94158");
		studienfaecher = new Studienfaecher("93996");
		anfrageOrte = new AnfrageOrte("Berlin_58.1234_10.3333;Langula_10.4194_51.1508");
		umkreisFacette = new UmkreisFacette("Bundesweit");
		sortingPaging = new Paging();
		anfrageMitAllenFilter = new StudienangebotsucheAnfrage(studienfelder, studienfaecher, anfrageOrte,
				studienformFacette, hochschulartFacette, fitFuerStudiumFacette, umkreisFacette, sortingPaging, studientypFacette,
				bundeslandFacette);
	}
	
	/**
	 * Integrationstest.
	 */
	@Test
	public void grundlegendeFacetten() {
		StudienangebotsucheErgebnisBuilder cut = new StudienangebotsucheErgebnisBuilder();
		StudienangebotsucheErgebnis ergebnis = cut.build(studienangebote, anfrage);

		assertEquals(3, ergebnis.getItems().size());
		assertEquals(studienangebote.get(0), ergebnis.getItems().get(0).getStudienangebot());
		assertEquals(studienangebote.get(1), ergebnis.getItems().get(1).getStudienangebot());
	}

	/**
	 * Integrationstest.
	 */
	@Test
	public void alleFacetten() {
		StudienangebotsucheErgebnisBuilder cut = new StudienangebotsucheErgebnisBuilder();
		StudienangebotsucheErgebnis ergebnis = cut.build(studienangebote, anfrageMitAllenFilter);

		assertEquals(2, ergebnis.getItems().size());
		assertEquals(studienangebote.get(2), ergebnis.getItems().get(0).getStudienangebot());
		assertEquals(studienangebote.get(4), ergebnis.getItems().get(1).getStudienangebot());
	}
	
	/**
	 * Erzeugt ein Studienangebot.
	 * 
	 * @param id
	 * @param studientyp
	 * @param studienform
	 * @param hochschulart
	 * @param fitFuerStudium
	 * @param bundesland
	 * @return
	 */
	private static Studienangebot sa(int id, StudientypFacettenOption studientyp, StudienformFacettenOption studienform,
			HochschulartFacettenOption hochschulart, FitFuerStudiumFacettenOption fitFuerStudium,
			RegionenFacettenOption bundesland) {
		Studienangebot s = new Studienangebot();
		s.setId(String.valueOf(id));
		s.setStudientyp(studientyp);
		s.setStudienform(studienform);
		s.setHochschulart(hochschulart);
		s.setRegion(bundesland);
		s.addFitFuerStudiumFacettenOption(fitFuerStudium);
		return s;
	}
}
