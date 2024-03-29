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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.teosgame.ban.banapi.exception.BaseException;
import com.teosgame.ban.banapi.service.TokenValidatorService;

public class AccessTokenFilter extends AbstractAuthenticationProcessingFilter {
    private final TokenValidatorService validator;
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    private static final NegatedRequestMatcher matcher = new NegatedRequestMatcher(
        new OrRequestMatcher(                                                                                     
            new AntPathRequestMatcher("/auth/**"),                                                                                   
            new AntPathRequestMatcher("/actuator/health"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString())                                                                  
        )
    );

    public AccessTokenFilter(TokenValidatorService validator, AuthenticationManager authenticationManager, HandlerExceptionResolver resolver) {
        super(matcher);
        setAuthenticationManager(authenticationManager);
        this.resolver = resolver;
        this.validator = validator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        logger.debug("Attempting to authenticate for a request {}", request.getRequestURI());

        String authorizationHeader = extractAuthorizationHeaderAsString(request);
        return this.getAuthenticationManager()
                .authenticate(validator.validateToken(authorizationHeader, request.getRequestedSessionId()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        logger.debug("Successfully authentication for the request {}", request.getRequestURI());

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        logger.error("Error occured on authentication attempt {}", failed.getMessage());
        resolver.resolveException(request, response, null, new BaseException(failed.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    private String extractAuthorizationHeaderAsString(HttpServletRequest request) throws AuthenticationCredentialsNotFoundException {
        try {
            return request.getHeader("Authorization");
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("There is no Authorization header in the request");
        }
    }
}
