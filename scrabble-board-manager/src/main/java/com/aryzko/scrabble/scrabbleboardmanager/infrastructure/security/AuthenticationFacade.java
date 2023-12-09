package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {

    private static final String USERNAME_ATTR = "preferred_username";

    private final JwtDecoder jwtDecoder;

    public String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getToken().getClaim(USERNAME_ATTR);
        } else {
            throw new IllegalStateException("Incorrect security context");
        }
    }

    public String getNameFromToken(String token) {
        return parseJwtToken(token).getToken().getClaim(USERNAME_ATTR).toString();
    }

    private JwtAuthenticationToken parseJwtToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return new JwtAuthenticationToken(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Error while decoding JWT", e);
        }
    }
}
