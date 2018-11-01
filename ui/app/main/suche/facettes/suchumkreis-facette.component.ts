import {Component, OnInit, OnDestroy, Input, ChangeDetectionStrategy} from "@angular/core";
import { UrlParamService } from "../../../main/suche/services/url-param.service";
import { Subscription } from "rxjs/Subscription";
import { SearchResult } from "../../../model/SearchResult";
import { OpenFacetteService } from "../services/open-facette.service";
import { Messages } from "../../../ui-components/model/Messages";


@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  selector: "ba-studisu-umkreis-facette",
  templateUrl: "./suchumkreis-facette.component.html"
})

export class SuchumkreisFacetteComponent implements OnInit, OnDestroy {

  private static FILTER_ID = UrlParamService.PARAM_UK;

  @Input()
  public step = 50;

  //  Parameters can be overriden by parent component
  @Input()
  public min = 50;
  @Input()
  public max = 250;
  @Input()
  public showMobile: boolean;
  public suchumkreis: number;

  // Gibt an, ob diese Komponente (=der Slider) aktiviert oder deaktivert ist.
  // Wird über Subscription an den Params geändert; wenn Ort-Param vorhanden ist, ist der Slider aktiviert.
  // Auswertung von disabled und eigentliche Deaktivierung/Aktivierung erfolgt in der component.html
  public disabled = true;

  @Input()
  private searchResult: SearchResult;
  @Input()
  private icon: string;

  private hidden = true;
  private onParameterChangeSubscription: Subscription;
  private onOrtParameterChangeSubscription: Subscription;
  //  Subscription for the open facette event
  private openFacetteSubscription: Subscription;

  /**
   * constructor
   * @param urlParamService (Injected) UrlParamService for triggering url updates
   * @param openFacetteService (Injected) OpenFacetteService for triggering facette openings
   */
  constructor(private urlParamService: UrlParamService,
    private openFacetteService: OpenFacetteService) {
  }

  /**
   * Initialization of this facette. Registers callback on url parameter change
   * which initializes default values. Subscribes on open facette event.
   */
  public ngOnInit() {
    this.onParameterChangeSubscription = this.urlParamService.currentParams
      .map(params => params.get(UrlParamService.PARAM_UK)) // jetzt haben wir nur noch unseren Param
      .distinctUntilChanged() // jetzt verarbeiten wir in nur, wenn er sich geändert hat
      .subscribe(uk => this.parseParameters(uk));

    /**
     * Hört auf Änderungen am Orts-Parameter. Falls der Parameter nicht gesetzt ist, bleibt this.disbaled auf true;
     * diese Variable wird vom getter isSliderDisabled zurück geliefert und der suchumkreis-facette.component übergeben.
     * Ist der Ort nicht gesetzt, wird die Umkreis-Komponente deaktiviert,
     * ist disabled false (=Ort gesetzt), ist die Komponente aktiviert
     * und kann vom Benutzer verändert werden.
     * @type {Subscription}
     */
    this.onOrtParameterChangeSubscription = this.urlParamService.currentParams
      .map(params => params.get(UrlParamService.PARAM_ORTE)) // jetzt haben wir nur noch unseren Param
      .distinctUntilChanged() // jetzt verarbeiten wir in nur, wenn er sich geändert hat
      .subscribe(ort => {
        this.disabled = ort == null;
      });

    this.openFacetteSubscription = this.openFacetteService.getFilterIdOpened().subscribe(filterIdOpened => {
      if (SuchumkreisFacetteComponent.FILTER_ID !== filterIdOpened && !this.hidden) {
        this.hidden = true;
      }
    });
  }

  /**
   * OnDestroy Hook to unsubscribe made Subscriptions of this component
   */
  public ngOnDestroy() {
    this.onParameterChangeSubscription.unsubscribe();
    this.onOrtParameterChangeSubscription.unsubscribe();
    if (this.openFacetteSubscription != null) {
      this.openFacetteSubscription.unsubscribe();
    }
  }

  /**
   * Liefert den Text für den Suchumkreis.
   */
  public getCurrentSelection() {
    return this.suchumkreis >= this.max ? Messages.URLPARAM_UK_VALUE_BUNDESWEIT : (this.suchumkreis + " km");
  }

  /**
   * Liefert das Array mit allen Auswahl-Optionen auf Basis der übergebenen Parameter.
   */
  public getRange() {
    let range = [];
    for (let value = this.min; value <= this.max; value += this.step) {
      if (value + this.step > this.max) {
        range.push({ "value": this.max, "label": "Bundesweit" });
      } else {
        range.push({ "value": value, "label": "" + value + " km" });
      }
    }
    return range;
  }

  /**
   * Listener which is called when the slider value changes.
   * is called from HTML template,
   * tslint:disable + review CKU+CSP
   *
   * @param value current value of the slider component
   */
  public onChange(value) {
    // update view because URL param does not contain selected value yet
    this.toggleDisplay();
    this.updateSuchumkreisWithValue(+value >= this.max ? Messages.URLPARAM_UK_VALUE_BUNDESWEIT : value.toString(), true);
  }

  /**
   * Parser Function for URL Parameters. Will be called on url parameter change
   * @param params query params object
   */
  private parseParameters(uk: any) {
    // do not update view because URL param already contains selected value
    this.updateSuchumkreisWithValue(uk, false);
  }

  /**
   * Blendet die Filteroptionen ein bzw. aus
   */
  private toggleDisplay() {
    this.hidden = !this.hidden;
    // informiere andere Facetten, dass diese Facette geöffnet wurde
    if (!this.hidden) {
      this.openFacetteService.setFilterIdOpened(SuchumkreisFacetteComponent.FILTER_ID);
    }
  }

  /**
   * Updates current selected value, registers it on the UrlParamService.
   *
   * WARNING writes history stacks, dont call me too often
   *
   * @param value new Value for the slider
   * @param updateView if the view has to be updated
   */
  private updateSuchumkreisWithValue(value: string, updateView: boolean) {
    if (value === Messages.URLPARAM_UK_VALUE_BUNDESWEIT) {
      this.suchumkreis = this.max;
    } else if (isNaN(parseInt(value, 10))) {
      this.suchumkreis = 50;
    } else {
      this.suchumkreis = value ? Math.max(Math.min(+value, this.max), this.min) : 50;
    }
    if (updateView) {
      this.urlParamService.updateView({ [UrlParamService.PARAM_UK]: value, [UrlParamService.PARAM_PAGE]: null });
      if (this.showMobile) {
        this.hidden = true; // Schließe die Facette in der Mobilansicht automatisch nach Auswahl.
      }
    }
  }
}
