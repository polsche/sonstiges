package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.StudienangeboteMapper;
import de.ba.bub.studisu.common.model.BundeslandEnum;
import de.ba.bub.studisu.common.model.Studienangebot;
import de.ba.bub.studisu.studienangebote.model.facetten.StudienformFacettenOption;
import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bundesland;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Schulart;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Unterrichtsform;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Adresse;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsanbieter;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Bildungsangebot;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Ort;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Systematik;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.findstudienveranstaltungen_v_1.Studienveranstaltung;

public class StudienangeboteMapperTest {

	List<Studienveranstaltung> veranstaltungen = null;

	private static String ID = "50";
	private static String TITEL = "Veranstaltung";
	private static String INHALTE = "Hier kommen die Kursinhalte";
	private static Integer BA_ID = 12345;
	private static String BA_NAME = "Anbietername";
	private static String VG_ORTSNAME = "Stadtname";
	private static String VG_PLZ = "90402";
	private static String VG_STRASSE = "Strasse 12";
	private static Bundesland VG_BUNDESLAND = Bundesland.BAYERN;
	private static Unterrichtsform STUDIENFORM = Unterrichtsform.VOLLZEIT;
	private static Schulart HOCHSCHULART = Schulart.UNIVERSITAET;

	@Before
	public void setUp() throws Exception {
		veranstaltungen = new ArrayList<Studienveranstaltung>();

		Studienveranstaltung veranstaltung1 = new Studienveranstaltung();

		veranstaltung1.setId(Integer.valueOf(ID));
		veranstaltung1.setUnterrichtsform(STUDIENFORM);
		veranstaltung1.setSchulart(HOCHSCHULART);

		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());

		veranstaltung1.setZeitBeginn(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));

		Adresse vgAdresse = new Adresse();
		vgAdresse.setStrasse(VG_STRASSE);
		Ort ort = new Ort();
		ort.setOrtsname(VG_ORTSNAME);
		ort.setPostleitzahl(VG_PLZ);
		ort.setBundesland(VG_BUNDESLAND);
		vgAdresse.setOrt(ort);

		veranstaltung1.setAdresse(vgAdresse);

		Systematik sy1 = new Systematik();
		sy1.setDkzCodenr("HA 43113-939");
		sy1.setDkzBezeichnung("Verwaltungsinformatik (grundständig)");

		Bildungsangebot angebot = new Bildungsangebot();
		angebot.setTitel(TITEL);
		angebot.setInhalte(INHALTE);
		angebot.getSystematik().add(sy1);

		Bildungsanbieter anbieter = new Bildungsanbieter();
		anbieter.setId(BA_ID);
		anbieter.setName(BA_NAME);
		anbieter.setHasSignet(true);

		angebot.setBildungsanbieter(anbieter);

		veranstaltung1.setBildungsangebot(angebot);
		veranstaltungen.add(veranstaltung1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMap() {
		List<Studienangebot> returnList = StudienangeboteMapper.map(veranstaltungen,
				UmkreisFacette.DE_MITTE.getKoordinaten(), UmkreisFacette.BUNDESWEIT.getUmkreisKm());

		assertTrue(!returnList.isEmpty());
	}

	@Test
	public void testMapingKorrekt() {
		/*
		 * Werte, die gemapt werden müssen, um in der Oberfläche eine fachlich korrekte Anzeige darzustellen: BA-Name id
		 * zeitBeginn Titel (StudiBezeichnung) studiInhalt BA-Adresse Studientyp Studienform (Unterrichtsform)
		 */

		List<Studienangebot> returnList = StudienangeboteMapper.map(veranstaltungen,
				UmkreisFacette.DE_MITTE.getKoordinaten(), UmkreisFacette.BUNDESWEIT.getUmkreisKm());
		Studienangebot angebot = returnList.get(0);

		assertEquals(angebot.getBildungsanbieterName(), BA_NAME);
		assertEquals(angebot.getBildungsanbieterId(), BA_ID);
		assertEquals(angebot.getId(), ID);
		assertNotNull(angebot.getStudiBeginn());
		assertEquals(angebot.getStudiBezeichnung(), TITEL);
		assertTrue(!angebot.getStudiInhalt().isEmpty());
		assertNotNull(angebot.getStudienort());
		assertEquals(angebot.getStudienort().getOrt(), VG_ORTSNAME);
		assertEquals(angebot.getStudienort().getPostleitzahl(), VG_PLZ);
		assertEquals(angebot.getStudienort().getStrasse(), VG_STRASSE);
		assertEquals(angebot.getStudienort().getBundesland(), BundeslandEnum.valueOf(VG_BUNDESLAND.name()).value());
		assertNotNull(angebot.getStudientyp());
		assertTrue(angebot.getStudienform().equals(StudienformFacettenOption.VOLLZEIT));
		assertTrue(angebot.getBildungsanbieterHasSignet());
	}

}
