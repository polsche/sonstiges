// modules
import { NgModule, Injectable, enableProdMode } from "@angular/core";
import { BrowserModule, HammerGestureConfig, HAMMER_GESTURE_CONFIG } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { routing } from "./app.routes";
import { ReactiveFormsModule } from "@angular/forms";
import { FormsModule }   from "@angular/forms";

// app components
import { AppComponent } from "./app.component";
import { HeaderComponent } from "./header/header.component";
import { HeaderFallbackComponent } from "./header-fallback/header-fallback.component";
import { FooterComponent } from "./footer/footer.component";
import { FooterFallbackComponent } from "./footer-fallback/footer-fallback.component";
// Status component and service
import { StatusComponent } from "./monitoring/status.component";
import { SeiteNichtGefundenComponent } from "./seitenichtgefunden/seitenichtgefunden.component";
import { SeiteNichtGefundenTxtComponent } from "./seitenichtgefunden/seitenichtgefundentxt/seitenichtgefundentxt.component";

import { StatusService } from  "./monitoring/status.service";

import { HttpModule } from "@angular/http";

import { StudisuConfig, getStudisuConfig } from "./config/studisu.config";

import { ErrorService } from "./services/error.service";
import { ErrorForDirective } from "./directives/errorfor.directive";
import { LiveAnnouncer } from "./services/live-announcer";
import { TabComponent } from "./monitoring/tab.component";
import { TabsComponent } from "./monitoring/tabs.component";
import { MaxLength } from "./pipes/maxlength.pipe";
import { MainComponent } from "./main/main.component";
import { SucheComponent } from "./main/suche/suche.component";
import { SucheFormComponent } from "./main/suche/form/suche-form.component";
import { SucheResultComponent } from "./main/suche/result/suche-result.component";
import { SucheFacettesComponent } from "./main/suche/facettes/suche-facettes.component";
import { InputtextAutocompleteComponent } from "./ui-components/inputtext-autocomplete/inputtext-autocomplete.component";
import { TagcloudComponent } from "./ui-components/tagcloud/tagcloud.component";
import { OrtSucheComponent } from "./main/suche/form/ort-suche/ort-suche.component";
import { ErgebnislisteComponent} from "./main/suche/result/ergebnisliste/ergebnisliste.component";
import { StudienangebotComponent} from "./main/suche/result/ergebnisliste/studienangebot.component";
import { UrlParamService } from "./main/suche/services/url-param.service";
import { StudienfelderComponent } from "./main/studienfelder/studienfelder.component";
import { StudienfeldinfoComponent } from "./main/studienfeldinfo/studienfeldinfo.component";
import { FacettenfilterComponent } from "./ui-components/facettenfilter/facettenfilter.component";
import { InputrangeComponent } from "./ui-components/inputrange/inputrange.component";
import { RegionenFacetteComponent } from "./main/suche/facettes/regionen-facette.component";
import { SuchumkreisFacetteComponent } from "./main/suche/facettes/suchumkreis-facette.component";
import { LoggingService } from "./services/logging.service";
import { StudienbereichSucheComponent } from "./main/suche/form/studienbereich-suche/studienbereich-suche.component";
import { EventService } from "./services/event.service";
import { StudisuErrorService } from "./error/studisuerror.service";
import { ErrorHandlerService } from "./error/errorhandler.service";
import { StudienangebotInfoComponent } from "./main/studienangebotinfo/studienangebotinfo.component";
import { CheckboxComponent } from "./ui-components/checkbox/checkbox.component";
import { StudisuHeaderComponent } from "./main/header/studisu-header.component";
import { CollapsableComponent } from "./ui-components/collapsable/collapsable.component";
import { OpenFacetteService } from "./main/suche/services/open-facette.service";
import { VersionInfoComponent } from "./versioninfo/versioninfo.component";
import { VersionInfoService } from "./versioninfo/services/versioninfo.service";
import { ZuletztAngesehenListe } from "./services/zuletztangesehen.service";
import { ModalDialogComponent } from "./ui-components/modaldialog/modaldialog.component";
import { ModalHinweisDialogComponent } from "./ui-components/modalhinweisdialog/modalhinweisdialog.component";
import { MessageService } from "./services/message.service";
import { BackComponent } from "./ui-components/back/back.component";
import { HistoryService } from "./ui-components/back/history.service";
import { LoadSpinnerComponent } from "./ui-components/load-spinner/load-spinner.component";
import { NormanizeId } from "./pipes/normanize-id.pipe";
import { ResetAllFacettesComponent } from "./main/suche/facettes/reset-all-facettes.component";
import { DetailPagerComponent} from "./ui-components/detailpager/detail-pager.component";
import { InfoblockComponent} from "./main/studienangebotinfo/infoblock.component";
import { NavTabbarComponent } from "./ui-components/navtabbar/navtabbar.component";
import {StudisuHttpService} from "./services/studisu-http.service";
import {OagHttpModule} from "bapf-oag";
import {OagConfigServiceImpl} from "./oag/oagconfig.service";
import {HeaderFooterService} from "./services/headerfooter.service";
import {SignetComponent} from "./ui-components/signet/signet.component";
import {StudisuDetailseitenheaderComponent} from "./main/detailseitenheader/studisu-detailseitenheader.component";
import {StoeberweltenComponent} from "./main/stoeberwelten/stoeberwelten.component";

