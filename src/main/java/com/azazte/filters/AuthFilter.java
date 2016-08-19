package com.azazte.filters;



import com.sun.net.httpserver.HttpExchange;

import javax.servlet.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
