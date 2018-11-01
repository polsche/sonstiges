package de.ba.bub.studisu.common.filter;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;

public class HttpMethodsFilterTest {

	@Test
	public void testDestroy() {
		// Methode hat leere Implementierung
		new HttpMethodsFilter().destroy();
	}
	
	@Test
	public void testInit() {
		//Methode hat leere Implementierung
		try {
			FilterConfig filterConfigMock = Mockito.mock(FilterConfig.class);
			new HttpMethodsFilter().init(filterConfigMock);
		} catch (ServletException ex) {
			fail("leer implementierte Methode darf keine Exception werfen");
		}
	}

	@Test
	public void testDoFilterMethodOptions() {
		HttpServletRequest servletRequestMock = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse servletResponseMock = Mockito.mock(HttpServletResponse.class);
		FilterChain filterChainMock = Mockito.mock(FilterChain.class);
		Mockito.when(servletRequestMock.getMethod()).thenReturn(HttpMethodsFilter.HTTP_METHOD_OPTIONS);
		try {
		    new HttpMethodsFilter().doFilter(servletRequestMock, servletResponseMock, filterChainMock);
		    Mockito.verify(servletResponseMock, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);
		    Mockito.verify(servletResponseMock, Mockito.times(0)).sendError(Mockito.anyInt());
		    Mockito.verify(filterChainMock, Mockito.times(0)).doFilter(servletRequestMock, servletResponseMock);
		} catch (ServletException ex) {
			fail("doFilter (Method Options) sollte keine ServletException werfen");
		} catch (IOException ex) {
			fail("doFilter (Method Options) sollte keine IOException werfen");
		}
	}
	
	@Test
	public void testDoFilterMethodHead() {
		HttpServletRequest servletRequestMock = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse servletResponseMock = Mockito.mock(HttpServletResponse.class);
		FilterChain filterChainMock = Mockito.mock(FilterChain.class);
		Mockito.when(servletRequestMock.getMethod()).thenReturn(HttpMethodsFilter.HTTP_METHOD_HEAD);
		try {
		    new HttpMethodsFilter().doFilter(servletRequestMock, servletResponseMock, filterChainMock);
		    Mockito.verify(servletResponseMock, Mockito.times(1)).sendError(HttpServletResponse.SC_FORBIDDEN);
		    Mockito.verify(servletResponseMock, Mockito.times(0)).setStatus(Mockito.anyInt());
		    Mockito.verify(filterChainMock, Mockito.times(0)).doFilter(servletRequestMock, servletResponseMock);
		} catch (ServletException ex) {
			fail("doFilter (Method Head) sollte keine ServletException werfen");
		} catch (IOException ex) {
			fail("doFilter (Method Head) sollte keine IOException werfen");
		}
	}
	
	@Test
	public void testDoFilterMethodOther() {
		HttpServletRequest servletRequestMock = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse servletResponseMock = Mockito.mock(HttpServletResponse.class);
		FilterChain filterChainMock = Mockito.mock(FilterChain.class);
		Mockito.when(servletRequestMock.getMethod()).thenReturn("andere Methode");
		try {
		    new HttpMethodsFilter().doFilter(servletRequestMock, servletResponseMock, filterChainMock);
		    Mockito.verify(filterChainMock, Mockito.times(1)).doFilter(servletRequestMock, servletResponseMock);
		    Mockito.verify(servletResponseMock, Mockito.times(0)).setStatus(Mockito.anyInt());
		    Mockito.verify(servletResponseMock, Mockito.times(0)).sendError(Mockito.anyInt());
		} catch (ServletException ex) {
			fail("doFilter (Method Other) sollte keine ServletException werfen");
		} catch (IOException ex) {
			fail("doFilter (Method Other) sollte keine IOException werfen");
		}
	}
	


	

}
