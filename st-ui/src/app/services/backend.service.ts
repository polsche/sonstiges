import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { StundenEintrag } from './model/stunden-eintrag';
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/do'
import 'rxjs/add/operator/catch'
import { HttpHeaders } from '@angular/common/http';

@Injectable()
export class BackendService {

  restURL: string = "http://localhost:8080/api/";
  headers = new HttpHeaders({'Content-Type': 'application/json'});
  
  constructor(private http: HttpClient) { 

  }

  getAllDataFromServer(monat: number, jahr: number): Observable<StundenEintrag[]> {
    return this.http.get(this.restURL + "eintraege/" + monat + "/" + jahr).map(this.extractData).catch(this.handleErrorObservable);
  }

  getById(id: number) {
    return this.http.get(this.restURL + "eintraege/id/" + id).map(this.extractData).catch(this.handleErrorObservable);
  }

  updateNote(eintrag: StundenEintrag) {
    return this.http.put(this.restURL + "eintraege/" + eintrag.id ,  JSON.stringify(eintrag), {headers: this.headers}).map(this.extractData)
    .catch(this.handleErrorObservable);
  }

  private extractData(res: Response) {
    return res;
}

private handleErrorObservable (error: Response | any) {
  console.error(error.message || error);
  return Observable.throw(error.message || error);
} 

}
