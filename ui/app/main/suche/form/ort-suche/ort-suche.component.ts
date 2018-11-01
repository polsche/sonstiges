import {Component, OnInit, OnDestroy, Input, ChangeDetectionStrategy} from "@angular/core";
import {UrlParamService} from "../../../../main/suche/services/url-param.service";
import {OrtService} from "../../services/ort.service";
import {Ort} from "../../../suche/services/model/Ort";
import {Subscription} from "rxjs/Subscription";
import {isNumeric} from "rxjs/util/isNumeric";
import {SelectItem} from "../../../../ui-components/model/SelectItem";
import {Messages} from "../../../../ui-components/model/Messages";
import {LoggingService} from "../../../../services/logging.service";
import {ServiceConstants} from "../../../../services/serviceconstants";
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";
import {ISelectItem} from "../../services/model/ISelectItem";

@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  providers: [
    OrtService
  ],
  selector: "ba-studisu-ort-suche",
  templateUrl: "./ort-suche.component.html"
})
export class OrtSucheComponent implements OnInit, OnDestroy {

  /**
   * Observable für die Ergebnisliste nach Eingabeänderung.
   */
  public inputObservable: Observable<ISelectItem[]>;

  /**
   * Wenn der Zähler erhöht wird, dann wird der aktuelle Ortswert übernommen.
   * => benötigt für die Funktion des SUCHEN-Buttons.
   *
   * @param {number} counter
   */
  @Input()
  set doSubmit(counter: number) {
    if (counter > 0
      && this._ort.length >= this.AUTOSUGGEST_MINLEN
      && this.listSuggested.length > 0
      && this.listSuggested[0].name.toLocaleLowerCase().startsWith(this.ort.toLocaleLowerCase())) {
      let ort = this.listSuggested[0];
      this.onSubmit(new SelectItem(ort.name, ort, ort.name, false, true));
      this.ort = "";
    }
  }

  public disabled = false;

  /**
   * Text, der im Eingabefeld der Ortsauswahl erscheint;
   */
  public placeholderText = Messages.ORT_EINGABEFELD_INFO;

  /**
   * Text, der im Eingabefeld der Ortsauswahl erscheint;
   * ab 3 Orten erscheint der fachlich gewünschte Text
   */
  public placeholderDisabledText = Messages.ORT_AUSWAHL_MAX_ORTE;

  set ort(ort: string) {
    this._ort = ort;
    this.ortSubject.next(ort); // emit change to ort. See subscription in on init
  }

  get ort(): string {
    return this._ort;
  }

  private validateRegex = "^(\\s*|\\d{1,5}|([a-zA-ZäöüàâæçèéêëîïôœùûÿÀÂÆÇÈÉÊËÎÏÔŒÙÛŸÄÖÜß/().,-]+\\s*)+)$";
  private _ort = "";
  private MAX_ORTE = 3;
  private AUTOSUGGEST_PLZ_LEN = 5;
  private AUTOSUGGEST_MINLEN = 2;
  private listSuggested: Ort[] = [];

  //  Array gewaehlter Orte => Anzeige ueber FilterCloud
  //  Two-Way-Binding
  private _selektierteOrte: Ort[] = [];

  set selektierteOrte(orte: Ort[]) {
    this._selektierteOrte = orte;
    this.updateOrtParam(false, false);
  }

  get selektierteOrte(): Ort[] {
    return this._selektierteOrte;
  }

  private paramsSubscription: Subscription;
  // emits changes in ort string
  private ortSubject: Subject<string> = new Subject<string>();

  /**
   * constructor
   * @param urlService Injected Dependency
   * @param activeRoute Injected Dependency
   * @param logger Injected Dependency
   */
  constructor(private urlService: UrlParamService,
              private ortService: OrtService,
              private logger: LoggingService) {
  }

