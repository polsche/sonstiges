package de.ba.bub.studisu.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter für HTTP-Methoden.
 * Die <code>security-constraint</code>-Einstellungen aus web.xml 
 * werden von Spring Boot nicht berücksichtigt.
 * <br/>
 * Falls Anwendung nicht mehr unter WebLogic laufen soll (Servlet 2.5), 
 * kann es wieder entfallen und man kann versuchen, Spring mit CORS zu konfigurieren. 
 * 
 * @author lesyukovo
 */
@WebFilter("/*")
public class HttpMethodsFilter implements Filter {
	
	static final String HTTP_METHOD_OPTIONS = "OPTIONS";
	static final String HTTP_METHOD_HEAD = "HEAD";
	
    @Override
    public void destroy() {
        // leer
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) 
    		throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // WebLogic Server hat Servlet 2.5 im Einsatz. 
        // Spring Boot unterschützt Servlet 2.5 offiziell nicht.  
        // Hier wird Internal Server Error bei OPTIONS verhindert.
        if(httpServletRequest.getMethod().equalsIgnoreCase(HTTP_METHOD_OPTIONS)) {
        	HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        	httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        // Es wird für HEAD immer Status 200 liefert, obwohl in web.xml HEAD verbietet ist.
        else if(httpServletRequest.getMethod().equalsIgnoreCase(HTTP_METHOD_HEAD)) {
        	HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        	httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        else {
        	filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // leer
    }
}