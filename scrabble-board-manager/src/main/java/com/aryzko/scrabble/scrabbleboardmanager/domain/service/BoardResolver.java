package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Letter;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardResolver {

    private final BoardService boardService;
    private final DictionaryProvider dictionaryProvider;
    private final LinePreparationService linePreparationService;
    private final ScoringService scoringService;
    private final RelatedWordsFillService relatedWordsSearchService;

    public Solution resolve(final Board board) {
        Board boardFromDb = boardService.getBoard(board.getId());
        board.setBoardParameters(boardFromDb.getBoardParameters());

        Solution solution = Solution.builder().words(
                        linePreparationService.prepareLines(board).getLines().stream()
                                .map(preparedLine -> dictionaryProvider.resolve(preparedLine, prepareAvailableLetters(board)))
                                .map(Solution::getWords)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()))
                .build();

        scoringService.score(board, solution);
        relatedWordsSearchService.fill(board, solution);

        solution.getWords().sort(Comparator.comparing(Solution.Word::getPoints).reversed());
        return solution;
    }

    private List<Character> prepareAvailableLetters(Board board) {
        return board.getRacks().get(0).getLetters().stream()
                .map(Letter::getLetter)
                .collect(Collectors.toList());
    }
}
