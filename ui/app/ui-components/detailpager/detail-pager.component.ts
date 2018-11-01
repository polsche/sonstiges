import {
  Component, Input, Output, EventEmitter, AfterViewChecked, ViewChild, ElementRef,
  HostListener
} from "@angular/core";
import { EventManager } from "@angular/platform-browser";

@Component({
  selector: "ba-studisu-detail-pager",
  templateUrl: "./detail-pager.component.html"
})
export class DetailPagerComponent implements AfterViewChecked {

  @Input()
  public numPrev = 0;

  @Input()
  public numNext = 0;

  @Input()
  public studiangebotTitle: string;

  @Output()
  public backClicked: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  public prevClicked: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  public nextClicked: EventEmitter<any> = new EventEmitter<any>();

  @ViewChild("affixMe") private affixMe: ElementRef;
  @ViewChild("navID") private navID: ElementRef;
  @ViewChild("studienangebotPagerTitle") private studienangebotPagerTitle: ElementRef;

  private offsetFromTop = 0;

  private initialOffsetFromTop = -1;

  constructor(eventManager: EventManager) {
    eventManager.addGlobalEventListener("window", "resize",
      e => this.calculateHeight());
  }

  public ngAfterViewChecked() {
    this.calculateHeight();
    if (this.initialOffsetFromTop < this.offsetFromTop) {
      this.initialOffsetFromTop = this.offsetFromTop;
    }
  }

  public onbuttonclicked(emitter) {
    emitter.emit();
  }

  /**
   * Über den Decorator HostListener wird ein Listener auf
   * das Scroll-Event erzeugt. Falls die vorgegebene Scrollhöhe
   * erreicht wird, wird der Scroll-Up-Button eingeblendet.
   */
  @HostListener("window:scroll", ["$event"])
  private onScroll(): void {
    let h2Element = document.getElementById("studienangebotPagerTitle");
    if (window.pageYOffset > this.offsetFromTop) {
      // Block mit Navigation + Überschrift fixieren
      this.affixMe.nativeElement.className = "affix";
      // Ausgleichenden Block gleich Höhe erzeugen
      // Schriftgröße reduzieren anhand der Scrollposition bis zum Minimum von 1.1rem.
      let fontOffset = window.pageYOffset - this.initialOffsetFromTop;
      if (h2Element.offsetHeight > 55 || h2Element.style.fontSize !== "") {
        let fontSize = 1.775 - Math.max(0, 0.675 * Math.min(1, fontOffset / 130));
        h2Element.style.fontSize = fontSize + "rem";
      }
    } else {
      // Alles zurücksetzen.
      this.affixMe.nativeElement.className = "unfix";
      h2Element.style.fontSize = "";
    }
  }

  /**
   * Höhe so berechnen, als ob kein "Affix" vorhanden wäre.
   */
  private calculateHeight() {
    let className = this.affixMe.nativeElement.className;
    this.affixMe.nativeElement.className = "";
    this.offsetFromTop = this.navID.nativeElement.offsetTop;
    this.affixMe.nativeElement.className = className;
  }
}
