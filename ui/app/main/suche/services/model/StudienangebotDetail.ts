import {Auswahl} from "./Auswahl";
import {AdresseKurz} from "./AdresseKurz";
import {Kontakt} from "./Kontakt";
import {Dauer} from "./Dauer";
import {Studiengangsinformationen} from "./Studiengangsinformationen";
import {ExternalLink} from "./ExternalLink";
import {Zugangsinformationen} from "./Zugangsinformationen";

/**
 * Representation eines Studienangebotes
 */
export class StudienangebotDetail {

  public bezeichnung: string;

  public studiengangsinformationen: Studiengangsinformationen;
  // Facetten werden für den Browser-Back-Button benötigt.
  public studientyp: Auswahl;
  public studienform: Auswahl;
  public hochschulart: Auswahl;

  // zusatzlink der veranstaltung
  public veranstaltungZusatzlink: string;

  // studienort
  public studienort: AdresseKurz;

  // dauer und termine
  public dauer: Dauer;

  // studieninhalte
  public inhalt: string;
  public studienschwerpunkte: string;
  public studiuminformationen: string;

  // zugangsinformationen
  public zugangsinformationen: Zugangsinformationen;

  // kosten/gebuehren/foerderungen
  public kosten: string;

  // kontakt
  public kontakt: Kontakt;

  // studienanbieter
  public bildungsanbieter: AdresseKurz;
  public bildungsanbieterId: number;
  public bildungsanbieterHasSignet: boolean;
  public isHrkDatensatz: boolean;

  // veroeffentlichungsinfos
  public id: string;
  public aktualisierungsdatum: string;

  public studienfaecherCsv: string;
  public externalLinks: ExternalLink[];

  // Navigationsinformationen
  public numPrev = 0;
  public prevId: string;
  public numNext = 0;
  public nextId: string;
  public currentPage: 1;

  constructor(jsobj) {
    this.externalLinks = [];
    this.bezeichnung = jsobj.bezeichnung;
    this.studiengangsinformationen = jsobj.studiengangsinformationen;
    this.studientyp = jsobj.studientyp;
    this.studienform = jsobj.studienform;
    this.hochschulart = jsobj.hochschulart;
    this.veranstaltungZusatzlink = jsobj.veranstaltungZusatzlink;
    this.studienort = jsobj.studienort;
    this.dauer = jsobj.dauer;
    this.inhalt = jsobj.inhalt;
    this.studienschwerpunkte = jsobj.studienschwerpunkte;
    this.studiuminformationen = jsobj.studiuminformationen;
    this.zugangsinformationen = jsobj.zugangsinformationen;
    this.kosten = jsobj.kosten;
    this.kontakt = jsobj.kontakt;
    this.bildungsanbieter = jsobj.bildungsanbieter;
    this.bildungsanbieterId = jsobj.bildungsanbieterId;
    this.bildungsanbieterHasSignet = jsobj.bildungsanbieterHasSignet;
    this.isHrkDatensatz = jsobj.isHrkDatensatz;
    this.id = jsobj.id;
    this.aktualisierungsdatum = jsobj.aktualisierungsdatum;
    this.studienfaecherCsv = jsobj.studienfaecherCsv;
    this.numPrev = jsobj.numPrevElements;
    this.prevId = jsobj.prevElementId;
    this.numNext = jsobj.numNextElements;
    this.nextId = jsobj.nextElementId;
    this.currentPage = jsobj.currentPage;
    if (jsobj.externalLinks != null) {
      jsobj.externalLinks.forEach((elem, idx, arr) => {
        this.externalLinks.push(new ExternalLink(elem));
      }, this);
    }
  }

  public hasStudicheckOsa(): boolean {
    return this.externalLinks.length > 0;
  }
}
