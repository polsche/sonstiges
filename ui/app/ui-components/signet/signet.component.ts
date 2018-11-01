import {Component, Input, ViewChild} from "@angular/core";
import {StudisuConfig, getStudisuConfig} from "../../config/studisu.config";
import {StudisuHttpService} from "../../services/studisu-http.service";

@Component({
  selector: "studisu-signet",
  templateUrl: "./signet.component.html",
})
export class SignetComponent {
  @Input()
  public hasSignet = false;

  @Input()
  public set banid(banid: Number) {
    this.loaded = false;
    this.imgBlock.nativeElement.style.display = "none";
    this._banid = banid;
  }

  @ViewChild("imgBlock")
  private imgBlock;

  private _banid: Number;
  private conf: StudisuConfig = getStudisuConfig();
  private serverURL: string;
  private loaded = false;

  constructor(private studisuHttp: StudisuHttpService) {
    this.serverURL = this.conf.studienangeboteServiceHost + this.conf.bildungsanbieterSignet;
  }

  /**
   * Event-Handler, der aufgerufen wird, wenn die Bilddaten geladen wurden.
   *
   * Dann wird der Platzhalter durch das geladene Bild ersetzt...
   */
  public loadingComplete(): void {
    this.loaded = true;
    this.imgBlock.nativeElement.style.display = "block";
  }

  /**
   * Liefert das Flag, ob das Bild bereits geladen wurde, zurück.
   *
   * @returns {boolean}
   */
  public isLoaded(): boolean {
    return this.loaded;
  }

  /**
   * Liefert die URL für das Bild auf Basis der übergebenen BAN-ID zurück.
   *
   * @returns {string}
   */
  public getImageUrl(): string {
    // using studisuhttp service here to let it decide wheter to add urlparam cb
    // to have its use consistant
    let url = this.serverURL + "?banid=" + this._banid;
    url = this.studisuHttp.addCacheBustingToUrl(url);
    return  url;
  }
}
