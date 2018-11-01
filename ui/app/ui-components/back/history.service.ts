import { Injectable } from "@angular/core";
import { PlatformLocation } from "@angular/common";
import { Router, NavigationEnd } from "@angular/router";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import "rxjs/add/operator/filter";

@Injectable()
export class HistoryService {

    // Subject für die aktuelle Back-URL
    public backUrl: BehaviorSubject<string> = new BehaviorSubject<string>(null);

    // Stack von möglichen Back-URLs
    private stack = [];

    // Von diesen Pfaden soll es keine Zurück-Funktionalität geben.
    private roots = ["/", "/suche"];

    constructor(
        private router: Router,
        private platformLocation: PlatformLocation
    ) {

        // War die letzte Navigation auslösende Aktion ein Browser back oder forward?
        let pop = false;

        // Events kommen immer bei einem Browser back oder forward.
        platformLocation.onPopState(() => pop = true);

        this.router.events
            .filter(e => e instanceof NavigationEnd)
            .map(e => (<NavigationEnd>e).url)
            .subscribe((currentUrl) => {

                // Wenn ein Browser back passiert ist...
                if (pop && this.stack.length >= 2 && this.stack[this.stack.length - 2] === currentUrl) {
                    // ...müssen wir die beiden letzen Elemente aus dem Stack entfernen.
                    // Hier holen wir also nach, was durch die fehlende Ausführung von navigateBack() fehlt.
                    this.stack.pop();
                    this.stack.pop();
                }
                pop = false;

                let currentPath = currentUrl.split("?")[0];

                // Befinden wir uns auf einer Seite ohne Zurück-Funktionalität, können wir den Stack löschen
                if (this.roots.indexOf(currentPath) >= 0) {
                    this.stack = [];
                }

                // Nur wenn die Paths der jetzigen under der vorherigen Seite abweichen, speichern wir
                // beide URLs im Stack, sonst ersetzt die jetzige die vorherige URL.
                let previousUrl = this.stack.pop();
                if (previousUrl) {

                    let previousPath = previousUrl.split("?")[0];

                    if (currentPath !== previousPath) {
                        this.stack.push(previousUrl);

                        // backURL bekannt geben
                        this.backUrl.next(previousUrl);
                    }
                } else {
                    this.backUrl.next(null);
                }

                // Jetzige URL dem Stack hinzufügen
                this.stack.push(currentUrl);
             });
    }

    public navigateBack() {
        // Wir müssen die aktuelle und vorherige URL entfernen
        this.stack.pop();
        let backUrl = this.stack.pop();
        this.router.navigateByUrl(backUrl);
    }
}
