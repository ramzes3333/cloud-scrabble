package com.aryzko.scrabblegame.application.provider;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;

import java.util.List;

public interface BoardProvider {
    String createBoard(List<String> playerIds);
    Board getBoard(String id);
    BoardValidationResultResponse validateBoard(Board board);
    Board update(Board board);
    Integer scoreWord(String boardId, Tiles tiles);

    record Tiles (List<Tile> tiles) { }
    record Tile (int x, int y, char letter, boolean blank) { }
}
