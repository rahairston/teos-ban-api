package com.teosgame.ban.banapi.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.teosgame.ban.banapi.util.TokenValidator;

public class AccessTokenFilter extends AbstractAuthenticationProcessingFilter {
    private final TokenValidator validator;

    Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    public AccessTokenFilter(TokenValidator validator, AuthenticationManager authenticationManager) {
        super("/actuator/health");
        setAuthenticationManager(authenticationManager);
        this.validator = validator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        logger.info("Attempting to authenticate for a request {}", request.getRequestURI());

        String authorizationHeader = extractAuthorizationHeaderAsString(request);
        return this.getAuthenticationManager()
                .authenticate(validator.validateToken(authorizationHeader, request.getRequestedSessionId()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        logger.info("Successfully authentication for the request {}", request.getRequestURI());

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private String extractAuthorizationHeaderAsString(HttpServletRequest request) throws AuthenticationCredentialsNotFoundException {
        try {
            return request.getHeader("Authorization");
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("There is no Authorization header in a request");
        }
    }
}
