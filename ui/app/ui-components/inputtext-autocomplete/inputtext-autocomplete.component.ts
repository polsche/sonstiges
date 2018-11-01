import {
  Component, Input, Output, EventEmitter, HostListener, ElementRef, OnInit, OnDestroy, ViewChild
} from "@angular/core";
import {Subject} from "rxjs/Subject";
import {Subscription} from "rxjs/Subscription";
import {SelectItem} from "../../ui-components/model/SelectItem";
import {Observable} from "rxjs/Observable";

const KEY_CODE_TAB = 9;
const KEY_CODE_ENTER = 13;
const KEY_CODE_ARROW_UP = 38;
const KEY_CODE_ARROW_DOWN = 40;
const KEY_CODE_ARROW_LEFT = 37;
const KEY_CODE_ARROW_RIGHT = 39;
const KEY_CODE_PG_UP = 33;
const KEY_CODE_PG_DOWN = 34;
const KEY_CODE_ESC = 27;
const KEY_CODE_SHIFT = 16;

@Component({
  selector: "ba-studisu-inputtext-autocomplete",
  templateUrl: "./inputtext-autocomplete.component.html"
})
export class InputtextAutocompleteComponent implements OnInit, OnDestroy {
  @Input()
  public model = "";

  @Input()
  set suggestionOracle(observable: Observable<SelectItem[]>) {
    this.oracleSubscription = observable.subscribe(
      result => {
        if (this.submitPending) {                   // Die Übergabe des ermittelten Wertes steht noch an?
          this.doSubmit();                          // dann ggf. Auswahl übermitteln
        }
        this.oraclePending = false;                   // Autocomplete-Liste ist bearbeitet
        this.suggestionList = [];                     // Liste erst mal leer
        if (result.length > 0) {
          this.suggestionList = result;               // ggf. vorhandene Ergebnisse (inkl. Error-States) übernehmen
          this.showAutocomplete = this.hasFocus;    // andernfalls ggf. Drop-Down öffnen
        }
      }
    );
  }

  @Input()
  public name: string;
  @Input()
  public ariaLabel: string;
  @Input()
  public id: string;
  @Input()
  public placeholder: string;
  @Input()
  public placeholderDisabled: string;
  @Input()
  public styleClass: string;
  @Input()
  public disabled = false;
  //  Default debounce time is 200ms but can be overriden by parent component
  @Input()
  public debounceTime = 200;

  @Output()
  public onSubmit = new EventEmitter<SelectItem>();
  @Output()
  public modelChange = new EventEmitter<string>();

  public onTypeSubject = new Subject<Event>();
  public onTypeSubscription: Subscription;
  public showAutocomplete = false;
  public suggestionList: SelectItem[] = [];

  @ViewChild("autocompleteContainer")
  private autocompleteContainer: ElementRef;
  // Initialer Wert des Autocomplete-Index: kein Element gewählt (-1)
  private itemIndex = -1;

  private hasFocus = false;

  private oracleSubscription: Subscription;

  // Vom Benutzer eingegebener Wert; wird gespeichert, damit dieser bei KeyUp wieder hergestellt werden kann
  private originalInput: string;

  // Hilfs-Flag zur korrekten Markierung der Autocomplete-Liste
  private _mouseMoved: Boolean = false;

  // Hilfs-Flag zur Markierung einer ausstehenden Änderung der Autocomplete-Liste.
  private oraclePending = false;

  // Hilfs-Flag zur Markierung eines ausstehenden Submits, während eine Autocomplete-Liste gerade geladen wird.
  private submitPending = false;

  constructor(private elementRef: ElementRef) {
  }

  /**
   * Setzt die Styleklasse, falls das Element disabled ist.
   * @returns {string|string}
   */
  public disabledStyleClass(): string {
    return this.disabled === true ? "disabledStyleClass" : "";
  }

  public getInputPlaceholder(): string {
    if (this.disabled) {
      return this.placeholderDisabled;
    }
    return this.placeholder;
  }

