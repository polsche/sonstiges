package de.ba.bub.studisu.common.integration.dkz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;
import de.ba.bub.studisu.common.model.Studienfach;
import de.ba.bub.studisu.common.model.Systematik;
import de.ba.bub.studisu.common.model.SystematikZustand;

/**
 * Implementierung des {@link DKZService}.
 *
 * @author StraubP
 */
@Service("dkz-mock")
public class DKZServiceMock implements DKZService {

	private static final String S = "S";

	protected static final String NOT_IMPLEMENTED = "Mock ist nicht implementiert";
	
	// String Konstanten fuer Systematiken
	private static final String HC = "HC";
	private static final String HA = "HA";

	private static final String HA_21 = "HA 21";
	private static final String HC_21 = "HC 21";
	private static final String HC_23 = "HC 23";
	private static final String HA_23 = "HA 23";
	private static final String HC_27 = "HC 27";
	private static final String HA_27 = "HA 27";
	private static final String HC_29 = "HC 29";
	private static final String HA_29 = "HA 29";
	private static final String HC_35 = "HC 35";
	private static final String HA_35 = "HA 35";
	private static final String HC_37 = "HC 37";
	private static final String HA_37 = "HA 37";
	private static final String HC_25 = "HC 25";
	private static final String HA_25 = "HA 25";
	private static final String HC_31 = "HC 31";
	private static final String HA_31 = "HA 31";
	private static final String HC_33 = "HC 33";
	private static final String HA_33 = "HA 33";
	
	// String Konstanten fuer die Label der Systematiken, Zweisteller
	private static final String H_27_LABEL = "Medizin, Gesundheitswissenschaften, Psychologie, Sport";
	private static final String H_31_LABEL = "Rechts-, Sozialwissenschaften";
	private static final String H_21_LABEL = "Agrar-, Forst-, Ernährungswissenschaften";
	private static final String H_35_LABEL = "Sprach-, Kulturwissenschaften";
	private static final String H_25_LABEL = "Mathematik, Naturwissenschaften";
	private static final String H_23_LABEL = "Ingenieurwissenschaften";
	private static final String H_37_LABEL = "Kunst, Musik";
	private static final String H_29_LABEL = "Wirtschaftswissenschaften";
	private static final String H_33_LABEL = "Erziehungs-, Bildungswissenschaften, Lehrämter";

