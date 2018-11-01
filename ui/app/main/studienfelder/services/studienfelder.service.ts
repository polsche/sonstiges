/**
 * Service für die Ermittlung der Studienfelder vom Backend.
 */
import { Injectable } from "@angular/core";
import { Response, Headers } from "@angular/http";
import { LoggingService } from "../../../services/logging.service";

// rxjs imports
import { Observable } from "rxjs/Observable";
import "rxjs/add/operator/map";

import { Studienfeldgruppe } from "./model/Studienfeldgruppe";
import { Studienfeld } from "./../../studienfelder/services/model/Studienfeld";
import { StudisuConfig, getStudisuConfig } from "../../../config/studisu.config";
import { ErrorHandlerService } from "../../../error/errorhandler.service";
import {StudisuHttpService} from "../../../services/studisu-http.service";

@Injectable()
export class StudienfelderService {

    private conf: StudisuConfig = getStudisuConfig();
    private studienfelder: Observable<Studienfeldgruppe[]> = null;
    private prefixStudienfachParameter = "?sfa=";

    constructor(private oagHttp: StudisuHttpService,
        private logger: LoggingService,
        private errorHandlerService: ErrorHandlerService) {
    }


    /**
     * Holt ein Objekt, dessen 'studienbereiche' Attribut das Array mit den Studienbereichen enthält.
     */
    public getStudienfelder(): Observable<Studienfeldgruppe[]> {
        if (null === this.studienfelder) {
            let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.studienfelderServicePath);
            this.studienfelder = this.oagHttp.get(url, { headers: this.getHeaders() })
                .map(response => this.mapResponse(response))
                .catch(error => this.errorHandlerService.handleError(error));
        }
        return this.studienfelder;
    }

    /**
     * Service-Methode die aus dem Backend Studienfelder für ein Studienfach abruft
     * @param studienfach-Id, die gesucht wird, als String
     * @return studienfelder Observable<String[]> die für den gesuchte Studienfach gefunden wurden
     */
    public getStudienfelderForStudienfach(studienfach: number): Observable<String[]> {
        let url = encodeURI(this.conf.studienangeboteServiceHost +
                            this.conf.studienfelderByStudienfachServicePath +
                            this.prefixStudienfachParameter + studienfach);

        let studienfelder = this.oagHttp.get(url, { headers: this.getHeaders() })
            .map(x => x.json())
            .catch(error => this.errorHandlerService.handleError(error));
        return studienfelder;
    }

    /**
     * Suche nach einem Teilstring in den Studienfeldern.
     *
     * @param searchTerm Gesuchter Teilstring
     */
    public suchen(searchTerm: string): Observable<Studienfeld[]> {
        let lcSearchTerm = searchTerm.toLocaleLowerCase();
        return this.getStudienfelder()
            .map((studienbereiche: Studienfeldgruppe[]) =>
                studienbereiche
                    .map(studienbereich =>
                        studienbereich.studienfelder
                            .filter(studienfeld => studienfeld.name.toLocaleLowerCase().indexOf(lcSearchTerm) > -1)
                    )
                    .reduce((acc, value) => acc.concat(value), [])
            );
    }

    /**
     * Takes a Response Object which should contain Studienfeldgruppe and map
     * them to real Studienfeldgruppe Objects. This will enable us to use the interface methods of the
     * Studienfeldgruppe class. This will also map Studienfelder inside the Studienfeldgruppe.
     * If used with existing Studienfeldgruppe Objects they are just recreated.
     * @param response
     */
    private mapResponse(response: Response) {
        let result: Studienfeldgruppe[] = [];
        let felder: Studienfeldgruppe[] = response.json().studienfeldgruppen;

        felder.forEach(feld => {
            result.push(new Studienfeldgruppe(feld.key, feld.name, feld.dkzIds, this.mapStudienfelder(feld.studienfelder), feld.icon));
        });
        return result;
    }

    /**
     * Takes an array of studienfeld items (which are castet from any usually) and map
     * them to real Studienfeld Objects. This will enable us to use the interface methods of the
     * Studienfeld class.
     * If used with existing Studienfeld Objects they are just recreated.
     * @param felder array of type any castet to Studienfeld[]
     */
    private mapStudienfelder(felder: Studienfeld[]) {
        let result: Studienfeld[] = [];
        felder.forEach( feld => {
            result.push(new Studienfeld(feld.key, feld.name, feld.dkzIds));
        });

        return result;
    }

    /**
     * Liefert die Header für die HTTP-Anfrage, hier für JSON-Ergebnisse.
     */
    private getHeaders() {
        let headers = new Headers();
        headers.append("Accept", "application/json");
        return headers;
    }

}
