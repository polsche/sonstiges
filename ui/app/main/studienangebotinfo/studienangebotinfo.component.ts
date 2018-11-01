import { StudienangebotDetail } from "../suche/services/model/StudienangebotDetail";
import {Component, Input, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, HostListener} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { UrlParamService } from "./../suche/services/url-param.service";
import { LoggingService } from "../../services/logging.service";
import { StudienangebotinfoService } from "./studienangebotinfo.service";
import { StudisuErrorService } from "../../error/studisuerror.service";
import { Messages } from "../../ui-components/model/Messages";
import { ServiceConstants } from "../../services/serviceconstants";
import { ZuletztAngesehenListe } from "../../services/zuletztangesehen.service";
import { LiveAnnouncer } from "../../services/live-announcer";
import { OsaInfoPopupDelegete } from "../../ui-components/delegetes/osa-info-popup.delegete";
import { MessageService } from "../../services/message.service";

@Component({
  providers: [StudienangebotinfoService],
  selector: "ba-studisu-studenangebotinfo",
  templateUrl: "./studienangebotinfo.component.html"
})
export class StudienangebotInfoComponent implements OnInit, AfterViewInit, OnDestroy {

  public studienangebot: StudienangebotDetail;
  public fehler = false;

  @ViewChild("ueberschrift") private element: ElementRef;

  /**
   * Subscribe der ActivatedRoute
   */
  private sub: any;

  private _infopopupDelegate: OsaInfoPopupDelegete;

  @Input()
  private showInfo = false;

  @Input()
  set infopopupDelegate(delegate: OsaInfoPopupDelegete) {
    this._infopopupDelegate = delegate;
    this.showInfo = this._infopopupDelegate != null;
  }

  get infopopupDelegate(): OsaInfoPopupDelegete {
    return new OsaInfoPopupDelegete(this.messageService);
  }

  constructor(private _urlParamService: UrlParamService,
    private _route: ActivatedRoute,
    private _studienangebotService: StudienangebotinfoService,
    private logger: LoggingService,
    private zuletztAngesehenListe: ZuletztAngesehenListe,
    private studisuErrorService: StudisuErrorService,
    private announcer: LiveAnnouncer,
    public messageService: MessageService) {
  }

  public ngOnInit() {
    // Benötigt, da ein Bug in der aktuellen Angular-Version vorhanden ist,
    // dass der Router nicht automatisch auf den Anfang der Seite Navigiert.
    // Hier noetig, da ueber das Router Objekt navigiert wird und nicht ueber das Template
    // Ueber das Template koennen wir (noch) keine Parameter uebergeben
    this.announcer.announce("Studienangebots Seite geladen");
    window.scrollTo(0, 0);
    this.sub = this._route.params.subscribe(params => {
      //noinspection TypeScriptUnresolvedVariable
      this._studienangebotService.getStudienangebot(params.id)
        .subscribe(angebot => {
          this.studienangebot = angebot;
          if (this.studienangebot !== undefined) {
            //noinspection TypeScriptUnresolvedVariable
            // add angebot to zuletztAngesehen
            this.zuletztAngesehenListe.addAngesehen(angebot.id);
            // setzte als "gerade angesehen" (weil wir gerade details anschauen)
            // siehe STUDISU-95,
            this.zuletztAngesehenListe.setGeradeAngesehen();
          }
        },
        // (Nicht der usrpüngl. Autor) Vermute, wenn kein spezifischer error Vorhanden
        // soll ein Dialog angezeigt werden, sonst soll wohl eine Weiterleitung
        // auf eine Fehlerseite stattfinden
        error => {
          if (error === ServiceConstants.NOT_FOUND_MESSAGE) {
            this.fehler = true;
          } else {
            this.fehler = false;
            // STUDISU-105 zeige Fehlerdialog
            this.studisuErrorService.pushError(Messages.FEHLER_LADEN_DATEN, error);
          }
        });
    });
  }

  public ngAfterViewInit() {
    this.element.nativeElement.focus();
  }
  public onPrevClicked() {
    if (this.studienangebot.numPrev > 0 && this.studienangebot.prevId) {
      this._urlParamService.navigateTo("studienangebot/" + this.studienangebot.prevId, this.getUrlParamsForDeeplink());
    }
  }

