package com.aryzko.scrabblegame.application.provider;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;
import lombok.Data;

import java.util.List;

public interface BoardProvider {
    String createBoard(List<String> playerIds);
    Board getBoard(String id);
    BoardValidationResultResponse validateBoard(Board board);
    Board update(Board board);
    ScoreResult scoreWord(Board board, Tiles tiles);
    Solution resolve(Board board, String playerId);

    record Tiles (List<Tile> tiles) { }
    record Tile (int x, int y, char letter, boolean blank) { }

    record ScoreResult (String word, String tiles, Integer points) { }

    record Solution(List<Word> words) { }
    record Word (int points, List<Element> elements, List<Word> relatedWords, List<Bonus> bonuses) { }
    record Element (int x, int y, char letter, int points, boolean blank, boolean onBoard) { }

    enum Bonus {
        DoubleWordScore,
        TripleWordScore,
        DoubleLetterScore,
        TripleLetterScore,
        None
    }
}
