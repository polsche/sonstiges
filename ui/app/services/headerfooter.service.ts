import {Injectable, isDevMode} from "@angular/core";
import {LoggingService} from "./logging.service";
import {StudisuHttpService} from "./studisu-http.service";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";
import {getStudisuConfig, StudisuConfig} from "../config/studisu.config";

// Can use jQuery here
declare var $: any;

/**
 * Dieser Service laedt den Header und den Footer von APOK.
 *
 * Wenn beide Komponenten geladen sind wird das JavaScript zum Betrieb vom Header und Footer ebenfalls nachgeladen.
 */
@Injectable()
export class HeaderFooterService {

  public conf: StudisuConfig = getStudisuConfig();

  private hasRestHeader = false;
  private hasHeaderDOM = false;
  private hasRestFooter = false;
  private hasFooterDOM = false;
  private hasJSLoaded = false;

  constructor(private oagHttp: StudisuHttpService,
              private _sanitizer: DomSanitizer,
              private logger: LoggingService) {
  }

  /**
   * Laedt den Header per {@link StudisuHttpService} aus der angegebenen URL.
   *
   * Falls das Laden erfolgreich war, wird das entsprechende Header-Flag gesetzt.
   *
   * @param {string} url
   * @param callback
   */
  public getHeaderFromOAG(url: string, callback): void {
    this.getSanitizedStringFromURL(
      url,
      (value: SafeHtml) => {
        this.hasRestHeader = null !== value;
        callback(value);
      }
    );
  }

  /**
   * Informiert den Service, dass der DOM-Tree fuer den Header initialisiert wurde.
   *
   * Laedt das JavaScript fuer den Header-Footer-Betrieb nach dem Laden von Header UND Footer.
   */
  public signalHeaderDOMInit(): void {
    if (this.hasRestHeader && !this.hasHeaderDOM) {
      this.hasHeaderDOM = true;
      this.loadJavaScript();
    }
  }

  /**
   * Laedt den Footer per {@link StudisuHttpService} aus der angegebenen URL.
   *
   * Falls das Laden erfolgreich war, wird das entsprechende Footer-Flag gesetzt.
   *
   * @param {string} url
   * @param callback
   */
  public getFooterFromOAG(url: string, callback): void {
    this.getSanitizedStringFromURL(
      url,
      (value: SafeHtml) => {
        this.hasRestFooter = null !== value;
        callback(value);
      }
    );
  }


  /**
   * Informiert den Service, dass der DOM-Tree fuer den Footer initialisiert wurde.
   *
   * Laedt das JavaScript fuer den Header-Footer-Betrieb nach dem Laden von Header UND Footer.
   */
  public signalFooterDOMInit(): void {
    if (this.hasRestFooter && !this.hasFooterDOM) {
      this.hasFooterDOM = true;
      this.loadJavaScript();
    }
  }

  /**
   * Laedt die angegebene URL vom {@link StudisuHttpService}.
   *
   * @param {string} url
   * @param callback
   */
  private getSanitizedStringFromURL(url: string, callback): void {
    this.oagHttp.get(url).subscribe((...args) => {
      callback(this._sanitizer.bypassSecurityTrustHtml(args[0]._body));
    }, (message) => {
      callback(null);
      if (isDevMode()) {
        this.logger.error(message);
      }
    });
  }

  /**
   * Laedt einmalig (!) das JavaScript fuer den Header und den Footer.
   *
   * Stellt sicher, dass das Laden erst nach der Initialierung des DOM-Trees fuer Header + fuer Footer stattfindet.
   */
  private loadJavaScript(): void {
    // Nur ausführen, wenn noch nicht geladen + DOM-Trees für Header + Footer initialisiert.
    if (this.hasJSLoaded || !(this.hasHeaderDOM && this.hasFooterDOM)) {
      return;
    }

    this.hasJSLoaded = true; // Script nur 1x laden!!!
    let url = "";

    // JS fuer Header und Footer per REST
    const hfJsTag = document.getElementById("ba-headerfooter-js");
    if (hfJsTag && hfJsTag.getAttribute("src")) {
      // URL aus dem Header entnommen
      url = hfJsTag.getAttribute("src");
    } else {
      // Fallback-URL
      url = this.conf.baseUrlHeaderFooter + this.conf.jsRestPath;
    }

    $.getScript(url); // Produziert Fehler auf Console, funktioniert aber.
  }
}
