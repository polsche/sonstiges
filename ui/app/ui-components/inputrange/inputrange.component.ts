import {Component, Input, Output, EventEmitter} from "@angular/core";
import {UrlParamService} from "../../main/suche/services/url-param.service";

@Component({
  selector: "ba-studisu-inputrange",
  templateUrl: "./inputrange.component.html"
})
export class InputrangeComponent {

  @Input()
  public name: string;
  @Input()
  public id: string;
  @Input()
  public min = 1;
  @Input()
  public max = 100;
  @Input()
  public step = 1;
  @Input()
  public value: number;
  @Input()
  public disabled = false;
  @Input()
  public ariaValueNow = "";
  @Input()
  public ariaValueText = "";

  @Output()
  public onchange: EventEmitter<number> = new EventEmitter<number>();

  constructor(private urlParaService: UrlParamService) {
  }

  public onChange(value) {
    this.value = value;
    this.onchange.emit(this.value);
  }
}
