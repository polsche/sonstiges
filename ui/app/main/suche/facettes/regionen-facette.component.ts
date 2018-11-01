import {Component, OnInit, OnDestroy, Input, ViewChild, ChangeDetectionStrategy} from "@angular/core";
import {UrlParamService} from "../../../main/suche/services/url-param.service";
import {Subscription} from "rxjs/Subscription";
import {SearchResult} from "../../../model/SearchResult";
import {OpenFacetteService} from "../services/open-facette.service";
import {Messages} from "../../../ui-components/model/Messages";
import {Facette} from "../services/model/Facetten";
import {LoggingService} from "../../../services/logging.service";
import {SelectItem} from "../../../ui-components/model/SelectItem";

@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  selector: "ba-studisu-regionen-facette",
  templateUrl: "./regionen-facette.component.html"
})

export class RegionenFacetteComponent implements OnInit, OnDestroy {

  private static FILTER_ID = UrlParamService.PARAM_REGION;

  @Input()
  public showMobile = false;

  /**
   * Vom Mobile-Flag abhängiger Scope-Name, der für die Funktionalität des Scope-Umschalters benötigt wird.
   * @returns {string | string}
   */
  public get scopeName() {
    return this.showMobile ? "mobileScope" : "desktopScope";
  }

  public filterList: SelectItem[] = [];
  public hidden = true;

  public get regioScope() {
    return this._regioScope;
  }
  public set regioScope(scope: string) {
    if (this._regioScope.startsWith(scope)) { // Ignorieren, wenn sich Status nicht ändert => keine Mehrfach-Animation
      return;
    }
    // Animation durch CSS-Klasse "animated" erlauben, 20ms nach Animationsende wieder abschalten per Timeout.
    this._regioScope = scope + " animated";
    setTimeout(() => this._regioScope = scope, 1220); // 20ms mehr als $animation-time im SASS-File!
  }

  @Input()
  set searchResult(searchResult: SearchResult) {
    // Nichts tun, wenn sich die Facetten nicht geändert haben
    if (this._searchResult && searchResult && this._searchResult.facetten === searchResult.facetten) {
      return;
    }
    this._searchResult = searchResult;
    this.parseFacetteDefinition();
  }

  @ViewChild("svg_germany")
  private svgGermany;

  @ViewChild("svg_europe")
  private svgEurope;

  private _searchResult: SearchResult;
  private _regioScope = "national";
  private keineErgebnisse = Messages.KEINE_ERGEBNISSE;
  private hasInternational = false;
  private showReset = false;
  private openFacetteSubscription: Subscription;

  private laenderLabels = {
    "BB": "Brandenburg",
    "BE": "Berlin",
    "BW": "Baden-Württemberg",
    "BY": "Bayern",
    "HB": "Bremen",
    "HE": "Hessen",
    "HH": "Hamburg",
    "MV": "Mecklenburg-Vorpommern",
    "NI": "Niedersachsen",
    "NW": "Nordrhein-Westfalen",
    "RP": "Rheinland-Pfalz",
    "SH": "Schleswig-Holstein",
    "SL": "Saarland",
    "SN": "Sachsen",
    "ST": "Sachsen-Anhalt",
    "TH": "Thüringen",
    "iA": "Österreich",
    "iB": "Belgien",
    "iCH": "Schweiz",
    "iCZ": "Tschechien",
    "iD": "Deutschland",
    "iDK": "Dänemark",
    "iE": "Spanien",
    "iF": "Frankreich",
    "iGB": "Großbritannien",
    "iH": "Ungarn",
    "iI": "Italien",
    "iLU": "Luxemburg",
    "iN": "Norwegen",
    "iNL": "Niederlande",
    "iPL": "Polen",
    "iS": "Schweden",
    "iSK": "Slowakei"
  };
  private focusedRegion: string = null;
  private focusAfterUpdate: string = null;

  get searchResult(): SearchResult {
    return this._searchResult;
  }

