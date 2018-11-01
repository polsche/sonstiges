package de.ba.bub.studisu.common.model;

/**
 * J	Beruf wird beschrieben
 * N	Beruf wird nicht beschrieben
 * A	Beruf wird archiviert
 * -	keine Relevanz
 * Created by loutzenhj on 05.04.2018.
 */
public enum BeschreibungsZustand {
    BESCHRIEBEN("J"),
    NICHT_BESCHRIEBEN("N"),
    ARCHIVIERT("A"),
    KEINE_RELEVANZ("-");

    private String value;

    BeschreibungsZustand(String value) {
        this.value = value;
    }

    public boolean isValid() {
        return this.equals(BESCHRIEBEN);
    }


    /**
     * Liefert einen BeschreibungsZustand entsprechend seines String-Wertes.
     *
     * @param v String-Wert, der den Zusatand repräsentiert
     * @return der Zustand
     * @throws IllegalArgumentException, falls String-Wert nicht bekannt ist
     */
    public static BeschreibungsZustand fromValue(String v) {
        for (BeschreibungsZustand c : BeschreibungsZustand.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
