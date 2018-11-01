import {Component, OnInit, Inject, HostListener, OnDestroy, ViewChild, ChangeDetectionStrategy} from "@angular/core";
import {HttpModule} from "@angular/http";
import {FormBuilder} from "@angular/forms";
import {ErrorService} from "../services/error.service";
import {LiveAnnouncer} from "../services/live-announcer";
import {StudisuConfig, getStudisuConfig} from "../config/studisu.config";
import {ModalData} from "../ui-components/modaldialog/modalData";
import {Messages} from "../ui-components/model/Messages";
import {StudisuErrorService} from "../error/studisuerror.service";
import {Subscription} from "rxjs/Subscription";
import {StudisuError} from "../error/studisuerror.model";
import {ModalHinweisDialogComponent} from "../ui-components/modalhinweisdialog/modalhinweisdialog.component";
import {HinweisModalData} from "../ui-components/modalhinweisdialog/hinweismodalData";
import {MessageService} from "../services/message.service";


// no one likes declaration errors =)
// hides the type error for jQuery
// TODO REAL solution for jQuery integration in our NG App
declare var $: any;

@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  providers: [
    HttpModule,
    ErrorService
  ],
  selector: "ba-studisu-main",
  templateUrl: "./main.component.html"
})


export class MainComponent implements OnInit, OnDestroy {

  public headerUrl: string;
  public footerUrl: string;
  public modalData: ModalData;
  public isScrollButtonVisible;

  @ViewChild("infodialog")
  private infoDialog: ModalHinweisDialogComponent;

  private errorService: Subscription;
  private messageServiceSubscription: Subscription;
  private conf: StudisuConfig = getStudisuConfig();

  constructor(private formBuilder: FormBuilder,
              @Inject(LiveAnnouncer) private liveAnnouncer: LiveAnnouncer,
              private errorDialogService: StudisuErrorService,
              private messageService: MessageService) {
    this.headerUrl = this.conf.baseUrlHeaderFooter + this.conf.headerRestPath;
    this.footerUrl = this.conf.baseUrlHeaderFooter + this.conf.footerRestPath;
  }

  /**
   * initializes MainComponent-component, receives default-values from ProfilService-service
   */
  public ngOnInit() {
    this.errorService = this.errorDialogService.errors
      .subscribe(error => {
        this.buildMessage(error);
      });
    this.messageServiceSubscription = this.messageService.showHinweisDialog
      .subscribe(hinweisData => {
        if (hinweisData != null) {
          this.infomodalOeffnen(hinweisData);
        }
      });
  }

  public ngOnDestroy(): void {
    this.errorService.unsubscribe();
    this.messageServiceSubscription.unsubscribe();
  }

  /**
   * Wird benötigt um zu einem Anker zu springen.
   * @param location
   */
  public goTo(location: string): void {
    window.location.hash = "";
    window.location.hash = location;
  }

  /**
   * Über den Decorator HostListener wird ein Listener auf
   * das Scroll-Event erzeugt. Falls die vorgegebene Scrollhöhe
   * erreicht wird, wird der Scroll-Up-Button eingeblendet.
   */
  @HostListener("window:scroll", ["$event"])
  private onScroll(): void {
    if (window.pageYOffset > 200) {
      this.isScrollButtonVisible = true;
    } else {
      this.isScrollButtonVisible = false;
    }
  }

  /**
   * Baut die Fehlermeldung zusammen
   * @param errorMessage: StudisuError[]
   */
  private buildMessage(errorMessage: StudisuError[]) {
    if (errorMessage.length > 0) {
      this.modalData = new ModalData(Messages.FEHLER_AUFGETRETEN, "", "");
      this.modalData.text = errorMessage.map(err => err.message).join("<br/>");
      this.errorDialogService.clearErrors();
    }
  }

  /**
   * Öffnet das Infomodal bei Klick auf das i-Icon.
   */
  private infomodalOeffnen(hinweisModalData: HinweisModalData) {
    this.infoDialog.showModalDialog(hinweisModalData);
  }
}