  /**
   * constructor
   * @param urlParamService (Injected) UrlParamService for triggering url updates
   * @param openFacetteService (Injected) OpenFacetteService for triggering facette openings
   */
  constructor(private urlParamService: UrlParamService,
              private logger: LoggingService,
              private openFacetteService: OpenFacetteService) {
  }

  /**
   * Initialization of this facette. Registers callback on url parameter change
   * which initializes default values. Subscribes on open facette event.
   */
  public ngOnInit() {
    // We do not subscribe to the URLParams because that is not needed here!

    this.openFacetteSubscription = this.openFacetteService.getFilterIdOpened()
      .subscribe(filterIdOpened => {
        if (RegionenFacetteComponent.FILTER_ID !== filterIdOpened && !this.hidden) {
          this.hidden = true;
        }
      });
  }

  public hasSelected(): boolean {
    return this.filterList.find(value => value.selected) != null;
  }

  /**
   * OnDestroy Hook to unsubscribe made Subscriptions of this component
   */
  public ngOnDestroy() {
    if (this.openFacetteSubscription != null) {
      this.openFacetteSubscription.unsubscribe();
    }
  }

  /**
   * Parses the searchResult to extract the filter facette definition
   * and generate the view model for this filter
   */
  private parseFacetteDefinition() {
    if (!this._searchResult) {
      return;
    }
    // Waehle den Filter, der diese ID traegt, indem die Liste auf genau einen gefiltert wird.
    let filter: Facette = this._searchResult.facetten
      .filter((value: Facette) => value.id === RegionenFacetteComponent.FILTER_ID)[0];

    let filterListNeu = [];
    if (filter) {
      let nothingSelected = filter.auswahl.every(item => !item.preset);
      filterListNeu = filter.auswahl.map(
        item => {
          let isSelected = item.preset;
          let plus = (!nothingSelected && !isSelected) ? "weitere " : "";
          // Texte fuer BF-Zugriff
          let label = `${item.label} (${item.trefferAnzahl} ${plus}Ergebnisse)`;
          if (1 === item.trefferAnzahl) {
            label = label
              .replace("weitere", "weiteres")
              .replace("Ergebnisse", "Ergebnis");
          }
          let isActive = true;
          this.logger.debug(label + ": " + isSelected);
          return new SelectItem(item.id, item, label, isSelected, isActive);
        }
      );
    }
    this.filterList = filterListNeu;
    this.hasInternational = 0 < this.filterList.filter(item => item.key.startsWith("i")).length;
    if ( !this.hasInternational ) {
      this.regioScope = "national";
    }

    this.updateResetButtonVisibility();

    this.logger.debug("Filter Definition parsed");
  }

  /**
   * Blendet die Filteroptionen ein bzw. aus
   */
  private toggleDisplay() {
    this.hidden = !this.hidden;
    // informiere andere Facetten, dass diese Facette geöffnet wurde
    if (!this.hidden) {
      this.openFacetteService.setFilterIdOpened(RegionenFacetteComponent.FILTER_ID);
    }
  }

  /**
   * Toggles the selection for a Bundesland OR country and restarts the search.
   *
   * @param {SelectItem} region The Bundesland OR country that is (de-)selected.
   */
  private toggleRegion(region: SelectItem | string) {
    let item: SelectItem;
    if (region instanceof SelectItem) {
      item = region;
    } else {
      item = this.getRegionByKey(region);
      if (null === item) {
        return;
      }
    }
    item.selected = !item.selected;
    this.focusAfterUpdate = item.key;
    this.toggleDisplay();
    this.updateView();
  }

  /**
   * Clear all selected Bundeslaender.
   */
  private resetFilters() {
    this.filterList.forEach(x => x.selected = false);
    this.toggleDisplay();
    this.updateView();
  }


