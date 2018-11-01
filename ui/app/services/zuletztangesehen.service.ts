import {Injectable} from "@angular/core";

/**
 *
 */
@Injectable()
export class ZuletztAngesehenListe {
  private MAX_ANGEBOTIDS = 1000;
  private _angebotids: Array<string> = new Array<string>();

  /**
   * geradeAngesehen - the angebot whose details are being looked at right now
   * Set when in detail page, and reset to "" after navigation back to results
   * @type {string}
   */
  private geradeAngesehen = "";

  public addAngesehen(angebotid: string) {
    // falls Angebot schon früher aufgerufen wurde, alten Aufruf rausfiltern
    this._angebotids = this._angebotids.filter((id, idx, arr) => {
      return id !== angebotid;
    });
    // neuen Aufruf an Anfang des Arrays hängen
    this._angebotids.unshift(angebotid);
    // Array bei Bedarf auf MAX_ANGEBOTIDS Einträge kürzen
    this._angebotids.splice(this.MAX_ANGEBOTIDS);
  }

  /**
   * check if id in angesehen liste
   * @param id
   */
  public contains(id: string): boolean {
    return this._angebotids.indexOf(id) > -1;
  }

  /**
   * set gerade angesehen to angebot id
   * when in detail page for example.
   */
  public setGeradeAngesehen() {
    this.geradeAngesehen = this._angebotids.length > 0 ? this._angebotids[0] : "";
  }

  /**
   * clear or reset gerade angesehen (after focus or new search for example)
   */
  public clearGeradeAngesehen() {
    this.geradeAngesehen = "";
  }

  /**
   * true if passed in id string is a real string and equals
   * gerade angesehen
   * @param id
   * @returns {boolean}
   */
  public isGeradeAngesehen(id: string): boolean {
    return id && id === this.geradeAngesehen;
  }
}
