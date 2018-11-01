import { OagConfigService, OagHttpConfig } from "bapf-oag";
import { Observable } from "rxjs/Observable";
import { Injectable } from "@angular/core";
import { Observer } from "rxjs/Observer";
import { StudisuConfig, getStudisuConfig } from "../config/studisu.config";

declare global {
  interface Window {
    config: any;
  }
}

/**
 * Implementation of OagConfigService which returns
 * a concrete config
 */
@Injectable()
export class OagConfigServiceImpl implements OagConfigService {
  private obsCfg: Observable<OagHttpConfig>;

  private oagHost = getStudisuConfig().oagHost;

  /**
   * Service method implementation returns and observable httpConfig
   * @returns {Observable<OagHttpConfig>}
   */
  public getOagConfig(): Observable<OagHttpConfig> {
    if (!this.obsCfg) {
      if (this.oagHost) {
        // the config has to have oagHost set, to get OagConfigService working
        this.obsCfg = Observable.create((obs: Observer<OagHttpConfig>) => {
          const oagConfig: OagHttpConfig = {
            anwendungsId: "",
            clientId: "5aee2cfe-1709-48a9-951d-eb48f8f73a74",
            clientSecret: "3309a57a-9214-40db-9abe-28b1bb30c08c",
            defaultHeaders: [],
            oagUrl: this.oagHost + "/oauth/gettoken_cc",
            picturePath: [],
            sendCredentials: true,
            verfahrendId: ""
          };
          obs.next(oagConfig);
          obs.complete();
        });
      } else {
        // if config is missing oagHost we throw an error to support development/analysis
        throw Error("ERROR in CONFIG: oagHost undefined !!!");
      }
    }
    return this.obsCfg;
  }
}
