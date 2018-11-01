package de.ba.bub.studisu.common.integration.util;

/**
 * Holder-Klasse, um eine Correlation-Id thread-lokal zwischen zu speichern.
 */
public class CorrelationIdHolder {
    ThreadLocal<String> correlationId = new ThreadLocal<>();

    /**
     * Setzt thread-lokale Uuid.
     *
     * @param correlationId
     */
    public  void setCorrelationId(String correlationId) {
        this.correlationId.set(correlationId);
    }

    /**
     * Liefert thread-lokale Uuid zurück.
     *
     * @return
     */
    public  String getCorrelationId() {
        return this.correlationId.get();
    }
}