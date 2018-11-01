
export class ExternalLink {
  public name: string;
  public link: string;
  public tooltip: string;

  /**
   * ctor from json/js obj
   * @param jsobj json/js obj
   */
  constructor(jsobj) {
    this.name = jsobj.name;
    this.link = jsobj.link;
    this.tooltip = jsobj.tooltip;
  }
}
