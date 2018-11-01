import { Studienangebot } from "../main/suche/services/model/Studienangebot";
import { Facette } from "../main/suche/services/model/Facetten";

export class SearchResult {
  public items: Studienangebot[];
  public facetten: Facette[];
  public maxErgebnisse = 0;
  public filteredOutErgebnisse = 0;
  public offset: number;
  public hasSearched = false;

  constructor () {
    this.items = [];
    this.facetten = [];
  }

  public get entriesCount(): number {
    return this.items.length;
  }
}

