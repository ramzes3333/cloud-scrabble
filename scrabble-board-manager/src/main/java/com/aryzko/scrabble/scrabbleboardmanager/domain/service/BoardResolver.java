package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Letter;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Based on The World’s Fastest Scrabble Program by ANDREW W. APPEL AND GUY J. JACOBSON
 */
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

        List<Solution.Word> words = new ArrayList<>();
        words.addAll(horizontalResolve(board).getWords());
        words.addAll(verticalResolve(board).getWords());

        words.sort(Comparator.comparing(Solution.Word::getPoints).reversed());

        return Solution.builder()
                .words(words)
                .build();
    }

    private Solution horizontalResolve(final Board board) {
        Solution solution = Solution.builder().words(
                        linePreparationService.prepareLines(board).getLines().stream()
                                .map(line -> dictionaryProvider.resolve(line, prepareAvailableLetters(board)))
                                .map(Solution::getWords)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()))
                .build();

        relatedWordsSearchService.fill(board, solution);
        scoringService.score(board, solution);

        return solution;
    }

    private Solution verticalResolve(final Board board) {
        final Board transposedBoard = board.transpose(Board.TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT);
        return horizontalResolve(transposedBoard)
                .transpose(Solution.TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_RIGHT);
    }

    private List<Character> prepareAvailableLetters(Board board) {
        return board.getRacks().get(0).getLetters().stream()
                .map(Letter::getLetter)
                .collect(Collectors.toList());
    }
}
