package com.aryzko.scrabblegame.application.response;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GameFailure {

    public static final String NO_GAME_WITH_ID = "E0000";
    public static final String PLAYER_ID_IS_EMPTY = "E0001";
    public static final String PLAYER_ID_IS_NOT_ACTUAL = "E0002";
    public static final String INCORRECT_MOVE_REQUEST = "E0003";

    @Singular
    private List<Error> errors;

    public record Error(String code, String message) { }
}
