package de.ba.bub.studisu.studienfach.model;

import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * Validator für Studienfachsuche.
 *
 * Es wird entweder die Suche per String oder die Suche per übergebenen DKZ-IDs validiert, aber niemals beides.
 *
 * Daher muss einer der beiden zu validierenden Parameter <code>null</code> sein.
 *
 * Bei der Sucheingabe werden folgende Zeichen akzeptiert:
 *
 * Alle Deutschen und Franzoesischen Zeichen:
 *
 * Deutsch: A Ä B C D E F G H I J K L M N O Ö P Q R S ß T U Ü V W X Y Z
 *
 * Französisch: A À Â Æ B C Ç D E È É Ê Ë F G H I Î Ï J K L M N O Ô Œ P Q R S T U Ù Û Ü V W X Y Ÿ Z
 *
 * in sowohl Groß- als auch Kleinschreibung werden akzeptiert.
 *
 * Die Zeichen: / ( ) . , - ' werden akzeptiert.
 *
 * Für die DKZ-IDs werden die Ziffern 0-9 akzeptiert.
 */
public class StudienfachSucheValidator {

	/**
	 * Pattern für den Suchbegriff.
	 */
	private static final Pattern SUCHBERGRIFF_PATTERN = Pattern
			.compile("[A-Za-zÄäÖöÜüßÀàÂâÆÇÈèÉéÊêË\\\\U+00EBÎîÏ\\\\U+00EFÔôŒÙùÛûŸ\\\\U+00FF/() .\',\\-]{2,250}");

	/**
	 * Ungültige Suchparameter.
	 */
	public static final int INVALID = 0;
	
	/**
	 * Suchbegriff zu lang - Ungültig
	 */
	public static final int INVALID_SUCHBEGRIFF_ZU_LANG = 3;

	/**
	 * Gültiger Suchbegriff.
	 */
	public static final int VALID_SUCHBEGRIFF = 1;

	/**
	 * Gültige Studienfach-DKZ-IDs.
	 */
	public static final int VALID_IDS = 2;

	/**
	 * Ergebnis der Validierung.
	 */
	private int result = INVALID;

	/**
	 * C-tor with input
	 *
	 * @param suchbegriff
	 * @param studienfaecher
	 */
	public StudienfachSucheValidator(String suchbegriff, Studienfaecher studienfaecher) {

		boolean suchwortNotEmpty = !StringUtils.isEmpty(suchbegriff);
		boolean ids = studienfaecher != null && !studienfaecher.getStudienfaecherIds().isEmpty();

		result = INVALID;

		if (suchwortNotEmpty && !ids && SUCHBERGRIFF_PATTERN.matcher(suchbegriff).matches()) {
			result = VALID_SUCHBEGRIFF;
		}
		
		if(suchwortNotEmpty && suchbegriff.length()>250){
			result = INVALID_SUCHBEGRIFF_ZU_LANG;
		}

		if (ids && !suchwortNotEmpty) {
			result = VALID_IDS;
		}
	}

	/**
	 * Validation result one of INVALID,VALID_SUCHBEGRIFF,VALID_IDS.
	 *
	 * @return validation result
	 */
	public int getResult() {
		return result;
	}
}
