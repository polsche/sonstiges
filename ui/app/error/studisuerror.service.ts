import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {StudisuError} from "./studisuerror.model";
import {LoggingService} from "../services/logging.service";

/**
 * Serviceklasse, die Fehlerinformationen haelt und allgemein zur Verfuegung stellt.
 * @since STUDISU-105: Anzeige Fehlerdialog bei fehlgeschlagenem Servercall.
 */
@Injectable()
export class StudisuErrorService {

  /**
   * BehaviourSubject zur Beobachtung der aufgelaufenen Fehlern
   */
  public errors: BehaviorSubject<StudisuError[]>;
  private errorArray: StudisuError[] = [];

  /**
   * Konstruktor, der Hilfsinstanz fuer Logging erwartet
   * @param logger Instanz der Logging-Hilfsklasse
   */
  constructor(private logger: LoggingService) {
    this.errors = new BehaviorSubject(this.errorArray);
  }

  /**
   * setzt Fehlerobjekt und Meldung in die Liste der aufgetretenen Fehler
   * @param message Meldung zum aufgetretenen Fehler
   * @param error Fehlerobjekt
   */
  public pushError(message: string, error: Error) {
    this.errorArray.push(new StudisuError(message, error));
    this.errors.next(this.errorArray);
    this.logger.info("uebergebe Fehler in Fehlerdialog mit Meldung " + (message !== null ? message : error.message), this);
  }

  /**
   * leert Liste der aufgelaufenen Fehler
   */
  public clearErrors() {
    this.errorArray = [];
    this.errors.next(this.errorArray);
  }
}
