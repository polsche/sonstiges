import { Response, Headers } from "@angular/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { StudienfeldInfo } from "./model/StudienfeldInfo";
import { StudienfachInfo } from "./model/StudienfachInfo";
import { StudisuConfig, getStudisuConfig } from "../../../config/studisu.config";
import { LoggingService } from "../../../services/logging.service";
import { DomSanitizer } from "@angular/platform-browser";
import { ErrorHandlerService } from "../../../error/errorhandler.service";
import {StudisuHttpService} from "../../../services/studisu-http.service";

@Injectable()
export class StudieninfoService {

    private conf: StudisuConfig = getStudisuConfig();

    constructor(private oagHttp: StudisuHttpService,
        private logger: LoggingService,
        private errorHandlerService: ErrorHandlerService,
        private domSanitizer: DomSanitizer) { }

    /**
     * Liefert Name und Beschreibungen des uebergebenen Studienfeldes.
     *
     * @param studienfeld
     *          DKZ-ID des Studienfeldes, für das Name + WCC-Beschreibung gesucht werden sollen.
     */
    public sucheStudienfeld(studienfeld: number): Observable<StudienfeldInfo> {
        let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.studienfeldBeschreibung + `?dkz=${studienfeld}`);
        return this.oagHttp.get(url, { headers: this.getHeaders() })
            .catch(error => this.errorHandlerService.handleError(error))
            .map(response => this.extractStudienfeldData(response));
    }

    /**
     * Liefert ein Array mit allen StudienfachInfo-Eintraegen zu den Studienfaechern des Studienfeldes.
     *
     * @param studienfeld
     *          Die DKZ-ID des Studienfeldes, für das die Liste der Studienfaecher ermittelt werden soll.
     */
    public sucheStudienfaecherZuStudienfeld(studienfeld: number): Observable<StudienfachInfo[]> {
        let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.studienfeldInformationen + `?dkz=${studienfeld}`);
        return this.oagHttp.get(url, { headers: this.getHeaders() })
            .catch(error => this.errorHandlerService.handleError(error))
            .map(response => {
                let studienfachInfos: StudienfachInfo[] = this.extractStudienfachData(response);
                studienfachInfos.forEach(studienfachInformation => {
                    // OLE 04.07.17 Wert für filmURL Feld muss im Service berechnet worden sein.
                    // Wenn die Methode getFilmURL() in Studienfeldinfo-Componente ausgefürt wird,
                    // aktualisiert der Browser alle Film-URLs nach jedem Scroll-Event.
                    studienfachInformation.filmURL = this.getFilmURL(studienfachInformation.studienfachFilmId);
                });
                return studienfachInfos;
            }
            );
    }

    private getFilmURL(id: any) {
        return this.domSanitizer.bypassSecurityTrustResourceUrl(`https://www.berufe.tv/embed.html?vendorID=114&filmID=${id}`);
    }

    private getHeaders() {
        let headers = new Headers();
        headers.append("Accept", "application/json");
        return headers;
    }

    /**
     * Liest das uebergebene Antwortobjekt für die Studienfeld-Informationen aus.
     *
     * Enthaelt die Antwort keinen Text, gibt die Methode null zurueck.
     *
     * @param response Antwortobjekt aus der Serveranfrage
     * @return StudienfeldInfo oder null, falls Antwortobjekt keinen Textinhalt hat
     */
    private extractStudienfeldData(response: Response): StudienfeldInfo | null {
        let info: StudienfeldInfo = null;
        if (response.text() !== null) {
            info = response.json();
        }
        return info;
    }

    /**
     * Liest das uebergebene Antwortobjekt für die Studienfächer-Infos aus.
     *
     * Enthaelt die Antwort keinen Text, gibt die Methode null zurueck.
     *
     * @param response Antwortobjekt aus der Serveranfrage
     * @return Array mit Studienfach-Informationen oder null, falls Antwortobjekt keinen Textinhalt hat
     * @version STUDISU-105 zeige Fehlerdialog - Nullabfrage eingefuegt
     */
    private extractStudienfachData(response: Response): StudienfachInfo[] | null {
        let info: StudienfachInfo[] = null;
        if (response.text() !== null) {
            info = response.json().studienfachInformationen;
        }
        return info;
    }
}
