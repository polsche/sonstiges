package de.ba.bub.studisu.monitoring.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility Klasse um HTML-Markup zu erzeugen
 *
 * Wenn die Klasse hier und da mal unleserlich wird, oder die ganzen Konstanten
 * zuviel erscheinen, der Dank gilt Sonar...
 *
 * @author RHA
 */
public final class HTMLUtil {
    private static final String TABLE_TAG = "table";
    private static final String THEAD_TAG = "thead";
    private static final String TBODY_TAG = "tbody";
    private static final String DIV_TAG = "div";

    private static final String BOOTSTRAP_CSS =
			"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">";
    private static final String BOOTSTRAP_JS =
			"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>";
    private static final String JQUERY_JS =
			"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";

    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLUtil.class);
    private static final String EXCEPTION_MESSAGE = "Exception: ";

    private HTMLUtil() {
        super();
    }




    /**
     * Convenience Method to reflect over a Class and invoke all Methods with a certain prefix.
     * Excludes Methods that contain the passed String (case insensitive).
     * @param clazz Class of the Reflection-Target
     * @param obj Object to invoke the methods on
     * @param methodPrefix i.e "get"
     * @param excludeContains ignores Methods that contain a keyword i.e "password"
     * @return List of Name-Result Mappings
     */
    public static Map<String, String> invokeGettersReflective(Class<?> clazz, Object obj, String methodPrefix,
                                                              String excludeContains) {
        Map<String, String> tmp = new TreeMap<String, String>();
        for (Method method : clazz.getMethods()) {
            try {
                if (method.getName().startsWith(methodPrefix) &&
                    !method.getName().toLowerCase().contains(excludeContains.toLowerCase()) &&
                    method.getTypeParameters().length == 0) {

                    // handing over untyped null would result in
                    // warning: non-varargs call of varargs method with inexact argument type for last parameter;
                    //          cast to java.lang.Object for a varargs call
                    //          cast to java.lang.Object[] for a non-varargs call and to suppress this warning
                    Object[] nullRef = null;
                    tmp.put(method.getName().substring(3), String.valueOf(method.invoke(obj, nullRef)));
                }
            } catch (IllegalAccessException e) {
                LOGGER.error(EXCEPTION_MESSAGE + e.getMessage(), e);
            } catch (InvocationTargetException e) {
                LOGGER.error(EXCEPTION_MESSAGE + e.getMessage(), e);
            } catch (IllegalArgumentException iae) {
                LOGGER.error(EXCEPTION_MESSAGE + iae.getMessage(), iae);
            }
        }
        return tmp;
    }

    /**
     * Convenience-Method to get a PropertiesFile as Map, with optional Exclusion of Entries.
     * @param props The Properties File to map
     * @param excludeContains ignores Properties that contain a keyword i.e "password"
     * @return Map of Property Key-Value-Pairs
     */
    public static Map<String, String> getPropertiesAsMap(Properties props, String excludeContains) {
        Map<String, String> tmp = new TreeMap<String, String>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (!isNullOrEmpty(excludeContains)) {
                if (!key.toLowerCase().contains(excludeContains)) {
                    tmp.put(key, value);
                }
            } else {
                tmp.put(key, value);
            }
        }
        return tmp;
    }

    /**
     * Generic Method to construct a HTML-Tag with optional Value.
     * If no value is supplied, a self-closing Tag is constructed.
     * @param tag The HTML-Tag to construct, e.g. div
     * @param value The optional Text inside the tag, can be null or empty
     * @param styleClass Optional CSS-Style-Class
     * @param style Optional inline CSS-Style
     * @return HTML-Markup
     * @see de.ba.ben.common.healthcheck.servlet.HTMLUtil#constructClosingTag(String tag)
     * @see de.ba.ben.common.healthcheck.servlet.HTMLUtil#constructOpeningTag(String tag, String styleClass, String style)
     */
    public static String constructElement(String tag, String value, String styleClass, String style) {
        StringBuilder sb = new StringBuilder();
        sb.append(constructOpeningTag(tag, styleClass, style));
        sb.append(value);
        sb.append(constructClosingTag(tag));

        return sb.toString();
    }

    /**
     * Constructs markup of an Opening-Tag
     * @param tag The Tag to construct the opening of, e.g. "div"
     * @param styleClass Optional CSS-class
     * @param style Optional inline CSS-Style
     * @return HTML-Markup as String, e.g. &lt;div&gt;
     */
    public static String constructOpeningTag(String tag, String styleClass, String style) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tag);

        if (styleClass != null && !styleClass.isEmpty()) {
            sb.append(" class=\"").append(styleClass).append("\"");
        }

        if (style != null && !style.isEmpty()) {
            sb.append(" style=\"").append(style).append("\"");
        }

        sb.append(">");
        return sb.toString();
    }

    /**
     * Constructs markup of a Closing-Tag
     * @param tag The Tag to construct the opening of, e.g. "div"
     * @return HTML-Markup as String, e.g. &lt;/div&gt;
     */
    public static String constructClosingTag(String tag) {
        StringBuilder sb = new StringBuilder();
        sb.append("</").append(tag).append(">");
        return sb.toString();
    }


    /**
     * Convenience-Method to construct Markup of a HTML-Table Element
     * @param columnHeader
     * @param values
     * @param styleClass Optional CSS-Class
     * @param style Optional Inline-CSS Style
     * @return HTML Markup as String
     */
    public static String constructTable(Collection<String> columnHeader, Collection<String> values, String styleClass,
                                        String style) {
        StringBuilder builder = new StringBuilder();
        Integer maxCols = columnHeader.size();
        Integer currCol = 0;
        builder.append(constructOpeningTag(TABLE_TAG, styleClass, style));

        builder.append(constructOpeningTag(THEAD_TAG, null, null));
        builder.append(constructOpeningTag("tr", null, null));
        for (String columnHead : columnHeader) {
            builder.append(constructElement("th", columnHead, null, null));
        }
        builder.append(constructClosingTag("tr"));
        builder.append(constructClosingTag(THEAD_TAG));

        builder.append(constructOpeningTag(TBODY_TAG, null, null));
        for (String value : values) {
            if (currCol == 0) {
                builder.append(constructOpeningTag("tr", null, null));
            }
            if (currCol.equals(maxCols)) {
                currCol = 0;
                builder.append(constructOpeningTag("tr", null, null));
            }

            builder.append(constructElement("td", value, null, null));
            currCol++;

            if (currCol.equals(maxCols)) {
                builder.append(constructClosingTag("tr"));
            }
        }
        builder.append(constructClosingTag(TBODY_TAG));
        builder.append(constructClosingTag(TABLE_TAG));

        return builder.toString();
    }

    /**
     * Variant of constructTable
     * As we want a sorted datastructure we use a List of TableRows
     * @param columnHeader
     * @param values
     * @param styleClass Optional CSS-class
     * @param style Optional Inline-CSS Style
     * @return HTML-Markup as String
     */
    public static String constructTable(Collection<String> columnHeader, List<TableRow> values, String styleClass,
                                        String style) {
        StringBuilder builder = new StringBuilder();

        if (style != null && !style.isEmpty()) {
            builder.append(constructOpeningTag(TABLE_TAG, styleClass, style));
        } else {
            builder.append(constructOpeningTag(TABLE_TAG, null, null));
        }

        builder.append(constructOpeningTag(THEAD_TAG, null, null));
        builder.append(constructOpeningTag("tr", null, null));
        for (String columnHead : columnHeader) {
            builder.append(constructElement("th", columnHead, null, null));
        }
        builder.append(constructClosingTag("tr"));
        builder.append(constructClosingTag(THEAD_TAG));

        builder.append(constructOpeningTag(TBODY_TAG, null, null));
        for (TableRow tr : values) {
            builder.append(constructOpeningTag("tr", null, null));
            if (!tr.getTableData().isEmpty()) {
                for (String td : tr.getTableData()) {
                    builder.append(constructElement("td", td, null, null));
                }
                builder.append(constructClosingTag("tr"));
            }
        }
        builder.append(constructClosingTag(TBODY_TAG));
        builder.append(constructClosingTag(TABLE_TAG));

        return builder.toString();
    }

    /**
     * Map-Variant of constructTable
     * @param columnHeader
     * @param values
     * @param styleClass Optional CSS-class
     * @param style Optional Inline-CSS Style
     * @return HTML-Markup as String
     */
    public static String constructTable(Collection<String> columnHeader, Map<String, String> values, String styleClass,
    String style) {
        List<TableRow> valuesList = new ArrayList<>();
        // add every MapEntry to the list
        for (Map.Entry<String,String> mapEntry : values.entrySet()) {
            TableRow td;
            td = new TableRow(mapEntry.getKey(), mapEntry.getValue());
            valuesList.add(td);
        }
        return constructTable(columnHeader, valuesList, styleClass, style);
    }

    /**
     * Constructs markup for a bootstrap header (CSS/JS)
     * @return HTML-Markup
     */
    public static String constructBootstrapHeader() {
        StringBuilder builder = new StringBuilder();
        builder.append(HTMLUtil.constructOpeningTag("head", null, null));
        builder.append(JQUERY_JS);
        builder.append(BOOTSTRAP_CSS);
        builder.append(BOOTSTRAP_JS);
        builder.append(HTMLUtil.constructClosingTag("head"));

        return builder.toString();
    }

    /**
     * Constructs markup for a Bootstrap 3 Accordion
     * @param header The clickable titles of the accordion tabs
     * @param values The Text/Markup that should be rendered inside accordion tabs
     * @return HTML Markup
     */
    public static String constructAccordion(Collection<String> header, Collection<String> values) {
        StringBuilder builder = new StringBuilder();

        String[] allHeaders = header.toArray(new String[header.size()]);
        String[] allValues = values.toArray(new String[values.size()]);

        builder.append("<div id=\"accordion\" class=\"panel-group\">");
        //Builds an accordion Group for each header-value pair
        for (int i = 0; i < header.size(); i++) {
            builder.append("<div class=\"panel panel-primary\">");
            builder.append("<div class=\"panel-heading\">");
            builder.append("<h4 class=\"panel-title\">");
            builder.append("<a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse" + i + "\">" +
                           allHeaders[i] + "</a>");
            builder.append("</h4>");
            builder.append(constructClosingTag(DIV_TAG));

            //Render first Tab as open, otherwise closed
            if (i == 0) {
                builder.append("<div id=\"collapse" + i + "\" class=\"panel-collapse collapse in\">");
            } else {
                builder.append("<div id=\"collapse" + i + "\" class=\"panel-collapse collapse\">");
            }
            builder.append("<div class=\"panel-body\">");
            builder.append(allValues[i]);
            builder.append(constructClosingTag(DIV_TAG));
            builder.append(constructClosingTag(DIV_TAG));
            builder.append(constructClosingTag(DIV_TAG));
        }
        builder.append(constructClosingTag(DIV_TAG));

        return builder.toString();
    }

    /**
     * Convenience-Method to check a String for being null or empty
     * @param string to be checked
     * @return Boolean result
     */
    public static Boolean isNullOrEmpty(String string) {
        return (string == null || string.isEmpty());
    }
}
