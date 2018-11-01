package de.ba.bub.studisu.common.integration.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.Test;
import org.mockito.Mockito;

public class CorrelationIdSOAPHandlerTest {
    private static final String CORRELATION_ID_KEY = "BACorrelationID";

	@SuppressWarnings("unchecked")
	@Test
	public final void testHandleMessage() {
		
		SOAPMessageContext context = Mockito.mock(SOAPMessageContext.class);
		when((Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(true);
		Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
		when((Map<String, List<String>>)context.get(MessageContext.HTTP_REQUEST_HEADERS)).thenReturn(requestHeaders);	
		CorrelationIdSOAPHandler correlationIdSOAPHandler = new CorrelationIdSOAPHandler();
		correlationIdSOAPHandler.handleMessage(context);
		List<String> tempListUuids1 = requestHeaders.get(CORRELATION_ID_KEY);
		String tempCorrId = correlationIdSOAPHandler.getCorrelationIdHolder().getCorrelationId();
		
		correlationIdSOAPHandler.handleMessage(context);		
		List<String> tempListUuids2 = requestHeaders.get(CORRELATION_ID_KEY);
		String tempCorrId2 = correlationIdSOAPHandler.getCorrelationIdHolder().getCorrelationId();
		
		assertThat(tempCorrId, not(equalTo(tempCorrId2)));
		assertThat(context.get(MessageContext.HTTP_REQUEST_HEADERS), notNullValue());
		assertThat(tempListUuids1, notNullValue());
		assertThat(tempListUuids1, not(equalTo(tempListUuids2)));
	}

	//Testet Vorhandensein der Methode
	@Test
	public final void testClose() {
		CorrelationIdSOAPHandler correlationIdSOAPHandler = new CorrelationIdSOAPHandler();
		correlationIdSOAPHandler.close(null);
	}
	
	//Testet Vorhandensein der Methode
	@Test
	public final void testGetHeaders() {
		CorrelationIdSOAPHandler correlationIdSOAPHandler = new CorrelationIdSOAPHandler() ;
		correlationIdSOAPHandler.getHeaders();
	}

	//Testet Vorhandensein der Methode
	@Test
	public final void testHandleFault() {
		CorrelationIdSOAPHandler correlationIdSOAPHandler = new CorrelationIdSOAPHandler() ;
		correlationIdSOAPHandler.handleFault(null);
	}

}
