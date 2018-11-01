import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {UrlParamService} from "../../main/suche/services/url-param.service";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {SearchResult} from "../../model/SearchResult";
import {Facette} from "../../main/suche/services/model/Facetten";
import {SelectItem} from "../../ui-components/model/SelectItem";
import {LoggingService} from "../../services/logging.service";
import {OpenFacetteService} from "../../main/suche/services/open-facette.service";
import {Messages} from "../model/Messages";
import {MessageService} from "../../services/message.service";
import {OsaInfoPopupDelegete} from "../../ui-components/delegetes/osa-info-popup.delegete";

@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  selector: "ba-studisu-facettenfilter",
  templateUrl: "./facettenfilter.component.html"
})
export class FacettenfilterComponent implements OnInit, OnDestroy {

  @Input()
  set infopopupDelegate(delegate: OsaInfoPopupDelegete) {
    this._infopopupDelegate = delegate;
    this.showInfo = this._infopopupDelegate != null;
  }

  get infopopupDelegate(): OsaInfoPopupDelegete {
    return this._infopopupDelegate;
  }

  @Input()
  set searchResult(searchResult: SearchResult) {
    // Nichts tun, wenn sich die Facetten nicht geändert haben
    if (this._searchResult && searchResult && this._searchResult.facetten === searchResult.facetten) {
      return;
    }
    this._searchResult = searchResult;
    this.initFacette();
  }

  get searchResult(): SearchResult {
    return this._searchResult;
  }

  @Input()
  set urlParam(value: string) {
    this._urlParam = value;
    this.filterId = value;
    this.initFacette();
  }

  get urlParam(): string {
    return this._urlParam;
  }

  @Input()
  public filterFacettenName = "Generic Filter Facette";
  @Input()
  public styleClass: string;
  @Input()
  public icon: string;
  @Input()
  public showMobile: boolean;

  public filterList: SelectItem[] = [];

  private showInfo = false;
  private _searchResult: SearchResult;
  private _urlParam: string;
  private keineErgebnisse = Messages.KEINE_ERGEBNISSE;
  private _infopopupDelegate: OsaInfoPopupDelegete;

  @Output()
  private onSelectionChange = new EventEmitter<SelectItem>();
  //  Subscription for the open facette event
  private openFacetteSubscription: Subscription;
  //  Information about selected Filters, retrieved from url, will be stored in this list
  private selectedFilters: string[] = [];
  private showReset = false;
  private hidden = true;
  private filterId: string;

  constructor(
    private urlParamService: UrlParamService,
    private activeRoute: ActivatedRoute,
    private logger: LoggingService,
    private openFacetteService: OpenFacetteService,
    public messageService: MessageService) {
  }

  /**
   * On Init -  subscribing on url parameter change event and open facette event
   */
  public ngOnInit() {
    this.logger.debug(`Filter is being initialized with URL param ${this.urlParam}`);
    this.openFacetteSubscription = this.openFacetteService.getFilterIdOpened().subscribe(filterIdOpened => {
      if (this.filterId !== filterIdOpened && !this.hidden) {
        this.hidden = true;
      }
    });
  }

  /**
   * cleanup like unsubscribing from used observables
   */
  public ngOnDestroy() {
    if (this.openFacetteSubscription != null) {
      this.openFacetteSubscription.unsubscribe();
    }
  }

  public hasSelected(): boolean {
    return this.filterList.find(value => value.selected) != null;
  }

  /**
   * Initialisierung der Facette.
   *
   * Diese Methode wird von den beiden Settern für urlParam und searchResult aufgerufen und führt die Initialisierung
   * dann aus, wenn wirklich *beide* Werte gesetzt sind.
   *
   * => Auflösung einer Race-Condition beim Setzen der Parameter, die eine korrekte Anzeige der Facette verhinderte.
   */
  private initFacette() {
    if (this._searchResult === undefined || this._urlParam === undefined) {
      return;
    }
    let param = this.activeRoute.snapshot.queryParams[this.urlParam];
    this.parseParameters(param);
    this.parseFacetteDefinition();
  }

