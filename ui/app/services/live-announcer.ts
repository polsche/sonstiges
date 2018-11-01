/**
 * @license MIT https://github.com/angular/material2/blob/9954ca86ecac9f811a7c8aeb7b19e61fa89828eb/LICENSE
 * @see https://github.com/angular/material2/blob/9954ca86ecac9f811a7c8aeb7b19e61fa89828eb/src/lib/core/a11y/live-announcer.ts
 * @copyright Copyright (c) 2016 Google, Inc.
 */

import {
  Injectable,
  Optional,
  Inject, InjectionToken
} from "@angular/core";

export const LIVE_ANNOUNCER_ELEMENT_TOKEN  = new InjectionToken("liveAnnouncerElement");

export type AriaLivePoliteness = "off" | "polite" | "assertive";

@Injectable()
export class LiveAnnouncer {

  private _liveElement: Element;

  constructor(@Optional() @Inject(LIVE_ANNOUNCER_ELEMENT_TOKEN) elementToken: any) {

    // We inject the live element as `any` because the constructor signature cannot reference
    // browser globals (HTMLElement) on non-browser environments, since having a class decorator
    // causes TypeScript to preserve the constructor signature types.
    this._liveElement = elementToken || this._createLiveElement();
  }

  /**
   * @param message Message to be announced to the screenreader
   * @param politeness The politeness of the announcer element.
   */
  public announce(message: string, politeness: AriaLivePoliteness = "assertive"): void {
    this._liveElement.textContent = "";

    // TODO: ensure changing the politeness works on all environments we support.
    if (this._liveElement && this._liveElement.setAttribute) {
      this._liveElement.setAttribute("aria-live", politeness);
    }

    // This 100ms timeout is necessary for some browser + screen-reader combinations:
    // - Both JAWS and NVDA over IE11 will not announce anything without a non-zero timeout.
    // - With Chrome and IE11 with NVDA or JAWS, a repeated (identical) message won't be read a
    //   second time without clearing and then using a non-zero delay.
    // (using JAWS 17 at time of this writing).
    setTimeout(() => this._liveElement.textContent = message, 100);
  }

  private _createLiveElement(): Element {
    let liveEl = document.createElement("div");

    liveEl.classList.add("sr-only");
    liveEl.setAttribute("aria-atomic", "true");
    liveEl.setAttribute("aria-live", "assertive");
    liveEl.setAttribute("aria-relevant", "additions removals");

    document.body.appendChild(liveEl);

    return liveEl;
  }

}
