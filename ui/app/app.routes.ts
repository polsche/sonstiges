import { Routes, RouterModule } from "@angular/router";
import { StatusComponent } from "./monitoring/status.component";
import { SeiteNichtGefundenComponent } from "./seitenichtgefunden/seitenichtgefunden.component";
import { SucheComponent } from "./main/suche/suche.component";
import { StudienfelderComponent } from "./main/studienfelder/studienfelder.component";
import { StudienfeldinfoComponent } from "./main/studienfeldinfo/studienfeldinfo.component";
import { StudienangebotInfoComponent } from "./main/studienangebotinfo/studienangebotinfo.component";
import { SeiteNichtGefundenTxtComponent } from "./seitenichtgefunden/seitenichtgefundentxt/seitenichtgefundentxt.component";
import { MainComponent } from "./main/main.component";
// tslint:disable:object-literal-sort-keys

export const routes: Routes = [
  {
    path: "", component: MainComponent,
    children: [
      {
        path: "suche",
        component: SucheComponent
      },
      {
        path: "studienfelder",
        component: StudienfelderComponent
      },
      {
        path: "studienfeldinfo",
        component: StudienfeldinfoComponent
      },
      {
        path: "studienangebot/",
        component: SeiteNichtGefundenTxtComponent
      },
      {
        path: "studienangebot/:id",
        component: StudienangebotInfoComponent
      },
      {
        path: "studienangebot",
        component: SeiteNichtGefundenTxtComponent
      },
      {
        path: "",
        redirectTo: "/suche",
        pathMatch: "full"
      }
    ]
  },
  { path: "mt/status", component: StatusComponent },
  { path: "404", component: SeiteNichtGefundenComponent },
  { path: "**", component: SeiteNichtGefundenComponent }
];

export const routing = RouterModule.forRoot(routes);
