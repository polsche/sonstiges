import { Headers } from "@angular/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { StudisuConfig, getStudisuConfig } from "../../config/studisu.config";
import { LoggingService } from "../../services/logging.service";
import { VersionInfo } from "./model/VersionInfo";
import { ErrorHandlerService } from "../../error/errorhandler.service";
import {StudisuHttpService} from "../../services/studisu-http.service";

/**
 * Service-Klasse zur Abfrage von Datenstand und Version aus dem Backend.
 *
 * @author OettlJ
 */
@Injectable()
export class VersionInfoService {

    private conf: StudisuConfig = getStudisuConfig();

    constructor(private oagHttp: StudisuHttpService,
                private logger: LoggingService,
                private errorHandlerService: ErrorHandlerService) { }

    /**
     * Methode zur Abfrage von Datenstand und Version aus dem Backend.
     * @returns {Observable<VersionInfo>}
     */
    public getVersion(): Observable<VersionInfo> {
        let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.versionInfoPath);
        return this.oagHttp.get(url, { headers: this.getHeaders() })
                           .catch(error => this.errorHandlerService.handleError(error))
                           .map( x => <VersionInfo> x.json());
    }

    /**
     * Methode zum Setzen der Header für den REST-Aufruf.
     * @returns {Headers}
     */
    private getHeaders() {
        let headers = new Headers();
        headers.append("Accept", "application/json");
        return headers;
    }

}
