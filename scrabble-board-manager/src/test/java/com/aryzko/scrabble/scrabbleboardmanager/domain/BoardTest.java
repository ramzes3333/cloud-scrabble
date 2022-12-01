package com.aryzko.scrabble.scrabbleboardmanager.domain;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BoardTest {

    @Test
    void transpose() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/board-to-rotate-5x5.json", Board.class);

        //when
        Board result = board.transpose(Board.TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT);

        //then
        assertNotNull(result);
        assertFieldCorrect(result, 0, 0, null, null, Bonus.DoubleLetterScore);
        assertFieldCorrect(result, 1, 0, null, null, null);
        assertFieldCorrect(result, 2, 0, 'K', 2, null);
        assertFieldCorrect(result, 3, 0, null, null, null);
        assertFieldCorrect(result, 4, 0, null, null, null);

        assertFieldCorrect(result, 0, 1, null, null, null);
        assertFieldCorrect(result, 1, 1, null, null, null);
        assertFieldCorrect(result, 2, 1, 'O', 1, null);
        assertFieldCorrect(result, 3, 1, null, null, null);
        assertFieldCorrect(result, 4, 1, null, null, null);

        assertFieldCorrect(result, 0, 2, null, null, null);
        assertFieldCorrect(result, 1, 2, null, null, null);
        assertFieldCorrect(result, 2, 2, 'J', 3, null);
        assertFieldCorrect(result, 3, 2, null, null, null);
        assertFieldCorrect(result, 4, 2, null, null, null);

        assertFieldCorrect(result, 0, 3, 'O', 1, null);
        assertFieldCorrect(result, 1, 3, 'T', 2, null);
        assertFieldCorrect(result, 2, 3, 'O', 1, null);
        assertFieldCorrect(result, 3, 3, null, null, null);
        assertFieldCorrect(result, 4, 3, null, null, null);

        assertFieldCorrect(result, 0, 4, null, null, Bonus.TripleWordScore);
        assertFieldCorrect(result, 1, 4, null, null, null);
        assertFieldCorrect(result, 2, 4, 'T', 2, null);
        assertFieldCorrect(result, 3, 4, null, null, null);
        assertFieldCorrect(result, 4, 4, null, null, null);
    }

    private void assertFieldCorrect(Board result, Integer x, Integer y, Character letter, Integer points, Bonus bonus) {
        List<Field> fields = result.getFields().stream()
                .filter(f -> f.getX().equals(x) && f.getY().equals(y))
                .collect(Collectors.toList());

        assertEquals(1, fields.size());
        Field field = fields.get(0);
        assertTrue((letter == null && field.getLetter() == null)
                || field.getLetter().getLetter().equals(letter));
        assertTrue((points == null && field.getLetter() == null || field.getLetter().getPoints() == null)
                || field.getLetter().getPoints().equals(points));
        assertTrue((bonus == null && field.getBonus() == null) || field.getBonus().equals(bonus));
    }
}