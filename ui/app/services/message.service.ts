import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { HinweisModalData } from "../ui-components/modalhinweisdialog/hinweismodalData";


/**
 * Serviceklasse für die Kommunikation zwischen Main-Komponente und Dialogkomponenten
 */

@Injectable()
export class MessageService {
    /**
     * BehaviorSubject mit Informationen zum Anzeigen von Informationen
     */
    public showHinweisDialog: BehaviorSubject<HinweisModalData> = new BehaviorSubject(null);
}
