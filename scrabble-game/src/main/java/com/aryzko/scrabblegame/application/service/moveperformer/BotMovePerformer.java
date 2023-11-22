package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.BoardTile;
import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.Letter;
import com.aryzko.scrabblegame.application.model.board.Rack;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.provider.TileProvider;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.application.service.botstrategies.BotWordSearcherStrategy;
import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Level;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
public class BotMovePerformer extends PlayerMovePerformer {

    public BotMovePerformer(BoardProvider boardProvider,
                            TileProvider tileProvider,
                            List<BotWordSearcherStrategy> strategies) {

        super(boardProvider, tileProvider);
        strategies.forEach(strategy -> botWordSearcherStrategies.put(strategy.level(), strategy));
    }

    private final Map<Level, BotWordSearcherStrategy> botWordSearcherStrategies = new HashMap<>();

    public Either<MoveResult.PlayerMove, GameFailure> perform(Game game, Board board, BotPlayer botPlayer) {
        BoardProvider.Word botWord = ofNullable(botWordSearcherStrategies.get(botPlayer.getLevel()))
                .orElseThrow(() -> new IllegalStateException("No bot strategy for level: %s".formatted(botPlayer.getLevel().toString())))
                .search(board, botPlayer.getId());

        List<BoardTile> tiles = selectTiles(botWord, board.getRack(botPlayer.getId()));

        return this.perform(game, board, botPlayer.getId(), tiles);
    }

    private List<BoardTile> selectTiles(BoardProvider.Word botWord, Rack rack) {
        if(botWord == null) {
            return Collections.emptyList();
        }

        List<BoardTile> selectedTiles = new ArrayList<>();

        for (BoardProvider.Element element : botWord.elements()) {
            if (!element.onBoard()) {
                Letter foundLetter = findLetterInRack(element, rack);
                if (foundLetter != null) {
                    BoardTile tile = new BoardTile(element.x(), element.y(), element.letter(), foundLetter.getPoints(), foundLetter.isBlank());
                    selectedTiles.add(tile);
                } else {
                    throw new IllegalStateException("No required tile (%s) in rack: %s".formatted(
                            element.letter(), rack.getLetters().stream()
                                    .map(Letter::getLetter)
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(", "))));
                }
            }
        }

        return selectedTiles;
    }

    private Letter findLetterInRack(BoardProvider.Element element, Rack rack) {
        for (Letter letter : rack.getLetters()) {
            if (element.blank() && letter.isBlank() ||
                    (!element.blank() &&
                            letter.getLetter().equals(element.letter()))) {
                return letter;
            }
        }
        return null;
    }
}