  /**
   * Initialisierung der Komponente
   * Hier wird der zugehoerige Url Parameter ausgelesen und
   * verarbeitet
   */
  public ngOnInit() {
    // subject hört auf Änderungen in autocomplete component
    // hier ist validierung und suche implementiert
    this.inputObservable = this.ortSubject
      .distinctUntilChanged()
      .debounceTime(150)
      .switchMap(value => {
        this.listSuggested = [];
        let valid = new RegExp(this.validateRegex, "i").test(value);
        if (!valid) {
          return Observable.of([new SelectItem("err", null, Messages.ORT_EINGABEPARAMETER_FEHLERHAFT, false, false)]);
        }
        if (isNumeric(value) && (value.length < this.AUTOSUGGEST_PLZ_LEN)) {
          return Observable.of([new SelectItem("err", null, Messages.ORT_EINGABEPARAMETER_PLZ, false, false)]);
        }
        if (!isNumeric(value) && value.length < this.AUTOSUGGEST_MINLEN) {
          return Observable.of([]);
        }
        return this.ortService.suchen(this.ort)
          .map(orte => {
            if (orte.length === 0) {
              return [new SelectItem("err", null, Messages.ORT_EINGABEPARAMETER_FEHLERHAFT, false, false)];
            }
            this.listSuggested = orte;
            return orte.map(sf => new SelectItem(sf.name, sf, sf.name, false, true));
          })
          .catch(err => {
            this.logger.error(Messages.FEHLER_LADEN_ORT, this);
            return Observable.of([new SelectItem("err", ServiceConstants.SERVICE_NOT_AVAILABLE,
              Messages.FEHLER_LADEN_ORT, false, false)]);
          });
      });

    this.paramsSubscription = this.urlService.currentParams
      .map(params => params.get(UrlParamService.PARAM_ORTE))
      .distinctUntilChanged()
      .subscribe(orteParam => {
        this.validateOrte(orteParam);
        this.parseUrlParameters(orteParam);
        this.updateUmkreisComponent();
        this.maxOrteDisableInput();
      });
  }

  public ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  /**
   * Speichert den neuen Ort und ruft die Auswahl auf.
   *
   * @param newOrtItem SelectItem mit dem neu eingegebenen Ort
   */
  public onSubmit(newOrtItem: SelectItem) {
    this.selectOrt(this.listSuggested.find((ort: Ort) => ort.name === newOrtItem.label));
    this.updateOrtParam(false, true);
  }

  /**
   * Validiert einen neuen Ort und fuegt ihn im erfolgsfall,
   * der Auswahl hinzu
   * @param newOrt neuer Ort
   */
  public selectOrt(newOrt: Ort) {
    let hasOrte = this._selektierteOrte.length > 0;

    if (this._selektierteOrte.findIndex(ort => ort.name === newOrt.name) === -1) {
      this._selektierteOrte.push(newOrt);
      if (!hasOrte) {
        this.urlService.updateView({[UrlParamService.PARAM_UK]: "50"});
      }
    }
    // Damit man die gleiche Suchanfrage wieder stellen kann, muss (wg. "Debounce") das Subject gelöscht werden.
    this.ortSubject.next("");
  }

  public removeOrt(ort) {
    let hasOrte = this._selektierteOrte.length > 0;

    this.selektierteOrte.forEach((val, index, arr) => {
      if (val.name === ort.name) {
        arr.splice(index, 1);
      }
    });

    if (hasOrte && this.selektierteOrte.length === 0) {
      this.urlService.updateView({[UrlParamService.PARAM_UK]: Messages.URLPARAM_UK_VALUE_BUNDESWEIT});
    }

    this.updateOrtParam(false, false);
  }


  /**
   * Validierung des URL-Parameters
   * Wenn mehr als drei Werte für Ort mitgegeben wurden
   * wir der letzte Wert abgeschnitten
   * @param params Orte Parameter als String
   */
  private validateOrte(orteParam: String) {
    let orteArray: String[];
    if (null != orteParam) {
      orteArray = orteParam.split(UrlParamService.VALUE_SEPARATOR);
      let orteNoDupes: Set<String> = new Set<String>();
      orteArray.filter(ort => {
        if (!orteNoDupes.has(ort)) {
          orteNoDupes.add(ort);
          return true;
        }
      });
    }
  }

