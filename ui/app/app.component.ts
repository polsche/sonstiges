import "./rx-imports";
import { Component, ViewEncapsulation } from "@angular/core";
import { HistoryService } from "./ui-components/back/history.service";

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: "ba-studisu-app",
  styleUrls: [
    "./app.component.scss"
  ],
  templateUrl: "./app.component.html"
})
export class AppComponent {

  constructor(private historyService: HistoryService) {
    // Der HistoryService muss hier sein, damit er gleich zu Beginn der Anwendung
    // initalisiert wird. Nur so kann er von Beginn an die aufgerufenen URLs tracken.
  }
}
