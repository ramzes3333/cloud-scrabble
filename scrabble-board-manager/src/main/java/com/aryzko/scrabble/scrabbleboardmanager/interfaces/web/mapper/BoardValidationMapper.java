package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidationResult;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.BoardValidationResultResponse;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BoardValidationMapper {
    BoardValidationResultResponse toBoardValidationResultResponse(BoardValidationResult result);

    default Character map(Optional<Character> value) {
        return value.orElse(null);
    }
}