  /**
   * Initialize component
   */
  public ngOnInit() {
    this.onTypeSubscription = this.onTypeSubject.distinctUntilChanged()
      .debounceTime(this.debounceTime)
      .subscribe(event => {
        /**
         * Bei einem Esc beenden wir die Verarbeitung, zeigen das Autocomplete nicht an und setzen den
         * Index des ausgewählten Elements auf -1, damit, falls der Benutzer nach dem Escape ein Tab drückt, nicht das zuletzt
         * gewählte Element übernommen wird.
         */
        if (event instanceof KeyboardEvent) {
          if (event.keyCode === KEY_CODE_ESC) {
            this.showAutocomplete = false;
            // Zurücksetzen des selektierten-Element-Index sowie einfügen des original Eingabefeld-Wertes
            this.itemIndex = -1;
            this.model = this.originalInput;
            return;
          }
          // Falls der Benutzer durch das Autocomplete navigiert, wird durch die Übernahme des selektierten
          // Textes durch die Pfeilhoch-runter-Tasten keine neue Suche ausgeführt, damit die ursprünglichen
          // Autocomplete-Werte erhalten bleiben.
          if (event.keyCode === KEY_CODE_ARROW_UP
            || event.keyCode === KEY_CODE_ARROW_DOWN
            || event.keyCode === KEY_CODE_PG_UP
            || event.keyCode === KEY_CODE_PG_DOWN
            || event.keyCode === KEY_CODE_ARROW_LEFT
            || event.keyCode === KEY_CODE_ARROW_RIGHT
            ) {
            return;
          }
        }
        // emit change
        this.notifyOracleOfModelChange();
        this.showAutocomplete = this.hasFocus;
      });
  }

  /**
   * cleanup
   */
  public ngOnDestroy() {
    this.onTypeSubscription.unsubscribe();
    this.oracleSubscription.unsubscribe();
  }

  /**
   * Aktionen, die beim Fokussieren des Eingabefeldes durchgeführt werden.
   */
  public onFocus() {
    if (!this.hasFocus) {
      this.submitPending = false;
      this.originalInput = this.model;
    }
    this.hasFocus = true; // Status des Fokus merken
    this.showAutocomplete = this.model.length > 0;

    // Scrollt zur passenden Position im Autocomplete, nachdem dieses wirklich vorhanden ist.
    if (this.showAutocomplete) {
      if (this.suggestionList.length > 0) {
        this.callAfterAutocompleteExists(() => this.scrollToView(this.itemIndex));
      } else {
        this.notifyOracleOfModelChange();
      }
    }
  }

  /**
   * If specific keys are pressed the input control should submit its current value
   * @param event KeyboardEvent
   */
  public onKeyDown(event: KeyboardEvent) {
    this._mouseMoved = false;
    switch (event.keyCode) {

      case KEY_CODE_TAB:
      case KEY_CODE_ENTER:
        this.submitPending = true; // vor dem Test auf Oracle pending, damit das Submit keinesfalls verloren geht!
        if (!this.oraclePending) {

          if (this.itemIndex > -1 || this.model === this.originalInput) {
            this.doSubmit();
          } else {
            this.notifyOracleOfModelChange();
          }
        }
        this.hasFocus = event.keyCode === KEY_CODE_ENTER; // keep focus for enter only.
        break;

      case KEY_CODE_ARROW_UP:
        this.handleItemChange(event, -1);
        break;
      case KEY_CODE_ARROW_DOWN:
        this.handleItemChange(event, +1);
        break;
      case KEY_CODE_PG_UP:
        this.handleItemChange(event, -6);
        break;
      case KEY_CODE_PG_DOWN:
        this.handleItemChange(event, +6);
        break;
    }
  }

  /**
   * Setzt das von der Maus selektierte Element als das momentan
   * ausgewählte Element
   * @param event
   */
  public select(event: any, index: number) {
    if (this._mouseMoved && this.itemIndex !== index) {
      this.itemIndex = index;
    }
  }

  /**
   * Auswahl oder vergleichbare Aktion, die tendenziell eine
   * Suche anstoßen wuerde, wird ueber diese Function gehandelt.
   * Das entsprechende Event wird als Event an die naechst hoehere Komponente
   * weitergereicht und nicht hier behandelt.
   */
  public onDropdownAuswahl(item: SelectItem) {
    if (item.key !== "err") {
      this.model = item.label;
      this.doSubmit();
    }
    this.showAutocomplete = false;
  }

