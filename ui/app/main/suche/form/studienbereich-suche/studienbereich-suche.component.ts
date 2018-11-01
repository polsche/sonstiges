import {ISelectItem} from "../../services/model/ISelectItem";
import {Component, OnInit, OnDestroy, Input, ChangeDetectionStrategy} from "@angular/core";
import {UrlParamService} from "../../../../main/suche/services/url-param.service";
import {LoggingService} from "../../../../services/logging.service";
import {Studienfach} from "../../../suche/services/model/Studienfach";
import {Subscription} from "rxjs/Subscription";
import {SelectItem} from "../../../../ui-components/model/SelectItem";
import {StudienfelderService} from "../../../../main/studienfelder/services/studienfelder.service";
import {StudienfachService} from "../../../../main/suche/services/studienfach.service";
import {StudienfelderDelegate, MAX_STUDIENBEREICH_PARAMS} from "../../../../main/studienfelder/studienfelder.delegate";
import {Messages} from "../../../../ui-components/model/Messages";
import {ServiceConstants} from "../../../../services/serviceconstants";
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";

/**
 * Diese Komponente ist für die Auswahl von Studienfächern (NICHT Studienfeldern!) zuständig.
 *
 * Während die Studienfelder über die entsprechende Auswahlseite bzw. über den entsprechenden
 * URL-Parameter gewählt werden, können die StudienFÄCHER über das Eingabefeld mit Drop-Down
 * selektiert und eingegeben werden.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  providers: [
    StudienfelderService,
    StudienfachService,
    StudienfelderDelegate
  ],
  selector: "ba-studisu-studienbereich-suche",
  templateUrl: "./studienbereich-suche.component.html"
})
// TODO: AK_9: Sind bereits ein oder mehrere Studienfelder gewaehlt, verhaelt sich der
// zugeordnete weitere Studienfach wie eine ODER- Verknuepfung.
export class StudienbereichSucheComponent implements OnInit, OnDestroy {

  /**
   * Observable für die Ergebnisliste nach Eingabeänderung.
   */
  public inputObservable: Observable<ISelectItem[]>;

  /**
   * Wenn der Zähler erhöht wird, dann wird der aktuelle Studiengang übernommen.
   * => benötigt für die Funktion des SUCHEN-Buttons.
   *
   * @param {number} counter
   */
  @Input()
  set doSubmit(counter: number) {
    if (counter > 0
      && this._studienfachEingabe.length >= this.AUTOSUGGEST_MINLEN
      && this.listSuggested.length > 0
      && this.listSuggested[0].name.toLocaleLowerCase().startsWith(this._studienfachEingabe.toLocaleLowerCase())) {
      let sf = this.listSuggested[0];
      this.onSubmit(new SelectItem(sf.name, sf, sf.name, false, true));
      this.studienfach = "";
    }
  }

  /**
   * Text, der im Eingabefeld der Studienbereichsauswahl erscheint
   */
  public placeholderText = Messages.STUDIENBEREICH_EINGABEFELD_INFO;

  /**
   * Text, der im disableten Eingabefeld der Studienbereichsauswahl erscheint
   */
  public placeholderDisabledText = "Max. 40 Studienbereiche";

  get studienbereiche() {
    return this.studienfelderDelegate.studienfeldgruppen;
  }

  get tagCloudItems(): ISelectItem[] {
    return this.selektierteStudienfelder.concat(this.selektierteStudienfaecher);
  }

  get selektierteStudienfaecher() {
    return this.studienfelderDelegate.selektierteStudienfaecher;
  }

  get selektierteStudienfelder() {
    return this.studienfelderDelegate.selektierteStudienfelder;
  }

  set studienfach(_studienfachEingabe: string) {
    this._studienfachEingabe = _studienfachEingabe;
    this.studienbereichSubject.next(this._studienfachEingabe);
  }

  get studienfach(): string {
    return this._studienfachEingabe;
  }

  get isMaxParameterCountExceeded(): boolean {
    return this.studienfelderDelegate.selektierteStudienfelder.length +
      this.selektierteStudienfaecher.length >= MAX_STUDIENBEREICH_PARAMS;
  }

  private validateRegex = "^(\\s*|\\d{1,5}|([a-zA-ZäöüàâæçèéêëîïôœùûÿÀÂÆÇÈÉÊËÎÏÔŒÙÛŸÄÖÜß/().,-]+\\s*)+)$";
  private AUTOSUGGEST_MINLEN = 2;
  private urlParamSubscription: Subscription;
  private _studienfachEingabe = "";
  private studienbereichSubject: Subject<string> = new Subject<string>();
  private listSuggested: Studienfach[] = [];

  /**
   * Constructor der Komponente mit injizierten Services.
   *
   * @param urlParamService Injected Service.
   * @param studienfelderService Injected Service.
   * @param errorDialogService Injected Service zur Fehlerhaltung
   */
  constructor(private urlParamService: UrlParamService,
              private studienfachService: StudienfachService,
              public studienfelderDelegate: StudienfelderDelegate,
              private logger: LoggingService) {
  }

  /**
   * Initialisierung der Komponente
   *
   * Hier wird der zugehoerige URL-Parameter ausgelesen und verarbeitet
   */
  public ngOnInit() {
    // subject hört auf Änderungen in autocomplete component
    // hier ist validierung und suche implementiert
    this.inputObservable = this.studienbereichSubject
      .distinctUntilChanged()
      .debounceTime(150)
      .switchMap(value => {
        this.listSuggested = [];
        let valid = new RegExp(this.validateRegex, "i").test(value);
        if (!valid) {
          return Observable.of([new SelectItem("err", null, Messages.STUDIENBEREICHE_EINGABEPARAMETER_FEHLERHAFT, false, false)]);
        }
        if (value.length < this.AUTOSUGGEST_MINLEN) {
          return Observable.of([]);
        }
        return this.studienfachService.suchen(this.studienfach)
          .map(studienfelder => {
            if (studienfelder.length === 0) {
              return [new SelectItem("err", null, Messages.STUDIENBEREICHE_EINGABEPARAMETER_FEHLERHAFT, false, false)];
            }
            this.listSuggested = studienfelder;
            return studienfelder.map(sf => new SelectItem(sf.name, sf, sf.name, false, true));
          })
          .catch(err => {
            this.logger.error(Messages.FEHLER_LADEN_STUDIENBEREICH, this);
            return Observable.of([new SelectItem("err", ServiceConstants.SERVICE_NOT_AVAILABLE,
              Messages.FEHLER_LADEN_STUDIENBEREICH, false, false)]);
          });
      });

    this.studienfelderDelegate.init();

    // Wir subscriben für URL-Aenderungen bei den Studienfaechern.
    this.urlParamSubscription = this.urlParamService.currentParams
      .map(params => params.get(UrlParamService.PARAM_STUDIENFAECHER))
      .distinctUntilChanged()
      .subscribe(studienfachIds => this.parseUrlParameters(studienfachIds));
  }

  public ngOnDestroy() {
    this.studienfelderDelegate.destroy();
    if (null !== this.urlParamSubscription) {
      this.urlParamSubscription.unsubscribe();
      this.urlParamSubscription = null;
    }
  }

  /**
   * Nimmt die bestätigte Eingabe eines Studienfaches entgegen.
   *
   * Führt anschließend die Auswahl des Studienfaches aus der Vorschlagliste aus.
   *
   * @param newStudienfach Ausgewähltes Item für das neu eingegebene Studienfach.
   */
  public onSubmit(newStudienfachItem: SelectItem): void {
    this.addStudienfach(this.listSuggested.find((studienfach: Studienfach) => studienfach.name === newStudienfachItem.key));
  }

  /**
   * Helper Function zum parsen der Orte aus der Url
   * @param params QueryParams des Routers
   *
   * TODO abwaegen, ob dies nicht in studienfelder.delegate zu refactoren ist
   *      ggf. sollte er eher studienbereiche.delegate heissen
   */
  private parseUrlParameters(studienfachIds: string) {
    let idList = studienfachIds ? studienfachIds.split(UrlParamService.VALUE_SEPARATOR) : [];
    this.studienfelderDelegate.selektierteStudienfaecher = [];
    this.studienfachService.getIds(idList).subscribe(
      studienfaecher => {
        this.studienfelderDelegate.selektierteStudienfaecher = studienfaecher;

        // falls mehr als die maximale anzahl faecher in der url sind, werden nur die ersten MAX geladen
        if (this.selektierteStudienfaecher.length > MAX_STUDIENBEREICH_PARAMS) {
          this.logger.debug("studienbereich-suche.parseUrlParamseters() begrenzt studienfaecher aus url");
          this.studienfelderDelegate.selektierteStudienfaecher =
            this.studienfelderDelegate.selektierteStudienfaecher.slice(0, MAX_STUDIENBEREICH_PARAMS);
        }

        // falls zuvor schon felder geladen sind, (init ruft init des delegates auf)
        // duerfen hier nur noch soviele faecher geladen werden,
        // dass die summe nicht MAX ueberschreitet.
        let anzahlSelektierteStudienbereiche = this.selektierteStudienfaecher.length +
          this.selektierteStudienfelder.length;
        if (anzahlSelektierteStudienbereiche > MAX_STUDIENBEREICH_PARAMS) {
          let anzahlFuerStudienfaecher = MAX_STUDIENBEREICH_PARAMS - this.selektierteStudienfelder.length;
          this.studienfelderDelegate.selektierteStudienfaecher =
            this.selektierteStudienfaecher.slice(0, anzahlFuerStudienfaecher);
          this.logger.debug("studienbereich-suche.parseUrlParamseters() begrenzt studienfaecher aus url wegen feldern");
        }
      }
    );
  }


  private addStudienfach(studienfach: Studienfach) {
    let index = this.selektierteStudienfaecher.findIndex((x: Studienfach) => x.dkzId === studienfach.dkzId);
    if (index < 0) {
      this.selektierteStudienfaecher.push(studienfach);
    }
    this.updateView();
    // Damit man die gleiche Suchanfrage wieder stellen kann, muss (wg. "Debounce") das Subject gelöscht werden.
    this.studienbereichSubject.next("");
  }

  private updateView() {
    let urlParam: string = this.selektierteStudienfaecher
      .map(studienfach => studienfach.dkzId)
      .reduce((previousValue: number[], dkzId: number) => {
        previousValue.push(dkzId);
        return previousValue;
      }, [])
      .join(UrlParamService.VALUE_SEPARATOR);
    this.urlParamService.updateView({
      [UrlParamService.PARAM_STUDIENFAECHER]: urlParam,
      [UrlParamService.PARAM_PAGE]: null
    });
  }
}

