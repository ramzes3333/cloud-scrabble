package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class DefaultBoardCreatorTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private DefaultBoardCreator defaultBoardCreator = new DefaultBoardCreator();

    @Test
    void prepareEmptyBoard() throws IOException {
        //given
        Board board = prepareBoard("new-board-15x15.json");

        //when
        Board result = defaultBoardCreator.prepareEmptyBoard();

        //then
        assertNotNull(result.getId());
        assertFalse(result.getId().toString().isBlank());
        assertEquals(result.getFields(), board.getFields());
        assertEquals(result.getRacks(), board.getRacks());
        assertEquals(result.getBoardParameters(), board.getBoardParameters());
    }

    private Board prepareBoard(String filename) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/validator/%s", filename)), Board.class);
    }
}