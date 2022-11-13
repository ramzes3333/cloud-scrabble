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
class BoardOrphanSearcherTest {

    private ObjectMapper objectMapper = new ObjectMapper();;
    private BoardOrphanSearcher boardOrphanSearcher = new BoardOrphanSearcher();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getWords() throws IOException {
        //given
        Board board = prepareBoard();

        //when
        List<CharacterWithPosition> orphans = boardOrphanSearcher.searchOrphans(board);

        //then
        assertNotNull(orphans);
        assertEquals(3, orphans.size());
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('T')).x(4).y(12).build()));
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('N')).x(10).y(11).build()));
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('A')).x(10).y(11).build()));
    }

    private Board prepareBoard() throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath("/domain/validator/sample-board.json"), Board.class);
    }
}