	// String Konstanten fuer die Label der Systematiken, Viersteller
	private static final String H_2109_LABEL = "Lebensmittel-, Getränketechnologie";
	private static final String H_2105_LABEL = "Forstwissenschaften, -wirtschaft";
	private static final String H_2107_LABEL = "Garten-, Landschaftsbau";
	private static final String H_2103_LABEL = "Ernährungswissenschaften";
	private static final String H_2101_LABEL = "Agrarwissenschaften";
	private static final String H_2323_LABEL = "Medien-, Veranstaltungstechnik";
	private static final String H_2329_LABEL = "Physikalische Technik";
	private static final String H_2309_LABEL = "Energietechnik, Energiemanagement";
	private static final String H_2345_LABEL = "Werkstoff-, Materialwissenschaften";
	private static final String H_2317_LABEL = "Geoinformation, Vermessung";
	private static final String H_2333_LABEL = "Qualitätsmanagement";
	private static final String H_2311_LABEL = "Fahrzeug-, Verkehrstechnik";
	private static final String H_2337_LABEL = "Sicherheit und Gefahrenabwehr, Rettungsingenieurwesen";
	private static final String H_2327_LABEL = "Nanowissenschaften";
	private static final String H_2325_LABEL = "Medizintechnik, Technisches Gesundheitswesen";
	private static final String H_2303_LABEL = "Automatisierungs-, Produktionstechnik";
	private static final String H_2335_LABEL = "Rohstoffgewinnung, Hüttenwesen";
	private static final String H_2315_LABEL = "Gebäude-, Versorgungstechnik, Facility Management";
	private static final String H_2321_LABEL = "Mechatronik, Mikro- und Optotechnik";
	private static final String H_2343_LABEL = "Verfahrens-, Chemietechnik";
	private static final String H_2341_LABEL = "Umwelttechnik, Umweltschutz";
	private static final String H_2339_LABEL = "Technik, Ingenieurwissenschaften (übergreifend)";
	private static final String H_2347_LABEL = "Wirtschaftsingenieurwesen, Technologiemanagement";
	private static final String H_2307_LABEL = "Elektro- und Informationstechnik";
	private static final String H_2331_LABEL = "Produktentwicklung, Konstruktion";
	private static final String H_2313_LABEL = "Fertigungstechnologien";
	private static final String H_2319_LABEL = "Maschinenbau, Mechanik";
	private static final String H_2301_LABEL = "Architektur, Raumplanung";
	private static final String H_2305_LABEL = "Bautechnik";
	private static final String H_2701_LABEL = "Biomedizin, Neurowissenschaften";
	private static final String H_2707_LABEL = "Psychologie";
	private static final String H_2703_LABEL = "Gesundheitswissenschaften";
	private static final String H_2711_LABEL = "Therapien";
	private static final String H_2705_LABEL = "Human-, Tier-, Zahnmedizin";
	private static final String H_2709_LABEL = "Sport";
	private static final String H_2927_LABEL = "Tourismuswirtschaft, Sport- und Eventmanagement";
	private static final String H_2905_LABEL = "Betriebswirtschaft";
	private static final String H_2903_LABEL = "Bau-, Immobilienwirtschaft";
	private static final String H_2915_LABEL = "Internationale Wirtschaft";
	private static final String H_2901_LABEL = "Automobilwirtschaft";
	private static final String H_2925_LABEL = "Personalmanagement, -dienstleistung";
	private static final String H_2929_LABEL = "Wirtschaftswissenschaften, Volkswirtschaft";
	private static final String H_2907_LABEL = "Finanz- und Rechnungswesen, Controlling, Steuern";
	private static final String H_2909_LABEL = "Finanzdienstleistungen, Versicherungswirtschaft";
	private static final String H_2917_LABEL = "Logistik, Verkehr";
	private static final String H_2923_LABEL = "Medienwirtschaft, -management";
	private static final String H_2921_LABEL = "Marketing, Vertrieb";
	private static final String H_2919_LABEL = "Management";
	private static final String H_2911_LABEL = "Gesundheitsmanagement, -ökonomie";
	private static final String H_2931_LABEL = "Öffentliche Verwaltung";
	private static final String H_2913_LABEL = "Handel, Industrie, Handwerk";
	private static final String H_3520_LABEL = "Liberal Arts";
	private static final String H_3531_LABEL = "Ältere europäische Sprachen und Kulturen";
	private static final String H_3515_LABEL = "Kleinere europäische Sprachen Kulturen";
	private static final String H_3505_LABEL = "Archiv, Bibliothek, Dokumentation";
	private static final String H_3509_LABEL = "Germanistik";
	private static final String H_3503_LABEL = "Anglistik, Amerikanistik";
	private static final String H_3519_LABEL = "Kulturwissenschaften";
	private static final String H_3511_LABEL = "Geschichtswissenschaften";
	private static final String H_3521_LABEL = "Philosophie, Theologie, Religionspädagogik";
	private static final String H_3527_LABEL = "Slawistik";
	private static final String H_3507_LABEL = "Außereuropäische Sprachen und Kulturen";
	private static final String H_3529_LABEL = "Sprach-, Literaturwissenschaften, Dolmetschen und Übersetzen";
	private static final String H_3513_LABEL = "Jüdische Studien, Judaistik";
	private static final String H_3501_LABEL = "Altertumswissenschaften, Archäologie";
	private static final String H_3517_LABEL = "Kommunikation und Medien";
	private static final String H_3523_LABEL = "Regionalwissenschaften";
	private static final String H_3525_LABEL = "Romanistik";
	private static final String H_3709_LABEL = "Schauspiel, Tanz, Film, Fernsehen";
	private static final String H_3705_LABEL = "Kunst";
	private static final String H_3703_LABEL = "Gestaltung, Design";
	private static final String H_3701_LABEL = "Bühnenbild, Szenografie";
	private static final String H_3707_LABEL = "Musik";
	private static final String H_2511_LABEL = "Mathematik, Statistik";
	private static final String H_2501_LABEL = "Angewandte Naturwissenschaften";
	private static final String H_2505_LABEL = "Chemie, Pharmazie";
	private static final String H_2507_LABEL = "Geowissenschaften, -technologie";
	private static final String H_2509_LABEL = "Informatik";
	private static final String H_2503_LABEL = "Bio-, Umweltwissenschaften";
	private static final String H_2513_LABEL = "Physik";
	private static final String H_3105_LABEL = "Rechtswissenschaften";
	private static final String H_3109_LABEL = "Sozialwissenschaften, Soziologie";
	private static final String H_3101_LABEL = "Arbeitsmarktmanagement";
	private static final String H_3103_LABEL = "Politikwissenschaften";
	private static final String H_3107_LABEL = "Sozialwesen";
	private static final String H_3303_LABEL = "Lehrämter";
	private static final String H_3301_LABEL = "Erziehungs-, Bildungswissenschaften";
	
	

