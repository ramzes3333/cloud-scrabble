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

    public Integer scoreTiles(final String boardId, final Tiles tiles) {
        if(tiles.getTiles().isEmpty()) {
            return 0;
        }

        Board board = boardService.getBoard(UUID.fromString(boardId));
        Word word = wordFinder.findWord(board, tiles);

        word.getRelatedWords().addAll(relatedWordsSearchService.getRelatedWords(board, word));
        word.getRelatedWords().addAll(relatedWordsSearchService.getRelatedWords(
                board.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT),
                word.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT)).stream()
                .map(w -> w.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_RIGHT))
                .toList());

        scoringService.score(board, word);
        return word.getPoints();
    }
}
