package com.aryzko.scrabblegame.interfaces.external;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultBoardProvider implements BoardProvider {

    private final BoardClient boardClient;
    private final BoardMapper boardMapper;

    @Override
    public String createBoard(List<String> playerIds) {
        return boardClient.createBoard(new BoardClient.CreateBoardRequest(playerIds)).getId();
    }

    public Board getBoard(String id) {
        return boardMapper.toBoard(boardClient.getBoard(id));
    }

    @Override
    public BoardValidationResultResponse validateBoard(Board board) {
        return boardMapper.convert(boardClient.validateBoard(boardMapper.toBoardRequest(board)));
    }

    @Override
    public Board update(Board board) {
        return boardMapper.toBoard(boardClient.updateBoard(boardMapper.toBoardRequest(board)));
    }

    @Override
    public Integer scoreWord(String boardId, Tiles tiles) {
        return boardClient.scoreTiles(boardId, new BoardClient.Tiles(tiles.tiles().stream()
                .map(t -> new BoardClient.Tile(t.x(), t.y(), t.letter(), t.blank()))
                .toList()));
    }
}