  /**
   * Update the view aka redo the search with current filter options.
   */
  private updateView() {
    let selectedRegions = this.filterList
      .filter(x => x.selected)
      .map(x => x.key)
      .join(UrlParamService.VALUE_SEPARATOR);
    this.urlParamService.updateView({[UrlParamService.PARAM_REGION]: selectedRegions});
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
   * Get the CSS class names for the map group for the Bundesland.
   *
   * @param {string} state The ISO 2-letter name of the Bundesland OR "i"+international car sign.
   * @returns {string}
   */
  private getClassnameForRegion(region: string): string {
    let item = this.getRegionByKey(region);
    if (null === item) {
      return "map-group";
    } else {
      return "map-group"
        + (item.selected ? " selected" : "")
        + (item.active && !item.selected ? " enabled" : "")
        + (this.focusedRegion === item.key ? " checkbox-focus" : "");
    }
  }

  /**
   * Get either the 1st or 2nd line for region comments.
   *
   * The first line always returns the name of the Bundesland and the second a dynamic status.
   *
   * @param {string} region The ISO 2-letter name of the Bundesland OR the country.
   * @param {number} number The number for the comment (1 or 2).
   * @returns {string}
   */
  private getCommentLine(region: string, number: number): string {
    let result = "(keine Ergebnisse)";
    if (1 === number) {
      result = this.laenderLabels[region];
    } else {
      let matchList = this.filterList.filter(x => x.key === region);
      if (matchList.length > 0) {
        let item = matchList[0];
        let plus = "";
        if (!item.selected && this.filterList.filter(x => x.selected).length > 0) {
          plus = "+";
        }
        if (item.selected || item.active) {
          result = plus + item.value.trefferAnzahl
            + (item.value.trefferAnzahl === 1 ? " Ergebnis" : " Ergebnisse");
        }
      }
    }
    return result;
  }

  /**
   * Berechnet die Anzahl für alle in Deutschland vorhandenen
   * Studienangebote und erzeugt einen entsprechenden Infotext.
   *
   * @returns {string}
   */
  private getAnzahlDeutschland() {
    let anzahlGesamt = this.filterList.filter(item => item.key.charAt(0) !== "i").length;
    if (anzahlGesamt === 0) {
      return "(keine Ergebnisse)";
    }
    let anzahlAusgewaehlt = this.filterList.filter(item => item.selected && !item.key.startsWith("i"))
      .map(item => item.value.trefferAnzahl)
      .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    if (anzahlAusgewaehlt === 0) {
      return "";
    }
    if (anzahlAusgewaehlt === 1) {
      return "1 Ergebnis";
    }
    return anzahlAusgewaehlt + " Ergebnisse";
  }

  /**
   * Set the focus for the checkbox for the given Bundesland.
   *
   * Appends the Bundesland OR country to the end of the SVG in order to "raise" it for highlighting.
   *
   * @param {string} state The Bundesland OR country to be focused.
   */
  private focusCheckbox(region: string) {
    if (region.startsWith("i")) {
      if (typeof(this.svgEurope) !== "undefined") {
        this.svgEurope.nativeElement
          .appendChild(this.svgEurope.nativeElement.querySelector(`[id=country-${region.substr(1)}]`));
        this.regioScope = "international";
      }
    } else {
      if (typeof(this.svgEurope) !== "undefined") {
        this.svgGermany.nativeElement
          .appendChild(this.svgGermany.nativeElement.querySelector(`[id=state-${region}]`));
        this.regioScope = "national";
      }
    }
    this.focusedRegion = region;
  }

  /**
   * Blurs the checkbox for the given Bundesland or State
   * @param {string} region
   */
  private blurCheckbox(region: string) {
    this.focusedRegion = null;
  }

  /**
   * Get the region object from the ISO 2-letter name OR international car sign.
   *
   * @param {string} region The ISO 2-letter name of the Bundesland OR international car sign.
   * @returns {SelectItem}
   */
  private getRegionByKey(region: string): SelectItem | null {
    let matchList = this.filterList.filter(x => x.key === region);
    if (0 === matchList.length) {
      return null;
    }
    return matchList[0];
  }
}
