import { Component, OnInit } from "@angular/core";
import { VersionInfo } from "./services/model/VersionInfo";
import { VersionInfoService } from "./services/versioninfo.service";
import { LoggingService } from "../services/logging.service";

@Component({
  selector: "ba-studisu-versioninfo",
  templateUrl: "./versioninfo.component.html"
})
export class VersionInfoComponent implements OnInit {
  public versionInfo: VersionInfo;

  /**
   * Constructor
   * @param versionInfoService Injected Dependency
   * @param logger Injected Dependency
   */
  constructor(
    public versionInfoService: VersionInfoService,
    private logger: LoggingService
  ) { }

  /**
   * Initialisierung der Komponente
   * Hier werden Version und Datenstand aus dem Backend geholt.
   */
  public ngOnInit() {
    this.versionInfoService.getVersion()
      .subscribe(versionInfo => {
          this.versionInfo = versionInfo;
        },
        err => {
          this.logger.error("Service Request Failed with error: " + err, this);
        }
      );
  }
}
