import { Pipe, PipeTransform } from "@angular/core";
import { UIConstants } from "../ui-components/UIConstants";

/**
 * Pipe zum Kuerzen einer Textausgabe auf eine bestimmte Laenge.
 * Ist der Text laenger als der zulaessige Maximalwert, wird der Text auf Maximalwert-3 abgeschnitten
 * und es werden drei Punkte hinzugefuegt.
 * Die maximale Laenge kann in der html-Seite uebergeben werden: | sgMaxLength:<Laenge>
 * Wird kein Wert uebergeben, dann verwendet die Pipe den Defaultwert:  | sgMaxLength
 * @since STUDISU-139  maximale Laenge der Beschriftung auf Tagcloud-Elementen und Ergebnislisten-Tags
 */
@Pipe({
    name: "sgMaxLength"
})
export class MaxLength implements PipeTransform {

    /**
     * kuerzt den uebergebenen Text entsprechend den im Klassenkommentar beschriebenen Vorgaben
     * @param bezeichnung zu formatierender Text
     * @param length maximale Anzahl Zeichen; falls nicht angegeben, wird der Defaultwert verwendet
     */
    public transform(bezeichnung: string, length: number) {
        let formatierterText: string;
        if (length != null) {
            formatierterText = this.kuerzeAufMaxLength(bezeichnung, length);
        } else {
            formatierterText = this.kuerzeAufMaxLength(bezeichnung, UIConstants.TAG_ERGEBNISLISTE_BEZEICHNUNG_MAXLENGTH);
        }
        return formatierterText;
    }

    /**
     * kuerzt den uebergebenen Text auf die uebergebene Laenge-3 + drei Punkte,
     * falls er laenger als die maximale Laenge ist
     * @param text zu pruefender und ggf. zu kuerzender Text
     * @param length maximale Zeichenanzahl
     */
    private kuerzeAufMaxLength(text: string, length: number) {
        let maxLengthText;
        if (text.length > length) {
            maxLengthText = text.substr(0, length - 3) + UIConstants.ZEICHEN_WEITERE;
        } else {
            maxLengthText = text;
        }
        return maxLengthText;
    }
}
