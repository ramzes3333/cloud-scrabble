package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles;
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
        Word word = wordFinder.findWord(board, tiles);

        relatedWordsSearchService.fill(board, word);
        scoringService.score(board, word);

        return word;
    }
}
