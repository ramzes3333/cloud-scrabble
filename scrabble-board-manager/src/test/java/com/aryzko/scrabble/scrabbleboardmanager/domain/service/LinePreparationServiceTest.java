package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLines;
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

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
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
        Board board = prepareBoard("board-to-resolve-5x5.json");

        when(dictionaryProvider.fillGap("*j")).thenReturn(List.of('a', 'e', 'o'));
        when(dictionaryProvider.fillGap("*t")).thenReturn(List.of('a', 'e', 'o', 'u'));
        when(dictionaryProvider.fillGap("*o")).thenReturn(List.of('b', 'c', 'd', 'e', 'g', 'h',
                'k', 'n', 'o', 'p', 'r', 't', 'y'));
        when(dictionaryProvider.fillGap("*k")).thenReturn(List.of('o'));
        when(dictionaryProvider.fillGap("t*")).thenReturn(List.of('a', 'e', 'o', 's', 'u', 'y', 'ą', 'ę'));
        when(dictionaryProvider.fillGap("o*")).thenReturn(List.of('d', 'h', 'j', 'k', 'm', 'n', 'o',
                'p', 'r', 's', 't', 'z', 'ń', 'ś', 'ż'));
        when(dictionaryProvider.fillGap("oto*")).thenReturn(List.of('k'));
        when(dictionaryProvider.fillGap("j*")).thenReturn(List.of('a', 'e', 'ą'));
        when(dictionaryProvider.fillGap("k*")).thenReturn(List.of('a', 'i', 'o', 'u'));


        //when
        PreparedLines preparedLines = resolver.prepareLines(board);


        //then
        assertNotNull(preparedLines);
        List<PreparedLine> lines = preparedLines.getLines();

        assertNotNull(lines);

        assertEquals(List.of('o'),
                getField(lines, 0, 1).getAvailableLetters());
        assertEquals(List.of('b', 'c', 'd', 'e', 'g', 'h', 'k', 'n', 'o', 'p', 'r', 't', 'y'),
                getField(lines, 1, 1).getAvailableLetters());
        assertEquals(List.of('a', 'e', 'o'),
                getField(lines, 2, 1).getAvailableLetters());
        assertEquals(List.of('a', 'e', 'o', 'u'),
                getField(lines, 4, 1).getAvailableLetters());

        assertEquals(List.of('a', 'i', 'o', 'u'),
                getField(lines, 0, 3).getAvailableLetters());
        assertEquals(List.of('d', 'h', 'j', 'k', 'm', 'n', 'o', 'p', 'r', 's', 't', 'z', 'ń', 'ś', 'ż'),
                getField(lines, 1, 3).getAvailableLetters());
        assertEquals(List.of('a', 'e', 'ą'),
                getField(lines, 2, 3).getAvailableLetters());
        assertEquals(List.of('k'),
                getField(lines, 3, 3).getAvailableLetters());
        assertEquals(List.of('a', 'e', 'o', 's', 'u', 'y', 'ą', 'ę'),
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

        assertEquals('o', getField(lines, 3, 0).getLetter());
        assertEquals('t', getField(lines, 3, 1).getLetter());
        assertEquals('k', getField(lines, 0, 2).getLetter());
        assertEquals('o', getField(lines, 1, 2).getLetter());
        assertEquals('j', getField(lines, 2, 2).getLetter());
        assertEquals('o', getField(lines, 3, 2).getLetter());
        assertEquals('t', getField(lines, 4, 2).getLetter());

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

    private Board prepareBoard(String filename) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/service/%s", filename)), Board.class);
    }
}