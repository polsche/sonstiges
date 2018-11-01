/**
 * Enthält Daten für die Anzeige im Fehler-Popup
 */
export class ModalData {
    /**
     * Konstruktor, der Meldungs-/Beschreibungstext und Fehlerobjekt
     * @param title Überschrift
     * @param subtitle Unterüberschrift
     * @param text Konkrete Fehler
     */
    constructor(public title?: String, public subtitle?: String, public text?: String) {}
}