  public ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
  public onNextClicked() {
    if (this.studienangebot.numNext > 0 && this.studienangebot.nextId) {
      this._urlParamService.navigateTo("studienangebot/" + this.studienangebot.nextId, this.getUrlParamsForDeeplink());
    }
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks DauerUndTermine gesetzt ist;
   * ngIf rendert dann den gesamten Block nicht.
   */
  private isDauerUndTermineGesetzt(): boolean {
    return this.studienangebot != null && this.studienangebot.dauer != null && (
      this.studienangebot.dauer.beginn != null ||
      this.studienangebot.dauer.ende != null ||
      this.studienangebot.dauer.bemerkung != null ||
      this.studienangebot.dauer.zulassungssemester != null ||
      this.studienangebot.dauer.individuellerEinstieg != null ||
      this.studienangebot.dauer.unterrichtszeiten != null);
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks Studienort gesetzt ist;
   * ngIf rendert dann den gesamten Block nicht.
   */
  private isStudienortGesetzt(): boolean {
    return this.studienangebot != null && this.studienangebot.studienort != null && (
      this.studienangebot.studienort.strasse != null || this.studienangebot.studienort.postleitzahl != null ||
      this.studienangebot.studienort.ort != null || this.studienangebot.studienort.bundesland != null);
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks Kontaktdaten gesetzt ist;
   * ngIf rendert dann den gesamten Block nicht.
   */
  private isKontaktdatenGesetzt(): boolean {
    return this.studienangebot != null && this.studienangebot.kontakt != null && (
      this.studienangebot.kontakt.telefonNummer != null ||
      this.studienangebot.kontakt.telefaxNummer != null ||
      this.studienangebot.kontakt.internet != null ||
      this.studienangebot.kontakt.email != null);
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks Studienanbieter gesetzt ist;
   * ngIf rendert dann den gesamten Block nicht.
   */
  private isStudienanbieterGesetzt(): boolean {
    return this.studienangebot != null && this.studienangebot.bildungsanbieter != null && (
      this.studienangebot.bildungsanbieter.name != null || this.studienangebot.bildungsanbieter.strasse != null ||
      this.studienangebot.bildungsanbieter.ort != null || this.studienangebot.bildungsanbieter.postleitzahl != null);
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks Studiengangsinformationen gesetzt ist
   * oder ein Studienfach bekannt ist.
   * ngIf rendert dann den gesamten Block nicht.
   */
  private isStudiengangsinformationenGesetzt(): boolean {
    return this.studienangebot != null && this.studienangebot.studiengangsinformationen != null && (
      this.studienangebot.studiengangsinformationen.studienfaecher != null ||
      this.studienangebot.studiengangsinformationen.bildungsart != null ||
      this.studienangebot.studiengangsinformationen.abschlusstyp != null ||
      this.studienangebot.studiengangsinformationen.studienform != null ||
      this.studienangebot.studiengangsinformationen.schulart != null ||
      this.studienangebot.studiengangsinformationen.abschlussgrad != null ||
      this.studienangebot.studiengangsinformationen.abschlussgradIntern != null ||
      this.studienangebot.studiengangsinformationen.regelstudienzeit != null ||
      this.studienangebot.studiengangsinformationen.lehramtsbefaehigung != null ||
      this.studienangebot.studiengangsinformationen.lehramtstyp != null ||
      this.studienangebot.studiengangsinformationen.unterrichtssprache != null ||
      this.studienangebot.studiengangsinformationen.internationalerDoppelabschluss != null ||
      this.studienangebot.studiengangsinformationen.dualeStudienmodelle != null);
  }

  /**
   * Helper-Methode für UI: Gibt false zurück, wenn nicht mindestens ein Element des UI-Blocks Zugangsinformantionen gesetzt ist;
   * ngIf rendert dann den gesamten Block nicht.
   */
  private isZugangsinformationenGesetzt(): boolean {
    return this.studienangebot != null && this.studienangebot.zugangsinformationen != null && (
      this.studienangebot.zugangsinformationen.voraussetzungen != null ||
      this.studienangebot.zugangsinformationen.zulassungsmodus != null ||
      this.studienangebot.zugangsinformationen.zulassungsmodusInfo != null ||
      this.studienangebot.zugangsinformationen.ohneAbiMoeglich != null ||
      this.studienangebot.zugangsinformationen.ohneAbiZugangsbedingungen != null ||
      this.studienangebot.zugangsinformationen.akkreditierung != null ||
      this.studienangebot.zugangsinformationen.akkreditierungVon != null ||
      this.studienangebot.zugangsinformationen.akkreditierungBis != null ||
      this.studienangebot.zugangsinformationen.akkreditierungsbedingungen  != null);
  }

  /**
   * Helper-Methode für UI: Fügt Leerzeichen hinter ",;:" ein, wenn keine vorhanden sind.
   *
   * @param text Der zu bearbeitende Text
   * @return Der Text mit eingefügten Leerzeichen
   */
  private insertSpaces(text: string): string {
    return text
      .replace(/,([^\s])/g, ", $1")
      .replace(/;([^\s])/g, "; $1")
      .replace(/:([^\s])/g, ": $1");
  }

  private onSucheClicked() {
    let params = this.getUrlParamsForDeeplink();
    if (this.studienangebot.currentPage > 1) {
      params[UrlParamService.PARAM_PAGE] = this.studienangebot.currentPage;
    }
    this._urlParamService.navigateTo("suche", params);
  }


  /**
   * Ermittelt im Falle eines Deeplinks die Basisinformationen aus den Tags, um daraus eine Suchanfrage zu bauen.
   *
   * @returns Objekt, entweder leer oder mit Parameter
   */
  private getUrlParamsForDeeplink() {
    let params = this._urlParamService.currentParams.getValue();
    // Studienfaecher oder Studienfelder vorhanden, dann kein Deeplink und keine Aenderung der Parameter.
    if (params.has(UrlParamService.PARAM_STUDIENFAECHER) || params.has(UrlParamService.PARAM_STUDIENFELDER)) {
      return {};
    }
    // Weder Studienfaecher noch Studienfelder vorhanden, also Parameter aus dem Angebot uebernehmen.
    return {
      [UrlParamService.PARAM_STUDIENFAECHER]: this.studienangebot.studienfaecherCsv,
      [UrlParamService.PARAM_HOCHSCHULART]: this.studienangebot.hochschulart.id,
      [UrlParamService.PARAM_STUDIENFORMEN]: this.studienangebot.studienform.id,
      [UrlParamService.PARAM_STUDT]: this.studienangebot.studientyp.id
    };
  }
}
