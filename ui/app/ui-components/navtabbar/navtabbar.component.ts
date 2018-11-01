import {StudisuConfig, getStudisuConfig} from "../../config/studisu.config";
import {Component} from "@angular/core";
import {NavtabbarItem} from "./navtabbar-item";

@Component({
  selector: "ba-studisu-navtabbar",
  templateUrl: "./navtabbar.component.html"
})
export class NavTabbarComponent {
    public linkListe: NavtabbarItem[];
    private widthStyleClass: string;
    private conf: StudisuConfig = getStudisuConfig();

    constructor() {
        this.linkListe = this.conf.navtabbarInformationFooter;
        if (typeof this.linkListe !== "undefined" && this.linkListe.length >= 1 && this.linkListe.length <= 4) {
            this.widthStyleClass = "elements" + this.linkListe.length;
        } else {
            this.widthStyleClass = "elements1";
        }
    }

}
