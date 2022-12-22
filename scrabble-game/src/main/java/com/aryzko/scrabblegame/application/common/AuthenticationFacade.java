package com.aryzko.scrabblegame.application.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    private static final String USERNAME_ATTR = "preferred_username";

    public String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getToken().getClaim(USERNAME_ATTR);
        } else {
            throw new IllegalStateException("Incorrect security context");
        }
    }
}
