import {Component, Input, ViewChild, ElementRef} from "@angular/core";
import {ModalData} from "./modalData";
import {Messages} from "../model/Messages";

declare var $: any; // bereits vorhandenes jQuery verwendbar machen

/**
 * Komponente zur Anzeige des Fehler-Popups
 */
@Component({
  selector: "ba-studisu-modal-dialog",
  templateUrl: "./modaldialog.component.html"
})
export class ModalDialogComponent {

  @Input()
  public errorModal: Boolean;

  // Die Modal-Dialog Komponente als Objekt
  @ViewChild("modalDialog")
  public modalDialog: ElementRef;

  // Message-Element im Template bereitstellen
  public messages = Messages;

  // Datenhalter für die Infoanzeige
  private _modalData: ModalData;

  /**
   * Lädt die aktuelle Seite neu
   */
  public reloadPage(): void {
    window.location.reload(true);
  }

  @Input() set modalData(modalData: ModalData) {
    if (modalData != null) {
      this._modalData = modalData;
      $(this.modalDialog.nativeElement).modal();
    }
  }

  get modalData() {
    return this._modalData;
  }
}
