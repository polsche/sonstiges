import { LoggingService } from "../../services/logging.service";
import { Injectable } from "@angular/core";
import { UrlParamService } from "../../main/suche/services/url-param.service";
import { Subscription } from "rxjs/Subscription";
import { StudienfelderService } from "./services/studienfelder.service";
import { Studienfeld } from "./services/model/Studienfeld";
import { Studienfeldgruppe } from "./services/model/Studienfeldgruppe";
import { Studienfach } from "../suche/services/model/Studienfach";
import { Messages } from "../../ui-components/model/Messages";
import { StudisuErrorService } from "../../error/studisuerror.service";

/**
 * Das Delegate erlaubt das Laden und die Verwendung von Studienfeldgruppen und Studienfeldern.
 *
 * Die bereitgestellten Methoden können in den Livecycle-Hooks der nutzenden Komponenten
 * aufgerufen werden und erlauben so die Wiederverwendung des Codes dieses Delegates.
 *
 * Insbesondere wird die URL mit einer Auswahl von Studienfeldern synchron gehalten, was sowohl
 * auf der Sucheseite als auch auf der Seite zur Auswahl der Studienfelder verwendet wird.
 */
export const MAX_STUDIENBEREICH_PARAMS = 40;
@Injectable()
export class StudienfelderDelegate {

    public studienfeldgruppen: Studienfeldgruppe[] = [];
    public selektierteStudienfelder: Studienfeld[] = [];

    public selektierteStudienfaecher = [];

    public removeTooltip = Messages.TAG_STUDIENFELD_TOOLTIP_REMOVE;

    private studienfachParamsTotal: number;
    private urlParamSubscription: Subscription;
    private studienfeldSubscription: Subscription;

    /**
     * Constructor der Komponente mit injizierten Services.
     *
     * @param urlParamService Injected Service.
     * @param studienfelderService Injected Service.
     * @param errorDialogService Injected Service zur Fehlerhaltung
     * @param loggingService Injected Service.
     */
    constructor(
        private urlParamService: UrlParamService,
        private studienfelderService: StudienfelderService,
        private studisuErrorService: StudisuErrorService,
        private loggingService: LoggingService
    ) { }

    /**
     * Initialisierung der Komponente
     * Hier wird der zugehoerige Url Parameter ausgelesen und
     * verarbeitet und der Studienfeld-Service aufgerufen
     */
    public init() {
        // Die Studienfelder werden nur 1x geladen, daher am Ende gleich ein unsubscribe()!
        this.studienfeldSubscription = this.studienfelderService.getStudienfelder().subscribe(
            result => {
                // Laden der Studienfeldgruppen aus dem Ergebnis des Subscription.
                this.ladeStudienfeldgruppen(result);

                // Wir subscriben für URL-Änderungen, nachdem wir die Studienfeldgruppen geladen haben.
                // Dadurch brauchen wir keine Fallunterscheidung für Race-Conditions!
                this.urlParamSubscription = this.urlParamService.currentParams
                    .map(params => {
                        return {
                            studienfaecher: params.get(UrlParamService.PARAM_STUDIENFAECHER),
                            studienfelder: params.get(UrlParamService.PARAM_STUDIENFELDER)
                        };
                    })
                    .distinctUntilChanged()
                    .subscribe(studienfelder => this.parseUrlParams(studienfelder));
            },
            err =>  {
                // STUDISU-105 zeige Fehlerdialog
                this.studisuErrorService.pushError(Messages.FEHLER_LADEN_DATEN, err);
            }
        );
    }

    public get isMaxParameterCountExceeded() {
        return this.studienfachParamsTotal + this.selektierteStudienfelder.length >= MAX_STUDIENBEREICH_PARAMS;
        // eigentlich richtig waere unten stehender code, jedoch muss vorher noch das urlparameter laden in den delegate gebracht werden
        // da dieser sonst auf studienfelder auswahlseite die anzahl faecher nicht kennt.
        // return this.selektierteStudienfaecher.length + this.selektierteStudienfelder.length >= MAX_STUDIENBEREICH_PARAMS;
    }

