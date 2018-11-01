import {Component, OnInit, OnDestroy, Output, AfterViewInit, ElementRef, ViewChild} from "@angular/core";
import {UrlParamService} from "../../main/suche/services/url-param.service";
import {StudienfelderService} from "./services/studienfelder.service";
import {Studienfeld} from "./services/model/Studienfeld";
import {Messages} from "../../ui-components/model/Messages";
import {StudienfelderDelegate} from "./studienfelder.delegate";
import {Studienfeldgruppe} from "./services/model/Studienfeldgruppe";
import {LiveAnnouncer} from "../../services/live-announcer";

/**
 * Komponente fuer die Darstellung der Seite zur Auswahl der Studienfelder.
 */
@Component({
  providers: [
    StudienfelderService,
    StudienfelderDelegate
  ],
  selector: "ba-studisu-studienfelder",
  templateUrl: "./studienfelder.component.html"
})
export class StudienfelderComponent implements OnInit, AfterViewInit, OnDestroy {

  public label: Object = Messages;

  public ueberschriftMessage: string = Messages.UEBERSCHRIFT;

  public einleitenderText: string = Messages.EINLEITENDER_TEXT;
  @ViewChild("ueberschrift") private element: ElementRef;

  // Filterung der Liste aufgrund der selektierten Checkboxen im selektierteStudienfelder-Array
  private selectedStudienfeldgruppen: Studienfeldgruppe[] = [];

  // Map der geöffneten Studienfeldgruppen anhand des key-Attributes
  // NB: Wir brauchen eine Map für die korrekte Initialisierung, ein Array mit den geöffneten Elementen "tut" es nicht!
  private studienfeldGruppenOpenState: { [key: string]: boolean } = {};

  get studienfeldgruppen(): Studienfeldgruppe[] {
    return this.studienfelderDelegate.studienfeldgruppen;
  }

  get selektierteStudienfelder(): Studienfeld[] {
    return this.studienfelderDelegate.selektierteStudienfelder;
  }

  /**
   * Constructor der Komponente mit injizierten Services.
   *
   * @param urlParamService Injected Service.
   * @param studienfelderService Injected Service.
   * @param studienfelderDelegate Delegate Class injected as Service.
   */
  constructor(private urlParamService: UrlParamService,
              private studienfelderService: StudienfelderService,
              private studienfelderDelegate: StudienfelderDelegate,
              private announcer: LiveAnnouncer) {
  }

  public ngOnInit() {
    this.announcer.announce("Studienfelder Seite geladen");
    this.studienfelderDelegate.init();
    window.scrollTo(0, 0);
  }

  public ngAfterViewInit() {
    this.element.nativeElement.focus();
  }

  public ngOnDestroy() {
    this.studienfelderDelegate.destroy();
  }

  public get isMaxParameterCountExceeded() {
    return this.studienfelderDelegate.isMaxParameterCountExceeded;
  }

  /**
   * Prueft, ob das Studienfeld selektiert ist oder nicht.
   *
   * @param studienfeld Das zu pruefende Studienfeld.
   */
  public isChecked(studienfeld: Studienfeld): boolean {
    return this.selektierteStudienfelder.indexOf(studienfeld) > -1;
  }

  /**
   * Callback u.a. fuer die Checkbox, selektiert oder de-selektiert ein Studienfeld.
   *
   * @param studienfeld Das umzuschaltende Studienfeld
   */
  public toggleChecked(studienfeld: Studienfeld) {
    this.studienfelderDelegate.remove(studienfeld);
  }

  public remove(studienfeld: Studienfeld) {
    this.studienfelderDelegate.remove(studienfeld);
  }

