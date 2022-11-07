package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardInspector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoardValidator {

    private final BoardInspector boardInspector;
    //private final DictionaryChecker dictionaryChecker;

    public BoardValidationResult validate(Board board) {
        return null;
    }
}
