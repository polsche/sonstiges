package de.ba.bub.studisu.common.exception;

import org.springframework.http.HttpStatus;

import de.ba.bub.studisu.common.model.facetten.FacettenOption;

/**
 * Wann immer ein URI-Parameter nicht zum Typ passt (z.B. auf keinen gültigen Enum-Wert mappt), kann diese Exception geworfen werden.
 */
@ErrorDataMapping(status = HttpStatus.BAD_REQUEST, message = "FACETTENAUSPRAEGUNG_UNBEKANNT")
public class UnbekannteFacettenauspraegungException extends ResponseDataException {

	/**
	 * serializable stuff
	 */
	private static final long serialVersionUID = -175727243605604710L;
	
	protected final static String ERRORMESSAGE_START = "Facette der Klasse '";
	protected final static String ERRORMESSAGE_MID = "' besitzt keine Ausprägung mit Namen '";
	protected final static String ERRORMESSAGE_END = "'";

	/**
	 * Constructor
	 * @param facettenOptionClass
	 * @param uriParameterWert
	 * @param rootCause
	 */
	public UnbekannteFacettenauspraegungException(Class<? extends FacettenOption> facettenOptionClass, 
    		String uriParameterWert, 
    		Exception rootCause) {
    	
        super(ERRORMESSAGE_START + facettenOptionClass.getSimpleName() 
        	+ ERRORMESSAGE_MID + uriParameterWert + ERRORMESSAGE_END, rootCause);
        
    }
}
