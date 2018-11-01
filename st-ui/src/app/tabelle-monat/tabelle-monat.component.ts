import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { BackendService } from '../services/backend.service';
import { StundenEintrag } from '../services/model/stunden-eintrag';
import { Observable } from 'rxjs/Observable';


@Component({
  selector: 'app-tabelle-monat',
  templateUrl: './tabelle-monat.component.html',
  styleUrls: ['./tabelle-monat.component.css']
})
export class TabelleMonatComponent implements OnInit, OnChanges {
  
  @Input()
  jahr: any;

  @Input()
  monat: any;

  eintraege: Observable<StundenEintrag[]>;
  value: Date;
  constructor(private backendService: BackendService) { }

  ngOnInit() {    
  }

  ngOnChanges() {
   if (this.isValid()) {
      this.eintraege = this.backendService.getAllDataFromServer(this.monat, this.jahr);
      console.log(this.eintraege);
    }
  }

  updateNote(eintrag) {
    // hole eintrag nach aktueller id
    this.backendService.updateNote(eintrag) .subscribe(
      result => console.log(result),
      error => console.log(error)
); 
  }

  getDaysInMonth(month: number, year: number) : number[] {
    if ( month == null && year == null) {
      let jetzt = new Date();
      let cntDays =  new Date(jetzt.getFullYear(), jetzt.getMonth()+1, 0).getDate();
      return this.createRange(cntDays);
    }
    return this.createRange(new Date(year, month, 0).getDate());
  }

  createRange(number){
    var items: number[] = [];
    for(var i = 1; i <= number; i++){
       items.push(i);
    }
    return items;
  }

  isValid(): boolean {
    return this.monat != null && this.jahr != null;
  }

}
