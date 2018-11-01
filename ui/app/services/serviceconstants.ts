/*
*  servicebezogene Konstanten
*/
export const ServiceConstants = Object.freeze({

  // CKU tslint disabled because this fields could be grouped in this file for better readability
  // tslint:disable:object-literal-sort-keys
  EINGABEN_UNVOLLSTAENDIG_ODER_FEHLERHAFT: "EINGABEN_UNVOLLSTAENDIG_ODER_FEHLERHAFT",
  // UNPROCESSABLE_ENTITY: 422,
  EINGABEPARAMETER_FEHLERHAFT: "EINGABEPARAMETER_FEHLERHAFT",
  EINGABEPARAMETER_ZU_LANG: "EINGABEPARAMETER_ZU_LANG",
  NOT_FOUND: 404,
  NOT_FOUND_MESSAGE: "Not Found",
  SERVICE_NOT_AVAILABLE: "SERVICE_NOT_AVAILABLE",
  VALUE_SEPARATOR: ";"
});

export class FormErrorHandler {
  private errormap: { [key: string]: string };

  constructor(errormap: { [key: string]: string }) {
    this.errormap = errormap;
  }

}




