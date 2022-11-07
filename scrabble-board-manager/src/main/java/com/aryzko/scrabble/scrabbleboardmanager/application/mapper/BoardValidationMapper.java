package com.aryzko.scrabble.scrabbleboardmanager.application.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.application.request.BoardRequest;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardResponse;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardValidationResultResponse;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidationResult;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BoardValidationMapper {
    BoardValidationResultResponse toBoardValidationResultResponse(BoardValidationResult result);

    default Character map(Optional<Character> value) {
        return value.orElse(null);
    }
}
