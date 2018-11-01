/**
 * Representation des Abstandes von einem Suchort.
 */
export class SuchortAbstand {
  public suchort: string;
  private abstand: string;

  constructor(jsobj) {
    this.suchort = jsobj.suchort;
    this.abstand = Number(jsobj.abstand).toFixed(1).replace(".", ",");
  }
}
