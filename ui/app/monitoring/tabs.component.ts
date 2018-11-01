import { Component } from "@angular/core";
import { TabComponent } from "./tab.component";

@Component({
  selector: "ba-tabs",
  template: `
    <ul class="nav nav-tabs">
      <li *ngFor="let tab of tabs" (click)="selectTab(tab)" [ngClass]="{'active': tab.tabActive }">
        <a>{{tab.tabTitle}}</a>
      </li>
    </ul>
    <ng-content></ng-content>
  `
})
export class TabsComponent {
  public tabs: TabComponent[] = [];

  public addTab(tab: TabComponent) {
    if (this.tabs.length === 0) {
      tab.tabActive = true;
    }
    this.tabs.push(tab);
  }

  private selectTab(tab: TabComponent) {
    this.tabs.forEach( (tab2) => {
      tab2.tabActive = false;
    });
    tab.tabActive = true;
  }
}
