import { Component, OnInit } from '@angular/core';
import { BackendService } from '../services/backend.service';
import { StundenEintrag } from '../services/model/stunden-eintrag';
import { Observable } from 'rxjs/Observable';


@Component({
  selector: 'app-tabelle-monat',
  templateUrl: './tabelle-monat.component.html',
  styleUrls: ['./tabelle-monat.component.css']
})
export class TabelleMonatComponent implements OnInit {
  
  eintraege: Observable<StundenEintrag[]>;
  value: Date;
  constructor(private backendService: BackendService) { }

  ngOnInit() {
    this.eintraege = this.backendService.getAllDataFromServer();
    
  }

  updateNote(eintrag) {
    // hole eintrag nach aktueller id
    this.backendService.updateNote(eintrag) .subscribe(
      result => console.log(result),
      error => console.log(error)
); 
  }

}
