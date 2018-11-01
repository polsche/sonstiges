import {Component, Input, Output, EventEmitter} from "@angular/core";

@Component({
  selector: "studisu-checkbox",
  templateUrl: "./checkbox.component.html",
})
export class CheckboxComponent {
  //    Property wird ueber getter setter verdeckt (setter zum emitieren von value change)
  //    der input rename ist hier gewünscht => checked und checkedChange sind wichtig für
  //    TwoWay Binding Funktionalitaet auf diese Property
  // tslint:disable-next-line:no-input-rename
  @Input("checked")
  public _checked = false;
  //    ermoeglicht TwoWayBinding auf model Property
  @Output()
  public checkedChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  @Input()
  public style = "";
  @Input()
  public class = "";
  @Input()
  public id = "cb";
  @Input()
  public title = "";
  @Input()
  public label = "";
  @Input()
  public arialabel = "";
  @Input()
  public disabled = false;

  public set checked(value: boolean) {
    if (value !== this._checked) {
      this._checked = value;
      this.checkedChange.emit(this._checked);
    }
  }

  public get checked(): boolean {
    return this._checked;
  }
}
