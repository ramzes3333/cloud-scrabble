package com.aryzko.scrabblegame.infrastructure.db;

import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class GameConverter {
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    public static String toData(Game game) {
        try {
            return OBJECT_MAPPER.writeValueAsString(game);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize object to json", e);
        }
    }

    public static Game fromData(String data) {
        try {
            return OBJECT_MAPPER.readValue(data, Game.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot deserialize json to object", e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.aryzko.scrabblegame.domain.model.")
                        .allowIfSubType("java.util.ArrayList")
                        .build(),
                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        objectMapper.addMixIn(Player.class, PlayerMixIn.class);
        objectMapper.addMixIn(Game.class, GameMixIn.class);

        objectMapper.findAndRegisterModules();

        return objectMapper;
    }

    @JsonIgnoreProperties("type")
    private abstract class PlayerMixIn {
    }

    @JsonIgnoreProperties("id")
    private abstract class GameMixIn {
    }
}
