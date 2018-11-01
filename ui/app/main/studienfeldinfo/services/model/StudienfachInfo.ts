/**
 * Representation einer Studienfachinformation.
 */
import { SafeResourceUrl } from "@angular/platform-browser/src/security/dom_sanitization_service";

export class StudienfachInfo {
  public neutralBezeichnung: string;
  public studienfachbeschreibungen: string[];
  public studiengangsbezeichnungen: string[];
  public studienfachFilmId: number;
  public id: number;
  public count: number;
  public filmURL: SafeResourceUrl;
}