    /**
     *  Objekt nimmt sich aus den Subscriptions raus
     */
    public destroy() {
        if (this.studienfeldSubscription) {
            this.studienfeldSubscription.unsubscribe();
        }
        if (this.urlParamSubscription) {
            this.urlParamSubscription.unsubscribe();
        }
    }

    /**
     * Aktualisiert die aktuelle Selektion von Studienfeldern mit dem UrlParamService.
     */
    public updateUrlParamFeld() {
        let urlParamFelder: string = this.selektierteStudienfelder
            // map / bilde ab von Studienfelder[] auf Array of Number[]
            .map((studienfeld: Studienfeld) => studienfeld.dkzIds)
            // reduziere Number[] dkzIds auf EIN Number[] ALLER dkzIds
            .reduce((previousValue: number[], dkzIds: number[]) => previousValue.concat(dkzIds), [])
            // join / stringconcateniere das Array aller dkzIds zu einem String mit angegebenem trennzeichen
            .join(UrlParamService.VALUE_SEPARATOR);

        this.urlParamService.updateView({ [UrlParamService.PARAM_STUDIENFELDER]: urlParamFelder, [UrlParamService.PARAM_PAGE]: null });
    }

    /**
     * Aktualisiert die aktuelle Selektion von Studienfaechern mit dem UrlParamService.
     * analog zu updateUrlParamFeld
     */
    public updateUrlParamFach() {
        let urlParamFaecher: string = this.selektierteStudienfaecher
            // map / bilde ab von Studienfach[] auf Number[]
            .map((studienfach: Studienfach) => studienfach.dkzId)
            // join / stringconcateniere das Array der dkzId zu einem String mit angegebenem trennzeichen
            .join(UrlParamService.VALUE_SEPARATOR);

        this.urlParamService.updateView({ [UrlParamService.PARAM_STUDIENFAECHER]: urlParamFaecher, [UrlParamService.PARAM_PAGE]: null });
    }

    public clickHandler(item: any) {
        if (item && item instanceof Studienfeld && item.clickable) {
            this.gotoStudienfeld(item);
        } else if (item && item instanceof Studienfach && item.clickable) {
            this.gotoStudienfach(item);
        }
    }

    public gotoStudienfeld(studienfeld: Studienfeld) {
        this.urlParamService.navigateTo("studienfeldinfo",
            {
                [UrlParamService.PARAM_STUDIENFELDER]: studienfeld.dkzIds.join(UrlParamService.VALUE_SEPARATOR),
                [UrlParamService.PARAM_PAGE]: null
            });
    }

    /**
     * De-selektiert ein Studienfeld oder -fach.
     *
     * @param item Das zu entfernende Item.
     */
    public remove(item: any) {
        if (item instanceof Studienfach) {
            this.removeFach(item);
        } else {
            this.removeFeld(item);
        }
    }

    // Beim Click auf ein Tag mit Studienfach, wird diese Methode aufgerufen
    // Zum ausgewählten Studienfach, werden Studienfelder über ein Service ermittelt und
    // auf die Studienfeld-Info Seite umgeleitet
    private gotoStudienfach(studienfach: Studienfach) {

        let studienfachDkzId = studienfach.dkzId;
        let studienbereichDkzIds = this.studienfelderService.getStudienfelderForStudienfach(studienfachDkzId);
        studienbereichDkzIds.subscribe(x => {
            this.urlParamService.navigateTo("studienfeldinfo",
                {
                    [UrlParamService.PARAM_STUDIENFELDER]: x.join(UrlParamService.VALUE_SEPARATOR),
                    [UrlParamService.PARAM_STUDIENFAECHER]: studienfachDkzId,
                    [UrlParamService.PARAM_PAGE]: null
                });
        });
    }

    /**
     * Callback u.a. für die Checkbox, selektiert oder de-selektiert ein Studienfeld.
     *
     * @param studienfeld Das umzuschaltende Studienfeld
     */
    private removeFeld(studienfeld: Studienfeld) {
        let index = this.selektierteStudienfelder.indexOf(studienfeld);
        if (index < 0) {
            this.selektierteStudienfelder.push(studienfeld);
        } else {
            this.selektierteStudienfelder.splice(index, 1);
        }

        // STUDISU-45
        // falls der letzte studienbereich geloescht wurde,
        // sollen die selektierten facettenfilter verworfen werden
        if (this.selektierteStudienfelder.length + this.selektierteStudienfaecher.length === 0) {
            this.urlParamService.resetFilter();
        }

        this.updateUrlParamFeld();
    }

