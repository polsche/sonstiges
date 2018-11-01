import { Headers } from "@angular/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { Ort } from "./model/Ort";
import { StudisuConfig, getStudisuConfig } from "../../../config/studisu.config";
import { ErrorHandlerService } from "../../../error/errorhandler.service";
import {StudisuHttpService} from "../../../services/studisu-http.service";
import {ServiceConstants} from "../../../services/serviceconstants";

@Injectable()
export class OrtService {

    private conf: StudisuConfig = getStudisuConfig();
    private suchparamstring = "?ortsuche=";

    constructor(private oagHttp: StudisuHttpService,
        private errorHandlerService: ErrorHandlerService) { }

    public suchen(searchTerm: string): Observable<Ort[]> {
        let url = encodeURI(this.conf.studienangeboteServiceHost + this.conf.ortServicePath + this.suchparamstring + searchTerm);
        return this.oagHttp.get(url, { headers: this.getHeaders() })
            .catch(err => Observable.throw(ServiceConstants.SERVICE_NOT_AVAILABLE))
            .map(x => <Ort[]>x.json());
    }

    private getHeaders() {
        let headers = new Headers();
        headers.append("Accept", "application/json");
        return headers;
    }
}




