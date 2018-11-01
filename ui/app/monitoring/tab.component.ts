import {Component, Input} from "@angular/core";
import { TabsComponent } from "./tabs.component";

@Component({
  selector: "ba-tab",
  template: `
    <div [hidden]='!tabActive'>
      <ng-content></ng-content>
    </div>
  `
})
export class TabComponent {
  public tabActive: boolean;

  @Input()
  private tabTitle;

  constructor(tabs: TabsComponent) {
    tabs.addTab(this);
  }
}
