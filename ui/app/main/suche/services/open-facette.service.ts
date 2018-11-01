import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";

/**
 * Service-Klasse zum Verteilen von Informationen zur Öffnung
 * von Facetten-Filtern in der mobilen Ansicht.
 */
@Injectable()
export class OpenFacetteService {

    private subject: Subject<string> = new Subject<string>();

    /**
     * Setter zur Mitteilung, dass die Facette mit der
     * angegebenen ID in der mobilen Ansicht geöffnet wurde.
     * @param filterIdOpened ID der geöffneten Facette
     */
    public setFilterIdOpened(filterIdOpened: string): void {
        this.subject.next(filterIdOpened);
    }

    /**
     * Getter zum Mithören, welche Facette mit welcher ID
     * in der mobilen Ansicht geöffnet wurde. Alle Facetten
     * hören mit und werden geschlossen, wenn eine Facette
     * mit einer anderen ID in der mobilen Ansicht geöffnet
     * wurde.
     * @returns {Observable<string>} ID der geöffneten Facette
     */
    public getFilterIdOpened(): Observable<string> {
        return this.subject.asObservable();
    }
}
