package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordFinderTest {

    private final WordFinder wordFinder = new WordFinder();

    public static Stream<TilesAndWords> source() {
        return Stream.of(
                TilesAndWords.builder()
                        .tiles(new Tiles(Arrays.asList(
                                new Tile(1, 1, 'M', false)
                        )))
                        .word(Word.builder()
                                        .element(Word.Element.builder()
                                                .x(1).y(0).letter('I').blank(false).onBoard(true).build())
                                        .element(Word.Element.builder()
                                                .x(1).y(1).letter('M').blank(false).onBoard(false).build())
                                .build()).build(),
                TilesAndWords.builder()
                        .tiles(new Tiles(Arrays.asList(
                                new Tile(1, 2, 'O', false)
                        )))
                        .word(Word.builder()
                                .element(Word.Element.builder()
                                        .x(1).y(2).letter('O').blank(false).onBoard(false).build())
                                .element(Word.Element.builder()
                                        .x(2).y(2).letter('K').blank(false).onBoard(true).build())
                                .build()).build(),
                TilesAndWords.builder()
                        .tiles(new Tiles(Arrays.asList(
                                new Tile(0, 1, 'O', false),
                                new Tile(0, 2, 'T', false)
                        )))
                        .word(Word.builder()
                                .element(Word.Element.builder()
                                        .x(0).y(0).letter('K').blank(false).onBoard(true).build())
                                .element(Word.Element.builder()
                                        .x(0).y(1).letter('O').blank(false).onBoard(false).build())
                                .element(Word.Element.builder()
                                        .x(0).y(2).letter('T').blank(false).onBoard(false).build())
                                .build()).build()
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("source")
    void findWord(TilesAndWords tilesAndWords) throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-3x3-words-finder.json", Board.class);

        //when
        Word word = wordFinder.findWord(board, tilesAndWords.getTiles());
        //then
        assertNotNull(word);
        assertEquals(tilesAndWords.getWord(), word);
    }

    @Value
    @Builder
    public static class TilesAndWords {
        Tiles tiles;
        Word word;
    }
}