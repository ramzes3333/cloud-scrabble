package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.preview.BoardPreview;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.EmptyField;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.BoardController;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.request.BoardRequest;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.BoardResponse;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardResponse toBoardResponse(Board board);

    Board toBoard(BoardRequest board);

    BoardController.BoardPreviewResponse convert(BoardPreview preview);
    BoardController.BoardPreviewResponse.Field convert(EmptyField preview);
}