  /**
   * Helper Function zum parsen der Orte aus der Url
   * @param params QueryParams des Routers
   */
  private parseUrlParameters(orte: string) {
    let orteList: string[] = orte ? orte.split(UrlParamService.VALUE_SEPARATOR) : [];
    let result: Ort[] = [];
    orteList.forEach(val => {
      result.push(this.getUrlParamAsOrt(val));
    });
    this._selektierteOrte = result;

    if (this._selektierteOrte.length > this.MAX_ORTE) {
      this._selektierteOrte = this._selektierteOrte.slice(0, this.MAX_ORTE);
      this.updateOrtParam(true, false);
    }
  }

  /**
   * Konvertiert einen Ort zu einem Url Parameter String in der Form:
   * <NAME>_<LAENGENGRAD>_<BREITENGRAD>
   * @param ort ort
   */
  private getOrtAsUrlParam(ort: Ort): string {
    return ort.name + "_" + ort.laengengrad + "_" + ort.breitengrad;
  }

  /**
   * Konvertiert einen Ort zu einem Url Parameter String in der Form:
   * <NAME>_<LAENGENGRAD>_<BREITENGRAD>
   * @param ort ort
   */
  private getUrlParamAsOrt(ort: string): Ort {
    if (!ort) {
      return null;
    }
    let values = ort.split("_");

    return values.length === 3 ? new Ort(values[0], values[1], values[2]) : null;
  }

  /**
   * Aktualisiert die aktuelle Ortsselektion mit dem UrlParamService
   * und updated das Binding
   * @param replaceUrl Aktuelle URL in der Browser-History ersetzen?
   * @param resetBundeslaender Filterung nach Bundeslaendern aufheben?
   */
  private updateOrtParam(replaceUrl: boolean, resetBundeslaender: boolean) {
    let urlParams = {
      [UrlParamService.PARAM_ORTE]:
        this._selektierteOrte
          .map(ort => this.getOrtAsUrlParam(ort))
          .join(UrlParamService.VALUE_SEPARATOR)
    };
    this.logger.debug(urlParams[UrlParamService.PARAM_ORTE]);

    if (resetBundeslaender) {
      urlParams[UrlParamService.PARAM_REGION] = null;
    }

    if (replaceUrl) {
      this.urlService.updateView(urlParams, {replaceUrl: true});
    } else {
      urlParams[UrlParamService.PARAM_PAGE] = null;
      this.urlService.updateView(urlParams);
    }
  }


  /**
   * Aktualisiert die Umkreis-Komponente aufgrund des URL-Parameters ort. Falls kein Ort gewählt wurde, wird der Umkreis
   * auf Bundesweit gesetzt, falls dieser noch nicht auf Bundesweit gesetzt ist.
   */
  private updateUmkreisComponent() {
    // URL Parameter muss nur gesetzt werden, wenn kein Ort gewählt und URL Parameter noch nicht auf 'Bundesweit'
    if (this._selektierteOrte.length === 0
      && !this.urlService.hasParamWithValue(UrlParamService.PARAM_UK, Messages.URLPARAM_UK_VALUE_BUNDESWEIT)) {

      let ukParam = Messages.URLPARAM_UK_VALUE_BUNDESWEIT;
      this.urlService.updateView({[UrlParamService.PARAM_UK]: ukParam}, {replaceUrl: true});
    }
  }

  /**
   * STUDISU-39: Ab drei Orten im Eingabefeld wird das Feld deaktiviert;
   *             diese Methode wird in der ngOnInit Orts-Parameteränderung subscribed und aufgerufen.
   *             Auswertung des disabled-Attributs erfolgt in der Component-HTML
   */
  private maxOrteDisableInput(): void {
    if (this._selektierteOrte.length >= this.MAX_ORTE) {
      this.disabled = true;
      this.placeholderText = Messages.ORT_AUSWAHL_MAX_ORTE;
    } else {
      this.disabled = false;
      this.placeholderText = Messages.ORT_EINGABEFELD_INFO;
    }
  }
}