  /**
   * Handles change of the currently selected element according to the given index offset.
   *
   * Also prevents the default function for the event in order to not change cursor position within the input field.
   *
   * @param {KeyboardEvent} event
   * @param {number} offset
   */
  private handleItemChange(event: KeyboardEvent, offset: number): void {
    // Verhindert Cursor-Bewegungen innerhalb des Eingabefeldes.
    event.preventDefault();

    // Abbruchbedingung: Falls das gewaehlte Element die Fehlermeldung der Validierung ist,
    // wird der Wert (natürlich) nicht in das Autocomplete uebernommen
    if (this.suggestionList.length === 0 || this.suggestionList[0].key === "err") {
      return;
    }

    let nextIndex = this.itemIndex + offset;
    let maxIndex = this.suggestionList.length - 1;

    // Wenn der Benutzer durch KeyUp/PgUp! über das 1. Element hinweg geht, dann wird der ursprünglich
    // eingegebene Text angezeigt (wie bei Google).
    if (this.itemIndex > -1 && (nextIndex < 0 || nextIndex > maxIndex || (this.itemIndex === maxIndex && offset > 0))) {
      this.itemIndex = -1;
      this.model = this.originalInput;
      return;
    }

    if (this.itemIndex === -1 && offset < 0) {
      this.itemIndex = maxIndex;
    } else {
      this.itemIndex = Math.max(0, Math.min(maxIndex, nextIndex));
    }

    // Dropdown ggf. so scrollen, dass das aktuelle Item sichtbar wird.
    this.scrollToView(this.itemIndex);

    // Übernahme des selektierten Textes in das Eingabefeld
    this.model = this.suggestionList[this.itemIndex].label || "";
  }

  /**
   * This will handle the onblur event for this component.
   * Currently this component handles the event directly. It will submit
   * the current value, if a click is made outside of this component.
   * Behaviour:
   *  - if the autocomplete dropdown is visible, it will be closed
   *      and the input receives focus
   *  - if no dropdown is showing the value will simply be submitted
   *      and the parent component has to handle the input
   *
   * => The behaviour might change as me might want to achieve a comparable
   *      behaviour across all apok application (compare BEN and )
   * @param event
   */
  @HostListener("document:click", ["$event"])
  private onDocumentClick(event: Event) {
    if (this.elementRef.nativeElement.contains(event.target)) {
      // Klick ins Eingabefeld öffnet ggf. Autocomplete
      this.showAutocomplete = true;

    } else if (this.autocompleteContainer && !this.autocompleteContainer.nativeElement.contains(event.target)) {
      // Klick außerhalb des Autocomplete schließt geöffnetes Autocomplete
      this.showAutocomplete = false;
      this.hasFocus = false;
    }
  }

  /**
   * Wird die Maus bewegt, wird ein entsprechender
   * Flag gesetzt. Außerdem wird überprüft, ob zuvor die
   * Pfeiltasten benutzt wurden (über ein weiteres Flag,
   * welchses bei onKeyDown gesetzt wird).
   * Wenn ja, wird das durch die Nutzung der
   * Pfeiltasten selektierte Element vorab gelöscht.
   * @param event
   */
  private mouseMoved(event: any) {
    this._mouseMoved = true;
  }

  /**
   * Benachrichtigt den Listener, dass sich die Eingabe geändert hat.
   *
   * Gleichzeitig merken wird uns, welche Anfrage gestellt wurde (originalInput) und dass gerade eine Anfrage
   * läuft (oraclePending).
   */
  private notifyOracleOfModelChange(): void {
    this.oraclePending = true;
    this.modelChange.emit(this.model);
    this.originalInput = this.model;
  }

  /**
   * Scrollt zu dem Eintrag mit dem index in der Autocomplete-Liste index; dieser wird bei keydown/key events berechnet
   * @param index Index des zu scrollenden Elenets in der Autocomplete-Box
   */
  private scrollToView(index): void {
    const container = this.autocompleteContainer.nativeElement;
    const ul = container.querySelector("ul");
    const li = ul.querySelector("li");  // just sample the first li to get height
    const liHeight = li.offsetHeight;
    const scrollTop = ul.scrollTop;
    const viewport = scrollTop + ul.offsetHeight;
    const scrollOffset = liHeight * index;
    if (scrollOffset < scrollTop || (scrollOffset + liHeight) > viewport) {
      ul.scrollTop = scrollOffset;
      // setzen von Flags zur korrekten Anzeige der Markierung in der Liste.
      this._mouseMoved = false;
    }
  }

  /**
   * Submits the current value
   */
  private doSubmit(): boolean {
    if (!this.model || this.model.length === 0 || this.suggestionList.length === 0 || this.suggestionList[0].key === "err") {
      return false;
    }

    this.submitPending = false;
    this.onSubmit.emit(this.suggestionList[Math.max(0, this.itemIndex)]);
    this.showAutocomplete = false;
    this.model = "";
    this.originalInput = "";
    this.itemIndex = -1;
    this.suggestionList = [];

    return true;
  }

  /**
   * Wartet auf die Existenz des Autocomplete-Elementes und führt dann das Callback aus.
   *
   * @param callback Das ausführende Callback
   */
  private callAfterAutocompleteExists(callback) {
    if (this.autocompleteContainer) {
      callback();
    } else {
      setTimeout(() => this.callAfterAutocompleteExists(callback), 10);
    }
  }
}
