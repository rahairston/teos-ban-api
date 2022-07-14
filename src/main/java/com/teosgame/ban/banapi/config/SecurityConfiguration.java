package com.teosgame.ban.banapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.teosgame.ban.banapi.filters.AccessTokenFilter;
import com.teosgame.ban.banapi.service.AuthenticationService;
import com.teosgame.ban.banapi.util.TokenValidator;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private AuthenticationService authenticationService;
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
            .addFilterBefore(
                new AccessTokenFilter(
                        tokenValidator,
                        authenticationService),
    BasicAuthenticationFilter.class)
            .authorizeHttpRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers( "/actuator/health", "/auth/**", "/error").permitAll()
            .antMatchers("/actuator/**").hasRole("DEVELOPER")
            .anyRequest().authenticated()
            .and()
            .build();
    }
}
