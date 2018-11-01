import {ChangeDetectionStrategy, Component} from "@angular/core";
import {Messages} from "../../../ui-components/model/Messages";

@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  selector: "ba-studisu-suche-form",
  templateUrl: "./suche-form.component.html"
})
export class SucheFormComponent {
  public submittedCounter = 0;
  public label = Messages;

  /**
   * Klick auf den SUCHEN-Button erhöht den Zähler und löst damit Auflösung des Eingabefeldes aus.
   */
  public onButtonClick() {
    this.submittedCounter++;
  }
}
