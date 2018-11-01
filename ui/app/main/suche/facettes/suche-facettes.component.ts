import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {SearchResult} from "../../../model/SearchResult";
import {UrlParamService} from "../services/url-param.service";
import {OsaInfoPopupDelegete} from "../../../ui-components/delegetes/osa-info-popup.delegete";
import {MessageService} from "../../../services/message.service";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "ba-studisu-suche-facettes",
  templateUrl: "./suche-facettes.component.html"
})
export class SucheFacettesComponent {

  @Input()
  public searchResult: SearchResult;

  // anbieten der constanten fuer das html template dieser component
  protected paramHochschulart = UrlParamService.PARAM_HOCHSCHULART;
  protected paramStudienform = UrlParamService.PARAM_STUDIENFORMEN;
  protected paramStudientyp = UrlParamService.PARAM_STUDT;
  protected paramFFStudium = UrlParamService.PARAM_FFSTUDIUM;

  constructor(public messageService: MessageService) {
  }

  /**
   * lazy accessor for a info popup delegate for fit fuer studium
   * @returns {OsaInfoPopupDelegete}
   */
  get fitFurStudiumInfoPopupDelegate(): OsaInfoPopupDelegete {
    return new OsaInfoPopupDelegete(this.messageService);
  }
}
