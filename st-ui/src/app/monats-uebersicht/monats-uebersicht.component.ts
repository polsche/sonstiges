import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-monats-uebersicht',
  templateUrl: './monats-uebersicht.component.html',
  styleUrls: ['./monats-uebersicht.component.css']
})
export class MonatsUebersichtComponent implements OnInit {
  selectedValueMonat = null;
  selectedValueJahr = null;

  constructor() { }

  ngOnInit() {
  }

  lblDDJahr = "Jahr";
  lblDDMonat = "Monat";
  

  years: string[] = [ "2016", "2017", "2018"];

  monate = [ 
  { id:  "01", monat: "Januar" },
  { id:  "02", monat: "Februar" },
  { id:  "03", monat: "März" },
  { id:  "04", monat: "April" },
  { id:  "05", monat: "Mai" },
  { id:  "06", monat: "Juni" },
  { id:  "07", monat: "Juli" },
  { id:  "08", monat: "August" },
  { id:  "09", monat: "September" },
  { id:  "10", monat: "Oktober" },
  { id:  "11", monat: "November" },
  { id:  "12", monat: "Dezember" } ];

  public chNameDropdownJahr(event) {
    this.lblDDJahr = event.target.textContent;
  }

  public chNameDropdownMonat(event) {
    this.lblDDMonat = event.target.textContent;
    
  }

  loadData() {
     // console.log(this.selectedValueMonat + "." + this.selectedValueJahr)
  }

}
