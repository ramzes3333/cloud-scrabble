package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Letter;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardResolver {
    private final DictionaryProvider dictionaryProvider;
    private final LinePreparationService linePreparationService;

    public Solution resolve(final Board board) {
        return Solution.builder().words(
                        linePreparationService.prepareLines(board).getLines().stream()
                                .map(preparedLine -> dictionaryProvider.resolve(preparedLine, prepareAvailableLetters(board)))
                                .map(Solution::getWords)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()))
                .build();
    }

    private List<Character> prepareAvailableLetters(Board board) {
        return board.getRacks().get(0).getLetters().stream()
                .map(Letter::getLetter)
                .collect(Collectors.toList());
    }
}
