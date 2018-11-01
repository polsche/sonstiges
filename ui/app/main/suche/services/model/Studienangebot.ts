import {Auswahl} from "./Auswahl";
import {AdresseKurz} from "./AdresseKurz";
import {ExternalLink} from "./ExternalLink";
import {SuchortAbstand} from "./SuchortAbstand";

/**
 * Representation eines Studienangebotes
 */
export class Studienangebot {
  public id: string;
  public studiBezeichnung: string;
  public studiInhalt: string;
  public studienfaecher: string[];
  public bildungsanbieterName: string;
  public bildungsanbieterId: number;
  public bildungsanbieterHasSignet: boolean;
  public studiBeginn: string;
  public studienform: Auswahl;
  public hochschulart: Auswahl;
  public studientyp: Auswahl;
  public studienort: AdresseKurz;
  public externalLinks: ExternalLink[];
  public abstaende: SuchortAbstand[];

  /**
   * C-tor with json obj
   * @param jsobj json/js object with property structure of Studienangebot
   */
  constructor(jsobj) {
    this.externalLinks = [];
    if (jsobj != null) {
      this.id = jsobj.studienangebot.id;
      this.studiBezeichnung = jsobj.studienangebot.studiBezeichnung;
      this.studiInhalt = jsobj.studienangebot.studiInhalt;
      this.studienfaecher = jsobj.studienangebot.studienfaecher;
      this.bildungsanbieterName = jsobj.studienangebot.bildungsanbieterName;
      this.bildungsanbieterId = jsobj.studienangebot.bildungsanbieterId;
      this.bildungsanbieterHasSignet = jsobj.studienangebot.bildungsanbieterHasSignet;
      this.studienort = jsobj.studienangebot.studienort;
      this.studiBeginn = jsobj.studienangebot.studiBeginn;
      this.studienform = jsobj.studienangebot.studienform;
      this.hochschulart = jsobj.studienangebot.hochschulart;
      this.studientyp = jsobj.studienangebot.studientyp;
      this.abstaende = jsobj.abstaende.map(x => new SuchortAbstand(x));
      if (jsobj.studienangebot.externalLinks != null) {
        jsobj.studienangebot.externalLinks.forEach((elem, idx, arr) => {
          this.externalLinks.push(new ExternalLink(elem));
        }, this);
      }
    }
  }

  public hasStudicheckOsa(): boolean {
    return this.externalLinks.length > 0;
  }
}
