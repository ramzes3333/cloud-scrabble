package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.configuration.websockets;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;

public class GamePrincipal implements Principal {

    private final String name;

    public GamePrincipal(JwtAuthenticationToken token) {
        this.name = token.getName();
    }

    public GamePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
