import {Injectable} from "@angular/core";
import {Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {UrlParamService} from "../suche/services/url-param.service";
import {StudisuConfig, getStudisuConfig} from "../../config/studisu.config";
import {LoggingService} from "../../services/logging.service";
import {StudienangebotDetail} from "../suche/services/model/StudienangebotDetail";
import {ErrorHandlerService} from "../../error/errorhandler.service";
import {StudisuHttpService} from "../../services/studisu-http.service";

@Injectable()
export class StudienangebotinfoService {
  private serverURL: string;
  private conf: StudisuConfig = getStudisuConfig();

  constructor(private oagHttp: StudisuHttpService,
              private urlParamService: UrlParamService,
              private logger: LoggingService,
              private errorHandlerService: ErrorHandlerService) {
    this.serverURL = this.conf.studienangeboteServiceHost + this.conf.studienangebotInformationen;
  }

  public getStudienangebot(id: number): Observable<StudienangebotDetail> {
    let params = this.urlParamService.getUrlParamsForBackend() + "&stangid=" + id;
    const url = this.serverURL + params;
    this.logger.debug("requestet Studienangebot on: " + url);
    return this.oagHttp.request(url)
      .map(response => this.extractData(response))
      .catch(error => this.errorHandlerService.handleError(error));
  }

  /**
   * liest das uebergebene Antwortobjekt aus. Enthaelt die Antwort keinen Text, gibt die Methode null zurueck.
   * @param response Antwortobjekt aus der Serveranfrage
   * @return StudienangebotDetail-Objekt oder null, falls Antwortobjekt keinen Textinhalt hat
   * @version STUDISU-105 zeige Fehlerdialog - Nullabfrage eingefuegt
   */
  private extractData(response: Response): StudienangebotDetail {
    let angebot: StudienangebotDetail = null;
    if (response.text() !== null) {
      angebot = new StudienangebotDetail(response.json());
    }
    return angebot;
  }

}
