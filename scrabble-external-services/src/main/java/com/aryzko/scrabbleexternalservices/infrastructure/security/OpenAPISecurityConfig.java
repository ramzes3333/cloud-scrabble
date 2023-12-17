package com.aryzko.scrabbleexternalservices.infrastructure.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISecurityConfig {

    @Value("${keycloak.auth-server-url}")
    String authServerUrl;
    @Value("${keycloak.realm}")
    String realm;

    private static final String OPEN_ID_SCHEME_NAME = "openId";
    private static final String OPENID_CONFIG_FORMAT = "%s/realms/%s/.well-known/openid-configuration";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(new Components()
                        .addSecuritySchemes(OPEN_ID_SCHEME_NAME, createOAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(OPEN_ID_SCHEME_NAME));
    }

    private SecurityScheme createOAuthScheme() {
        String connectUrl = String.format(OPENID_CONFIG_FORMAT, authServerUrl, realm);

        return new SecurityScheme()
                .type(SecurityScheme.Type.OPENIDCONNECT)
                .openIdConnectUrl(connectUrl);
    }

}
