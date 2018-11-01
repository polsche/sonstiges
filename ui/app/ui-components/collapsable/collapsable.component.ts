/**
 * TODO:
 * Pruefen, ob es eine bessere Loesung fuer die imports gibt.
 * Quelle:
 * https://stackoverflow.com/questions/37074485/angular2-rxjs-missing-observable-interval-method
 */
import { Observable } from "rxjs/Observable";
import "rxjs/add/observable/interval";
import "rxjs/add/operator/take";
import { Subscription } from "rxjs/Subscription";
import { OnDestroy, ViewChild } from "@angular/core";
/**
 * Komponente um Content mittels label ein-/ausklappbar zu gestalten.
 * Ähnlich einem Accordion.
 * Komponente blendet Inhalt nur fuer sehenden Benutzer aus.
 **/
import { Component, ElementRef, Input, OnInit } from "@angular/core";

@Component({
    selector: "ba-studisu-collapsable",
    templateUrl: "./collapsable.component.html"
})
export class CollapsableComponent implements OnInit, OnDestroy {
    @Input()
    public id: string;
    @Input()
    public disclosed = false;
    @Input()
    public labelCollapsed = "einblenden";
    @Input()
    public labelDisclosed = "ausblenden";

    public class: any;

    public currentHeight: string;
    public currentVisibility: string;
    /**
     * must be set in css vice versa
     */
    private animationTimeMillis = 400;
    private delaySubscription: Subscription;


    @ViewChild("collapsableArea")
    private containerElement: ElementRef;

    public ngOnInit() {
        this.updateCurrentHeightNVisibility();
    }
    public ngOnDestroy() {
        if (this.delaySubscription) {
            this.delaySubscription.unsubscribe();
        }
    }

    public toggle() {
        this.disclosed = this.disclosed ? false : true;
        this.updateCurrentHeightNVisibility();
    }

    private getChildHeight(): number {
        return this.containerElement.nativeElement.offsetHeight;
    }

    /**
     * Updates the height of the container div accordingly to the
     * toggle state of this component. Furthermore it takes care
     * of transition the height from 0 to auto and vice versa.
     */
    private updateCurrentHeightNVisibility() {
        if (this.disclosed) {
            this.currentHeight = this.getChildHeight() + "px";
            this.currentVisibility = "visible";
            this.updateHeightToAuto(this.animationTimeMillis);
        } else {
            if (this.delaySubscription) {
                //    cancel active callback, so the height is not
                //    set to auto
                this.delaySubscription.unsubscribe();
            }
            //    first set the height back from auto to its actual height
            //    so that the transtition is smooth
            this.currentHeight = this.getChildHeight() + "px";
            //    delay height set to 0 so it acutally will be animated
            //    (setting it directly to 0 would omit the previous call)
            setTimeout(() => {
            this.currentHeight = "0px";
                this.currentVisibility = "hidden";
            }, 0);
        }
    }

    private updateHeightToAuto(delay: number) {
        this.delaySubscription = Observable.interval(delay)
            .take(1)
            .subscribe(i => {
                this.currentHeight = "auto";
            });
    }
}
