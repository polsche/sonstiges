import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { TabelleMonatComponent } from './tabelle-monat/tabelle-monat.component';
import {HttpClientModule} from '@angular/common/http';
import { BackendService } from './services/backend.service';
import { FormsModule } from '@angular/forms';
import { DetailSeiteComponent } from './detail-seite/detail-seite.component';
import { MonatsUebersichtComponent } from './monats-uebersicht/monats-uebersicht.component';


const appRoutes: Routes = [
  {
    path: "",
    component: AppComponent,
    children: [
      {
        path: "start",
        component: MonatsUebersichtComponent,
      },
      {
        path: 'detailSeite/:id', 
        component: DetailSeiteComponent 
      }
    ]
    
    
  }
];
export const routing = RouterModule.forRoot(appRoutes);

@NgModule({
  declarations: [
    AppComponent,
    TabelleMonatComponent,
    DetailSeiteComponent,
    MonatsUebersichtComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    NgbModule.forRoot(),
    HttpClientModule,
    FormsModule,
    RouterModule,
    routing
  ],
  providers: [ BackendService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
