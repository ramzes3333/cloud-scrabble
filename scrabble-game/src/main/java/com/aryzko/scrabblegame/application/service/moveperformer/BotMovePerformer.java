package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.domain.model.Game;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class BotMovePerformer {
    public Either<MoveResult.PlayerMove, GameFailure> perform(Game game, Board board, String playerId) {
        return null;
    }
}