  /**
   * Gibt true zurueck, falls eines der Studienfelder, die uebergeben wurden,
   * vom Anwender selektiert wurde UND falls ein Studienfeld
   * jemals selektiert wurde (siehe unterer Kommentar)
   * Wird in der HTML-Component dazu verwendet, das Accordion automatisch auszuklappen, falls der Benutzer auf die Studienfelder-Seite
   * zurueckkehrt und zuvor ein Studienfeld ausggewaehlt hat.
   * STUDISU-43.
   *
   * @param studienfeldgruppe Die in der HTML-Component-Iteration aktuelle Studienfeldgruppe, der gerendert wird
   * @returns {boolean} True falls ein Studienfeld der Studienfeldgruppe bereits selektiert wurde.
   */
  public isCheckboxSelected(studienfeldgruppe: Studienfeldgruppe) {

    // Filterung der Liste aufgrund der selektierten Checkboxen im selektierteStudienfelder-Array
    let selectedCheckboxVorhanden = this.selektierteStudienfelder.filter(item => studienfeldgruppe.studienfelder.indexOf(item) > -1);
    if (selectedCheckboxVorhanden.length > 0) {
      // Zur spaeteren ueberpruefung, ob die Studienfeldgruppe jemals selektiert wurde
      // und das damit assoziierte Accordion geoeffnet bleiben soll, pushen wir den SB in das Array
      this.selectedStudienfeldgruppen.push(studienfeldgruppe);
      return true;
    }

    /**
     * Falls die letzte Checkbox abgewaehlt wurde, wuerde die Methode false zurueck geben und das Accordion geschlossen werden;
     * da laut Anforderung das Accordion dennoch geoeffnet bleiben soll,
     * ueberpruefen wir, ob die aktuelle Studienfeldgruppe in der Liste der jemals selektierten Studienfeldgruppen vorhanden ist.
     * Falls ja, geben wir true zurueck und das Accordion bleibt offen.
     */
    if (this.selectedStudienfeldgruppen.length > 0) {
      let s = this.selectedStudienfeldgruppen.filter(item => studienfeldgruppe === item);
      if (s.length > 0) {
        return true;
      }
    }
  }

  /**
   * Liefert ein JSON-Objekt mit den DKZ-IDs des angegebenen Studienfelds und behaelt alle anderen URL-Parameter bei,
   * ausser die Seite. Da es sich um eine neue Suchanfrage handelt, darf die Seite nicht uebernommen werden.
   *
   * Dies erlaubt die direkte Verwendung als [queryParams] eines routerLinks.
   *
   * @param studienfeld Das Studienfeld, dessen DKZ-ID(s) in die URL uebernommen werden.
   */
  @Output()
  private studienfeldAsParam(studienfeld: Studienfeld): { [key: string]: string } {
    return this.urlParamService.getQueryParams(
      {
        [UrlParamService.PARAM_STUDIENFELDER]: studienfeld.dkzIds.join(UrlParamService.VALUE_SEPARATOR),
        [UrlParamService.PARAM_PAGE]: null
      }
    );
  }

  /**
   * Schaltet den Öffnungszustand des Akkordeons für die übergebene Studienfeldgruppe um.
   *
   * @param {string} studienfachId
   */
  private toggleOpenState(studienfeldgruppe: Studienfeldgruppe): void {
    if (this.studienfeldGruppenOpenState.hasOwnProperty(studienfeldgruppe.key)) {
      this.studienfeldGruppenOpenState[studienfeldgruppe.key] = !this.studienfeldGruppenOpenState[studienfeldgruppe.key];
    } else {
      this.studienfeldGruppenOpenState[studienfeldgruppe.key] = true;
    }
  }

  /**
   * Liefert den Tooltipp für den Öffnen/Schließen-Schalter des Akkordeons.
   *
   * @param studienfeldgruppe Die Studienfeldgruppe, die im Akkordeon-Element enthalten ist.
   * @returns {string}
   */
  private getToolTipp(studienfeldgruppe: Studienfeldgruppe): string {
    let tooltippTemplate = this.isOpen(studienfeldgruppe) ? Messages.WEITERE_INFO_AUSBLENDEN : Messages.WEITERE_INFO_EINBLENDEN;
    return tooltippTemplate.replace(/%name%/, studienfeldgruppe.name);
  }

  /**
   * Ermittelt anhand der übergebenen Studienfeldgruppe, ob das Akkordeon-Element gerade geöffnet ist.
   *
   * @param {string} studienfachId
   * @returns {boolean}
   */
  private isOpen(studienfeldgruppe: Studienfeldgruppe): boolean {
    if (!this.studienfeldGruppenOpenState.hasOwnProperty(studienfeldgruppe.key)) {
      this.studienfeldGruppenOpenState[studienfeldgruppe.key] = this.selektierteStudienfelder
        .filter(item => studienfeldgruppe.studienfelder.indexOf(item) > -1).length > 0;
    }
    return this.studienfeldGruppenOpenState[studienfeldgruppe.key];
  }
}
