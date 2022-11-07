package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.BoardParameters;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Field;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Letter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardInspectorTest {

    public static final int BOARD_SIZE = 5;
    private BoardInspector boardInspector = new BoardInspector();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getWords() {
        //given
        Board board = prepareBoard();

        //when
        List<CharacterSequence> words = boardInspector.getWordsFromBoard(board);

        //then
        assertNotNull(words);
        assertEquals(5, words.size());
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character('A').x(2).y(0).build())
                .character(CharacterWithPosition.builder().character('A').x(3).y(0).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character('B').x(0).y(1).build())
                .character(CharacterWithPosition.builder().character('B').x(0).y(2).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character('C').x(0).y(4).build())
                .character(CharacterWithPosition.builder().character('C').x(1).y(4).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character('D').x(1).y(3).build())
                .character(CharacterWithPosition.builder().character('D').x(2).y(3).build()).build()));
        assertTrue(words.contains(CharacterSequence.builder()
                .character(CharacterWithPosition.builder().character('D').x(1).y(3).build())
                .character(CharacterWithPosition.builder().character('C').x(1).y(4).build()).build()));
    }

    private Board prepareBoard() {
        Board board = new Board();
        board.setBoardParameters(BoardParameters.builder()
                .horizontalSize(BOARD_SIZE)
                .verticalSize(BOARD_SIZE)
                .build());

        board.setFields(new ArrayList<>());
        board.getFields().add(createField(0, 0, ' '));
        board.getFields().add(createField(1, 0, ' '));
        board.getFields().add(createField(2, 0, 'A'));
        board.getFields().add(createField(3, 0, 'A'));
        board.getFields().add(createField(4, 0, ' '));

        board.getFields().add(createField(0, 1, 'B'));
        board.getFields().add(createField(1, 1, ' '));
        board.getFields().add(createField(2, 1, ' '));
        board.getFields().add(createField(3, 1, ' '));
        board.getFields().add(createField(4, 1, ' '));

        board.getFields().add(createField(0, 2, 'B'));
        board.getFields().add(createField(1, 2, ' '));
        board.getFields().add(createField(2, 2, ' '));
        board.getFields().add(createField(3, 2, ' '));
        board.getFields().add(createField(4, 2, ' '));

        board.getFields().add(createField(0, 3, ' '));
        board.getFields().add(createField(1, 3, 'D'));
        board.getFields().add(createField(2, 3, 'D'));
        board.getFields().add(createField(3, 3, ' '));
        board.getFields().add(createField(4, 3, ' '));

        board.getFields().add(createField(0, 4, 'C'));
        board.getFields().add(createField(1, 4, 'C'));
        board.getFields().add(createField(2, 4, ' '));
        board.getFields().add(createField(3, 4, ' '));
        board.getFields().add(createField(4, 4, ' '));
        return board;
    }

    private Field createField(int x, int y, Character character) {
        Letter letter = new Letter();
        letter.setLetter(character);

        Field field = new Field();
        field.setX(x);
        field.setY(y);
        field.setLetter(letter);

        return field;
    }
}