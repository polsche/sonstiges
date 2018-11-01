package de.ba.bub.studisu.common.model;

/**
 * Created by weigelt005 on 01.06.2017.
 *
 * Generated Enum Bundesland has the wrong mappings. But as it is generated we need to have a wrapper enum for that.
 *
 * @see de.baintern.dst.services.wsdl.operativ.unterstuetzung.aufzaehlungstypen_v_1.Bundesland
 */
public enum BundeslandEnum {
    BADEN_WUERTTEMBERG("Baden-Württemberg"),
    BAYERN("Bayern"),
    BERLIN("Berlin"),
    BRANDENBURG("Brandenburg"),
    BREMEN("Bremen"),
    HAMBURG("Hamburg"),
    HESSEN("Hessen"),
    MECKLENBURG_VORPOMMERN("Mecklenburg-Vorpommern"),
    NIEDERSACHSEN("Niedersachsen"),
    NORDRHEIN_WESTFALEN("Nordrhein-Westfalen"),
    RHEINLAND_PFALZ("Rheinland-Pfalz"),
    SAARLAND("Saarland"),
    SACHSEN("Sachsen"),
    SACHSEN_ANHALT("Sachsen-Anhalt"),
    SCHLESWIG_HOLSTEIN("Schleswig-Holstein"),
    THUERINGEN("Thüringen");
    private final String value;

    private BundeslandEnum(String v) {
        value = v;
    }

    /**
     * get Enum Value as String
     * @return name
     */
    public String value() {
        return value;
    }

    /**
     * Get BundeslandEnum from String
     * @param value String Value
     * @return  BundeslandEnum
     * @exception IllegalArgumentException if Enum could not be resolved
     */
    public static BundeslandEnum fromValue(String value) {
        for (BundeslandEnum be: BundeslandEnum.values()) {
            if (be.value.equals(value)) {
                return be;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
