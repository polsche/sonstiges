import {Injectable} from "@angular/core";
import {ServiceConstants} from "../services/serviceconstants";
import {Observable} from "rxjs/Observable";
import {LoggingService} from "../services/logging.service";
import {Response} from "@angular/http";

/**
 * Hilfs-Serviceklasse zur Auswertung von Fehlern
 * @since STUDISU-105: Anzeige Fehlerdialog bei fehlgeschlagenem Servercall.
 */
@Injectable()
export class ErrorHandlerService {

  /**
   * Konstruktor, der Serviceinstanz fuer Logging erwartet
   * @param loggingService Serviceinstanz fuer Logging
   */
  constructor(private loggingService: LoggingService) {
  }

  /**
   * Methode zur Fehlerauswertung, die zu uebergebenen Objekt einen Fehlermeldungstext bildet.
   * Hier wird zwischen Responsefehlern aus Serveraufrufen und sonstigen Fehlern unterschieden,
   * bei ersteren werden Fehlerstatus und Statusmeldung ausgewertet.
   * @param error auszuwertendes Fehlerobjekt
   * @return Observable mit Fehlermeldungstext
   */
  public handleError(error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      if (error.status === ServiceConstants.NOT_FOUND) {
        errMsg = ServiceConstants.NOT_FOUND_MESSAGE;
      } else if (error.statusText === ServiceConstants.EINGABEN_UNVOLLSTAENDIG_ODER_FEHLERHAFT) {
        errMsg = error.statusText;
      } else {
        const body = error.json() || "";
        if (body instanceof Object) {
          if (body.message === ServiceConstants.EINGABEPARAMETER_FEHLERHAFT) {
            errMsg = body.message;
          } else if (body.message === ServiceConstants.EINGABEPARAMETER_ZU_LANG) {
            errMsg = body.message;
          }
        } else {
          const err = JSON.stringify(body);
          errMsg = `${error.status} - ${error.statusText || ""} ${err}`;
        }
      }
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    if (this.loggingService != null) {
      this.loggingService.error(errMsg, this);
    } else {
      // tslint:disable-next-line:no-console
      console.log("[error]\t" + errMsg);
    }
    return Observable.throw(errMsg);
  }
}
