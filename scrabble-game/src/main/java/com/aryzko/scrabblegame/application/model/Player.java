package com.aryzko.scrabblegame.application.model;

import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.HumanPlayer;
import lombok.Builder;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
@Builder
public class Player {
    private String id;
    private Type type;
    private Integer order;
    private Map<String, String> parameters;

    public enum Type {
        HUMAN,
        BOT
    }

    public static Player of(com.aryzko.scrabblegame.domain.model.Player player) {
        return Player.builder()
                .id(player.getId())
                .type(Type.valueOf(player.getType().name()))
                .order(player.getOrder())
                .parameters(prepareParameters(player))
                .build();
    }

    private static Map<String, String> prepareParameters(com.aryzko.scrabblegame.domain.model.Player player) {
        Map<String, String> parameters = new HashMap<>();
        switch (player.getType()) {
            case HUMAN -> parameters.put("login", ((HumanPlayer)player).getLogin());
            case BOT -> parameters.put("level", ((BotPlayer)player).getLevel().toString());
        }
        return parameters;
    }
}
