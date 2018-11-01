/**
 * Enthält Daten für die Anzeige im Fehler-Popup
 */
export class HinweisModalData {
    /**
     * Konstruktor, der Meldungs-/Beschreibungstext und Hinweisobjekt
     * @param title Überschrift
     * @param subtitle Unterüberschrift
     * @param text Konkrete Hinweismeldung
     */
    constructor(public title?: String, public subtitle?: String, public text?: String) {}
}
