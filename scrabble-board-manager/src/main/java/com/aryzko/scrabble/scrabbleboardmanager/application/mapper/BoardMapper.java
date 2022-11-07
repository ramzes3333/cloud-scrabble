package com.aryzko.scrabble.scrabbleboardmanager.application.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.application.request.BoardRequest;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardResponse;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardResponse toBoardResponse(Board board);

    Board toBoard(BoardRequest board);
}
