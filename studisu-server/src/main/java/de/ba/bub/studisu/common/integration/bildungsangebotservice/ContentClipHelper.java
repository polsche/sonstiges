package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Diese Helper-Klasse erlaubt das Kürzen von HTML-Texten.
 * 
 * Die Texte werden anhand von Zeichenlänge (ohne Tags) und Zeilenlänge (mit Tag-Zählung) gekürzt.
 * 
 * Schließende Tags bleiben erhalten.
 * 
 * Die Implementierung erfolgt klassisch als Akzeptor in Form einer Finite-State-Machine (Endlicher Automat), die die
 * folgenden Zustandsflags hat:
 * 
 * - isInTag<br>
 * - isClosingTag<br>
 * - isSelfClosingTag<br>
 * 
 * ACHTUNG, die Implementierung ist nicht Thread-safe. Ggf. eine neue Instanz erzeugen. 
 * 
 * @author StraubP
 * @author SchneideK084
 */
public class ContentClipHelper {

	/**
	 * Menge von Tags, die als "neue Zeile" gezählt werden.
	 */
	private static Set<String> linebreaks = new HashSet<String>();
	static {
		linebreaks.add("h4");
		linebreaks.add("br");
		linebreaks.add("p");
		linebreaks.add("li");
	}

	/**
	 * Stringbuilder für die Speicherung des Ergebnistextes.
	 */
	private StringBuilder sb = new StringBuilder();

	/**
	 * Stringbuilder, in den das aktuelle Tag eingelesen wird.
	 */
	private StringBuilder tag = new StringBuilder();

	/**
	 * Stack mit den aktuell geöffneten Tags.
	 */
	private Deque<String> stack = new ArrayDeque<String>();

	/**
	 * Flag, das angibt, ob gerade ein Tag eingelesen wird.
	 */
	private boolean isInTag;

	/**
	 * Flag, das angibt, ob es sich um ein schließendes Tag handelt.
	 */
	private boolean isClosingTag;

	/**
	 * Flag, das angibt, ob es sich um ein selbstschließendes Tag handelt.
	 */
	private boolean isSelfclosingTag;

	/**
	 * Anzahl sichtbarer Zeichen in der Ausgabe.
	 */
	private int textLength;

	/**
	 * Speicher für das jeweils zuvor eingelesene Zeichen.
	 */
	private char previousChar;

	/**
	 * Anzahl der erkannten Zeilen.
	 */
	private int lines;

	/**
	 * Kürzt den übergebenen HTML-Text bei Bedarf auf die angegebene Anzahl Zeichen + Zeilen.
	 * 
	 * Tags werden nicht als Zeichen gezählt. Als Zeile zählt jedes "p", "br", "h4" oder "li" Element.
	 * 
	 * Etwaige geöffnete Tags werden ordnungsgemäß geschlossen, um einen validen gekürzten Text zu erhalten.
	 *
	 * @param inhalt
	 *            Der ggf. zu kürzende Textinhalt.
	 * @param maxZeichen
	 *            Die maximale Zeichenzahl im Textinhalt.
	 * @param maxZeilen
	 *            Die maximale Zeilenzahl im Textinhalt.
	 * @return Der ggf. gekürzte HTML-Text.
	 */
	public String clipContent(String inhalt, int maxZeichen, int maxZeilen) {

		resetState();

		for (int i = 0; i < inhalt.length(); i++) {
			char currentChar = inhalt.charAt(i);

			if (currentChar == '<') {
				isInTag = true;
			} else if (isInTag) {
				parseTag(currentChar);
			} else {
				textLength++;
			}

			if (textLength > maxZeichen || lines > maxZeilen) {
				if (lines > maxZeilen) {
					sb.append(currentChar);
				}
				sb.append("&hellip;");
				writeClosingTags();
				break;
			}

			sb.append(currentChar);
			previousChar = currentChar;
		}

		return sb.toString();
	}

	/**
	 * Initialisiert das Objekt für einen neuen Durchgang.
	 */
	private void resetState() {
		sb.setLength(0);
		tag.setLength(0);
		stack.clear();
		isInTag = false;
		isClosingTag = false;
		isSelfclosingTag = false;
		textLength = 0;
		previousChar = '0';
		lines = 0;
	}

	/**
	 * Führt das Parsen des Textes innerhalb eines HTML-Tags durch.
	 * 
	 * @param currentChar
	 *            Das aktuelle Zeichen im Akzeptor.
	 */
	private void parseTag(char currentChar) {
		if (currentChar == '>') {
			finalizeParsedTag();
		} else if (currentChar == '/') {
			isClosingTag = previousChar == '<';
		} else {
			tag.append(currentChar);
		}
	}

	/**
	 * Schließt das Parsen eines HTML-Tags ab (letztes Zeichen ">" im Tag).
	 */
	private void finalizeParsedTag() {
		isSelfclosingTag = previousChar == '/';
		String tagName = tag.toString();
		if (isClosingTag) {
			handleClosingTag(tagName);
		} else {
			handleOpeningOrSelfClosingTag(tagName);
		}
		tag.setLength(0);
		isSelfclosingTag = false;
		isClosingTag = false;
		isInTag = false;
	}

	/**
	 * Behandelt ein schließendes HTML-Tag.
	 * 
	 * @param tagName
	 *            Name des zu schließenden Tags.
	 */
	private void handleClosingTag(String tagName) {
		String pop = stack.pop();
		if (!tagName.equalsIgnoreCase(pop)) {
			throw new IllegalStateException("closing tag '" + tagName + "' while tag '" + pop + "' was open");
		}
	}

	/**
	 * Behandelt ein öffnendes HTML-Tag oder eines, das für sich alleine steht ("self-closing").
	 * 
	 * @param tagName
	 *            Name des zu öffnenden Tags.
	 */
	private void handleOpeningOrSelfClosingTag(String tagName) {
		if (!isSelfclosingTag) {
			// opening tag
			stack.push(tagName);
		}
		if (linebreaks.contains(tagName)) {
			lines++;
		}
	}

	/**
	 * Fügt alle derzeit noch offenen Tags als Strings an den Ausgabepuffer an.
	 */
	private void writeClosingTags() {
		while (!stack.isEmpty()) {
			sb.append("</").append(stack.pop()).append(">");
		}
	}

}
