import { Injectable } from "@angular/core";
import { Response } from "@angular/http";
import { Observable } from "rxjs/Observable";
import { SearchResult } from "../model/SearchResult";
import { UrlParamService } from "../main/suche/services/url-param.service";
import { StudisuConfig, getStudisuConfig } from "../config/studisu.config";
import { LoggingService } from "../services/logging.service";
import { ErrorHandlerService } from "../error/errorhandler.service";
import { Studienangebot } from "../main/suche/services/model/Studienangebot";
import {StudisuHttpService} from "./studisu-http.service";

/**
 * Serviceklasse zur Ermittlung von Studienangeboten ueber Http-Backendzugriff
 *
 */
@Injectable()
export class StudienangeboteService {
    private serverURL: string;
    private conf: StudisuConfig = getStudisuConfig();

    /**
     * Konstruktor, der Objekte zum Zugriff auf das Backend erwartet
     * @param oagHttp Instanz zum Ausfuehren von Http-Requests
     * @param urlParamService Serviceinstanz zum Auslesen der URL-Parameter
     * @param logger Serviceinstanz fuer Logging
     * @param errorHandlerService Serviceinstanz fuer Fehlerauswertung
     */
    constructor(private oagHttp: StudisuHttpService,
        private urlParamService: UrlParamService,
        private logger: LoggingService,
        private errorHandlerService: ErrorHandlerService) {
        this.serverURL = this.conf.studienangeboteServiceHost + this.conf.studienangeboteServicePath;
    }

    /**
     * ermittelt ueber einen Backendzugriff Studienangebote ensprechend den in der URL enthaltenen Sucheinschraenkungen
     */
    public getStudienangebote(): Observable<SearchResult> {
        let params: string = this.urlParamService.getUrlParamsForBackend();

        if (!this.validateParams(params)) {
            return Observable.throw("Parameters not valid");
        }
        if (params.indexOf(UrlParamService.PARAM_STUDIENFELDER) !== -1
            || params.indexOf(UrlParamService.PARAM_STUDIENFAECHER) !== -1) {
            let url = encodeURI(this.serverURL) + params;
            this.logger.info("requestet Studienangebote on: " + url);
            return this.oagHttp.get(url)
                .map(this.extractData)
                .catch(error => this.errorHandlerService.handleError(error));
        } else {
            return new Observable<SearchResult>();
        }
    }

    /**
     * ermittelt ueber einen Backendzugriff nachzuladende Studienangebote ensprechend den in der URL enthaltenen Sucheinschraenkungen
     */
    public getStudienangeboteZumNachladen(page: number): Observable<SearchResult> {
        let params: string = this.urlParamService.getUrlParamsForBackend(
            {
                [UrlParamService.PARAM_PAGE]: page
            }
        );
        // ergänze Parameter für Nachladen, damit im Backend Offset entsprechend gesetzt wird
        params += ("&" + UrlParamService.PARAM_RELOAD + "=1");

        if (!this.validateParams(params)) {
            return Observable.throw("Parameters not valid");
        }
        let url = encodeURI(this.serverURL) + params;
        this.logger.info("requestet nachzuladende Studienangebote on: " + url);
        return this.oagHttp.get(url)
            .map(this.extractData)
            .catch(error => this.errorHandlerService.handleError(error));
    }

    /**
     * uebernimmt die vom Backend erhaltenen Suchergebnisse in das auf Clientseite verwendete Suchergebnisobjekt
     * @param res json-Antwortobjekt vom Backend
     */
    private extractData(res: Response) {
        let body = res.json();
        body.items = body.items.map(x => new Studienangebot(x));
        let searchResult: SearchResult = body;
        searchResult.hasSearched = true;
        return searchResult;
    }

    /**
     * This function will validate the query parameters. Unused or unknown url parameters
     * will be ignored by this check.
     * @param queryParams false when parameters are missing or its values malformed
     */
    private validateParams(queryParams: string) {
        //  TODO rework validation to properly block unnecessary requests
        return queryParams.indexOf(UrlParamService.PARAM_ORTE) > -1
            || queryParams.indexOf(UrlParamService.PARAM_STUDIENFELDER) > -1
            || queryParams.indexOf(UrlParamService.PARAM_STUDIENFAECHER) > -1;
    }

}
