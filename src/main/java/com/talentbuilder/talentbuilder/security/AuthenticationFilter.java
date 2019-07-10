package com.talentbuilder.talentbuilder.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.talentbuilder.talentbuilder.constant.AppConstants;
import com.talentbuilder.talentbuilder.dto.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AppConstants appConstants;

    private static Logger logger = LogManager.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        response.addHeader("X-Powered-By", appConstants.APP_NAME);

        Optional<String> authorization = Optional.fromNullable(httpRequest.getHeader("Authorization"));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        logger.info("RESOUCE PATH => " + resourcePath);

        try {

            if (resourcePath.contains("/api-docs") || resourcePath.contains("/actuator") || resourcePath.contains("/health") || resourcePath.contains("/swagger-ui.html") || resourcePath.contains("/webjars") || resourcePath.equalsIgnoreCase("/api/v1")
                || resourcePath.contains("/swagger-resources") || resourcePath.contains("/docs") || resourcePath.contains("/user") || resourcePath.contains("/application") || resourcePath.contains("/invoice") || resourcePath.contains("/service")) {
                logger.info("REQUEST IS FOR API DOCS");
                chain.doFilter(request, response);
                return;
            }
            logger.info("REQUEST IS NOT FOR API DOCS");

            if (!authorization.isPresent()) {
                throw new BadCredentialsException("Authorization must be provided");
            }

            String[] authParts = authorization.get().split("\\s+");
            
            if (authParts.length != 2) {
                throw new InternalAuthenticationServiceException("Invalid authorization token");
            }

            logger.info("AUTHENTICATION IS COMPLETE BY FILTER CHAIN ... NOW PASSING TO THE NEXT CHAIN");

            chain.doFilter(request, response);

        } catch (BadCredentialsException e){


        }catch (InternalAuthenticationServiceException | NullPointerException | UsernameNotFoundException ex) {
   
        	logger.info("REQUEST IS NOT FOR API DOCS");
        	
            SecurityContextHolder.clearContext();
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.setMessage("Failed");
            serverResponse.setData(ex.getMessage());
            serverResponse.setSuccess(false);
            String jsonResponse = new ObjectMapper().writeValueAsString(serverResponse);
            httpResponse.addHeader("Content-Type", "application/json");

            httpResponse.getWriter().print(jsonResponse);
        }catch (Exception e){
            ServerResponse serverResponse = new ServerResponse();

            if (e.getMessage().contains("Access is denied")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                serverResponse.setData("");
                serverResponse.setMessage("Access is denied. You may not have the appropriate permissions to access the data");
            }else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                serverResponse.setData("");
                serverResponse.setMessage(e.getMessage());
            }
            serverResponse.setSuccess(false);
            String jsonResponse = new ObjectMapper().writeValueAsString(serverResponse);
            httpResponse.addHeader("Content-Type", "application/json");
            httpResponse.getWriter().write(jsonResponse);
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();

        } finally {
            httpResponse.getOutputStream()
                .flush();
            httpResponse.getOutputStream()
                .close();
        }

    }

}