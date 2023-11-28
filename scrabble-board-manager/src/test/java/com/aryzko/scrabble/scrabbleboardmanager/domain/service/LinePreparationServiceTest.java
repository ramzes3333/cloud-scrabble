package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.PreparedLines;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinePreparationServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TileManagerProvider tileManagerProvider;
    @Mock
    private DictionaryProvider dictionaryProvider;
    @InjectMocks
    private LinePreparationService resolver;

    @Test
    void prepareLines() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-5x5.json", Board.class);

        when(dictionaryProvider.fillGap("*J")).thenReturn(List.of('A', 'E', 'O'));
        when(dictionaryProvider.fillGap("*T")).thenReturn(List.of('A', 'E', 'O', 'U'));
        when(dictionaryProvider.fillGap("*O")).thenReturn(List.of('B', 'C', 'D', 'E', 'G', 'H',
                'K', 'N', 'O', 'P', 'R', 'T', 'Y'));
        when(dictionaryProvider.fillGap("*K")).thenReturn(List.of('O'));
        when(dictionaryProvider.fillGap("T*")).thenReturn(List.of('A', 'E', 'O', 'S', 'U', 'Y', 'Ą', 'Ę'));
        when(dictionaryProvider.fillGap("O*")).thenReturn(List.of('D', 'H', 'J', 'K', 'M', 'N', 'O',
                'P', 'R', 'S', 'T', 'Z', 'Ń', 'Ś', 'Ż'));
        when(dictionaryProvider.fillGap("OTO*")).thenReturn(List.of('K'));
        when(dictionaryProvider.fillGap("J*")).thenReturn(List.of('A', 'E', 'Ą'));
        when(dictionaryProvider.fillGap("K*")).thenReturn(List.of('A', 'I', 'O', 'U'));


        //when
        PreparedLines preparedLines = resolver.prepareLines(board);


        //then
        assertNotNull(preparedLines);
        List<PreparedLine> lines = preparedLines.getLines();

        assertNotNull(lines);

        assertEquals(List.of('O'),
                getField(lines, 0, 1).getAvailableLetters());
        assertEquals(List.of('B', 'C', 'D', 'E', 'G', 'H', 'K', 'N', 'O', 'P', 'R', 'T', 'Y'),
                getField(lines, 1, 1).getAvailableLetters());
        assertEquals(List.of('A', 'E', 'O'),
                getField(lines, 2, 1).getAvailableLetters());
        assertEquals(List.of('A', 'E', 'O', 'U'),
                getField(lines, 4, 1).getAvailableLetters());

        assertEquals(List.of('A', 'I', 'O', 'U'),
                getField(lines, 0, 3).getAvailableLetters());
        assertEquals(List.of('D', 'H', 'J', 'K', 'M', 'N', 'O', 'P', 'R', 'S', 'T', 'Z', 'Ń', 'Ś', 'Ż'),
                getField(lines, 1, 3).getAvailableLetters());
        assertEquals(List.of('A', 'E', 'Ą'),
                getField(lines, 2, 3).getAvailableLetters());
        assertEquals(List.of('K'),
                getField(lines, 3, 3).getAvailableLetters());
        assertEquals(List.of('A', 'E', 'O', 'S', 'U', 'Y', 'Ą', 'Ę'),
                getField(lines, 4, 3).getAvailableLetters());

        assertTrue(getField(lines, 0, 0).isAnyLetter());
        assertTrue(getField(lines, 1, 0).isAnyLetter());
        assertTrue(getField(lines, 2, 0).isAnyLetter());
        assertTrue(getField(lines, 4, 0).isAnyLetter());

        assertFalse(getField(lines, 0, 1).isAnyLetter());
        assertFalse(getField(lines, 1, 1).isAnyLetter());
        assertFalse(getField(lines, 2, 1).isAnyLetter());
        assertFalse(getField(lines, 4, 1).isAnyLetter());

        assertFalse(getField(lines, 0, 3).isAnyLetter());
        assertFalse(getField(lines, 1, 3).isAnyLetter());
        assertFalse(getField(lines, 2, 3).isAnyLetter());
        assertFalse(getField(lines, 3, 3).isAnyLetter());
        assertFalse(getField(lines, 4, 3).isAnyLetter());

        assertTrue(getField(lines, 0, 4).isAnyLetter());
        assertTrue(getField(lines, 1, 4).isAnyLetter());
        assertTrue(getField(lines, 2, 4).isAnyLetter());
        assertTrue(getField(lines, 3, 4).isAnyLetter());
        assertTrue(getField(lines, 4, 4).isAnyLetter());

        assertNull(getField(lines, 3, 0).getAvailableLetters());
        assertNull(getField(lines, 3, 1).getAvailableLetters());
        assertNull(getField(lines, 0, 2).getAvailableLetters());
        assertNull(getField(lines, 0, 2).getAvailableLetters());
        assertNull(getField(lines, 1, 2).getAvailableLetters());
        assertNull(getField(lines, 2, 2).getAvailableLetters());
        assertNull(getField(lines, 3, 2).getAvailableLetters());
        assertNull(getField(lines, 4, 2).getAvailableLetters());

        assertEquals('O', getField(lines, 3, 0).getLetter());
        assertEquals('T', getField(lines, 3, 1).getLetter());
        assertEquals('K', getField(lines, 0, 2).getLetter());
        assertEquals('O', getField(lines, 1, 2).getLetter());
        assertEquals('J', getField(lines, 2, 2).getLetter());
        assertEquals('O', getField(lines, 3, 2).getLetter());
        assertEquals('T', getField(lines, 4, 2).getLetter());

        assertEquals(2, getField(lines, 2, 0).getLeftLimit());
        assertEquals(0, getField(lines, 0, 1).getLeftLimit());
        assertEquals(0, getField(lines, 1, 1).getLeftLimit());
        assertEquals(0, getField(lines, 2, 1).getLeftLimit());
        assertEquals(0, getField(lines, 0, 3).getLeftLimit());
        assertEquals(0, getField(lines, 1, 3).getLeftLimit());
        assertEquals(0, getField(lines, 2, 3).getLeftLimit());
        assertEquals(0, getField(lines, 3, 3).getLeftLimit());
        assertEquals(0, getField(lines, 4, 3).getLeftLimit());

        assertTrue(getField(lines, 2, 0).isAnchor());
        assertTrue(getField(lines, 0, 1).isAnchor());
        assertTrue(getField(lines, 1, 1).isAnchor());
        assertTrue(getField(lines, 2, 1).isAnchor());
        assertTrue(getField(lines, 0, 3).isAnchor());
        assertTrue(getField(lines, 1, 3).isAnchor());
        assertTrue(getField(lines, 2, 3).isAnchor());
        assertTrue(getField(lines, 3, 3).isAnchor());
        assertTrue(getField(lines, 4, 3).isAnchor());
    }

    private static PreparedLine.LineField getField(List<PreparedLine> preparedLines, int x, int y) {
        return preparedLines.get(y).getFields().get(x);
    }
}