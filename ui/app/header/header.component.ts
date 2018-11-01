import {Component, Input, OnInit, AfterContentChecked} from "@angular/core";
import {StudisuConfig, getStudisuConfig} from "../config/studisu.config";
import {HeaderFooterService} from "../services/headerfooter.service";
import {SafeHtml} from "@angular/platform-browser";

@Component({
  selector: "ba-header",
  templateUrl: "./header.component.html"
})
export class HeaderComponent implements OnInit, AfterContentChecked {
  public content;
  private conf: StudisuConfig = getStudisuConfig();

  @Input()
  private getUrl: string = this.conf.baseUrlHeaderFooter + this.conf.headerRestPath;

  constructor(private headerFooterService: HeaderFooterService) {
  }

  public ngOnInit(): void {
    this.headerFooterService.getHeaderFromOAG(
      this.getUrl,
      (value: SafeHtml) => {
        this.content = value;
      }
    );
  }

  public ngAfterContentChecked(): void {
    this.headerFooterService.signalHeaderDOMInit();
  }

}
