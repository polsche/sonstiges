import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs/BehaviorSubject";


/**
 * Serviceklasse, die Informationen zu einem bestimmten Verarbeitungszustand haelt.
 * Eingefuehrt fuer Ladezustaende in Zusammenhang mit einer Aktualisierungsanzeige bei der Suche.
 * @since STUDISU-73: Aktualisierungsanzeige beim Laden der Ergebnisliste
 */
@Injectable()
export class EventService {
    /**
     * BehaviorSubject mit Informationen zum Ladezustand bei der Suche nach Studienangeboten.
     * moegliche Zustaende: ladend true/false oder Fehlermeldung.
     */
    public studienangeboteSucheStatus: BehaviorSubject<boolean | string> = new BehaviorSubject(false);
}
