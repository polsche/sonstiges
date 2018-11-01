
import {Component, ContentChild, HostListener, Input, OnInit, TemplateRef} from "@angular/core";

@Component({
  selector: "ba-studisu-infoblock",
  templateUrl: "./infoblock.component.html"
})
export class InfoblockComponent implements OnInit {

  @Input()
  public id: String;

  @Input()
  public title: String;

  @Input()
  public icon: String;

  @Input()
  public cssClass: String;

  public mobile = false;

  @ContentChild(TemplateRef)
  private parentTemplate;

  public ngOnInit() {
    this.determineMode();
  }

  @HostListener("window:resize", ["$event"])
  private onResize(event) {
    this.determineMode();
  }

  private determineMode() {
    this.mobile = window.innerWidth < 768;
  }
}
