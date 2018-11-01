import {Component, Input, SecurityContext} from "@angular/core";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: "ba-studisu-header",
  templateUrl: "./studisu-header.component.html"
})
export class StudisuHeaderComponent {

  @Input()
  public isLight = true;

  @Input()
  public isLarge = false;

  @Input()
  public bgImgName;

  constructor(private _sanitizer: DomSanitizer) { }

  public getBgImage() {
    if (this.bgImgName != null && this.bgImgName !== "") {
      return this._sanitizer.sanitize(SecurityContext.STYLE, `url(${this.bgImgName})`);
    } else {
      return "";
    }
  }

}
