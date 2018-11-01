import {Injectable} from "@angular/core";
import {URLSearchParams} from "@angular/http";
import {Router, ActivatedRoute, Params} from "@angular/router";
import {LoggingService} from "../../../services/logging.service";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class UrlParamService {
  // Separators
  public static VALUE_SEPARATOR = ";";

  // Parameter Constants - Searchparameters
  public static PARAM_ORTE = "ort";
  public static PARAM_STUDIENFAECHER = "sfa";
  public static PARAM_STUDIENFELDER = "sfe";
  public static PARAM_UK = "uk";

  // Parameter Constants - Filterparameters
  public static PARAM_STUDIENFORMEN = "sfo";
  public static PARAM_HOCHSCHULART = "hsa";
  //                     - Fit fuers Studium
  public static PARAM_FFSTUDIUM = "ffst";
  //                     - Studientypen
  public static PARAM_STUDT = "st";
  //                     - Regionen (Bundesländer und Nachbarländer)
  public static PARAM_REGION = "re";

  public static FILTER_PARAMS: string[] = [
    UrlParamService.PARAM_STUDIENFORMEN,
    UrlParamService.PARAM_HOCHSCHULART,
    UrlParamService.PARAM_STUDT,
    UrlParamService.PARAM_FFSTUDIUM,
    UrlParamService.PARAM_REGION
    // ? page
  ];

  // Parameter Constants - misc
  public static PARAM_PAGE = "pg";
  public static PARAM_RELOAD = "reload";

  public currentParams: BehaviorSubject<Map<string, string>> = new BehaviorSubject<Map<string, string>>(new Map<string, string>());

  private urlSearchParams: URLSearchParams;

  /**
   * Der Konstruktor wird mit dem Router, der aktive Route sowie dem Logger aufgerufen.
   *
   * @param router
   * @param activeRoute
   * @param logger
   */
  constructor(private router: Router,
              private activeRoute: ActivatedRoute,
              private logger: LoggingService) {
    // Wir müssen über Änderungen in der Browser-Route informiert werden, weil sonst
    // die korrekte Abfrage der Parameter nach einem Browser-Back / -Forward nicht möglich
    // ist.
    activeRoute.queryParams.subscribe(
      params => {

        let p: Params = params;
        let m = new Map<string, string>();

        this.urlSearchParams = new URLSearchParams();
        for (let key in p) {
          if (params.hasOwnProperty(key)) {
            this.urlSearchParams.set(key, p[key]);
            m.set(key, p[key]);
          }
        }

        this.currentParams.next(m);
      }
    );
  }

  /**
   * Löscht alle Filter Parameter
   */
  public resetFilter() {
    for (let param of UrlParamService.FILTER_PARAMS) {
      this.urlSearchParams.delete(param);
    }
  }

  /**
   * Liefert die aktuellen URL-Parameter für Weitergabe ins Backend, ggf. ergänzt um die angegebenen Parameter.
   *
   * Nur belegte Parameter werden ausgeliefert (die anderen werden entfernt) und das Ergebnis ist ein encodierter
   * String in der Form:
   *      ?param1=value&param2=value
   *
   * @param params Das JSON-Objekt mit den zu ergänzenden Parametern.
   */
  public getUrlParamsForBackend(params?: {}): string {
    return "?" + this.getSearchParams(params).toString();
  }

  /**
   * Liefert die aktuellen URL-Parameter fürs Frontend.
   *
   * Alle Parameter werden encodiertert.
   * String in der Form:
   *      ?param1=value&param2=value
   *
   * @param params Das JSON-Objekt mit den zu ergänzenden Parametern.
   */
  public getUrlParamsForFrontend(): string {
    let params: URLSearchParams = this.getSearchParams();
    return "?" + params.toString();
  }

  /**
   * Liefert ein Params-Objekt mit den gespeicherten Parameter, ggf. ergänzt um die angegebenen Parameter.
   *
   * Dieses Ergebnis kann direkt vom [queryParams] Parameter für einen routerLink verwendet werden.
   *
   * @param params Das JSON-Objekt mit den zu ergänzenden Parametern.
   */
  public getQueryParams(params?: Params): Params {
    let p = this.getSearchParams(params);
    let ret: Params = {};
    p.paramsMap.forEach((value, key) => ret[key] = p.get(key));
    return ret;
  }

  /**
   * Gibt an, ob ein bestimmter Parameter mit einem bestimmten Wert gesetzt ist.
   *
   * @param paramKey zu prüfender Parametername
   * @param paramValue Sollwert des Parameters
   * @returns {boolean} true, wenn der Parameter mit dem Wert gesetzt it, andernfalls false.
   */
  public hasParamWithValue(paramKey: string, paramValue: string) {
    let value = this.urlSearchParams.get(paramKey);
    return value != null && value === paramValue;
  }

  /**
   * Aktualisiert die aktuelle Seite.
   *
   * Als Query-Parameter werden alle aktuellen verwendet, die ggf.
   * mit den angegebenen params überschrieben bzw. angereichert werden.
   * @param params z.B. {[foo]: bar, [x]: y}
   */
  public updateView(params?: Params, options?: {}) {
    this.urlSearchParams = this.getSearchParams(params);
    let path = this.router.url.split("?")[0];
    this.navigateTo(path, null, options);
  }

  /**
   * Ruft eine bestimmte Seite auf.
   * Als Query-Parameter werden alle aktuellen verwendet, die ggf.
   * mit den angegebenen params überschrieben bzw. angereichert werden.
   * @param path z.B. /suche
   * @param params z.B. {[foo]: bar, [x]: y}
   */
  public navigateTo(path: string, params?: Params, options?: {}) {
    let newParams: URLSearchParams = this.getSearchParams(params);
    this.router.navigateByUrl(path + "?" + newParams.toString(), options);
  }

  private getSearchParams(params?: Params): URLSearchParams {
    let ret = this.urlSearchParams.clone();
    if (params) {
      for (let key in params) {
        if (params.hasOwnProperty(key)) {
          ret.set(key, params[key]);
        }
      }
    }
    // leere Parameter entfernen
    ret.paramsMap.forEach((value, key) => {
      if (value == null || value.length === 0 || (value.length === 1 && value[0].length === 0)) {
        ret.delete(key);
      }
    });
    return ret;
  }
}
