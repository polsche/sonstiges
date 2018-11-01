import { Component } from "@angular/core";
import { StudisuConfig, getStudisuConfig } from "../config/studisu.config";

@Component({
  selector: "ba-header-fallback",
  templateUrl: "./header-fallback.component.html"
})

export class HeaderFallbackComponent {

  public conf: StudisuConfig = getStudisuConfig();

}
