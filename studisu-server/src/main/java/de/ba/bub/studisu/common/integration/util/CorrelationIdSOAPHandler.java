package de.ba.bub.studisu.common.integration.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import de.ba.bub.studisu.common.integration.util.CorrelationIdHolder;


/**
 * SOAPHandler, der ausgehenden SOAP-Nachrichten eine UUID, Correlation-Id
 * genannt, hinzufügt.
 */
public class CorrelationIdSOAPHandler implements SOAPHandler<SOAPMessageContext> {
    private static final String CORRELATION_ID_KEY = "BACorrelationID";

    private CorrelationIdHolder correlationIdHolder = new CorrelationIdHolder();

    private String getUuid() {
        String verfahren = "10002-";
        String uuid = verfahren + UUID.randomUUID();
        return uuid;
    }

    private Map<String, List<String>> injectUuid(Map<String, List<String>> requestHeaders) {
        String uuid = getUuid();
        correlationIdHolder.setCorrelationId(uuid);
        requestHeaders.put(CORRELATION_ID_KEY, Arrays.asList(uuid));
        return requestHeaders;
    }

    /**
     * Fügt einer ausgehenden SOAP-Nachricht einen HTTP-Request-Header
     * "BACorrelationID" hinzu und setzt dessen Wert auf eine für jede
     * Nachricht neugenerierte UUID. Diese wird in einem Holder thread-lokal
     * gespeichert, so dass nach einem WebService-Call darauf zugegriffen werden
     * kann, um eine Log-Nachricht zu erzeugen.
     *
     * @param context
     * @return boolean immer true
     */
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean outBound =
            (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outBound) {
            // 1. Headers holen
            @SuppressWarnings("unchecked")
			Map<String, List<String>> requestHeaders =
                (Map<String, List<String>>)context.get(MessageContext.HTTP_REQUEST_HEADERS);
            if (null == requestHeaders) {
                requestHeaders = new HashMap<String, List<String>>();
            }

            // 2. Correlation-Id-Header hinzufügen
            injectUuid(requestHeaders);

            // 3. Headers wieder setzen
            context.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
        }
        return true;
    }

    /**
     * Muss implementiert werden. Tut nichts
     * @param context
     */
    public void close(MessageContext context) {
        // tut nichts
    }

    /**
     * Muss implementiert werden. Tut nichts
     * @return
     */
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    /**
     * Muss implementiert werden. Tut nichts
     * @param context
     * @return
     */
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    /**
     * Liefert den CorrelationIdHolder zurück.
     * @return
     */
    public CorrelationIdHolder getCorrelationIdHolder() {
        return correlationIdHolder;
    }
}