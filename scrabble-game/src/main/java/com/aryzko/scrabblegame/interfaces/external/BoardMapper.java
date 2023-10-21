package com.aryzko.scrabblegame.interfaces.external;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;
import com.aryzko.scrabblegame.application.model.board.Field;
import com.aryzko.scrabblegame.application.model.board.Letter;
import com.aryzko.scrabblegame.application.model.board.Rack;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    Board toBoard(BoardClient.BoardResponse boardResponse);

    BoardClient.BoardRequest toBoardRequest(Board board);

    BoardClient.Field convert(Field field);

    BoardClient.Rack convert(Rack rack);

    BoardClient.Letter convert(Letter letter);

    BoardValidationResultResponse convert(BoardClient.BoardValidationResultResponse boardValidationResultResponse);
}
