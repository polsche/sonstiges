import {
  Component, OnInit, OnDestroy, ViewChild, AfterViewInit, ElementRef, ViewChildren,
  QueryList
} from "@angular/core";
import {UrlParamService} from "../../main/suche/services/url-param.service";
import {StudienfeldInfo} from "./services/model/StudienfeldInfo";
import {StudienfachInfo} from "./services/model/StudienfachInfo";
import {StudieninfoService} from "./services/studieninfo.service";
import {Subscription} from "rxjs/Subscription";
import {LoggingService} from "../../services/logging.service";
import {StudisuErrorService} from "../../error/studisuerror.service";
import {Messages} from "../../ui-components/model/Messages";
import {ServiceConstants} from "../../services/serviceconstants";
import {ErrorHandlerService} from "../../error/errorhandler.service";
import {HinweisModalData} from "../../ui-components/modalhinweisdialog/hinweismodalData";
import {MessageService} from "../../services/message.service";
import {LiveAnnouncer} from "../../services/live-announcer";
import {HEADER_IMAGE_MAP} from "./headerimagemap";

@Component({
  providers: [
    StudieninfoService
  ],
  selector: "ba-studisu-studienfeldinfo",
  templateUrl: "./studienfeldinfo.component.html"
})
// nachdem hier 300 Zeilen zur Hauptfunktion der Komponente folgen,
// duerfen die Hilfsklassen ruhig darunter folgen, CKU 14.06.18
// tslint:disable:no-use-before-declare
export class StudienfeldinfoComponent implements OnInit, OnDestroy, AfterViewInit {

  /**
   * set accordion component.
   * Nötig um erste selektierte Accordion Panel zu fokussieren
   * beim laden (STUDISU-280)
   * @param {AccordionComponent} acc
   */
  @ViewChildren("accordion")
  public accordionL: QueryList<HTMLDivElement>;

  public name: string;
  public descriptions: string[] = [];
  public studienfelder: number[] = [];
  public studienfeldInfos: StudienfeldInfo[] = [];
  public studienfachInfos: StudienfachInfo[];
  public fehler = false;

  public label: Object = Messages;

  private _element: ElementRef;
  private scrollManager: ScrollManager;

  // Verzeichnis, in dem sich die Header-Bilder befinden
  private IMAGES_DIR = "assets/images/";
  private HEADER_PIC_DIR = this.IMAGES_DIR + "header-studienfeld/";

  // Default Header Bild (inklusive dir)
  private HEADER_DEFAULT_IMAGE = this.HEADER_PIC_DIR + "bg-large-header-studienfelder.jpg";

  @ViewChild("ueberschrift")
  private set element(elem: ElementRef) {
    if (elem != null && this._element == null) {
      this._element = elem;
    }
  }

  private urlParamSubscription: Subscription;

  // Counter Variablen
  private countGesamt = 0;
  private countCheckedSumme = 0;
  private selectedStudienfaecherIds: string[] = [];
  private selectedStudienfaecherIdsOnInit: string[] = [];
  private selectionModel = new SelectionModel<string>();

  // Map der geöffneten Studienfächer anhand des id-Attributes
  // NB: Wir brauchen eine Map für die korrekte Initialisierung, ein Array mit den geöffneten Elementen "tut" es nicht!
  private studienfaecherOpenState: { [key: number]: boolean } = {};

  /**
   * Constructor
   * @param urlParamService Injected Dependency
   * @param studieninfoService Injected Dependency
   * @param logger Injected Dependency
   * @param studisuErrorService Instanz des Service zur
   * @param errorHandlerService Instanz des Service zur
   * @param messageService Instanz des Service zur
   */
  constructor(private urlParamService: UrlParamService,
    private studieninfoService: StudieninfoService,
    private logger: LoggingService,
    private studisuErrorService: StudisuErrorService,
    private errorHandlerService: ErrorHandlerService,
    private messageService: MessageService,
    private announcer: LiveAnnouncer) {
      this.scrollManager = new ScrollManager();
    }

