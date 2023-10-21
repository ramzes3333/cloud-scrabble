package com.aryzko.scrabblegame.application.validator;

import com.aryzko.scrabblegame.application.request.GameMoveRequest;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.domain.model.Game;

import java.util.ArrayList;
import java.util.List;

import static com.aryzko.scrabblegame.application.response.GameFailure.NO_GAME_WITH_ID;
import static com.aryzko.scrabblegame.application.response.GameFailure.PLAYER_ID_IS_EMPTY;
import static com.aryzko.scrabblegame.application.response.GameFailure.PLAYER_ID_IS_NOT_ACTUAL;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;

public class MoveValidator {

    public static List<GameFailure.Error> validate(Game game, GameMoveRequest moveReq) {
        List<GameFailure.Error> errors = new ArrayList<>();

        if (game == null) {
            errors.add(new GameFailure.Error(NO_GAME_WITH_ID, format("No game with id: %d", moveReq.getGameId())));
        }

        if (isBlank(moveReq.getPlayerId())) {
            errors.add(new GameFailure.Error(PLAYER_ID_IS_EMPTY, "Player id is empty"));
        }

        if (game != null && !game.getActualPlayerId().equals(moveReq.getPlayerId())) {
            errors.add(new GameFailure.Error(PLAYER_ID_IS_NOT_ACTUAL, format("Player (id: %s) is not the first in queue", moveReq.getPlayerId())));
        }

        return errors;
    }
}