    /**
     * Callback u.a. für die Checkbox, selektiert oder de-selektiert ein Studienfach.
     *
     * @param fach Das umzuschaltende Studienfach
     */
    private removeFach(fach: Studienfach) {
        let index = this.selektierteStudienfaecher.indexOf(fach);
        if (index < 0) {
            this.selektierteStudienfaecher.push(fach);
        } else {
            this.selektierteStudienfaecher.splice(index, 1);
        }

        // STUDISU-45
        // falls der letzte studienbereich geloescht wurde,
        // sollen die selektierten facettenfilter verworfen werden
        if (this.selektierteStudienfelder.length + this.selektierteStudienfaecher.length === 0) {
            this.urlParamService.resetFilter();
        }

        this.updateUrlParamFach();
    }

    /**
     * Lädt die Studienfeldgruppen und fügt die Icons hinzu.
     */
    private ladeStudienfeldgruppen(studienfeldgruppen: Studienfeldgruppe[]) {
        this.studienfeldgruppen = studienfeldgruppen
            .map(studienfeldgruppe => {
                studienfeldgruppe.icon = "HA" + studienfeldgruppe.key;
                return studienfeldgruppe;
            });
    }

    /**
     * Helper-Funktion zum Parsen der Studienfeldgruppen aus der Url.
     *
     * @param studienfelderParams QueryParams
     */
    private parseUrlParams(studienfelderParams: any) {
        let studienfelderParamValue: string = studienfelderParams.studienfelder;
        let studienfaecherParamValue: string = studienfelderParams.studienfaecher;

        //    Faecher sfa
        this.studienfachParamsTotal = (studienfaecherParamValue
            ? studienfaecherParamValue.split(UrlParamService.VALUE_SEPARATOR).length
            : 0);

        //    Felder sfe
        let studienfelderParamValues: string[] = (studienfelderParamValue
            ? studienfelderParamValue.split(UrlParamService.VALUE_SEPARATOR)
            : []);

        // Genau die Studienfelder in die Selektion, die in der URL vorkommen.
        this.selektierteStudienfelder = this.studienfeldgruppen
            .map((studienfeldgruppen: Studienfeldgruppe) => studienfeldgruppen.studienfelder)
            .reduce((previousValue: Studienfeld[], currentValue: Studienfeld[]) => previousValue.concat(currentValue))
            .filter((studienfeld: Studienfeld) => {
                let res = studienfeld.dkzIds.filter(
                    (dkzId: number) => studienfelderParamValues.indexOf("" + dkzId) > -1).length;
                return res > 0;
            });

        // STUDISU-92:
        // Bei über 40 sfe und/oder sfa Parametern in der Url
        // sind beim Verarbeiten der Parameter zuerst die Studienfaecher
        // und erst anschliessend Studienfelder abzuschneiden.
        //
        // Herausforderung in der implementierung ist (noch),
        // dass delegate und delegierender sich jeweils auf eigene urlparams beziehen,
        // die hier aber kreuzvalidiert werden muessen
        if (this.selektierteStudienfelder.length > MAX_STUDIENBEREICH_PARAMS) {
            this.selektierteStudienfelder = this.selektierteStudienfelder.slice(0, MAX_STUDIENBEREICH_PARAMS);
            this.loggingService.debug("studienfelderdelegate.parseUrlParams() begrenzt studienfelder aus url");
        }

        let anzahlSelektierteStudienbereiche = this.selektierteStudienfaecher.length +
                                                this.selektierteStudienfelder.length;
        if (anzahlSelektierteStudienbereiche > MAX_STUDIENBEREICH_PARAMS) {
            let anzahlFuerStudienfaecher = MAX_STUDIENBEREICH_PARAMS - this.selektierteStudienfelder.length;
            this.selektierteStudienfaecher = this.selektierteStudienfaecher.slice(0, anzahlFuerStudienfaecher);
            this.loggingService.debug("studienfelderdelegate.parseUrlParams() begrenzt studienfaecher aus url");
        }
    }
}
