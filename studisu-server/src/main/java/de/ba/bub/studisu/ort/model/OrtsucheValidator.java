package de.ba.bub.studisu.ort.model;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * validator for ortseingabe
 *
 * AK_5 Bei der Eingabe werden folgende Zeichen akzeptiert:
 * Alle Deutschen und Französischen Zeichen:
 * Deutsch:
 * A Ä B C D E F G H I J K L M N O Ö P Q R S ß T U Ü V W X Y Z
 * Französisch:
 * A À Â Æ B C Ç D E È É Ê Ë F G H I Î Ï J K L M N O Ô Œ P Q R S T U Ù Û Ü V W X Y Ÿ Z
 * in sowohl Groß- als auch Kleinschreibung werden akzeptiert. Die Zeichen:
 * / ( ) . , - ' werden akzeptiert.
 * AK_6: Die Zahlen 0-9 werden für eine Eingabe der PLZ akzeptiert.
 * Created by loutzenhj on 04.04.2017.
 */
public class OrtsucheValidator {

    /**
     * pattern for ort name - at least three characters from the set below, case insensitive
     */
    private static final Pattern ORT_NAME_PATTERN = Pattern.compile("[A-Za-zÄäÖöÜüßÀàÂâÆÇÈèÉéÊêË\\\\U+00EBÎîÏ\\\\U+00EFÔôŒÙùÛûŸ\\\\U+00FF/() .\',\\-]{2,250}");


    /**
     * 5 numbers...
     */
    private static final Pattern PLZ_PATTERN = Pattern.compile("[0-9]{5}");
    

    public static final int INVALID = 0;
    public static final int VALID_NAME = 1;
    public static final int VALID_PLZ = 2;
    public static final int INVALID_ORT_ZU_LANG = 3;

    private int result = INVALID;

    /**
     * C-tor with input
     * @param input
     */
    public OrtsucheValidator(String input){
        if(StringUtils.isEmpty(input)){
            result = INVALID;
        }else if(ORT_NAME_PATTERN.matcher(input).matches()){
            result = VALID_NAME;
        }else if(PLZ_PATTERN.matcher(input).matches()){
            result = VALID_PLZ;
        }else if (!StringUtils.isEmpty(input) && input.length()>250){
        	result = INVALID_ORT_ZU_LANG;
        }
    }

    /**
     * validation result
     * one of
     * INVALID,VALID_NAME,VALID_PLZ
     * @return validation result
     */
    public int getResult(){
        return result;
    }
}
