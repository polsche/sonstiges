package de.ba.bub.studisu.common.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import de.ba.bub.studisu.common.controller.StudisuController.HTTP_CACHING_TYPE;
import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.exception.EingabeValidierungException;
import de.ba.bub.studisu.common.exception.EingabeZuVieleZeichenException;
import de.ba.bub.studisu.common.exception.ErrorData;
import de.ba.bub.studisu.common.exception.ResponseDataException;

public class StudisuControllerTest {

	private final class ClientAbortException extends ResponseDataException {
		public ClientAbortException(String message) {
			super(message);
		}

		private static final long serialVersionUID = 1L;

	}
	private class MyHttpServletResponse implements HttpServletResponse {
		
		private Map<String, String> args = new HashMap<String, String>();
				

		@Override
		public void flushBuffer() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getBufferSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getCharacterEncoding() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getContentType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isCommitted() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resetBuffer() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setBufferSize(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setCharacterEncoding(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setContentLength(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setContentLengthLong(long arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setContentType(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setLocale(Locale arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addCookie(Cookie arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addDateHeader(String arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addHeader(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addIntHeader(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean containsHeader(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String encodeRedirectURL(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String encodeRedirectUrl(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String encodeURL(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String encodeUrl(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getHeader(String arg0) {
			return args.get(arg0);
		}

		@Override
		public Collection<String> getHeaderNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<String> getHeaders(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getStatus() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void sendError(int arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendError(int arg0, String arg1) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendRedirect(String arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDateHeader(String arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setHeader(String arg0, String arg1) {
			this.args.put(arg0,  arg1);
			
		}

		@Override
		public void setIntHeader(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setStatus(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setStatus(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
	}

	
	@Test
	public void testBehandleMissingParamFehler() {
		StudisuController instance = Mockito.mock(StudisuController.class);
		MissingServletRequestParameterException ex = new MissingServletRequestParameterException("parameterName",
				"parameterTyp");
		Mockito.doCallRealMethod().when(instance).behandleMissingParamFehler(ex);
		ResponseEntity<?> response = Mockito.mock(ResponseEntity.class);
		BDDMockito.willReturn(response).given(instance)
				.behandleValidierungsFehler(Mockito.any(EingabeValidierungException.class));

		ResponseEntity<ErrorData> retResponse = instance.behandleMissingParamFehler(ex);
		Mockito.verify(instance, Mockito.times(1))
				.behandleValidierungsFehler(Mockito.any(EingabeValidierungException.class));
		assertTrue("erwartetes Responseobjekt zurueckerhalten bei MissingParamFehler", response == retResponse);
	}

	@Test
	public void testBehandleValidierungsFehler() {
		StudisuController instance = Mockito.mock(StudisuController.class);
		ResponseDataException ex = new ResponseDataException("myMessage");
		Mockito.doCallRealMethod().when(instance).behandleValidierungsFehler(ex);
		ResponseEntity<?> response = Mockito.mock(ResponseEntity.class);
		Boolean myBoolean = Boolean.FALSE;
		BDDMockito.willReturn(response).given(instance).behandleValidierungsFehler(ex, myBoolean);

		ResponseEntity<ErrorData> retResponse = instance.behandleValidierungsFehler(ex);
		Mockito.verify(instance, Mockito.times(1)).behandleValidierungsFehler(ex, myBoolean);
		assertTrue("erwartetes Responseobjekt zurueckerhalten bei ValidierungsFehler", response == retResponse);
	}

	@Test
	public void testBehandleValidierungsfehlerClientAbortException() {
		// Zweig isClientAbortException true
		StudisuController instance = Mockito.mock(StudisuController.class);
		ResponseDataException ex = new EingabeZuVieleZeichenException("myMessage");
		Boolean myBoolean = Boolean.FALSE;
		Mockito.doCallRealMethod().when(instance).behandleValidierungsFehler(ex, myBoolean);
		Mockito.when(instance.isClientAbortException(ex)).thenReturn(true);
		Mockito.when(instance.erzeugeFehlertext(Mockito.any(ErrorData.class))).thenReturn("mein Fehlertext");
		ResponseEntity<ErrorData> responseEntity = instance.behandleValidierungsFehler(ex, myBoolean);
		assertEquals("bei EingabeZuVieleZeichenException Request-Status BAD_REQUEST erwartet", HttpStatus.BAD_REQUEST,
				responseEntity.getStatusCode());
	}

	@Test
	public void testBehandleValidierungsfehlerMitBooleanTrue() {
		// Zweig isClientAbortException false, Loggen als Fehler
		StudisuController instance = Mockito.mock(StudisuController.class);
		ResponseDataException ex = new CommonServiceException("myMessage");
		Boolean myBoolean = Boolean.TRUE;
		Mockito.doCallRealMethod().when(instance).behandleValidierungsFehler(ex, myBoolean);
		Mockito.when(instance.isClientAbortException(ex)).thenReturn(false);
		Mockito.when(instance.erzeugeFehlertext(Mockito.any(ErrorData.class))).thenReturn("mein Fehlertext");
		ResponseEntity<ErrorData> responseEntity = instance.behandleValidierungsFehler(ex, myBoolean);
		assertEquals("bei CommonServiceException Request-Status INTERNAL_SERVER_ERROR erwartet",
				HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

	}

	@Test
	public void testBehandleValidierungsfehlerMitBooleanFalse() {
		// Zweig isClientAbortException false, Loggen als Warnung
		StudisuController instance = Mockito.mock(StudisuController.class);
		ResponseDataException ex = new EingabeValidierungException("myMessage");
		Boolean myBoolean = Boolean.FALSE;
		Mockito.doCallRealMethod().when(instance).behandleValidierungsFehler(ex, myBoolean);
		Mockito.when(instance.isClientAbortException(ex)).thenReturn(false);
		Mockito.when(instance.erzeugeFehlertext(Mockito.any(ErrorData.class))).thenReturn("mein Fehlertext");
		ResponseEntity<ErrorData> responseEntity = instance.behandleValidierungsFehler(ex, myBoolean);
		assertEquals("bei EingabeValidierungException Request-Status BAD_REQUEST erwartet", HttpStatus.BAD_REQUEST,
				responseEntity.getStatusCode());
	}

	@Test
	public void testBehandleFehlerResponseDataException() {
		StudisuController instance = Mockito.mock(StudisuController.class);
		ResponseDataException ex = new ResponseDataException("myMessage");
		Mockito.doCallRealMethod().when(instance).behandleFehler(ex);
		Mockito.doCallRealMethod().when(instance).behandleValidierungsFehler(ex);
		ResponseEntity<?> response = Mockito.mock(ResponseEntity.class);
		BDDMockito.willReturn(response).given(instance).behandleValidierungsFehler(ex);
		ResponseEntity<ErrorData> retResponse = instance.behandleFehler(ex);
		
		Mockito.verify(instance, Mockito.times(1)).behandleValidierungsFehler(ex);
		assertTrue("erwartetes Responseobjekt zurueckerhalten bei ResponseData-Fehler", response == retResponse);
	}

	@Test
	public void testBehandleConversionFehler() {
		StudisuController instance = Mockito.mock(StudisuController.class);
		ConversionFailedException ex = new ConversionFailedException(null, null, null, null);
		Mockito.doCallRealMethod().when(instance).behandleConversionFehler(ex);
		ResponseEntity<?> response = Mockito.mock(ResponseEntity.class);
		BDDMockito.willReturn(response).given(instance)
				.behandleValidierungsFehler(Mockito.any(EingabeValidierungException.class));

		ResponseEntity<ErrorData> retResponse = instance.behandleConversionFehler(ex);
		Mockito.verify(instance, Mockito.times(1))
				.behandleValidierungsFehler(Mockito.any(EingabeValidierungException.class));
		assertTrue("erwartetes Responseobjekt zurueckerhalten bei ConversionFehler", response == retResponse);
	}

	@Test
	public void testBehandleFehlerException() {
		StudisuController instance = Mockito.mock(StudisuController.class);
		Exception ex = new RuntimeException("myMessage");
		Mockito.doCallRealMethod().when(instance).behandleFehler(ex);
		@SuppressWarnings("unchecked")
		ResponseEntity<ErrorData> response = Mockito.mock(ResponseEntity.class);
		Mockito.when(instance.behandleValidierungsFehler(Mockito.any(CommonServiceException.class), ArgumentMatchers.eq(true)))
				.thenReturn(response);
		ResponseEntity<ErrorData> retResponse = instance.behandleFehler(ex);
		Mockito.verify(instance, Mockito.times(1)).behandleValidierungsFehler(Mockito.any(ResponseDataException.class),
				ArgumentMatchers.eq(true));
		assertTrue("erwartetes Responseobjekt zurueckerhalten bei Exception", response == retResponse);
	}

	@Test
	public void testErzeugeFehlerText() {
		ErrorData errorData = new ErrorData();
		String retFehlertext = new StudisuController().erzeugeFehlertext(errorData);
		String pattern = "[a-zA-Z0-9]{5}";
		assertTrue("erwarteter Beginn und Ende des Fehlertextes",
				retFehlertext.startsWith("[") && retFehlertext.endsWith("] Fehler"));
		assertTrue("erwartetes Logref-Pattern des Fehlertextes: " + pattern,
				retFehlertext.substring(1, 6).matches(pattern));
	}

	@Test
	public void testIsClientAbortExceptionFalse() {
		ResponseDataException ex = new CommonServiceException("myMessage", new NullPointerException());
		boolean ret = new StudisuController().isClientAbortException(ex);
		assertFalse("keine ClientAbortException als Cause uebergeben", ret);
	}

	@Test
	public void testIsClientAbortExceptionFalseOhneCause() {
		ResponseDataException ex = new CommonServiceException("myMessage");
		boolean ret = new StudisuController().isClientAbortException(ex);
		assertFalse("keine Cause uebergeben", ret);
	}

	@Test
	public void testIsClientAbortExceptionCauseTrue() {
		ResponseDataException ex = new CommonServiceException("myMessage", new ClientAbortException("otherMessage"));
		boolean ret = new StudisuController().isClientAbortException(ex);
		assertTrue("Klasse mit Name ClientAbortException uebergeben", ret);
	}
	
	@Test
	public void testIsClientAbortExceptionSelbstTrue() {
		ClientAbortException ex = new ClientAbortException("myMessage");
		boolean ret = new StudisuController().isClientAbortException(ex);
		assertTrue("Klasse mit Name ClientAbortException uebergeben", ret);
	}

	@Test
	public void testSetHttpCacheHeaderStaticContent() {
		// Zweig static content durchlaufen
		StudisuController instance = Mockito.mock(StudisuController.class);
		MyHttpServletResponse myResponse =  new MyHttpServletResponse();
		HTTP_CACHING_TYPE cachingType = HTTP_CACHING_TYPE.STATIC_CONTENT;
		Mockito.doCallRealMethod().when(instance).setHttpCacheHeader(myResponse, cachingType);

		// set caching time which is injected by config via spring
		String cachingTime = "1234";
		Whitebox.setInternalState(instance, "staticContentCachingTime", cachingTime);
		
		instance.setHttpCacheHeader(myResponse, cachingType);

		String expectedValue = "public,max-age=" + cachingTime;
		assertEquals("erwarteter Cache-Control-Wert fuer static content", expectedValue,
				myResponse.getHeader("Cache-Control"));
	}
	
	@Test
	public void testSetHttpCacheHeaderSonstiger() {
		// Zweig else zu static content durchlaufen
		StudisuController instance = Mockito.mock(StudisuController.class);
		MyHttpServletResponse myResponse =  new MyHttpServletResponse();
		HTTP_CACHING_TYPE cachingType = HTTP_CACHING_TYPE.DYNAMIC_CONTENT;
		Mockito.doCallRealMethod().when(instance).setHttpCacheHeader(myResponse, cachingType);

		// set caching time which is injected by config via spring
		String cachingTime = "5678";
		Whitebox.setInternalState(instance, "studienangeboteCachingTime", cachingTime);

		instance.setHttpCacheHeader(myResponse, cachingType);

		String expectedValue = "public,max-age=" + cachingTime;
		assertEquals("erwarteter Cache-Control-Wert fuer static content", expectedValue,
				myResponse.getHeader("Cache-Control"));
	}
}
