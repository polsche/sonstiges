import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {SearchResult} from "../../../model/SearchResult";
import {UrlParamService} from "../../../main/suche/services/url-param.service";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "ba-studisu-reset-all-facettes",
  templateUrl: "./reset-all-facettes.component.html"
})
export class ResetAllFacettesComponent {

  @Input()
  private searchResult: SearchResult;

  constructor(private _urlService: UrlParamService) {
  }


  /**
   * Liefert die Anzahl der Suchergebnisse, die durch das Löschen aller Filter hinzukämen.
   *
   * @returns {number}
   */
  public getAdditionalResultNum(): number {
    if (null !== this.searchResult && this.searchResult.filteredOutErgebnisse > 0) {
      return this.searchResult.filteredOutErgebnisse;
    } else {
      return 0;
    }
  }

  /**
   * Löscht alle gesetzten Filterfacetten und lädt die Seite neu.
   */
  private resetFilters() {
    this._urlService.resetFilter();
    this._urlService.updateView();
  }

}
