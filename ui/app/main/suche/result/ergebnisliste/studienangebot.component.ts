import {Component, Input, ElementRef, ViewChild, AfterViewInit, ChangeDetectionStrategy} from "@angular/core";
import {Studienangebot} from "./../../services/model/Studienangebot";
import {UrlParamService} from "./../../services/url-param.service";
import {ZuletztAngesehenListe} from "../../../../services/zuletztangesehen.service";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "ba-studisu-studienangebot",
  templateUrl: "./studienangebot.component.html"
})

export class StudienangebotComponent implements AfterViewInit {

  @Input()
  public studienangebot: Studienangebot;

  @ViewChild("studiDetails")
  private el: ElementRef;

  constructor(private urlService: UrlParamService,
              private zuletztAngesehenListe: ZuletztAngesehenListe) {
  }

  public getStudienangebotQueryParams(): {} {
    return this.urlService.getQueryParams();
  }

  /**
   * Bei Ruecksprung von Detailseite soll das zuvor angewaehlte Angebot in Liste
   * fokussiert werden bzw. dahin gescrollt
   * STUDISU-95
   */
  public ngAfterViewInit() {
    if (this.isGeradeAngesehen()) {
      // focus first
      this.el.nativeElement.focus();
      // then adjust scrolling if needed
      this.scrollToMiddle();
      // now reset gerade angesehen state in list
      // damit bei weitere suche der Fokus nicht bleibt
      this.zuletztAngesehenListe.clearGeradeAngesehen();
    }
  }

  /**
   * In last seen list?
   * @returns {boolean}
   */
  public isInZuletztAngesehenListe(): boolean {
    return this.zuletztAngesehenListe.contains(this.studienangebot.id);
  }

  /**
   * Details gerade angeschaut?
   * @type {boolean}
   */
  public isGeradeAngesehen(): boolean {
    return this.zuletztAngesehenListe.isGeradeAngesehen(this.studienangebot.id);
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks Studienort gesetzt ist;
   * ngIf rendert dann den gesamten Block nicht.
   */
  public isStudienortGesetzt(): boolean {
    return this.studienangebot != null
      && this.studienangebot.studienort != null
      && (
        this.studienangebot.studienort.strasse != null
        || this.studienangebot.studienort.postleitzahl != null
        || this.studienangebot.studienort.ort != null
        || this.studienangebot.studienort.bundesland != null
      );
  }

  /**
   * scroll to middle fix for ie and firefox.
   * After focus, chrome will scroll to middle perfectly, so the code will jump over the "if" below and return.
   * However ie and firefox, element will be scrolled "oberkante element to unterkante window"
   * In this case, the element will be below "middle" (top>middle) and we correct by scrolling
   * difference to middle.
   */
  private scrollToMiddle() {
    let viewportOffset = this.el.nativeElement.getBoundingClientRect();
    // these are relative to the viewport, i.e. the window
    let top = viewportOffset.top;
    let middle = ((window.innerHeight) / 2);
    let height = this.el.nativeElement.clientHeight;
    let offset = 0;
    if (top > middle) {
      offset = (top - middle) + (height / 2);
    } else if (middle > top) {
      offset = (middle - top) + (height / 2);
    }
    window.scrollBy(0, offset);
  }
}
