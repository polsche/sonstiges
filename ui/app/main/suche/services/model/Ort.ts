import { ISelectItem } from "./ISelectItem";

/**
 * Representation eines Ort
 */
export class Ort implements ISelectItem {
  constructor(public name?: string,
              public laengengrad?: string,
              public breitengrad?: string,
              public postleitzahl?: string) { }
  get label(): string {
    return this.name;
  }
  get value(): string {
    return this.postleitzahl;
  }
  get icon(): string {
    return "location";
  }
  get clickable(): boolean {
    return false;
  }
  get tooltip(): string {
    return null;
  }
}
