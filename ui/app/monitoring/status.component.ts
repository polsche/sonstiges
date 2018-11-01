import {Component, OnInit} from "@angular/core";
import {StudisuConfig, getStudisuConfig} from "../config/studisu.config";
import {StatusService} from "./status.service";

@Component({
  selector : "ba-status",
  templateUrl: "./status.component.html"
})
export class StatusComponent implements OnInit {
  public conf: StudisuConfig = getStudisuConfig();
  public backendUp: boolean;
  public backendHealthy: boolean;
  public backendDatenstand: string;
  public backendVersion: string;

  constructor(private statusService: StatusService) {
  }

  public ngOnInit() {
    this.statusService.isBackendUp(this);
    this.statusService.isBackendHealthy(this);
  }

  public setBackendUp(status) {
    this.backendUp = status;
  }

  public setBackendHealthy(status) {
    this.backendHealthy = status;
  }

  public setBackendDatenstand(datenstand) {
    this.backendDatenstand = datenstand;
  }

  public setBackendVersion(version) {
    this.backendVersion = version;
  }
}
