import {Component, Input, OnInit, AfterContentChecked} from "@angular/core";
import {StudisuConfig, getStudisuConfig} from "../config/studisu.config";
import {HeaderFooterService} from "../services/headerfooter.service";
import {SafeHtml} from "@angular/platform-browser";

@Component({
  selector: "ba-footer",
  templateUrl: "./footer.component.html"
})
export class FooterComponent implements OnInit, AfterContentChecked {
  public conf: StudisuConfig = getStudisuConfig();
  public content;

  @Input()
  public getUrl: string = this.conf.baseUrlHeaderFooter + this.conf.footerRestPath;

  constructor(private headerFooterService: HeaderFooterService) {
  }

  public ngOnInit(): void {
    this.headerFooterService.getFooterFromOAG(
      this.getUrl,
      (value: SafeHtml) => {
        this.content = value;
      }
    );
  }

  public ngAfterContentChecked(): void {
    this.headerFooterService.signalFooterDOMInit();
  }

}
