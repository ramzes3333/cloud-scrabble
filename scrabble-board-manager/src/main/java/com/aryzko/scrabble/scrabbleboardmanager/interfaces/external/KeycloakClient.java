package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "keycloak", url = "${KEYCLOAK_SERVER_URL}")
public interface KeycloakClient {

    @PostMapping("/realms/scrabble/protocol/openid-connect/token")
    Map<String, Object> getToken(@RequestBody MultiValueMap<String, String> parameters);
}
