package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.request.GameMoveRequest;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.application.validator.MoveValidator;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Player;
import com.aryzko.scrabblegame.domain.model.Type;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.domain.service.GameService;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.aryzko.scrabblegame.application.response.GameFailure.INCORRECT_MOVE_REQUEST;
import static com.aryzko.scrabblegame.application.response.GameFailure.PLAYER_ID_IS_EMPTY;

@Service
@RequiredArgsConstructor
public class MovePerformer {

    private final GameService gameService;
    private final BoardProvider boardProvider;
    private final HumanMovePerformer humanMovePerformer;
    private final BotMovePerformer botMovePerformer;

    public Either<MoveResult, GameFailure> move(GameMoveRequest moveReq) {
        Game game = gameService.getGame(moveReq.getGameId());
        Board board = boardProvider.getBoard(game.getBoardId());

        List<GameFailure.Error> errors = MoveValidator.validate(game, moveReq);

        if(!errors.isEmpty()) {
            return prepareGameFailure(errors);
        }

        MoveResult.MoveResultBuilder resultBuilder = MoveResult.builder();

        Player nextPlayer = getActualPlayer(game);

        if(nextPlayer.getType().equals(Type.HUMAN)) {
            humanMovePerformer.perform(game, board, moveReq.getPlayerId(), moveReq.getTiles()).fold(
                    success -> resultBuilder.playerMove(success),
                    failure -> errors.addAll(failure.getErrors())
            );
        }

        if(!errors.isEmpty()) {
            return prepareGameFailure(errors);
        }

        while ((nextPlayer = getNextPlayerAndUpdateGameActualPlayerId(game)).getType().equals(Type.BOT)) {
            botMovePerformer.perform(game, board, nextPlayer.getId()).fold(
                    success -> resultBuilder.playerMove(success),
                    failure -> errors.addAll(failure.getErrors())
            );
        }

        if(!errors.isEmpty()) {
            return prepareGameFailure(errors);
        }



        return Either.left(resultBuilder.build());
    }

    private static Either<MoveResult, GameFailure> prepareGameFailure(List<GameFailure.Error> errors) {
        return Either.right(GameFailure.builder()
                .errors(errors)
                .build());
    }

    private Player getNextPlayerAndUpdateGameActualPlayerId(Game game) {
        Player nextPlayer = getNextPlayer(game);
        game.setActualPlayerId(nextPlayer.getId());
        return nextPlayer;
    }

    private Player getNextPlayer(Game game) {
        final Integer currentPlayerOrder = getActualPlayer(game).getOrder();

        return game.getPlayers().stream()
                .sorted(Comparator.comparing(Player::getOrder))
                .filter(p -> p.getOrder() > currentPlayerOrder)
                .findFirst()
                .orElse(
                        game.getPlayers().stream()
                                .min(Comparator.comparing(Player::getOrder))
                                .orElseThrow(() -> new IllegalStateException("No players in game!"))
                );
    }

    private Player getActualPlayer(final Game game) {
        return game.getPlayers().stream()
                .filter(p -> p.getId().equals(game.getActualPlayerId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find actual player!"));
    }
}
