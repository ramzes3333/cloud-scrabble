package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;
import com.aryzko.scrabblegame.application.model.board.Field;
import com.aryzko.scrabblegame.application.model.board.Letter;
import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.model.Tile;
import com.aryzko.scrabblegame.application.model.board.Rack;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.domain.model.Game;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.aryzko.scrabblegame.application.response.GameFailure.INCORRECT_MOVE_REQUEST;

@Component
@RequiredArgsConstructor
public class HumanMovePerformer {

    private final BoardProvider boardProvider;

    public Either<MoveResult.PlayerMove, GameFailure> perform(Game game, Board board, String playerId, List<Tile> tiles) {
        MoveResult.PlayerMove.PlayerMoveBuilder playerMoveBuilder = MoveResult.PlayerMove.builder()
                .playerId(playerId);

        performRackChanges(board, playerId, tiles);
        playerMoveBuilder.moveTiles(performFieldChanges(board, tiles));

        BoardValidationResultResponse validationResponse = boardProvider.validateBoard(board);
        if(!validationResponse.getIncorrectWords().isEmpty() &&
            !validationResponse.getOrphans().isEmpty()) {
            return Either.right(GameFailure.builder()
                    .errors(Collections.singletonList(new GameFailure.Error(INCORRECT_MOVE_REQUEST, "Player id is empty")))
                    .build());
        }

        return Either.left(playerMoveBuilder.build());
    }

    private static void performRackChanges(Board board, String playerId, List<Tile> tiles) {
        Rack playerRack = getPlayerRack(board, playerId);
        for(Tile tile : tiles) {
            removeTileFromRack(playerRack, tile);
        }
    }

    private static void removeTileFromRack(Rack playerRack, Tile tile) {
        playerRack.getLetters().remove(
                playerRack.getLetters().stream()
                        .filter(l -> tile.isBlank() && l.isBlank() || tile.getLetter().equals(l.getLetter()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No tile in player rack!"))
        );
    }

    private static Rack getPlayerRack(Board board, String playerId) {
        return board.getRacks().stream()
                .filter(r -> r.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No rack for playerId: %s".formatted(playerId)));
    }

    private static List<Tile> performFieldChanges(Board board, List<Tile> tiles) {
        List<Tile> placedTiles = new ArrayList<>();

        for(Tile tile : tiles) {
            Field field = board.getField(tile.getX(), tile.getY());
            if(field.getLetter() == null) {
                field.setLetter(new Letter(tile.getLetter(), tile.getPoints(), tile.isBlank()));
                placedTiles.add(tile);
            }
        }

        return placedTiles;
    }
}