  public ngAfterViewInit(): void {
    if (this._element != null) {
      this._element.nativeElement.focus();
    }
    // check for accordion availability and scroll appropriately
    this.accordionL.changes.subscribe((accL: QueryList<ElementRef>) => {
      let y_offset = 0;
      let openAccordionComponents = accL.filter(elem => {
        if (elem === undefined) {
          return false;
        }
        return !(elem.nativeElement.classList.contains("collapsed"));
      });
      if (openAccordionComponents.length > 0) {
        y_offset = openAccordionComponents[0].nativeElement.offsetTop + (document.documentElement.clientHeight / 2);
      }
      this.scrollManager.scrollToY(y_offset);
    });
  }

  /**
   * Initialisierung der Komponente
   * Hier wird der zugehoerige Url Parameter ausgelesen und
   * verarbeitet
   */
  public ngOnInit() {
    this.announcer.announce("Studienfeldinfo Seite geladen");
    this.urlParamSubscription = this.getUrlParamSubscription();
    // Der URL-Parameter-Service reagiert jetzt auch auf Änderungen vom Parameter
    // für das Studienfach und fügt sie zu einer Liste der selektierten Studienfächer
    // hinzu
    this.urlParamSubscription.add(this.urlParamService.currentParams
      .map(params => params.get(UrlParamService.PARAM_STUDIENFAECHER))
      // mach nur was, wenn der Parameter sich wirklich ändert
      .distinctUntilChanged()
      .subscribe(studienfaecher => {
        if (studienfaecher) {
          this.selectedStudienfaecherIds = [];
          studienfaecher.split(ServiceConstants.VALUE_SEPARATOR).forEach(
            studienfachId => {
              this.selectionModel.get(studienfachId).selected = true;
              this.selectedStudienfaecherIds.push(studienfachId);
            }
          );
        }
        this.updateCounts();
      }));

    // Liste der initial beim betreten der Seite vorhandenen, zuvor selektierten Studienfächer
    this.selectedStudienfaecherIdsOnInit = this.selectedStudienfaecherIds.slice();
  }

  public ngOnDestroy() {
    if (null != this.urlParamSubscription) {
      this.urlParamSubscription.unsubscribe();
    }
  }

  /**
   * Wird aus der HTML aufgerufen, um das Accordeon auf- oder zugeklappt zu lassen, basierend darauf, ob die Checkbox zum
   * Accordeon angehakt wurde oder nicht. Das Array selectedStudienfaecherIdsOnInit wird bei der initialisierung gefüllt.
   * Grund: nur die angewählten Checkboxen beim betreten der Seite über URL-Parameter sind relevant.
   * @param checkboxId
   */
  public isStudienfachSelectedOnInit(checkboxId: string) {
    let found = false;
    this.selectedStudienfaecherIdsOnInit.forEach(id => {
      if (id === String(checkboxId)) {
        // Falls die übergebene ID mit der in der URL übereinstimmt: Accordeon aufklappen
        found = true;
        delete this.selectedStudienfaecherIdsOnInit[id];
      }
    });

    return found;
  }

  /**
   * Liefert die Subscription fuer den URL-Parameterservice.
   *
   * @return Die erzeugte Subscription
   */
  private getUrlParamSubscription(): Subscription {
    return this.urlParamService.currentParams
      .map(params => params.get(UrlParamService.PARAM_STUDIENFELDER))
      .distinctUntilChanged()
      .subscribe(studienfelder => {
          if (!studienfelder) {
            this.fehler = true;
          } else {
            this.processStudienfelder(studienfelder.split(";"));
            this.updateCounts();
          }
        },
        err => {
          this.logger.error("Service Request Failed with error: " + err, this);
          this.fehler = true;
        });
  }

