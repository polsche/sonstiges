import {OagHttpService} from "bapf-oag";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {
  Request,
  RequestOptionsArgs,
  Response,
} from "@angular/http";
import {getStudisuConfig, StudisuConfig} from "../config/studisu.config";

/**
 * Wrapper fuer den OagHttpService, der Cache-Busting hinzufuegt.
 */
@Injectable()
export class StudisuHttpService {

  // we use a token in config which should get substituted
  // if its static we got no value from adding the urlparam to requests
  private cacheBustUnsubstitutedToken = "[[cacheBust]]";

  private conf: StudisuConfig = getStudisuConfig();

  constructor(private oagHttp: OagHttpService) {}

  public request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    url = this.addCacheBusting(url);
    return this.oagHttp.request(url, options);
  }

  public get(url: string, options?: RequestOptionsArgs) {
    url = this.addCacheBusting(url);
    return this.oagHttp.get(url, options);
  }

  public post(url: string, body: any, options?: RequestOptionsArgs) {
    url = this.addCacheBusting(url);
    return this.oagHttp.post(url, body, options);
  }

  public put(url: string, body: any, options?: RequestOptionsArgs) {
    url = this.addCacheBusting(url);
    return this.oagHttp.put(url, body, options);
  }

  public delete(url: string, options?: RequestOptionsArgs) {
    url = this.addCacheBusting(url);
    return this.oagHttp.delete(url, options);
  }

  public addCacheBustingToUrl(url: string): string {
    // only add cacheBust if the config contains an substituted string
    if (this.conf.cacheBust && this.conf.cacheBust.length > 0
        && this.conf.cacheBust !== this.cacheBustUnsubstitutedToken) {
      url = url + (url.includes("?") ? "&" : "?");
      url += "cb=" + this.conf.cacheBust;
    }
    return url;
  }

  private addCacheBusting(url: string | Request): any {
    if (url instanceof Request) {
      return this.addCacheBustingToRequest(url as Request);
    }
    return this.addCacheBustingToUrl(url);
  }

  private addCacheBustingToRequest(request: Request): Request {
    request.url = this.addCacheBustingToUrl(request.url);
    return request;
  }
}
