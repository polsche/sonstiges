import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { Subscription } from "rxjs/Subscription";
import { HistoryService } from "./history.service";

@Component({
    selector: "ba-studisu-back",
    templateUrl: "./back.component.html"
})
export class BackComponent implements OnInit, OnDestroy {

    @Input()
    public defaultUrl;

    public backUrl;
    private linkText;
    private subscription: Subscription;

    constructor(private historyService: HistoryService) {
    }

    /**
     * Setzt die backUrl aus der History. Falls dort kein Eintrag vorhanden ist,
     * wird eine evtl. vorhandene defaultUrl verwendet.
     */
    public ngOnInit() {
        this.subscription = this.historyService.backUrl.subscribe(
            (backUrl) => {
                if (backUrl != null) {
                    this.backUrl = backUrl;
                } else {
                    this.backUrl = this.defaultUrl;
                }
                this.linkText = this.createLinkText(this.backUrl);
            });
    }

    public ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    public navigateBack() {
        this.historyService.navigateBack();
        return false;
    }

    private createLinkText(backUrl: string) {
        let linkText = "zurück";
        let path = (backUrl || "").split("?")[0];
        if (path === "/" || path === "/suche" || path === "suche") {
            linkText += " zur Suche";
        } else if (path === "/studienfelder") {
            linkText += " zur Studienfeldauswahl";
        }
        return linkText;
    }
}
