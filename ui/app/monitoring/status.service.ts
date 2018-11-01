import { Injectable } from "@angular/core";
import { getStudisuConfig, StudisuConfig } from "../config/studisu.config";
import { StudisuHttpService } from "../services/studisu-http.service";

@Injectable()
export class StatusService {

  private conf: StudisuConfig = getStudisuConfig();

  constructor(private oagHttp: StudisuHttpService) {
  }

  /**
   * check if rest backend is running
   */
  public isBackendUp(component): void {
    // vereinfachter get aufruf - wenn oag nicht erreichbar, dann auch unser service nicht
    // requests Versionsinformation from backend
    this.oagHttp.get(this.conf.studienangeboteServiceHost + this.conf.versionInfoPath).subscribe(
      data => {
        component.setBackendUp(true);
        if (data && data.json()) {
          component.setBackendDatenstand(data.json().datenstand);
          component.setBackendVersion(data.json().version);
        }
      },
      err => component.setBackendUp(false)
    );
  }

  /**
   * check if rest backend is healthy
   */
  public isBackendHealthy(component): void {
    // vereinfachter get aufruf - wenn oag nicht erreichbar, dann auch unser service nicht
    // monitoring controller from backend
    // outcome depends on availability of necessary consumed (soa) services

    this.oagHttp.get(this.conf.studienangeboteServiceHost + this.conf.monitoringPath).subscribe(
      data => component.setBackendHealthy(true),
      err => component.setBackendHealthy(false)
    );
  }
}
