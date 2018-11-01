import { UrlParamService } from "../../../suche/services/url-param.service";
import { ISelectItem } from "../../../suche/services/model/ISelectItem";
import { Studienfeld } from "./Studienfeld";
import { Messages } from "../../../../ui-components/model/Messages";

/**
 * Representation eines Studienbereichs (== Obergruppe von Studienfeldern).
 */
export class Studienfeldgruppe implements ISelectItem {
  public icon: string;

  constructor(public key?: string,
               public name?: string,
               public dkzIds?: number[],
               public studienfelder?: Studienfeld[],
               icon?: string) { }

  get label(): string {
    return this.name;
  }
  get value(): string {
    return this.dkzIds.join(UrlParamService.VALUE_SEPARATOR);
  }
  get clickable(): boolean {
    return true;
  }
  get tooltip(): string {
    return  Messages.TAG_STUDIENFELD_TOOLTIP_CLICK;
  }
}
