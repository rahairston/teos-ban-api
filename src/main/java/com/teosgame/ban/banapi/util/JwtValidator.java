package com.teosgame.ban.banapi.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.teosgame.ban.banapi.config.TwitchConfig;
import com.teosgame.ban.banapi.exception.InvalidJwtException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final TwitchConfig config;
    private final Credential credential;

    Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    public DecodedJWT validateAndParse(String idToken) throws InvalidJwtException {
        DecodedJWT jwt = JWT.decode(idToken);
        try {
            validateJwt(jwt);
        } catch (JwkException e) {
            logger.error(e.getLocalizedMessage());
            throw new InvalidJwtException(e.getLocalizedMessage());
        }
        return jwt;
    }
    
    private void validateJwt(DecodedJWT jwt) throws JwkException, InvalidJwtException {
        try {
            // Verify signature and algorithm
            URL url = new URL(config.getKeys());
            JwkProvider provider = new UrlJwkProvider(url);
            Jwk jwk = provider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            // Verify Issuer
            if (!config.getIss().equals(jwt.getIssuer())) {
                throw new InvalidJwtException("JWT Issuer \"" + jwt.getIssuer()
                     + "\" does not match expected issuer: \"" + config.getIss() + "\"");
            }

            // Verify Audience
            if (jwt.getAudience().size() != 1 || !credential.getClientId().equals(jwt.getAudience().get(0))) {
                throw new InvalidJwtException("JWT Audience \"" + jwt.getAudience().toString()
                     + "\" does not match expected Audience: \"" + credential.getClientId() + "\"");
            }
        } catch (MalformedURLException e) {
            logger.error("MALFORMED: " + e.getLocalizedMessage());
            throw new InvalidJwtException("URL is malformed. Please contact administrator.");
        }
    }
}
