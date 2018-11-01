import { isObject } from "util";
import { environment } from "../../environments/environment";
import {NavtabbarItem} from "../ui-components/navtabbar/navtabbar-item";

// Can use jQuery here
declare var $: any;

export class StudisuConfig {

  public oagHost: string;
  public baseUrlHeaderFooter: string;
  public headerRestPath: string;
  public footerRestPath: string;
  public jsRestPath: string;
  public baseUrlContent: string;
  public studienangeboteServiceHost: string;
  public studienfaecherServicePath: string;
  public studienfelderServicePath: string;
  public studienfelderByStudienfachServicePath: string;
  public studienfeldBeschreibung: string;
  public studienfeldInformationen: string;
  public studienangebotInformationen: string;
  public bildungsanbieterSignet: string;
  public studienangeboteServicePath: string;
  public ortServicePath: string;
  public versionInfoPath: string;
  public monitoringPath: string;
  public navtabbarInformationFooter: NavtabbarItem[];
  public cacheBust: string;
  public baseUrlMetasuche: string;
  public urlKontaktTechFehlerMelden: string;

  constructor(data) {
    Object.assign(this, data);
  }

  public keys(): Array<string> {
    return Object.keys(this);
  }
}

export function _getStudisuConfig(studisuConfig: any): StudisuConfig {
  let config = new StudisuConfig({});

  if (!isObject(studisuConfig)) {
    return new StudisuConfig(config);
  }
  for (let i in studisuConfig) {
    if (!studisuConfig.hasOwnProperty(i)) {
      continue;
    }

    let entry = studisuConfig[i];
    if (!(/^{{.*}}$/).test(entry)) {
      config[i] = entry;
      // handy for debugging, pls dont delete me :)
      // console.log("config[" + i + "] = " + entry);
    }
  }

  // dynamically substitute cacheBust read from config
  // this had be done before with webpack plugins on buildtime
  // now we go with jQuery at runtime to read the filehash of our main.js
  // which is set by angular-cli at buildtime
  // (at least for production builds, where output hashing is active)
  // therefore get script-tag from DOM whose src contains main
  let src: String;
  src = $("script[src*='main']")[0].src;
  if (src) {
    // search for a hash like main.HASH.js
    let searchStrMain = "/main.";
    let main = src.lastIndexOf(searchStrMain);
    let mainEnd = main + searchStrMain.length;
    let js = src.lastIndexOf(".js");
    if (main && js && mainEnd < js) {
      let hash = src.substring(mainEnd, js);
      if (hash) {
        config.cacheBust = hash;
        // handy for debugging, pls dont delete me :)
        // console.log("hash set as cacheBust: " + config.cacheBust);
      }
    }
  }

  return new StudisuConfig(config);
}

export function getStudisuConfig(): StudisuConfig {
  return _getStudisuConfig(environment.config || null);
}
