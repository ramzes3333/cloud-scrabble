package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.configuration.websockets;

import com.aryzko.scrabble.scrabbleboardmanager.infrastructure.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Collections;

import static java.util.Optional.ofNullable;

@Slf4j
/*@Component*/
@RequiredArgsConstructor
public class WebSocketAuthenticatorInterceptor implements ApplicationListener<SessionConnectEvent> {

    private final AuthenticationFacade authenticationFacade;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        try {
            String token = getToken(accessor);
            String username = authenticationFacade.getNameFromToken(token);
            accessor.setUser(new GamePrincipal(username));
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }

    private static String getToken(StompHeaderAccessor accessor) {
        return ofNullable(accessor.getNativeHeader("X-Authorization")).orElse(Collections.emptyList()).stream()
                .findFirst()
                .map(value -> value.replace("Bearer", ""))
                .orElseThrow(() -> new IllegalStateException("Invalid token while establishing websocket connection"));
    }
}
