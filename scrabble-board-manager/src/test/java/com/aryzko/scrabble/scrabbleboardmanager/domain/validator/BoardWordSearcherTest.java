package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.BoardParameters;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Field;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Letter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BoardWordSearcherTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private BoardWordSearcher boardWordSearcher = new BoardWordSearcher();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getWords() throws IOException {
        //given
        Board board = prepareBoard("sample-board-5x5.json");

        //when
        List<CharacterSequence> words = boardWordSearcher.getWordsFromBoard(board);

        //then
        assertNotNull(words);
        assertEquals(5, words.size());
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character(of('A')).x(2).y(0).build())
                .character(CharacterWithPosition.builder().character(of('A')).x(3).y(0).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character(of('B')).x(0).y(1).build())
                .character(CharacterWithPosition.builder().character(of('B')).x(0).y(2).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character(of('C')).x(0).y(4).build())
                .character(CharacterWithPosition.builder().character(of('C')).x(1).y(4).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character(of('D')).x(1).y(3).build())
                .character(CharacterWithPosition.builder().character(of('D')).x(2).y(3).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character(of('D')).x(1).y(3).build())
                .character(CharacterWithPosition.builder().character(of('C')).x(1).y(4).build()).build()));
    }

    private Board prepareBoard(String filename) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/validator/%s", filename)), Board.class);
    }
}