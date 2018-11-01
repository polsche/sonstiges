import { Component } from "@angular/core";
import { StudisuConfig, getStudisuConfig } from "../config/studisu.config";

@Component({
  selector: "ba-footer-fallback",
  templateUrl: "./footer-fallback.component.html"
})

export class FooterFallbackComponent {
  public conf: StudisuConfig = getStudisuConfig();
}
