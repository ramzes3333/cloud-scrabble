package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterWithPosition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BoardOrphanSearcherTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private BoardOrphanSearcher boardOrphanSearcher = new BoardOrphanSearcher();

    @BeforeEach
    void setUp() {
    }

    @Test
    void searchOrphans_withoutLonelyCentralTile_returnsOrphans() throws IOException {
        //given
        Board board = prepareBoard("sample-board-15x15.json");

        //when
        List<CharacterWithPosition> orphans = boardOrphanSearcher.searchOrphans(board);

        //then
        assertNotNull(orphans);
        assertEquals(3, orphans.size());
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('T')).x(4).y(12).build()));
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('N')).x(10).y(11).build()));
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('A')).x(11).y(11).build()));
    }

    @Test
    void searchOrphans_lonelyCentralTile_returnAsOrphan() throws IOException {
        //given
        Board board = prepareBoard("lonely-central-tile.json");

        //when
        List<CharacterWithPosition> orphans = boardOrphanSearcher.searchOrphans(board);

        //then
        assertNotNull(orphans);
        assertEquals(2, orphans.size());
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('T')).x(0).y(0).build()));
        assertTrue(orphans.contains(CharacterWithPosition.builder().character(of('L')).x(2).y(2).build()));
    }

    private Board prepareBoard(String filename) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/validator/%s", filename)), Board.class);
    }
}