import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {SearchResult} from "../../../model/SearchResult";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "ba-studisu-suche-result",
  templateUrl: "./suche-result.component.html"
})
export class SucheResultComponent {

  @Input()
  public searchResult: SearchResult;
}
