import { UrlParamService } from "../../../suche/services/url-param.service";
import { ISelectItem } from "../../../suche/services/model/ISelectItem";
import { Messages } from "../../../../ui-components/model/Messages";

const TOKEN_NAME = "%name%";

/**
 * Representation eines Studienfeldes.
 */
export class Studienfeld implements ISelectItem {

  private _tooltip: string;

  constructor(public key?: string,
              public name?: string,
              public dkzIds?: number[]) {

    let tooltip = Messages.TAG_STUDIENFELD_TOOLTIP_CLICK;
    if (tooltip && tooltip.indexOf(TOKEN_NAME) >= 0) {
      this._tooltip = tooltip.replace(TOKEN_NAME, this.name);
    }
  }

  get label(): string {
    return this.name;
  }
  get value(): string {
    return this.dkzIds.join(UrlParamService.VALUE_SEPARATOR);
  }
  get icon(): string {
    return "education-provider";
  }
  get clickable(): boolean {
    return true;
  }
  get tooltip(): string {
    return this._tooltip;
  }
}
