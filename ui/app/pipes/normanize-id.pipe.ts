import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: "sgNormanizeId"
  })
export class NormanizeId implements PipeTransform {
  public transform(value: string): string {
    /*
     The closure "removeUmlaute" using a regular expression to find umlaute
     and translate them according to the entries found in translate
    */
    let removeUmlaute = (function() {
      let translate_re = /[öäüÖÄÜß]/g;
      let translate = {
        "Ä": "A", "Ö": "O", "Ü": "U", "ß": "ss",
        "ä": "a", "ö": "o", "ü": "u"
      };
      return function(s) {
        return ( s.replace(translate_re, function(match) {
          return translate[match];
        }) );
      };
    })();

    let transformedValue = removeUmlaute(value.split(" ").join("").toLowerCase());
    return `${transformedValue}`;
  }
}
