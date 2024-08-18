package es.unican.CIBEL.util;

import org.springframework.stereotype.Component;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws javax.servlet.ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, javax.servlet.ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("Request Method: " + req.getMethod() + " Request URI: " + req.getRequestURI());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
