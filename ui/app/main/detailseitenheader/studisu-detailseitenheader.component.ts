import {Component, Input} from "@angular/core";

@Component({
  selector: "ba-studisu-detailseitenheader",
  templateUrl: "./studisu-detailseitenheader.component.html"
})
export class StudisuDetailseitenheaderComponent {

  @Input()
  public isLight = true;

  @Input()
  public isLarge = false;
}
