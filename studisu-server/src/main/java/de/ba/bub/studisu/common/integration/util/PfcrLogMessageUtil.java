package de.ba.bub.studisu.common.integration.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.DKZServiceFaultException;
import de.baintern.dst.services.wsdl.basis.systematiken.dkzservice_v_2.FehlerResponseMessage;
import de.baintern.dst.services.wsdl.operativ.unterstuetzung.bildungsangebotservice_v_1.BildungsangebotServiceFaultException;

/**
 * Hilfsklasse zum Erstellen von Messages für pfcr.log.
 *
 * @author KayaH005
 *
 */
public class PfcrLogMessageUtil {

	/** generelle Trennzeichen, etc. für Logeintrag */
	private final static String SEPARATOR_PIPE = "|";
	private final static String EQUAL_SIGN = "=";
	private final static String QUOTE = "\"";

	/** Formate, Property-Namen, etc. für Logeintrag */
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSSZ";
	private final static String SERVERNAME_SYSPROP = "weblogic.Name";

	/** Schlüssel für Logeintrag */
	private final static String CORRELATION_ID = "correlation_id";
	private final static String PROVIDER = "provider";
	private final static String VERFAHREN = "verfahren";
	private final static String SERVERNAME = "servername";
	private final static String SERVICE = "service";
	private final static String METHODE = "methode";
	private final static String DAUER = "dauer";
	private final static String FEHLERKENNZEICHEN = "fehlerkennzeichen";
	private final static String GROESSE = "groesse";
	private final static String FREITEXT = "freitext";

	/** fixe Werte für Logeintrag Schlüssel */
	private final static String PROVIDER_WERT_CONSUMER = "C";
	private final static String VERFAHREN_WERT = "STUDIENSUCHE";
	public final static String SERVICE_DKZ = "DKZService";
	public final static String SERVICE_BAS = "BildungsangebotService";
	private final static String FEHLERKENNZEICHEN_WERT = "server";

	private PfcrLogMessageUtil() {
	}

	/**
	 * Erstellt aus den übergebenen Werten und den notwendigen Fixwerten eine
	 * Message für die Ausgabe in die Datei pfcr.log.
	 *
	 * @param service
	 *            aufgerufener service.
	 * @param methode
	 *            aufgerufene Methode.
	 * @param dauer
	 *            Dauer der Ausführung in ms.
	 * @param fehler
	 *            evtl. aufgetretener Fehler.
	 * @param anzahlElemente
	 *            Anzahl Elemente in Ergebnisliste.
	 * @param uuid
	 * 			  Die Correlation-UUID für das Logging.
	 * @return Log-Message für die Ausgabe in die Datei pfcr.log.
	 */
	public static String getLogMessage(String service, String methode, long dauer, Exception fehler, Integer anzahlElemente, String uuid){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.GERMANY);

		Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
		StringBuilder message = new StringBuilder(sdf.format(cal.getTime()));
		message.append(SEPARATOR_PIPE).append(CORRELATION_ID).append(EQUAL_SIGN).append(QUOTE).append(uuid)
				.append(QUOTE).append(SEPARATOR_PIPE).append(PROVIDER).append(EQUAL_SIGN).append(QUOTE)
				.append(PROVIDER_WERT_CONSUMER).append(QUOTE).append(SEPARATOR_PIPE).append(VERFAHREN)
				.append(EQUAL_SIGN).append(QUOTE).append(VERFAHREN_WERT).append(QUOTE).append(SEPARATOR_PIPE)
				.append(SERVERNAME).append(EQUAL_SIGN).append(QUOTE)
				.append(System.getProperties().getProperty(SERVERNAME_SYSPROP)).append(QUOTE).append(SEPARATOR_PIPE)
				.append(SERVICE).append(EQUAL_SIGN).append(QUOTE).append(service).append(QUOTE)
				.append(SEPARATOR_PIPE).append(METHODE).append(EQUAL_SIGN).append(QUOTE).append(methode).append(QUOTE)
				.append(SEPARATOR_PIPE).append(DAUER).append(EQUAL_SIGN).append(QUOTE).append(dauer).append(QUOTE)
				.append(getFehlerkennzeichen(fehler)).append(SEPARATOR_PIPE).append(GROESSE).append(EQUAL_SIGN)
				.append(QUOTE).append(anzahlElemente != null ? anzahlElemente : "").append(QUOTE).append(getFreitext(fehler));

		return message.toString();
	}

	/**
	 * Prüft den übergebenen Fehler und gibt bei Vorhandensein eines Fehlers
	 * eine entsprechende Erweiterung für den Schlüssel 'fehlerkennzeichen' für
	 * die Log-Message zurück. Message zurück.
	 *
	 * @param fehler
	 *            evtl. aufgetretener Fehler.
	 * @return Leerstring oder entsprechende Erweiterung für den Schlüssel
	 *         'fehlerkennzeichen' für die Log-Message.
	 */
	private static String getFehlerkennzeichen(Exception fehler) {
		if (fehler != null) {
			StringBuilder message = new StringBuilder(SEPARATOR_PIPE);
			message.append(FEHLERKENNZEICHEN).append(EQUAL_SIGN).append(QUOTE).append(FEHLERKENNZEICHEN_WERT)
					.append(QUOTE);
			return message.toString();
		}
		return "";
	}

	/**
	 * Prüft den übergebenen Fehler und gibt bei Vorhandensein eines Fehlers
	 * eine entsprechende Erweiterung für den Schlüssel 'freitext' für die
	 * Log-Message zurück.
	 *
	 * @param fehler
	 *            evtl. aufgetretener Fehler.
	 * @return Leerstring oder entsprechende Erweiterung für den Schlüssel
	 *         'freitext' für die Log-Message.
	 */
	private static String getFreitext(Exception fehler) {
		if (fehler != null) {
			String freitext = "";
			if (fehler instanceof DKZServiceFaultException || fehler instanceof BildungsangebotServiceFaultException) {
				freitext = getFaultInfo(fehler);
			} else if (fehler instanceof FehlerResponseMessage) {
				FehlerResponseMessage ex = (FehlerResponseMessage) fehler;
				if (ex.getFaultInfo() != null) {
					freitext = ex.getFaultInfo().getFehlermeldung();
				}
			}
			StringBuilder message = new StringBuilder(SEPARATOR_PIPE);
			message.append(FREITEXT).append(EQUAL_SIGN).append(QUOTE).append(freitext).append(QUOTE);
			return message.toString();
		}
		return "";
	}

	private static String getFaultInfo(Exception fehler) {
		String freitext = "";

		if (fehler instanceof DKZServiceFaultException) {
			DKZServiceFaultException ex = (DKZServiceFaultException) fehler;

			if (ex.getFaultInfo() != null && ex.getFaultInfo().getFehler() != null
					&& !ex.getFaultInfo().getFehler().isEmpty()) {
				freitext = ex.getFaultInfo().getFehler().get(0).getFehlermeldung();
			}
		} else if (fehler instanceof BildungsangebotServiceFaultException ) {
			BildungsangebotServiceFaultException ex = (BildungsangebotServiceFaultException) fehler;

			if (ex.getFaultInfo() != null && ex.getFaultInfo().getFaultInfo() != null) {
				freitext = ex.getFaultInfo().getFaultInfo();
			}
		}

		return freitext;

	}
}
