/**
 * 
 */
package de.ba.bub.studisu.common.integration.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author HossainB
 *
 */
public class CorrelationIdHelperTest {

	/**
	 * Test method for {@link de.ba.bub.studisu.common.integration.util.CorrelationIdHelper#addCorrelationIdHandler(javax.xml.ws.BindingProvider)}.
	 */
	@Test
	public final void testAddCorrelationIdHandler() {
		BindingProvider bindingProvider = Mockito.mock(BindingProvider.class);
		Binding binding = Mockito.mock(Binding.class);
		@SuppressWarnings("rawtypes")
		List<Handler> hChain = new ArrayList<>();
		when(bindingProvider.getBinding()).thenReturn(binding);
		when(binding.getHandlerChain()).thenReturn(hChain);
		CorrelationIdHolder correlationIdHolder = CorrelationIdHelper.addCorrelationIdHandler(bindingProvider);
		verify(binding,times(1)).setHandlerChain(hChain);
		assertThat(((CorrelationIdSOAPHandler)hChain.get(0)).getCorrelationIdHolder(), equalTo(correlationIdHolder));
	}

}
