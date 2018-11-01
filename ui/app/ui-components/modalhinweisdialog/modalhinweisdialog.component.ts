import {Component, ViewChild, ElementRef} from "@angular/core";
import {HinweisModalData} from "./hinweismodalData";
import {Messages} from "../model/Messages";

declare var $: any; // bereits vorhandenes jQuery verwendbar machen

/**
 * Komponente zur Anzeige des Hinweis-Popups
 */
@Component({
  selector: "ba-studisu-modal-hinweisdialog",
  templateUrl: "./modalhinweisdialog.component.html"
})
export class ModalHinweisDialogComponent {

  // Die Modal-Dialog Komponente als Objekt
  @ViewChild("modalHinweisDialog")
  public modalDialog: ElementRef;

  // Message-Element im Template bereitstellen
  public messages = Messages;

  // Datenhalter für die Infoanzeige
  private _modalData: HinweisModalData;

  public showModalDialog(modalData: HinweisModalData) {
    if (modalData != null) {
      this._modalData = modalData;
      $(this.modalDialog.nativeElement).modal();
    }
  }

  get modalData() {
    return this._modalData;
  }
}
