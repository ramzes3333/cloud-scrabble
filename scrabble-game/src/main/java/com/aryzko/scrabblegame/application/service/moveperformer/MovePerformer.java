package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.request.GameMoveRequest;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.application.validator.MoveValidator;
import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Player;
import com.aryzko.scrabblegame.domain.model.State;
import com.aryzko.scrabblegame.domain.model.Type;
import com.aryzko.scrabblegame.domain.service.GameProvider;
import com.aryzko.scrabblegame.domain.service.GameUpdater;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovePerformer {

    private final GameProvider gameProvider;
    private final GameUpdater gameUpdater;
    private final BoardProvider boardProvider;
    private final HumanMovePerformer humanMovePerformer;
    private final BotMovePerformer botMovePerformer;

    public Either<MoveResult, GameFailure> move(GameMoveRequest moveReq) {
        Game game = gameProvider.getGame(moveReq.getGameId());
        Board board = boardProvider.getBoard(game.getBoardId().toString());

        List<GameFailure.Error> errors = MoveValidator.validate(game, moveReq);

        if(!errors.isEmpty()) {
            return prepareGameFailure(errors);
        }

        if(game.getState().equals(State.NOT_STARTED)) {
            game.setState(State.STARTED);
        }

        MoveResult.MoveResultBuilder resultBuilder = MoveResult.builder();

        Player actualPlayer = getActualPlayer(game);
        String startPlayerId = actualPlayer.getId();

        if(actualPlayer.getType().equals(Type.HUMAN)) {
            humanMovePerformer.perform(game, board, moveReq.getPlayerId(), moveReq.getTiles()).fold(
                    success -> resultBuilder.playerMove(success),
                    failure -> errors.addAll(failure.getErrors())
            );
            actualPlayer = getNextPlayerAndUpdateGameActualPlayerId(game);
        }

        if(!errors.isEmpty()) {
            return prepareGameFailure(errors);
        }

        do {
            botMovePerformer.perform(game, board, (BotPlayer)actualPlayer).fold(
                    success -> resultBuilder.playerMove(success),
                    failure -> errors.addAll(failure.getErrors())
            );
        } while ((actualPlayer = getNextPlayerAndUpdateGameActualPlayerId(game)).getType().equals(Type.BOT) && !startPlayerId.equals(actualPlayer.getId()));

        if(!errors.isEmpty()) {
            return prepareGameFailure(errors);
        }

        // TODO to migrate to async process
        gameUpdater.saveGame(game);
        boardProvider.update(board);

        resultBuilder.actualPlayerId(getActualPlayer(game).getId());
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
