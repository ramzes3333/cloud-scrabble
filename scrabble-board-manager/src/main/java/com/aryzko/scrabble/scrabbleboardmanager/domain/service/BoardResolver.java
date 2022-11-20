package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.external.DictionaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardResolver {
    private final DictionaryClient dictionaryClient;
    private final LinePreparationService linePreparationService;

    public Solution resolve(final Board board) {
        return null;
    }
}
