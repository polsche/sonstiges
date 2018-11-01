/**
 * Representation eines Fehlers mit Meldungs-/Beschreibungstext und Fehlerobjekt
 * @since STUDISU-105 evtl. noch weitere Angaben sinnvoll, z.B. Schweregrad des Fehlers
 */
export class StudisuError {

    /**
     * Konstruktor, der Meldungs-/Beschreibungstext und Fehlerobjekt erwartet
     * @param message Meldungs-/Beschreibungstext
     * @param error Fehlerobjekt
     */
    constructor(public message: string, public error: Error) {
    }

}