  /**
   * Parses the searchResult to extract the filter facette definition
   * and generate the view model for this filter
   */
  private parseFacetteDefinition() {
    if (!this._searchResult) {
      return;
    }
    // waehle den filter, der diese ID traegt, indem die liste auf genau einen gefiltert wird.
    let filter: Facette = this._searchResult.facetten
      .filter((value: Facette) => value.id === this.filterId)[0];
    let filterListNeu = [];
    if (filter) {
      let nothingSelected = filter.auswahl.every(item => !item.preset);
      filterListNeu = filter.auswahl.map(
        item => {
          let isSelected = item.preset;
          let plus = !nothingSelected && !isSelected;
          let cssClass = `cnt${plus ? " plus" : ""}`;
          // tslint:disable-next-line:max-line-length
          let label = `${item.label} (<span class="${cssClass}" title='${plus ? "Weitere " + item.trefferAnzahl + " Suchergebnisse in dieser Filterkategorie aufrufen." : ""}'>${plus ? "+" : ""}${item.trefferAnzahl}</span>)`;
          let isActive = true;
          this.logger.debug(label + ": " + isSelected);
          return new SelectItem(item.id, item, label, isSelected, isActive);
        }
      );
    }
    this.filterList = filterListNeu;
    this.updateResetButtonVisibility();

    this.logger.debug("Filter Definition parsed");

  }

  /**
   * Tries to parse it's Filter Values from the current Url Parameters.
   *
   * If the parameter is set it will be registered in the UrlParamService and
   * possible selected Filters are identified.
   * @param urlParamValue string
   */
  private parseParameters(urlParamValue: string) {
    this.selectedFilters = urlParamValue ? urlParamValue.split(UrlParamService.VALUE_SEPARATOR) : [];
  }

  /**
   * Aktualisiert den Wert des jeweiligen URL-Parameters anhand der selektierten Filter/Facetten
   * und gibt dies an den UrlParamService und updated das Binding
   */
  private updateParam() {
    let param = this.filterList
      .filter(f => f.selected)
      .map(f => f.key)
      .join(UrlParamService.VALUE_SEPARATOR);
    this.urlParamService.updateView({[this.urlParam]: param, [UrlParamService.PARAM_PAGE]: null});
  }

  /**
   * Publishes the onSelectionChange Event, which will emit the selected
   * item.
   * @param item SelectItem which has been selected
   */
  private onFilterFacetteAendern(item: SelectItem) {
    this.onSelectionChange.emit(item);
    this.updateResetButtonVisibility();
    this.updateParam();
    this.toggleDisplay();
  }

  /**
   * Setzt alle Filter zurück
   */
  private resetFilters() {
    this.filterList.forEach(filter => filter.selected = false);
    this.updateParam();
    this.onSelectionChange.emit();
    this.toggleDisplay();
    this.updateResetButtonVisibility();
  }

  /**
   * Blendet die Filteroptionen ein bzw. aus
   */
  private toggleDisplay() {
    this.hidden = !this.hidden;
    // informiere andere Facetten, dass diese Facette geöffnet wurde
    if (!this.hidden) {
      this.openFacetteService.setFilterIdOpened(this.filterId);
    }
  }

  /**
   * Updates the visibility of the filter reset button.
   */
  private updateResetButtonVisibility() {
    this.showReset = false;
    this.filterList.forEach(x => {
      if (x.selected) {
        this.showReset = true;
      }
    });
  }

  /**
   * Erzeugt ein Aria Label fuer einzelne Checkboxen des Facettenfilters.
   *
   * @param item Die Facette, deren Aria-Label erzeugt werden soll.
   * @return Aria-Repräsentation für die Facettenausprägung.
   */
  private createAriaLabelForCheckbox(item): string {
    if (item && item.label) {
      return item.label
        .replace(/\<[^>]+\>/g, "")                             // Tags aus dem Texts entfernen
        .replace(/\(\+([^)]+)\)/, ", fügt $1 Einträge hinzu"); // (+xx) behandeln
    } else {
      this.logger.debug("facettenfilter.createAriaLabelForCheckbox called with empty item.label");
      return "";
    }
  }

}
