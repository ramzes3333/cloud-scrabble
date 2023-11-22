package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.TransposeType;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TilesScoring {

    private final BoardService boardService;
    private final WordFinder wordFinder;
    private final ScoringService scoringService;
    private final RelatedWordsFillService relatedWordsSearchService;

    public Word scoreTiles(final String boardId, final Tiles tiles) {
        Board board = boardService.getBoard(UUID.fromString(boardId));
        return scoreTiles(board, tiles);
    }

    public Word scoreTiles(final Board board, final Tiles tiles) {
        if(tiles.getTiles().isEmpty()) {
            return null;
        }
        loadBoardParametersIfNeeded(board);

        Word word = wordFinder.findWord(board, tiles);
        WordAnalyzer.WordDirection wordDirection = WordAnalyzer.determineWordDirection(tiles);

        switch (wordDirection) {
            case HORIZONTAL -> findRelatedWords(board, word);
            case VERTICAL -> findRelatedWordsAfterTranspose(board, word);
            case UNDEFINED -> {
                findRelatedWords(board, word);
                findRelatedWordsAfterTranspose(board, word);
            }
        }

        scoringService.score(board, word);
        return word;
    }

    private void findRelatedWords(Board board, Word word) {
        word.getRelatedWords().addAll(relatedWordsSearchService.getRelatedWords(board, word));
    }

    private void findRelatedWordsAfterTranspose(Board board, Word word) {
        word.getRelatedWords().addAll(relatedWordsSearchService.getRelatedWords(
                        board.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT),
                        word.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT)).stream()
                .map(w -> w.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_RIGHT))
                .toList());
    }

    private void loadBoardParametersIfNeeded(Board board) {
        if(board.getBoardParameters() == null) {
            Board boardFromDb = boardService.getBoard(board.getId());
            board.setBoardParameters(boardFromDb.getBoardParameters());
        }
    }
}
