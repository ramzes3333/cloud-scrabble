package com.aryzko.scrabblegame.application.provider;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;

import java.util.List;
import java.util.UUID;

public interface BoardProvider {
    String createBoard(List<String> playerIds);
    Board getBoard(String id);
    BoardValidationResultResponse validateBoard(Board board);
    Board update(Board board);
}
