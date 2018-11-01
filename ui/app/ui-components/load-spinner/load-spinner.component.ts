import {ChangeDetectionStrategy, Component, Input} from "@angular/core";

/**
 * Komponente für die Darstellung eines Lade-Spinners.
 *
 * Der externen Box kann eine Klasse hinzugefügt und die Größe des Spinners (<= 100) kann definiert werden.
 *
 * Created by schneidek084 on 23.08.2017.
 */
@Component({
    changeDetection: ChangeDetectionStrategy.Default,
    selector: "ba-studisu-load-spinner",
    templateUrl: "./load-spinner.component.html"
})
export class LoadSpinnerComponent {
    @Input()
    public size = 33;
    @Input()
    public class = "";
}