  /**
   * Liefert das Studienfeld als Parameter für routerLink zurück.
   */
  private studienfeldAsParam() {
    return this.urlParamService.getQueryParams(
      {
        [UrlParamService.PARAM_STUDIENFELDER]: this.studienfelder.join(";"),
        [UrlParamService.PARAM_STUDIENFAECHER]: ""
      });
  }

  /**
   * Liefert das Studienfach als Parameter für routerLink zurück.
   */
  private studienfachAsParam(studienfach: StudienfachInfo) {
    return this.urlParamService.getQueryParams(
      {
        [UrlParamService.PARAM_STUDIENFELDER]: "",
        [UrlParamService.PARAM_STUDIENFAECHER]: studienfach.id
      }
    );
  }

  /**
   * Liefert das Studienfach als Parameter für routerLink zurück.
   */
  private studienfaecherAsParams() {
    return this.urlParamService.getQueryParams(
      {
        [UrlParamService.PARAM_STUDIENFELDER]: "",
        [UrlParamService.PARAM_STUDIENFAECHER]: this.selectedStudienfaecherIds.join(UrlParamService.VALUE_SEPARATOR)
      }
    );
  }

  /**
   * Verarbeitet eine beliebige Anzahl an Studienfeldern.
   * Fachlich sollten jedoch maximal zwei vorkommen.
   */
  private processStudienfelder(studienfelder: string[]) {
    this.studienfelder = studienfelder.map(id => +id); // type cast to number!
    this.loadStudienfelderDescriptions();
    this.loadStudienfaecher();
  }

  /**
   * Asynchrones Laden der Namen + WCC-Beschreibungen zu den gewählten Studienfelder.
   */
  private loadStudienfelderDescriptions() {
    for (let studienfeld of this.studienfelder) {
      this.studieninfoService.sucheStudienfeld(studienfeld).subscribe(
        studienfeldInfo => this.setStudienfeldInfo(studienfeldInfo),
        // (Nicht der usrpüngl. Autor) Vermute, wenn kein spezifischer error Vorhanden
        // soll ein Dialog angezeigt werden, sonst soll wohl eine Weiterleitung
        // auf eine Fehlerseite stattfinden
        error => this.handleError(error)
      );
    }
  }

  /**
   * Asynchrones Laden der Studienfächer zu allen gewählten Studienfeldern.
   */
  private loadStudienfaecher() {
    this.studienfelder.forEach(studienfeld => {
      this.studieninfoService.sucheStudienfaecherZuStudienfeld(studienfeld).subscribe(
        (studienfachInfos: StudienfachInfo[]) => {
          this.addStudienfachInfos(studienfachInfos);
          this.fehler = false;
          this.updateCounts();
        },
        // (Nicht der usrpüngl. Autor) Vermute, wenn kein spezifischer error Vorhanden
        // soll ein Dialog angezeigt werden, sonst soll wohl eine Weiterleitung
        // auf eine Fehlerseite stattfinden
        error => this.handleError(error)
      );
    });
  }

  /**
   * Speichert die Studienfeld-Information und ermittelt Titel + Beschreibung, wenn nicht leer + nicht vorhanden.
   */
  private setStudienfeldInfo(studienfeldInfo: StudienfeldInfo) {
    this.studienfeldInfos.push(studienfeldInfo);
    if (0 === this.descriptions.length) {
      this.descriptions = studienfeldInfo.studienfeldbeschreibungen;
      this.name = studienfeldInfo.neutralKurzBezeichnung;
    }
  }

  /**
   * Fügt Studienfeldinformationen zusammen, falls schon Informationen vorhanden sind.
   */
  private addStudienfachInfos(studienfachInfos: StudienfachInfo[]) {
    if (null == this.studienfachInfos) {
      this.studienfachInfos = studienfachInfos;
    } else {
      this.studienfachInfos = this.studienfachInfos.concat(studienfachInfos);
    }
    // alphabetische Sortierung nach der Bezeichnung
    this.studienfachInfos.sort((f1, f2) => f1.neutralBezeichnung.localeCompare(f2.neutralBezeichnung));
  }

