package de.ba.bub.studisu.studienangebote.model;

import java.util.ArrayList;
import java.util.List;

import de.ba.bub.studisu.studienangebote.model.facetten.UmkreisFacette;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrt;
import de.ba.bub.studisu.studienangebote.model.requestparams.AnfrageOrte;

/**
 * validator, der nach der spring validierung von z.b. required params
 * weiter logik pruefen kann.
 * Hier z.B. dass anfrageOrte keine Duplikate enthält und die Liste nicht größer drei ist.
 *
 * following the established pattern from anfrageOrte
 * @author KunzmannC
 *
 */
public class OrteValidator{

	/**
	 * Ungueltige Eingabe.
	 */
	public static final int INVALID = 0;

	/**
	 * Gueltige Eingabe.
	 */
	public static final int VALID = 1;

	/**
	 * Ergebnis der Validierung.
	 */
	private int result = INVALID;

	/**
	 * Konstante fuer Fehlermeldung
	 */
	public static final String TOO_MANY_ORT_VALUES_OR_DUPLICATES = "You may not request more than three places or duplicates";

	/**
	 * Gueltige Eingabe.
	 */
	public static final int MAX_ORTE = 3;

	/**
	 * constructor evaluating the result
	 *
	 * @param umkreisFacette
	 * 			Die Umkreisfacette mit dem zu verwendenden Suchumkreis.
	 * @param anfrageOrte
	 * 			Die Repräsentation der übergebenen Anfrageorte.
	 */
	public OrteValidator(UmkreisFacette umkreisFacette, AnfrageOrte anfrageOrte){
		if(umkreisFacette == null){
			//abbrechen falls umkreis nicht gesetzt
			return;
		}

		// umkreisfacette ist gesetzt
		if ((anfrageOrte != null && anfrageOrte.getOrte() != null && anfrageOrte.getOrte().size() <= MAX_ORTE)) {
			//anfrageort(e) sind valide
			List<AnfrageOrt> validOrteList = new ArrayList<AnfrageOrt>();
			//entfernen von dubletten
			for(AnfrageOrt ort : anfrageOrte.getOrte()){
				if(!validOrteList.contains(ort)){
					validOrteList.add(ort);
				}
			}
			if(validOrteList.size()==anfrageOrte.getOrte().size()){
				//eingabe war frei von dubletten
				this.result = VALID;
			}
		}
		if(anfrageOrte == null  && UmkreisFacette.BUNDESWEIT.equals(umkreisFacette.getSelectedOption())){
			// anfrage ohne orte aber bundesweit ist valide
			this.result = VALID;
		}
	}

	/**
	 * Validation result INVALID or VALID
	 *
	 * @return validation result
	 */
	public int getResult() {
		return result;
	}
}