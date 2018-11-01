import {Messages} from "../model/Messages";
import {HinweisModalData} from "../modalhinweisdialog/hinweismodalData";
import {MessageService} from "../../services/message.service";

/**
 * handles info dialog functionality for facettenfilters that need it
 */
export class OsaInfoPopupDelegete {

  private info = new Map<string, HinweisModalData>();

  /**
   * C-tor
   * @param {MessageService} messageService
   * @param {Map<string, HinweisModalData>} info with keys being ids of option that has an info popup, values
   * are modal data to show
   */
  constructor(public messageService: MessageService) {
    this.info.set("2", new HinweisModalData(
      Messages.POPUP_UEBERSCHRIFT_OSA,
      "",
      Messages.POPUP_INFO_OSA
    ));
    this.info.set("1", new HinweisModalData(
      Messages.POPUP_UEBERSCHRIFT_STUDICHECK,
      "",
      Messages.POPUP_INFO_STUDICHECK
    ));
  }

  /**
   * do we have info for option key?
   * @param {string} key
   * @returns {boolean}
   */
  public hasInfoFor(key: string): boolean {
    return this.info.has(key);
  }

  /**
   *
   * Service-Call um Info-Dialog anzuzeigen
   */
  public showInfoFor(key: string): void {
    if (this.hasInfoFor(key)) {
      this.messageService.showHinweisDialog.next(this.info.get(key));
    }
  }
}