  /**
   * Generisches Error-Handling für alle Laderoutinen der Klasse.
   *
   * @param error Der zu behandelnde Fehler
   */
  private handleError(error: any) {
    if (error === ServiceConstants.NOT_FOUND_MESSAGE) {
      this.fehler = true;
    } else {
      this.fehler = false;
      // STUDISU-105 zeige Fehlerdialog
      this.studisuErrorService.pushError(Messages.FEHLER_LADEN_DATEN, error);
    }
  }

  /**
   * Aktualisiert die Studienfach-Zähler für den oberen und den unteren Button "Studienangebote anzeigen"
   */
  private updateCounts() {
    let gesamt = 0;
    let selected = 0;

    if (this.studienfachInfos) {
      this.studienfachInfos.forEach(studienfach => {
        gesamt += studienfach.count;
        let item = this.selectionModel.get("" + studienfach.id);
        if (item && item.selected) {
          selected += studienfach.count;
        }
      });
    }

    this.countGesamt = gesamt;
    this.countCheckedSumme = selected;
  }

  /**
   * Methode um das selektieren einer Checkbox zu behandeln.
   * Es werden Counter erstellt und die URL bearbeitet.
   * @param event
   * @param index
   *  Index des selektieren Values
   */
  private onSelect(event: boolean, index: number) {
    // wichtig: als string
    let studienfach = this.studienfachInfos[index];
    let idString = "" + studienfach.id;
    this.selectionModel.get(idString).selected = event;
    let idx = this.selectedStudienfaecherIds.indexOf(idString);
    if (event && idx < 0) {
      this.selectedStudienfaecherIds.push(idString);
    } else if (!event && idx >= 0) {
      this.selectedStudienfaecherIds.splice(idx);
    }
    let urlIds = this.selectedStudienfaecherIds.join(UrlParamService.VALUE_SEPARATOR);
    this.urlParamService.updateView({[UrlParamService.PARAM_STUDIENFAECHER]: urlIds});
  }

  /**
   * Schaltet den Öffnungszustand des Akkordeons für das übergebene Studienfach um.
   *
   * @param {StudienfachInfo} studienfach
   */
  private toggleOpenState(studienfach: StudienfachInfo): void {
    if (this.studienfaecherOpenState.hasOwnProperty(studienfach.id)) {
      this.studienfaecherOpenState[studienfach.id] = !this.studienfaecherOpenState[studienfach.id];
    } else {
      this.studienfaecherOpenState[studienfach.id] = true;
    }
  }

  /**
   * Liefert den Tooltipp für den Öffnen/Schließen-Schalter des Akkordeons.
   *
   * @param studienfach Das Studienfach des Akkordeons.
   * @returns {string}
   */
  private getToolTipp(studienfach: StudienfachInfo): string {
    let tooltippTemplate = this.isOpen(studienfach) ? Messages.WEITERE_INFO_AUSBLENDEN : Messages.WEITERE_INFO_EINBLENDEN;
    return tooltippTemplate.replace(/%name%/, studienfach.neutralBezeichnung);
  }

  /**
   * Ermittelt anhand der übergebenen Studienfach-ID, ob das Akkordeon-Element geöffnet ist.
   *
   * @param {string} studienfachId
   * @returns {boolean}
   */
  private isOpen(studienfach: StudienfachInfo): boolean {
    if (!this.studienfaecherOpenState.hasOwnProperty(studienfach.id)) {
      this.studienfaecherOpenState[studienfach.id] = this.selectedStudienfaecherIds
        .findIndex(id => id === "" + studienfach.id) > -1;
    }
    return this.studienfaecherOpenState[studienfach.id];
  }

  /**
   * Berechnet die Höhe des Iframes anhand der übergebenen Breite.
   *
   * @param containerWidth Die für die Berechnung zu nutzende Containerbreite.
   */
  private getIframeHeight(containerWidth: number): string {
    return Math.floor(containerWidth * 0.625 + 20) + "px";
  }

