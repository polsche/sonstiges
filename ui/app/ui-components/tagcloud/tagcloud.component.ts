import { ISelectItem } from "../../main/suche/services/model/ISelectItem";
import { Component, Input, Output, EventEmitter } from "@angular/core";
import { UIConstants } from "../UIConstants";

/**
 * Komponente für die Darstellung einer Tag-Cloud.
 *
 * Das Array mit den Tag-Daten wird in "tagCloudListe" übergeben und über "removeTemplate" kann optional
 * ein Template für die Generierung der Item-Tooltipps vorgegeben werden, bei dem der String "%name%" durch
 * den Namen des Items (bei Strings: das Item selbst) ersetzt wird.
 *
 * Created by sprengerc001 on 04.04.2017.
 * Updated by schneidek084 on 24.03.2017.
 * Updated by weigelt005 on 20.06.2017, pushed by kunzmannc on 11.07.2017.
 * @version STUDISU-139 maximale Laenge der angezeigten Bezeichnung 60 Zeichen
 */
@Component({
    selector: "ba-studisu-tagcloud",
    templateUrl: "./tagcloud.component.html"
})
export class TagcloudComponent {
    @Input()
    public srListTitle = "";
    @Input()
    public tagcloudListe: (ISelectItem)[];
    @Input()
    public removeTemplate = "";
    @Input()
    public maxLength = UIConstants.TAGCLOUD_BEZEICHNUNG_MAXLENGTH_DEFAULT;
    @Input()
    public maxTagsVisible = -1;

    @Output()
    public tagcloudListeRemove = new EventEmitter<ISelectItem>();
    @Output()
    public tagcloudListeClick = new EventEmitter<ISelectItem>();

    /**
     * Returns the number parameter of items which are displayed by default.
     * if maxTagsVisible is not set (which means it should be -1) then
     * the total list size of tags is returned, the maxTagsVisible number otherwise.
     */
    public getVisibleItemCount() {
        if (this.maxTagsVisible > -1) {
            return this.maxTagsVisible;
        } else {
            return this.tagcloudListe ? this.tagcloudListe.length : 0;
        }
    }

    /**
     * should the item be a link?
     * depends on the items clickable value
     *
     * @param {ISelectItem} item to be visualized
     * @return boolean if the item is a link
     */
    private noLink(item: ISelectItem) {
        return !item.clickable;
    }

    /**
     * emit model change event
     * @param item Das zu entfernende Element.
     */
    private onRemove(item: ISelectItem) {
        this.tagcloudListeRemove.emit(item);
    }

    /**
     * emit model change event
     * @param index Das zu aufzurufende Element.
     */
    private onClick(item) {
        this.tagcloudListeClick.emit(item);
    }

    /**
     * Gibt entweder das Array Element (wenn es ein String ist) oder dessen 'name'-Attribut zurück.
     * @param item Das auszugebende Array-Element.
     */
    private _getName(item): string {
        return (typeof (item) === "string") ? item : item.name;
    }

    /**
     * Kuerzt die uebergebene Zeichenkette auf maximale Laenge-3 + drei Punkte, falls sie laenger als der Maximalwert ist
     * und gibt den laengenkonformen Wert zurueck.
     * @param item Das auszugebende Array-Element.
     * @version STUDISU-139 auf maxLength-1 gekuerzt, da noch Dreipunktezeichen angehaengt wird
     * @version STUDISU-204 wenn jemand einen Link, als Tagnamen einschmuggeln sollte, wird das Anzeigne verhindert
     *      indem '<' und '>' aus dem String entfernt werden
     */
    private getName(item): string {
        let name = this._getName(item).replace(">", "").replace("<", "");
        if (name && (name.length > this.maxLength)) {
            name = name.substr(0, this.maxLength - 3) + UIConstants.ZEICHEN_WEITERE;
        }
        return name;
    }

    /**
     * shift the index by the amount of max tags visible
     * used to continue index after
     * @param {number} index
     * @returns {number}
     */
    private shiftedIndex(index: number): number {
        // typescript sucks! without this fake number operations we get stringconcatenation here 3 + 1 = 31...
        return (this.maxTagsVisible - 0) + (index - 0);
    }

    /**
     * Gibt den Tooltipp-Text für den Namen auf Basis des übergebenen Templates zurück.
     *@param item Das auszugebende Array-Element.
     */
    private getClickTooltip(item): string {
        if (!item || !item.tooltip) {
            return null;
        }
        return item.tooltip;
    }

    /**
     * Gibt den Tooltipp-Text für das Entfernen-X auf Basis des übergebenen Templates zurück.
     *@param item Das auszugebende Array-Element.
     */
    private getRemoveTooltip(item): string {
        if (!this.removeTemplate || this.removeTemplate.length === 0) {
            return null;
        }
        return this.removeTemplate.replace("%name%", this._getName(item));
    }
}
