/**
 * Repräsentation von Studiengangsinformationen, die in dieser
 * Form vom Backend geliefert werden.
 */
export class Studiengangsinformationen {
  public bildungsart: string;
  public abschlusstyp: string;
  public studienform: string;
  public schulart: string;
  public abschlussgrad: string;
  public abschlussgradIntern: string;
  public regelstudienzeit: string;
  public lehramtsbefaehigung: string;
  public lehramtstyp: string;
  public unterrichtssprache: string;
  public internationalerDoppelabschluss: string;
  public dualeStudienmodelle: string[];
  public studienfaecher: string[];
}

