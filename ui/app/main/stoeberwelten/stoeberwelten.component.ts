import { Component, SecurityContext } from "@angular/core";
import { UrlParamService } from "../../main/suche/services/url-param.service";
import { Params } from "@angular/router";

@Component({
  selector: "ba-studisu-stoeberwelten",
  templateUrl: "./stoeberwelten.component.html"
})
export class StoeberweltenComponent {

  /**
   * Der Konstruktor benötigt den URL Parameter-Service, da dieser im Template verwendet wird!
   *
   * @param urlParamService
   */
  constructor(private urlParamService: UrlParamService) {
  }

  /**
   * liefert die aktuellen queryParams
   */
  public getQueryParams(): {} {
    return this.urlParamService.getQueryParams();
  }

  /**
   * liefert die aktuellen queryParams
   * erweitert um die studienfelder fuer lehramt
   */
  public getLehramtQueryParams(): {} {
    let sfe = UrlParamService.PARAM_STUDIENFELDER;
    let lehramtParams: Params = { sfe : "94333;94334" };
    let params: Params = this.urlParamService.getQueryParams(lehramtParams);
    return params;
  }
}
