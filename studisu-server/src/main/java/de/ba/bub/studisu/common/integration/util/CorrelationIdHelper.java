package de.ba.bub.studisu.common.integration.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;


/**
 * Tool für WebServiceClient
 */
public final class CorrelationIdHelper {
	/** fixe Werte für Logeintrag Schlüssel */
	public final static String SERVICE_DKZ = "DKZService";
	public final static String SERVICE_BAS = "BildungsangebotService";


    private CorrelationIdHelper() {
        // keine Instanzen erlaubt
    }

    /**
     * Fügt einen CorrelationIdHandler in die Handler-Chain ein.
     *
     * @param bindingProvider
     * @return CorrelationIdHolder
     */
    public static CorrelationIdHolder addCorrelationIdHandler(BindingProvider bindingProvider) {
        CorrelationIdSOAPHandler result = new CorrelationIdSOAPHandler();
        Binding binding = bindingProvider.getBinding();
        @SuppressWarnings("rawtypes")
		List<Handler> hChain = binding.getHandlerChain();
        if (null == hChain) {
            hChain = new ArrayList<>();
        }
        hChain.add(result);
        binding.setHandlerChain(hChain);
        return result.getCorrelationIdHolder();
    }
}