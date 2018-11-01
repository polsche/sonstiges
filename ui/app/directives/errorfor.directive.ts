import { Directive, AfterViewInit, ElementRef, Input, Inject } from "@angular/core";
import { ErrorService } from "../services/error.service";

@Directive({
  selector: "[baErrorFor]"
})
export class ErrorForDirective implements AfterViewInit {

  // tslint:disable-next-line:no-input-rename
  @Input("baErrorFor")
  public targetElement: HTMLElement;

  @Input()
  public errorNamespace: string;

  constructor(private element: ElementRef, @Inject(ErrorService) private errorService: ErrorService) {}

  public ngAfterViewInit(): void {
    const message: string = (<HTMLElement>this.element.nativeElement).innerText;
    this.errorService.emitElementValidationError(this.errorNamespace, message, this.targetElement);
  }
}
