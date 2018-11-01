import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { Observer } from "rxjs/Observer";
import { ElementValidationError } from "../model/error.model";

@Injectable()
export class ErrorService {

  private elementValidationErrorObservable: Observable<ElementValidationError>;
  private elementValidationErrorObservers: Observer<ElementValidationError>[] = [];

  constructor() {
    this.elementValidationErrorObservable = Observable.create(
      observer => {
        this.elementValidationErrorObservers.push(observer);
      }
    );
  }

  get elementValidationError(): Observable<ElementValidationError> {
    return this.elementValidationErrorObservable;
  }

  public emitElementValidationError(namespace: string, message: string, target: HTMLElement) {
    for (let observer of this.elementValidationErrorObservers) {
      observer.next({
        message: message,
        namespace: namespace,
        target: target
      });
    }
  }

}
