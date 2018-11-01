import { ISelectItem } from "./ISelectItem";
import { Messages } from "../../../../ui-components/model/Messages";

const TOKEN_NAME = "%name%";

/**
 * Representation eines Studienfach.
 */
export class Studienfach implements ISelectItem {

    private _tooltip: string;
  constructor(private _key?: string,
    public name?: string,
    public dkzId?: number) {
      let tooltip = Messages.TAG_STUDIENFACH_TOOLTIP_CLICK;
      if (tooltip && tooltip.indexOf(TOKEN_NAME) >= 0) {
        this._tooltip = tooltip.replace(TOKEN_NAME, this.name);
      }
    }

  get key(): string {
    if (typeof this._key === "undefined") {
      this._key = "" + this.dkzId;
    }
    return this._key;
  }
  get label(): string {
    return this.name;
  }
  get value(): string {
    return "" + this.dkzId;
  }
  get icon(): string {
    return "learning";
  }
  get clickable(): boolean {
    return true;
  }
  get tooltip(): string {
    return this._tooltip;
  }
}
