package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.BoardTile;
import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.model.Tile;
import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;
import com.aryzko.scrabblegame.application.model.board.Field;
import com.aryzko.scrabblegame.application.model.board.Letter;
import com.aryzko.scrabblegame.application.model.board.Rack;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.provider.TileProvider;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Move;
import com.aryzko.scrabblegame.domain.model.Player;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.aryzko.scrabblegame.application.response.GameFailure.INCORRECT_MOVE_REQUEST;

@RequiredArgsConstructor
public abstract class PlayerMovePerformer {

    private final BoardProvider boardProvider;
    private final TileProvider tileProvider;

    public Either<MoveResult.PlayerMove, GameFailure> perform(Game game, Board board, String playerId, List<BoardTile> tiles) {
        MoveResult.PlayerMove.PlayerMoveBuilder playerMoveBuilder = MoveResult.PlayerMove.builder()
                .playerId(playerId);

        BoardProvider.ScoreResult result = boardProvider.scoreWord(board, prepareWord(tiles));

        performRackChanges(board, playerId, tiles);
        playerMoveBuilder.tiles(convertBoardTile(performFieldChanges(board, tiles)));
        playerMoveBuilder.word(result.word());

        BoardValidationResultResponse validationResponse = boardProvider.validateBoard(board);
        if(!validationResponse.getIncorrectWords().isEmpty() &&
                !validationResponse.getOrphans().isEmpty()) {
            return Either.right(GameFailure.builder()
                    .errors(Collections.singletonList(new GameFailure.Error(INCORRECT_MOVE_REQUEST, "Player id is empty")))
                    .build());
        }

        List<com.aryzko.scrabblegame.application.provider.model.Tile> newTiles = tileProvider.getTiles(board.getId(), tiles.size());
        board.getRack(playerId).getLetters().addAll(convertTile(newTiles));

        Player player = updatePlayerData(playerId, game, result);

        playerMoveBuilder.movePoints(result.points());
        playerMoveBuilder.allPoints(player.getPoints());

        return Either.left(playerMoveBuilder.build());
    }

    private static Player updatePlayerData(String playerId, Game game, BoardProvider.ScoreResult result) {
        Player player = getPlayer(game, playerId);
        player.setPoints(player.getPoints() + result.points());
        player.getMoveHistory().add(Move.builder()
                .order(player.getMoveHistory().size()+1)
                .gameOrder(game.getPlayers().stream().mapToInt(p -> p.getMoveHistory().size()).sum()+1)
                .word(result.word())
                .tiles(result.tiles())
                .points(result.points())
                .build());
        return player;
    }

    private static Player getPlayer(Game game, String playerId) {
        return game.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player with id: %s".formatted(playerId)));
    }

    private BoardProvider.Tiles prepareWord(List<BoardTile> tiles) {
        return new BoardProvider.Tiles(tiles.stream()
                .map(t -> new BoardProvider.Tile(t.getX(), t.getY(), t.getLetter(), t.isBlank()))
                .toList());
    }

    public List<Tile> convertBoardTile(List<BoardTile> tiles) {
        return tiles.stream()
                .map(t -> Tile.builder()
                        .x(t.getX())
                        .y(t.getY())
                        .letter(t.getLetter())
                        .blank(t.isBlank())
                        .points(t.getPoints())
                        .build())
                .toList();
    }

    public List<Letter> convertTile(List<com.aryzko.scrabblegame.application.provider.model.Tile> tiles) {
        return tiles.stream()
                .map(t -> new Letter(t.getLetter(), t.getPoints(), t.getLetter().equals(' ')))
                .toList();
    }

    private static void performRackChanges(Board board, String playerId, List<BoardTile> tiles) {
        Rack playerRack = board.getRack(playerId);
        for(BoardTile tile : tiles) {
            removeTileFromRack(playerRack, tile);
        }
    }

    private static void removeTileFromRack(Rack playerRack, BoardTile tile) {
        playerRack.getLetters().remove(
                playerRack.getLetters().stream()
                        .filter(l -> tile.isBlank() && l.isBlank()
                                || tile.getLetter().equals(l.getLetter()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No tile in player rack!"))
        );
    }

    private static List<BoardTile> performFieldChanges(Board board, List<BoardTile> tiles) {
        List<BoardTile> placedTiles = new ArrayList<>();

        for(BoardTile tile : tiles) {
            Field field = board.getField(tile.getX(), tile.getY());
            if(field.getLetter() == null) {
                field.setLetter(new Letter(tile.getLetter(), tile.getPoints(), tile.isBlank()));
                placedTiles.add(tile);
            }
        }

        if(placedTiles.size() != tiles.size()) {
            throw new IllegalStateException("Problem with placing tiles on board");
        }

        return placedTiles;
    }
}
