import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  
  lblDDJahr = "Jahr";
  lblDDMonat = "Monat";
  
  years: string[] = [ "2016", "2017", "2018"];
  monate: string[] = [ "Januar",
                        "Februar",
                        "März",
                        "April",
                        "Mai",
                        "Juni",
                        "Juli",
                        "August",
                        "September",
                        "Oktober",
                        "November",
                        "Dezember" ];

  public chNameDropdownJahr(event) {
    this.lblDDJahr = event.target.textContent;
  }

  public chNameDropdownMonat(event) {
    this.lblDDMonat = event.target.textContent;
    
  }
}
