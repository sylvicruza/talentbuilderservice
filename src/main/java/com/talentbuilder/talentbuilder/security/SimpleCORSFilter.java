package com.talentbuilder.talentbuilder.security;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        System.out.println("SIMPLE CORS FILTER HAS BEEN REACHED => " + request.getRequestURL().toString());

        response.setHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method,Access-Control-Request-Headers,Authorization, Cache-Control, Expires");
        if (request.getMethod()
                .equals(HttpMethod.OPTIONS.name())) {

            System.out.println("BROWSER JUST SENT OPTIONS HEADER");

            response.setStatus(HttpStatus.NO_CONTENT.value());
        } else {

            chain.doFilter(req, res);
        }

    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}