	@Override
	public List<Systematik> findStudienfeldgruppeSystematiken(String obercodenr) {
		// obercodenr ist case sensitive!
		List<Systematik> list = data.get(obercodenr);
		return list;
	}

	/**
	 * Mock-Daten.
	 */
	private Map<String, List<Systematik>> data = new HashMap<>();

	/**
	 * constructor of the mock setting up the mock data
	 */
	public DKZServiceMock() {
		data.put(HA, getHA());
		data.put(HC, getHC());
		data.put(HA_33, getHA33());
		data.put(HC_33, getHC33());
		data.put(HA_31, getHA31());
		data.put(HC_31, getHC31());
		data.put(HA_25, getHA25());
		data.put(HC_25, getHC25());
		data.put(HA_37, getHA37());
		data.put(HC_37, getHC37());
		data.put(HA_35, getHA35());
		data.put(HC_35, getHC35());
		data.put(HA_29, getHA29());
		data.put(HC_29, getHC29());
		data.put(HA_27, getHA27());
		data.put(HC_27, getHC27());
		data.put(HA_23, getHA23());
		data.put(HC_23, getHC23());
		data.put(HA_21, getHA21());
		data.put(HC_21, getHC21());
	}

	private List<Systematik> getHA() {
		List<Systematik> ha = new ArrayList<Systematik>();
		ha.add(new Systematik(93696, HA_33, HA, H_33_LABEL, H_33_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(93813, HA_29, HA, H_29_LABEL, H_29_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(93847, HA_37, HA, H_37_LABEL, H_37_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(93713, HA_23, HA, H_23_LABEL, H_23_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(93593, HA_25, HA, H_25_LABEL, H_25_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(93661, HA_35, HA, H_35_LABEL, H_35_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(93767, HA_21, HA, H_21_LABEL, H_21_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(94346, HA_31, HA, H_31_LABEL, H_31_LABEL, SystematikZustand.fromValue(S)));
		ha.add(new Systematik(94350, HA_27, HA, H_27_LABEL, H_27_LABEL, SystematikZustand.fromValue(S)));
		return ha;
	}

	private List<Systematik> getHC() {
		List<Systematik> hc = new ArrayList<Systematik>();
		hc.add(new Systematik(94069, HC_33, HC, H_33_LABEL, H_33_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(93837, HC_31, HC, H_31_LABEL, H_31_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(93940, HC_25, HC, H_25_LABEL, H_25_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(94130, HC_37, HC, H_37_LABEL, H_37_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(93621, HC_35, HC, H_35_LABEL, H_35_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(93914, HC_29, HC, H_29_LABEL, H_29_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(94175, HC_27, HC, H_27_LABEL, H_27_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(94327, HC_23, HC, H_23_LABEL, H_23_LABEL, SystematikZustand.fromValue(S)));
		hc.add(new Systematik(94242, HC_21, HC, H_21_LABEL, H_21_LABEL, SystematikZustand.fromValue(S)));
		return hc;
	}

	private List<Systematik> getHA33() {
		List<Systematik> ha33 = new ArrayList<Systematik>();
		ha33.add(new Systematik(94142, "HA 3301", HA_33, H_3301_LABEL, H_3301_LABEL, SystematikZustand.fromValue(S)));
		ha33.add(new Systematik(94334, "HA 3303", HA_33, H_3303_LABEL, H_3303_LABEL, SystematikZustand.fromValue(S)));
		return ha33;
	}

	private List<Systematik> getHC33() {
		List<Systematik> hc33 = new ArrayList<Systematik>();
		hc33.add(new Systematik(94333, "HC 3303", HC_33, H_3303_LABEL, H_3303_LABEL, SystematikZustand.fromValue(S)));
		hc33.add(new Systematik(94363, "HC 3301", HC_33, H_3301_LABEL, H_3301_LABEL, SystematikZustand.fromValue(S)));
		return hc33;
	}

	private List<Systematik> getHA31() {
		List<Systematik> ha31 = new ArrayList<Systematik>();
		ha31.add(new Systematik(93959, "HA 3107", HA_31, H_3107_LABEL, H_3107_LABEL, SystematikZustand.fromValue(S)));
		ha31.add(new Systematik(93889, "HA 3103", HA_31, H_3103_LABEL, H_3103_LABEL, SystematikZustand.fromValue(S)));
		ha31.add(new Systematik(94240, "HA 3101", HA_31, H_3101_LABEL, H_3101_LABEL, SystematikZustand.fromValue(S)));
		ha31.add(new Systematik(94348, "HA 3109", HA_31, H_3109_LABEL, H_3109_LABEL, SystematikZustand.fromValue(S)));
		ha31.add(new Systematik(94352, "HA 3105", HA_31, H_3105_LABEL, H_3105_LABEL, SystematikZustand.fromValue(S)));
		return ha31;
	}

	private List<Systematik> getHC31() {
		List<Systematik> hc31 = new ArrayList<Systematik>();
		hc31.add(new Systematik(93694, "HC 3109", HC_31, H_3109_LABEL, H_3109_LABEL, SystematikZustand.fromValue(S)));
		hc31.add(new Systematik(93915, "HC 3103", HC_31, H_3103_LABEL, H_3103_LABEL, SystematikZustand.fromValue(S)));
		hc31.add(new Systematik(93650, "HC 3101", HC_31, H_3101_LABEL, H_3101_LABEL, SystematikZustand.fromValue(S)));
		hc31.add(new Systematik(94358, "HC 3105", HC_31, H_3105_LABEL, H_3105_LABEL, SystematikZustand.fromValue(S)));
		hc31.add(new Systematik(94258, "HC 3107", HC_31, H_3107_LABEL, H_3107_LABEL, SystematikZustand.fromValue(S)));
		return hc31;
	}

	private List<Systematik> getHA25() {
		List<Systematik> ha25 = new ArrayList<Systematik>();
		ha25.add(new Systematik(93825, "HA 2513", HA_25, H_2513_LABEL, H_2513_LABEL, SystematikZustand.fromValue(S)));
		ha25.add(new Systematik(93935, "HA 2503", HA_25, H_2503_LABEL, H_2503_LABEL, SystematikZustand.fromValue(S)));
		ha25.add(new Systematik(93995, "HA 2509", HA_25, H_2509_LABEL, H_2509_LABEL, SystematikZustand.fromValue(S)));
		ha25.add(new Systematik(94179, "HA 2507", HA_25, H_2507_LABEL, H_2507_LABEL, SystematikZustand.fromValue(S)));
		ha25.add(new Systematik(94374, "HA 2505", HA_25, H_2505_LABEL, H_2505_LABEL, SystematikZustand.fromValue(S)));
		ha25.add(new Systematik(94249, "HA 2501", HA_25, H_2501_LABEL, H_2501_LABEL, SystematikZustand.fromValue(S)));
		ha25.add(new Systematik(94144, "HA 2511", HA_25, H_2511_LABEL, H_2511_LABEL, SystematikZustand.fromValue(S)));
		return ha25;
	}

	private List<Systematik> getHC25() {
		List<Systematik> hc25 = new ArrayList<Systematik>();
		hc25.add(new Systematik(94116, "HC 2509", HC_25, H_2509_LABEL, H_2509_LABEL, SystematikZustand.fromValue(S)));
		hc25.add(new Systematik(93901, "HC 2503", HC_25, H_2503_LABEL, H_2503_LABEL, SystematikZustand.fromValue(S)));
		hc25.add(new Systematik(93928, "HC 2505", HC_25, H_2505_LABEL, H_2505_LABEL, SystematikZustand.fromValue(S)));
		hc25.add(new Systematik(93649, "HC 2507", HC_25, H_2507_LABEL, H_2507_LABEL, SystematikZustand.fromValue(S)));
		hc25.add(new Systematik(93651, "HC 2513", HC_25, H_2513_LABEL, H_2513_LABEL, SystematikZustand.fromValue(S)));
		hc25.add(new Systematik(94403, "HC 2511", HC_25, H_2511_LABEL, H_2511_LABEL, SystematikZustand.fromValue(S)));
		hc25.add(new Systematik(94405, "HC 2501", HC_25, H_2501_LABEL, H_2501_LABEL, SystematikZustand.fromValue(S)));
		return hc25;
	}

	private List<Systematik> getHA37() {
		List<Systematik> ha37 = new ArrayList<Systematik>();
		ha37.add(new Systematik(93774, "HA 3707", HA_37, H_3707_LABEL, H_3707_LABEL, SystematikZustand.fromValue(S)));
		ha37.add(new Systematik(93625, "HA 3701", HA_37, H_3701_LABEL, H_3701_LABEL, SystematikZustand.fromValue(S)));
		ha37.add(new Systematik(93733, "HA 3703", HA_37, H_3703_LABEL, H_3703_LABEL, SystematikZustand.fromValue(S)));
		ha37.add(new Systematik(94406, "HA 3709", HA_37, H_3709_LABEL, H_3709_LABEL, SystematikZustand.fromValue(S)));
		ha37.add(new Systematik(94230, "HA 3705", HA_37, H_3705_LABEL, H_3705_LABEL, SystematikZustand.fromValue(S)));
		return ha37;
	}

	private List<Systematik> getHC37() {
		List<Systematik> hc37 = new ArrayList<Systematik>();
		hc37.add(new Systematik(94065, "HC 3707", HC_37, H_3707_LABEL, H_3707_LABEL, SystematikZustand.fromValue(S)));
		hc37.add(new Systematik(93890, "HC 3701", HC_37, H_3701_LABEL, H_3701_LABEL, SystematikZustand.fromValue(S)));
		hc37.add(new Systematik(94379, "HC 3703", HC_37, H_3703_LABEL, H_3703_LABEL, SystematikZustand.fromValue(S)));
		hc37.add(new Systematik(94299, "HC 3705", HC_37, H_3705_LABEL, H_3705_LABEL, SystematikZustand.fromValue(S)));
		hc37.add(new Systematik(94422, "HC 3709", HC_37, H_3709_LABEL, H_3709_LABEL, SystematikZustand.fromValue(S)));
		return hc37;
	}

	private List<Systematik> getHA35() {
		List<Systematik> ha35 = new ArrayList<Systematik>();
		ha35.add(new Systematik(93797, "HA 3525", HA_35, H_3525_LABEL, H_3525_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94063, "HA 3523", HA_35, H_3523_LABEL, H_3523_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(93821, "HA 3517", HA_35, H_3517_LABEL, H_3517_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94101, "HA 3501", HA_35, H_3501_LABEL, H_3501_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94122, "HA 3513", HA_35, H_3513_LABEL, H_3513_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(93705, "HA 3529", HA_35, H_3529_LABEL, H_3529_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(93583, "HA 3507", HA_35, H_3507_LABEL, H_3507_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(93979, "HA 3527", HA_35, H_3527_LABEL, H_3527_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(93988, "HA 3521", HA_35, H_3521_LABEL, H_3521_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(93911, "HA 3511", HA_35, H_3511_LABEL, H_3511_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94398, "HA 3519", HA_35, H_3519_LABEL, H_3519_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94412, "HA 3503", HA_35, H_3503_LABEL, H_3503_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94322, "HA 3509", HA_35, H_3509_LABEL, H_3509_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94328, "HA 3505", HA_35, H_3505_LABEL, H_3505_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94246, "HA 3515", HA_35, H_3515_LABEL, H_3515_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(94257, "HA 3531", HA_35, H_3531_LABEL, H_3531_LABEL, SystematikZustand.fromValue(S)));
		ha35.add(new Systematik(128341, "HA 3520", HA_35, H_3520_LABEL, H_3520_LABEL, SystematikZustand.fromValue(S)));
		return ha35;
	}

	private List<Systematik> getHC35() {
		List<Systematik> hc35 = new ArrayList<Systematik>();
		hc35.add(new Systematik(93677, "HC 3529", HC_35, H_3529_LABEL, H_3529_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93699, "HC 3519", HC_35, H_3519_LABEL, H_3519_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93817, "HC 3513", HC_35, H_3513_LABEL, H_3513_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93828, "HC 3501", HC_35, H_3501_LABEL, H_3501_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93836, "HC 3521", HC_35, H_3521_LABEL, H_3521_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93842, "HC 3505", HC_35, H_3505_LABEL, H_3505_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93946, "HC 3525", HC_35, H_3525_LABEL, H_3525_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93888, "HC 3517", HC_35, H_3517_LABEL, H_3517_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(93638, "HC 3503", HC_35, H_3503_LABEL, H_3503_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94041, "HC 3511", HC_35, H_3511_LABEL, H_3511_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94310, "HC 3527", HC_35, H_3527_LABEL, H_3527_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94321, "HC 3507", HC_35, H_3507_LABEL, H_3507_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94330, "HC 3515", HC_35, H_3515_LABEL, H_3515_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94224, "HC 3523", HC_35, H_3523_LABEL, H_3523_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94253, "HC 3509", HC_35, H_3509_LABEL, H_3509_LABEL, SystematikZustand.fromValue(S)));
		hc35.add(new Systematik(94170, "HC 3531", HC_35, H_3531_LABEL, H_3531_LABEL, SystematikZustand.fromValue(S)));
		return hc35;
	}

	private List<Systematik> getHA29() {
		List<Systematik> ha29 = new ArrayList<Systematik>();
		ha29.add(new Systematik(94087, "HA 2913", HA_29, H_2913_LABEL, H_2913_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(93611, "HA 2931", HA_29, H_2931_LABEL, H_2931_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(93989, "HA 2911", HA_29, H_2911_LABEL, H_2911_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94000, "HA 2919", HA_29, H_2919_LABEL, H_2919_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(93736, "HA 2921", HA_29, H_2921_LABEL, H_2921_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(93739, "HA 2923", HA_29, H_2923_LABEL, H_2923_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(93904, "HA 2917", HA_29, H_2917_LABEL, H_2917_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94020, "HA 2909", HA_29, H_2909_LABEL, H_2909_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(93659, "HA 2907", HA_29, H_2907_LABEL, H_2907_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94033, "HA 2929", HA_29, H_2929_LABEL, H_2929_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94178, "HA 2925", HA_29, H_2925_LABEL, H_2925_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94304, "HA 2901", HA_29, H_2901_LABEL, H_2901_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94221, "HA 2915", HA_29, H_2915_LABEL, H_2915_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94228, "HA 2903", HA_29, H_2903_LABEL, H_2903_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94158, "HA 2905", HA_29, H_2905_LABEL, H_2905_LABEL, SystematikZustand.fromValue(S)));
		ha29.add(new Systematik(94357, "HA 2927", HA_29, H_2927_LABEL, H_2927_LABEL, SystematikZustand.fromValue(S)));
		return ha29;
	}

	private List<Systematik> getHC29() {
		List<Systematik> hc29 = new ArrayList<Systematik>();
		hc29.add(new Systematik(93799, "HC 2923", HC_29, H_2923_LABEL, H_2923_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93818, "HC 2907", HC_29, H_2907_LABEL, H_2907_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93846, "HC 2911", HC_29, H_2911_LABEL, H_2911_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93953, "HC 2917", HC_29, H_2917_LABEL, H_2917_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93977, "HC 2921", HC_29, H_2921_LABEL, H_2921_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93718, "HC 2913", HC_29, H_2913_LABEL, H_2913_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93719, "HC 2919", HC_29, H_2919_LABEL, H_2919_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(94008, "HC 2905", HC_29, H_2905_LABEL, H_2905_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93900, "HC 2925", HC_29, H_2925_LABEL, H_2925_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93909, "HC 2909", HC_29, H_2909_LABEL, H_2909_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(93648, "HC 2931", HC_29, H_2931_LABEL, H_2931_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(94375, "HC 2915", HC_29, H_2915_LABEL, H_2915_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(94384, "HC 2901", HC_29, H_2901_LABEL, H_2901_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(94303, "HC 2929", HC_29, H_2929_LABEL, H_2929_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(94217, "HC 2927", HC_29, H_2927_LABEL, H_2927_LABEL, SystematikZustand.fromValue(S)));
		hc29.add(new Systematik(94252, "HC 2903", HC_29, H_2903_LABEL, H_2903_LABEL, SystematikZustand.fromValue(S)));
		return hc29;
	}

	private List<Systematik> getHA27() {
		List<Systematik> ha27 = new ArrayList<Systematik>();
		ha27.add(new Systematik(94098, "HA 2709", HA_27, H_2709_LABEL, H_2709_LABEL, SystematikZustand.fromValue(S)));
		ha27.add(new Systematik(93575, "HA 2705", HA_27, H_2705_LABEL, H_2705_LABEL, SystematikZustand.fromValue(S)));
		ha27.add(new Systematik(93871, "HA 2711", HA_27, H_2711_LABEL, H_2711_LABEL, SystematikZustand.fromValue(S)));
		ha27.add(new Systematik(94197, "HA 2703", HA_27, H_2703_LABEL, H_2703_LABEL, SystematikZustand.fromValue(S)));
		ha27.add(new Systematik(94393, "HA 2707", HA_27, H_2707_LABEL, H_2707_LABEL, SystematikZustand.fromValue(S)));
		ha27.add(new Systematik(94373, "HA 2701", HA_27, H_2701_LABEL, H_2701_LABEL, SystematikZustand.fromValue(S)));
		return ha27;
	}

	private List<Systematik> getHC27() {
		List<Systematik> hc27 = new ArrayList<Systematik>();
		hc27.add(new Systematik(93804, "HC 2707", HC_27, H_2707_LABEL, H_2707_LABEL, SystematikZustand.fromValue(S)));
		hc27.add(new Systematik(93685, "HC 2709", HC_27, H_2709_LABEL, H_2709_LABEL, SystematikZustand.fromValue(S)));
		hc27.add(new Systematik(93698, "HC 2705", HC_27, H_2705_LABEL, H_2705_LABEL, SystematikZustand.fromValue(S)));
		hc27.add(new Systematik(93720, "HC 2701", HC_27, H_2701_LABEL, H_2701_LABEL, SystematikZustand.fromValue(S)));
		hc27.add(new Systematik(93891, "HC 2703", HC_27, H_2703_LABEL, H_2703_LABEL, SystematikZustand.fromValue(S)));
		hc27.add(new Systematik(94037, "HC 2711", HC_27, H_2711_LABEL, H_2711_LABEL, SystematikZustand.fromValue(S)));
		return hc27;
	}

	private List<Systematik> getHA23() {
		List<Systematik> ha23 = new ArrayList<Systematik>();
		ha23.add(new Systematik(93819, "HA 2305", HA_23, H_2305_LABEL, H_2305_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93936, "HA 2301", HA_23, H_2301_LABEL, H_2301_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94114, "HA 2319", HA_23, H_2319_LABEL, H_2319_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94121, "HA 2313", HA_23, H_2313_LABEL, H_2313_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93574, "HA 2331", HA_23, H_2331_LABEL, H_2331_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93970, "HA 2307", HA_23, H_2307_LABEL, H_2307_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93581, "HA 2347", HA_23, H_2347_LABEL, H_2347_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93592, "HA 2339", HA_23, H_2339_LABEL, H_2339_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93861, "HA 2341", HA_23, H_2341_LABEL, H_2341_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93724, "HA 2343", HA_23, H_2343_LABEL, H_2343_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93896, "HA 2321", HA_23, H_2321_LABEL, H_2321_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93751, "HA 2315", HA_23, H_2315_LABEL, H_2315_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(93907, "HA 2335", HA_23, H_2335_LABEL, H_2335_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94030, "HA 2303", HA_23, H_2303_LABEL, H_2303_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94187, "HA 2325", HA_23, H_2325_LABEL, H_2325_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94378, "HA 2327", HA_23, H_2327_LABEL, H_2327_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94383, "HA 2337", HA_23, H_2337_LABEL, H_2337_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94394, "HA 2311", HA_23, H_2311_LABEL, H_2311_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94408, "HA 2333", HA_23, H_2333_LABEL, H_2333_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94420, "HA 2317", HA_23, H_2317_LABEL, H_2317_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94143, "HA 2345", HA_23, H_2345_LABEL, H_2345_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94362, "HA 2309", HA_23, H_2309_LABEL, H_2309_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94269, "HA 2329", HA_23, H_2329_LABEL, H_2329_LABEL, SystematikZustand.fromValue(S)));
		ha23.add(new Systematik(94284, "HA 2323", HA_23, H_2323_LABEL, H_2323_LABEL, SystematikZustand.fromValue(S)));
		return ha23;
	}

	private List<Systematik> getHC23() {
		List<Systematik> hc23 = new ArrayList<Systematik>();
		hc23.add(new Systematik(93772, "HC 2335", HC_23, H_2335_LABEL, H_2335_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93795, "HC 2331", HC_23, H_2331_LABEL, H_2331_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94056, "HC 2317", HC_23, H_2317_LABEL, H_2317_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93690, "HC 2305", HC_23, H_2305_LABEL, H_2305_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93814, "HC 2347", HC_23, H_2347_LABEL, H_2347_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93815, "HC 2313", HC_23, H_2313_LABEL, H_2313_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93958, "HC 2301", HC_23, H_2301_LABEL, H_2301_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94120, "HC 2337", HC_23, H_2337_LABEL, H_2337_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93584, "HC 2325", HC_23, H_2325_LABEL, H_2325_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93598, "HC 2303", HC_23, H_2303_LABEL, H_2303_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93850, "HC 2345", HC_23, H_2345_LABEL, H_2345_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93853, "HC 2307", HC_23, H_2307_LABEL, H_2307_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93999, "HC 2319", HC_23, H_2319_LABEL, H_2319_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93627, "HC 2333", HC_23, H_2333_LABEL, H_2333_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(93918, "HC 2329", HC_23, H_2329_LABEL, H_2329_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94204, "HC 2343", HC_23, H_2343_LABEL, H_2343_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94307, "HC 2339", HC_23, H_2339_LABEL, H_2339_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94319, "HC 2309", HC_23, H_2309_LABEL, H_2309_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94324, "HC 2311", HC_23, H_2311_LABEL, H_2311_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94231, "HC 2327", HC_23, H_2327_LABEL, H_2327_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94237, "HC 2321", HC_23, H_2321_LABEL, H_2321_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94145, "HC 2323", HC_23, H_2323_LABEL, H_2323_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94256, "HC 2341", HC_23, H_2341_LABEL, H_2341_LABEL, SystematikZustand.fromValue(S)));
		hc23.add(new Systematik(94262, "HC 2315", HC_23, H_2315_LABEL, H_2315_LABEL, SystematikZustand.fromValue(S)));
		return hc23;
	}

	private List<Systematik> getHA21() {
		List<Systematik> ha21 = new ArrayList<Systematik>();
		ha21.add(new Systematik(93796, "HA 2101", HA_21, H_2101_LABEL, H_2101_LABEL, SystematikZustand.fromValue(S)));
		ha21.add(new Systematik(93986, "HA 2103", HA_21, H_2103_LABEL, H_2103_LABEL, SystematikZustand.fromValue(S)));
		ha21.add(new Systematik(94010, "HA 2107", HA_21, H_2107_LABEL, H_2107_LABEL, SystematikZustand.fromValue(S)));
		ha21.add(new Systematik(94014, "HA 2105", HA_21, H_2105_LABEL, H_2105_LABEL, SystematikZustand.fromValue(S)));
		ha21.add(new Systematik(94329, "HA 2109", HA_21, H_2109_LABEL, H_2109_LABEL, SystematikZustand.fromValue(S)));
		return ha21;
	}

	private List<Systematik> getHC21() {
		List<Systematik> hc21 = new ArrayList<Systematik>();
		hc21.add(new Systematik(93802, "HC 2103", HC_21, H_2103_LABEL, H_2103_LABEL, SystematikZustand.fromValue(S)));
		hc21.add(new Systematik(93701, "HC 2101", HC_21, H_2101_LABEL, H_2101_LABEL, SystematikZustand.fromValue(S)));
		hc21.add(new Systematik(93858, "HC 2107", HC_21, H_2107_LABEL, H_2107_LABEL, SystematikZustand.fromValue(S)));
		hc21.add(new Systematik(93757, "HC 2109", HC_21, H_2109_LABEL, H_2109_LABEL, SystematikZustand.fromValue(S)));
		hc21.add(new Systematik(94163, "HC 2105", HC_21, H_2105_LABEL, H_2105_LABEL, SystematikZustand.fromValue(S)));
		return hc21;
	}

	@Override
	public List<Systematik> findSystematik(int dkzid, List<String> codeNr) {
		throw new IllegalStateException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Systematik> findStudienfaecherFuerStudienfeld(Integer studfId) {
		throw new IllegalStateException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Systematik> findBeschriebeneStudienfaecherFuerStudienfeld(Integer studfId) {
		throw new IllegalStateException(NOT_IMPLEMENTED);
}

	@Override
	public List<Studienfach> findStudienfachBySuchwort(String suchbefriff) {
		throw new IllegalStateException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Studienfach> findStudienfachById(List<Integer> dkzIds) {
		throw new IllegalStateException(NOT_IMPLEMENTED);
	}

	@Override
	public ExternalServiceStatus getServiceStatus() throws CommonServiceException {
		ExternalServiceStatus status = new ExternalServiceStatus(
				this.getClass().getSimpleName(), ExternalServiceStatus.Status.VERFUEGBAR);
		return status;
	}
}