declare let Hammer: any;

@Injectable()
export class StudisuHammerConfig extends HammerGestureConfig {
  public overrides = <any> {
    "swipe": {velocity: 0.4, threshold: 20} // override default settings
  };
  // Stellt sicher, dass das vertikale Scrolling noch funktioniert.
  public buildHammer(element: HTMLElement) {
    delete Hammer.defaults.cssProps.userSelect;
    return new Hammer(element, {
      inputClass: Hammer.SUPPORT_POINTER_EVENTS ? Hammer.PointerEventInput : Hammer.TouchInput,
      touchAction: "pan-y"
    });
  }
}

export function announcerFactory() {
  return new LiveAnnouncer(undefined);
}


let config: StudisuConfig = getStudisuConfig();

// feature module
if ((<any>window).ENV === "prod") {
  enableProdMode();
}

@NgModule({
  bootstrap: [
    AppComponent
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    HeaderFallbackComponent,
    FooterComponent,
    FooterFallbackComponent,
    MainComponent,
    StudienfeldinfoComponent,
    SucheComponent,
    SucheFormComponent,
    OrtSucheComponent,
    StudienbereichSucheComponent,
    SucheResultComponent,
    SucheFacettesComponent,
    StudienfelderComponent,
    SignetComponent,
    ErrorForDirective,
    StatusComponent,
    SeiteNichtGefundenComponent,
    SeiteNichtGefundenTxtComponent,
    ErgebnislisteComponent,
    TabComponent,
    TabsComponent,
    MaxLength,
    InputtextAutocompleteComponent,
    FacettenfilterComponent,
    InputrangeComponent,
    RegionenFacetteComponent,
    SuchumkreisFacetteComponent,
    TagcloudComponent,
    StudienangebotInfoComponent,
    StudienangebotComponent,
    CheckboxComponent,
    StudisuHeaderComponent,
    StudisuDetailseitenheaderComponent,
    CollapsableComponent,
    VersionInfoComponent,
    ModalDialogComponent,
    ModalHinweisDialogComponent,
    BackComponent,
    LoadSpinnerComponent,
    NormanizeId,
    ResetAllFacettesComponent,
    DetailPagerComponent,
    InfoblockComponent,
    NavTabbarComponent,
    StoeberweltenComponent
    ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    HttpModule,
    OagHttpModule,
    routing
  ],
  providers: [
    StatusService,
    {provide: ErrorService, useClass: ErrorService},
    { provide: LiveAnnouncer, useFactory: announcerFactory },
    {provide: HAMMER_GESTURE_CONFIG, useClass: StudisuHammerConfig},
    UrlParamService,
    LoggingService,
    EventService,
    HeaderFooterService,
    StudisuErrorService,
    ErrorHandlerService,
    MessageService,
    OpenFacetteService,
    VersionInfoService,
    ZuletztAngesehenListe,
    HistoryService,
    OagConfigServiceImpl,
    {provide: "OagConfigService", useExisting: OagConfigServiceImpl},
    StudisuHttpService,
  ]
})
export class AppModule {}
