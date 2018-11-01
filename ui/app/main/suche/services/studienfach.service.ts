import { LoggingService } from "../../../services/logging.service";
import { Headers } from "@angular/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { Studienfach } from "./model/Studienfach";
import { StudisuConfig, getStudisuConfig } from "../../../config/studisu.config";
import { UrlParamService } from "./url-param.service";
import { ErrorHandlerService } from "../../../error/errorhandler.service";
import {StudisuHttpService} from "../../../services/studisu-http.service";
import {ServiceConstants} from "../../../services/serviceconstants";

@Injectable()
export class StudienfachService {

    private conf: StudisuConfig = getStudisuConfig();
    private prefixSuche = "?sfa-sw=";
    private prefixGetList = "?sfa-ids=";

    constructor(private oagHttp: StudisuHttpService,
        private errorHandlerService: ErrorHandlerService,
        private loggingService: LoggingService) { }

    public suchen(searchTerm: string): Observable<Studienfach[]> {
        let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.studienfaecherServicePath + this.prefixSuche + searchTerm);
        return this.loadStudienfaecher(url);
    }

    public getIds(idList: string[]): Observable<Studienfach[]> {
        if (idList.length === 0) {
            return Observable.from(new Array<Studienfach[]>());
        }
        let ids = idList.join(UrlParamService.VALUE_SEPARATOR);
        let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.studienfaecherServicePath + this.prefixGetList + ids);
        this.loggingService.info("SERVICE CALL " + url);

        return this.loadStudienfaecher(url);
    }

    /**
     * Lädt die Studienfaecher aus der uebergeben URL und liefert das Observable mit der Liste zurueck.
     *
     * @param url Die zu verwendende URL
     * @return Observable<Studienfach[]>
     */
    private loadStudienfaecher(url: string): Observable<Studienfach[]> {
        return this.oagHttp
            .get(url, { headers: this.getHeaders() })
            .catch(error => Observable.throw(ServiceConstants.SERVICE_NOT_AVAILABLE))
            .map(x => this.mapStudienfach(x.json()));
    }

    private mapStudienfach(studienfachJson: Studienfach[]) {
        let result: Studienfach[] = [];
        studienfachJson.forEach(fach => {
            result.push(new Studienfach(fach.key, fach.name, fach.dkzId));
        });

        return result;
    }

    private getHeaders() {
        let headers = new Headers();
        headers.append("Accept", "application/json");
        return headers;
    }

}
