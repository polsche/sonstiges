/**
 * Repräsentation von Zugangsinformationen, die in dieser
 * Form vom Backend geliefert werden.
 */
import {OhneAbiZugangsbedingung} from "./OhneAbiZugangsbedingung";

export class Zugangsinformationen {
  public zulassungsmodus: string;
  public zulassungsmodusInfo: string;
  public voraussetzungen: string;
  public ohneAbiMoeglich: boolean;
  public ohneAbiZugangsbedingungen: OhneAbiZugangsbedingung[];
  public akkreditierung: string;
  public akkreditierungVon: string;
  public akkreditierungBis: string;
  public akkreditierungsbedingungen: string;
}
