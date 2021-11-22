package org.jarvis.spring.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 此filter可以多次getInputStream或者getReader
 *
 * 参考：https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
 */
public class CachedBodyRequestFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requestToUse;
        if (request instanceof CachedBodyHttpServletRequest) {
            requestToUse = request;
        } else {
            requestToUse = new CachedBodyHttpServletRequest(request);
        }
        super.doFilter(requestToUse, response, chain);
    }
}
