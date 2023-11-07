package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.mapper.DictionaryProviderMapper;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Letter;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
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
 * Please see https://www.cs.cmu.edu/afs/cs/academic/class/15451-s06/www/lectures/scrabble.pdf
 */
@RequiredArgsConstructor
@Service
public class BoardResolver {

    private final BoardService boardService;
    private final DictionaryProvider dictionaryProvider;
    private final LinePreparationService linePreparationService;
    private final ScoringService scoringService;
    private final RelatedWordsFillService relatedWordsSearchService;
    private final DictionaryProviderMapper dictionaryMapper;

    public Solution resolve(final String playerId, final Board board) {
        Board boardFromDb = boardService.getBoard(board.getId());
        board.setBoardParameters(boardFromDb.getBoardParameters());

        List<Solution.Word> words = new ArrayList<>();
        words.addAll(horizontalResolve(playerId, board).getWords());
        words.addAll(verticalResolve(playerId, board).getWords());

        words.sort(Comparator.comparing(Solution.Word::getPoints).reversed());

        return Solution.builder()
                .words(words)
                .build();
    }

    private Solution horizontalResolve(final String playerId, final Board board) {
        Solution solution = Solution.builder().words(
                        linePreparationService.prepareLines(board).getLines().stream()
                                .map(dictionaryMapper::convert)
                                .map(line -> dictionaryProvider.resolve(line, prepareAvailableLetters(playerId, board)))
                                .map(DictionaryProvider.Solution::getWords)
                                .flatMap(Collection::stream)
                                .map(dictionaryMapper::convert)
                                .collect(Collectors.toList()))
                .build();

        relatedWordsSearchService.fill(board, solution);
        scoringService.score(board, solution);

        return solution;
    }

    private Solution verticalResolve(final String playerId, final Board board) {
        final Board transposedBoard = board.transpose(Board.TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT);
        return horizontalResolve(playerId, transposedBoard)
                .transpose(Solution.TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_RIGHT);
    }

    private List<Character> prepareAvailableLetters(final String playerId, Board board) {
        return board.getRacks().stream()
                .filter(r -> r.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No rack for player!"))
                .getLetters().stream()
                .map(Letter::getLetter)
                .collect(Collectors.toList());
    }
}
