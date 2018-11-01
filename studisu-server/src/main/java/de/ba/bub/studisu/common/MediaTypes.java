package de.ba.bub.studisu.common;


import org.springframework.http.MediaType;

/**
 * Specific view on Media Types ammended with charset information (typically UTF8).<p/>
 * (Cannot be enum.value() construct: Needs to be constant at usage point.)
 * 
 * TODO check if used by spring, otherwise remove it from our code CKU
 */
public class MediaTypes {
    public static final String TEXT_HTML_UTF8 = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8";

    private MediaTypes(){
    }
}