  /**
   * Service-Call um Info-Dialog anzuzeigen
   */
  private infomodalOeffnen(): void {
    this.messageService.showHinweisDialog.next(new HinweisModalData(
      Messages.POPUP_INFO_ANZAHL_TREFFER_STUDIENFELDINFO_UEBERSCHRIFT,
      "",
      Messages.POPUP_INFO_ANZAHL_TREFFER_STUDIENFELDINFO_TEXT
    ));
  }

  /**
   * Holt aus der im Build-Prozess erstellten JS-Datei headerimagemap.ts den Namen des Bildes für das Studienfeld.
   * Vorgehensweise: Ist zu der codeNr (=4-Steller) ein Bild-Eintrag in der JS-Datei vorhanden, wird das codeNr-Bild verwendet.
   * Ist kein codeNr-Bild vorhanden, wird auf die ObercodeNr (=2-Steller) gegangen. Ist auch dort kein Bild für das Studienfeld vorhanden,
   * wird ein Default-Bild verwendet.
   *
   * @returns {string} Name des Bildes, z.B. 21_Agrar-_Forst-_Ernaehrungswissenschaften.jpg
   */
  private getHeaderImage(): string {
    if (this.studienfeldInfos.length === 0) {
      // Fehlerfall, robust antworten mit Default
      return this.HEADER_DEFAULT_IMAGE;
    }

    let currStudienfeld = this.studienfeldInfos[0];
    let codeNr  = currStudienfeld.codeNr;

    // Falls CodeNr (Viersteller) gesetzt und lang genug ist (3 Prefix + 4 Stellen)
    if (codeNr && codeNr.length > 6) {
      // Entfernen des Prefixes "HA " bzw. "HC " bei der Codenr
      codeNr = codeNr.substr(3);
      if (codeNr in HEADER_IMAGE_MAP) {
        return this.HEADER_PIC_DIR + HEADER_IMAGE_MAP[codeNr];
      }
    }

    let obercodeNr  = currStudienfeld.oberCodeNr;
    // Falls ObercodeNr (Zweisteller) gesetzt und lang genug ist (3 Prefix + 2 Stellen)
    if (obercodeNr && obercodeNr.length > 4) {
      // Entfernen des Prefixes "HA " bzw. "HC " bei der obercodeNr
      obercodeNr = obercodeNr.substr(3);
      if (obercodeNr in HEADER_IMAGE_MAP) {
        return this.HEADER_PIC_DIR + HEADER_IMAGE_MAP[obercodeNr];
      }
    }

    // Falls weder fuer Viersteller noch fuer Zweisteller ein dediziertes Bild hinterlegt ist:
    // Default-Bild (inklsive dir)
    return this.HEADER_DEFAULT_IMAGE;
  }
}

export class SelectionModel<E> {

  private map: any = {};

  public get(item: E): SelectionItem<E> {
    let selectionItem = this.map[item];
    if (!selectionItem) {
      selectionItem = new SelectionItem(item);
      this.map[item] = selectionItem;
    }
    return selectionItem;
  }
}

export class SelectionItem<E> {
  public selected = false;

  constructor(private value: E) {
  }
}

/**
 * scroll behaviour wrapper.
 * might think about making this globally available, if smooth scrolling needed elsewhere
 */
class ScrollManager {
  private isMSIE: boolean;

  constructor() {
    this.isMSIE = ScrollManager.isMSIE();
  }

  /**
   * helper method for ie identification
   * @returns {boolean}
   */
  public static isMSIE(): boolean {
    let ua = window.navigator.userAgent;
    let msie = ua.indexOf("MSIE ");
    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * wrapper method. ignores smooth scrolling for ie - because shaky.
   * @param {number} y
   */
  public scrollToY(y: number): void {
    if (this.isMSIE) {
      window.scrollTo(0, y);
    } else {
      window.scrollTo({left: 0, top: y, behavior: "smooth"});
    }
  }


}
