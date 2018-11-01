import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from "@angular/core";
import {SearchResult} from "../../../../model/SearchResult";
import {UrlParamService} from "./../../services/url-param.service";
import {EventService} from "./../../../../services/event.service";
import {Subscription} from "rxjs";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {ActivatedRoute} from "@angular/router";
import {StudienangeboteService} from "../../../../services/studienangebote.service";
import {LoggingService} from "../../../../services/logging.service";
import {Messages} from "../../../../ui-components/model/Messages";
import {ZuletztAngesehenListe} from "../../../../services/zuletztangesehen.service";

@Component({
  animations: [
    trigger("loadingState", [
      state("loading", style({
        "color": "rgba(0,0,0,1.0)"
      })),
      transition("void => *", [
        style({"color": "rgba(0,0,0,0.0)"}),
        animate("500ms 200ms")
      ])
    ])
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "ba-studisu-ergebnisliste",
  templateUrl: "./ergebnisliste.component.html"
})
export class ErgebnislisteComponent implements OnInit, OnDestroy {

  public fehler = false;
  public loading = true;
  protected keineSuchkriterienAngegeben = Messages.KEINE_SUCHKRITERIEN_ANGEGEBEN;
  protected mindestensStudienbereichErforderlich = Messages.MINDESTENS_STUDIENFACH_ERFORDERLICH;
  protected keinePassendenErgebnisse = Messages.KEINE_PASSENDEN_ERGEBNISSE;
  protected fehlerLadenWeitereAngebote = Messages.FEHLER_LADEN_WEITERE_ANGEBOTE;
  protected bereitsAngesehenInPage = 0;
  private eventSubscription: Subscription;
  private page = 1;
  private reload = false;
  private _searchResult: SearchResult;

  constructor(private ref: ChangeDetectorRef,
              private urlParamService: UrlParamService,
              private eventService: EventService,
              private activeRoute: ActivatedRoute,
              private studienangeboteService: StudienangeboteService,
              private logger: LoggingService,
              private zuletztAngesehenListe: ZuletztAngesehenListe) {
  }

  public get searchResult(): SearchResult {
    return this._searchResult;
  }

  @Input()
  public set searchResult(sr: SearchResult) {
    this._searchResult = sr;
    this.ref.markForCheck();
    this.calcBereitsAngesehen();
  }

  public ngOnInit() {

    this.activeRoute.queryParams
      .map(params => {
        // lese Seite aus URL-Parameter, nur wenn Wert größer 1
        let pg = params[UrlParamService.PARAM_PAGE];
        return pg > 1 ? pg : "1";
      })
      .distinctUntilChanged()
      .subscribe(page => {

        this.page = parseInt(page, 10);

        // lade Angebote nach
        if (this.reload) {
          this.reload = false;
          this.studienangeboteService.getStudienangeboteZumNachladen(this.page).subscribe(searchResult => {
            this._searchResult.items = this._searchResult.items.concat((<SearchResult>searchResult).items);
            this.calcBereitsAngesehen();
            // STUDISU-107 Ueberprueft, ob weitere Angebote gesucht wurden
            if (this.searchResult.hasSearched === true) {
              this.fehler = false;
            }
            this.ref.markForCheck(); // Erzwinge Redraw nach Change der Liste für ChangeDetectionStrategy.OnPush
          }, err => {
            this.logger.error("Service Request Failed with error: " + err, this);
            // STUDISU-107 Setzt einen Flag, um die Fehlermeldung anzuzeigen und setzt den Wert der Seite um 1 zurueck
            this.fehler = true;
            this.urlParamService.updateView({[UrlParamService.PARAM_PAGE]: this.page - 1}, {replaceUrl: true});
          });
        }
      });

    this.eventSubscription = this.eventService.studienangeboteSucheStatus.subscribe(
      value => {
        let isLoading = (value === true);
        if (this.loading !== isLoading) {
          this.loading = isLoading;
          this.ref.markForCheck(); // Erzwinge Redraw nach Change der Liste für ChangeDetectionStrategy.OnPush
        }
      }
      // todo Fehlermeldung auswerten: falls value string, dann Fehlermeldung
    );
  }

  public ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  /**
   * Setzt das Reload-Flag auf true bzw. den URL-Parameter für die Seite
   * um 1 nach oben, sodass durch die Subscription die nächsten x Angebote
   * geladen werden.
   */
  public loadAngebote() {
    this.reload = true;
    this.urlParamService.updateView({[UrlParamService.PARAM_PAGE]: this.page + 1}, {replaceUrl: true});
  }

  /**
   * Berechnen wie viele von den Ergebnissen auf
   * "dieser Seite" in der zuletzt angesehenen Liste vorhanden sind.
   *  this.bereitsAngesehenInPage wird im Zahl "badge" angezeigt
   */
  private calcBereitsAngesehen() {
    this.bereitsAngesehenInPage = this._searchResult.items.reduce(
      (acc, curr) => {
        return acc + (this.zuletztAngesehenListe.contains(curr.id) ? 1 : 0);
      }, 0);
  }
}
