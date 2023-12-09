package com.aryzko.scrabble.scrabbleboardmanager.interfaces.common.aspect;

import com.aryzko.scrabble.scrabbleboardmanager.interfaces.external.KeycloakClient;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggedAsSystemUserAspect {

    private final KeycloakClient keycloakClient;

    @Value("${keycloak.system-username}")
    private String systemUserName;
    @Value("${keycloak.system-password}")
    private String systemUserPassword;

    private Jwt currentJwtToken;
    private long tokenExpiryTime = 0L;

    @Before("@annotation(LoggedAsSystemUser)")
    public void before(JoinPoint joinPoint) {
        if (shouldRefreshToken()) {
            refreshToken();
        }

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(currentJwtToken, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean shouldRefreshToken() {
        return currentJwtToken == null || System.currentTimeMillis() > tokenExpiryTime;
    }

    private void refreshToken() {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "password");
        parameters.add("client_id", "scrabble");
        parameters.add("username", systemUserName);
        parameters.add("password", systemUserPassword);

        Map<String, Object> response = keycloakClient.getToken(parameters);
        String accessToken = (String) response.get("access_token");
        String scope = (String) response.get("scope");

        currentJwtToken = Jwt.withTokenValue(accessToken)
                .header("alg", "none")
                .claim("scope", scope)
                .build();

        int expiresIn = (int) response.get("expires_in");
        tokenExpiryTime = System.currentTimeMillis() + (expiresIn * 1000L) - 30000L; // 30 sekund marginesu
    }
}
