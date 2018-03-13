import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { TabelleMonatComponent } from './tabelle-monat/tabelle-monat.component';
import {HttpClientModule} from '@angular/common/http';
import { BackendService } from './services/backend.service';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    TabelleMonatComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    NgbModule.forRoot(),
    HttpClientModule,
    FormsModule,
  ],
  providers: [ BackendService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
