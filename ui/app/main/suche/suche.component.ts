import {
  Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef,
  ChangeDetectionStrategy
} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {StudienangeboteService} from "../../services/studienangebote.service";
import {Subscription} from "rxjs/Subscription";
import {Observable} from "rxjs/Observable";
import {SearchResult} from "../../model/SearchResult";
import {LoggingService} from "../../services/logging.service";
import {EventService} from "../../services/event.service";
import {StudisuErrorService} from "../../error/studisuerror.service";
import {UrlParamService} from "./services/url-param.service";
import {Messages} from "../../ui-components/model/Messages";
import {LiveAnnouncer} from "../../services/live-announcer";

@Component({
    changeDetection: ChangeDetectionStrategy.Default,
    providers: [
        StudienangeboteService,
    ],
    selector: "ba-studisu-suche",
    templateUrl: "./suche.component.html"
})
export class SucheComponent implements OnInit, AfterViewInit, OnDestroy {
    public searchResult: SearchResult;
   @ViewChild("ueberschrift") private element: ElementRef;

    private urlSubscription: Subscription;

    private RELOAD_PARAMETERS = [
        UrlParamService.PARAM_HOCHSCHULART,
        UrlParamService.PARAM_ORTE,
        UrlParamService.PARAM_STUDIENFAECHER,
        UrlParamService.PARAM_STUDIENFELDER,
        UrlParamService.PARAM_STUDIENFORMEN,
        UrlParamService.PARAM_FFSTUDIUM,
        UrlParamService.PARAM_STUDT,
        UrlParamService.PARAM_UK,
        UrlParamService.PARAM_REGION
    ];

    /**
     * @param route
     * @param studienangeboteService Injected Service
     * @param logger Injected Service fuer Logausgaben
     * @param eventService Injected Service fuer Ladeanzeige
     * @param studisuErrorService Injected Service zur Fehlerhaltung
     */
    constructor(private route: ActivatedRoute,
        private studienangeboteService: StudienangeboteService,
        private urlParamService: UrlParamService,
        private logger: LoggingService,
        private eventService: EventService,
        private studisuErrorService: StudisuErrorService,
        private announcer: LiveAnnouncer
    ) { }


    public ngOnInit() {
        // STUDISU-105: falls kein Studienfeld oder -fach in URL enthalten, kein Serverzugriff
        this.announcer.announce("Studiensuche Seite geladen");
        this.urlSubscription = null;
        let previous: string = null;
        this.urlSubscription = this.route
            .queryParams
            .filter(params => {
                let clone = {};
                for (let reloadParam of this.RELOAD_PARAMETERS) {
                    if (params.hasOwnProperty(reloadParam)) {
                        clone[reloadParam] = params[reloadParam];
                    }
                }
                let json = JSON.stringify(clone);

                let ret = false;
                // Nur wenn sich ein relevanter Parameter geändert hat, laden wir neu.
                if (previous === null || previous !== json) {
                    ret = true;
                }

                previous = json;

                return ret;
            })
            /**
             * SwitchMap fuehrt dazu, dass vor dem Call zum Service der vorherige Call zum Service (falls vorhanden)
             * gecancelt wird. Dadurch kann es nicht passierern, dass ein langsamer erster Call die Antwort des
             * schnelleren zweiten Calls wieder ueberschreibt.
             */
            .switchMap(params => {
                if (!this.istSucheGewuenscht(params)) {
                    return Observable.of(new SearchResult());
                }
                if (this.searchResult == null) {
                    this.searchResult = new SearchResult();
                }
                this.eventService.studienangeboteSucheStatus.next(true);
                return this.studienangeboteService.getStudienangebote()

                    /**
                     * Hier fangen wir auftretende Fehler des Service-Calls und behandeln sie entsprechend.
                     * Würden wir die Fehlerbehandlung nicht hier sondern im subscribe machen, wuerde ein Fehler dazu
                     * fuehren, dass die gesamte Subscription - also die auf die Route - gecancelt wuerde. Das wollen
                     * wir nicht, da die Komponente dann keine Aenderungen an der URL mehr behandeln wuerde.
                     */
                    .catch(err => {
                        this.logger.error("Service Request Failed with error: " + err, this);
                        this.eventService.studienangeboteSucheStatus.next(err);
                        // STUDISU-105 zeige Fehlerdialog
                        this.studisuErrorService.pushError(Messages.FEHLER_LADEN_DATEN, err);
                        return Observable.of(new SearchResult());
                    });
            }).subscribe(searchResult => {
                this.searchResult = (<SearchResult>searchResult);
                this.eventService.studienangeboteSucheStatus.next(false);
            });
       /* }*/
        window.scrollTo(0, 0);
    }

    public ngAfterViewInit() {
        this.element.nativeElement.focus();
    }

    public ngOnDestroy() {
        this.urlSubscription.unsubscribe();
    }

    /**
     * gibt an, ob stoeberwelten oder facette und ergebnisliste angezeigt werden sollen
     */
    public showStoeberwelten(): boolean {
        // 1. Ansatz fuehrte zur Anzeige waehrend die Suche noch ausgefuehrt wird, was aber ungewollt ist
        // stoeberwelten genau dann, wenn keine suche ausgefuehrt wurde
        // return !this.searchResult  || !this.searchResult.hasSearched;

        // 2. Ansatz
        // stoeberwelten genau dann, wenn suche nicht gewuenscht ist :)
        // dazu muessen wir uns die aktuellen parameter holen
        // und damit nachfragen ob hier eine suche gewuenscht war
        return !this.istSucheGewuenscht(this.urlParamService.getQueryParams());
    }

    /**
     * gibt zurueck, ob eine Suche nach Studienangeboten ausgefuehrt werden soll
     * Dies ist der Fall, wenn weder ein Studienfeld- noch ein Studienfach-URL-Parameter gesetzt ist
     * @param params auszuwertende .URL-Params
     * @return ob eine Suche nach Studienangeboten ausgefuehrt werden soll
     */
    private istSucheGewuenscht(params) {
        let sucheGewuenscht = false;
        if (params[UrlParamService.PARAM_STUDIENFELDER] != null
            || params[UrlParamService.PARAM_STUDIENFAECHER] != null) {
            sucheGewuenscht = true;
        }
        return sucheGewuenscht;
    }
}
