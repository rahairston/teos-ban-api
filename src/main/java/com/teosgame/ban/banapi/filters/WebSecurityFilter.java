package com.teosgame.ban.banapi.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.teosgame.ban.banapi.service.AuthenticationService;
import com.teosgame.ban.banapi.service.TokenValidatorService;

@Configuration
public class WebSecurityFilter {

    @Autowired
    private TokenValidatorService tokenValidator;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        return http.csrf().disable()
            .addFilterBefore(new AccessTokenFilter(
              tokenValidator,
              authenticationService, 
              resolver),
                BasicAuthenticationFilter.class)
            .authorizeHttpRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers( "/actuator/health", "/auth/**", "/error", "/h2-console/**").permitAll()
            .antMatchers("/actuator/**").hasRole("DEVELOPER")
            .antMatchers("/bannedBy/**", "/evidence/**", "/judgement/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .build();
    }
}
