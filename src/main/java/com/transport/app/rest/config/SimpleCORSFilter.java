package com.transport.app.rest.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SimpleCORSFilter.class);
    @Value("${access.control.allow.origin}")
    private String accessControlAllowOrigin;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Initilisation du Middleware");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest requestToUse = (HttpServletRequest)servletRequest;
        HttpServletResponse responseToUse = (HttpServletResponse)servletResponse;

        responseToUse.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        filterChain.doFilter(requestToUse,responseToUse);
    }

    @Override
    public void destroy() {

    }
}
