package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.preview.BoardPreview;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultBoardCreatorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DefaultBoardCreator defaultBoardCreator = new DefaultBoardCreator();

    @Test
    void prepareEmptyBoard() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/validator/new-board-15x15.json", Board.class);

        //when
        Board result = defaultBoardCreator.prepareEmptyBoard();

        //then
        assertNotNull(result.getId());
        assertFalse(result.getId().toString().isBlank());
        assertEquals(result.getFields(), board.getFields());
        assertEquals(result.getRacks(), board.getRacks());
        assertEquals(result.getBoardParameters(), board.getBoardParameters());
    }

    @Test
    void prepareEmptyBoardPreview() throws IOException {
        //given
        BoardPreview board = loadObjectFromJson("/domain/validator/new-board-preview-15x15.json", BoardPreview.class);

        //when
        BoardPreview result = defaultBoardCreator.prepareEmptyBoardPreview();

        //then
        assertNotNull(result);
        assertEquals(result.getFields(), board.getFields());
        assertEquals(result.getBoardParameters(), board.getBoardParameters());
    }
}