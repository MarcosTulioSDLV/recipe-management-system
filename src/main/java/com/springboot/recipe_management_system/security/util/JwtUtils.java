package com.springboot.recipe_management_system.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt-secret-key}")
    private String secretKey;

    @Value("${jwt-expiration-time}")
    private Integer expirationTime;

    public String generateToken(Authentication authentication){
        try {
            Algorithm algorithm= Algorithm.HMAC256(secretKey);
            String username= authentication.getName();

            String commaSeparatedAuthorities= authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(username)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(getExpirationTime())
                    .withClaim("authorities",commaSeparatedAuthorities)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new JWTCreationException("Error generation JWT token!",e);
        }
    }

    private Instant getExpirationTime(){
        return Instant.now().plus(expirationTime, ChronoUnit.MINUTES);
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTCreationException("Invalid JWT Token!",e);
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    public Claim extractSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    public Map<String,Claim> extractAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }

}
