package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLines;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.external.DictionaryClient;
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
class BoardResolverTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DictionaryClient dictionaryClient;
    @Mock
    private LinePreparationService linePreparationService;

    @InjectMocks
    private BoardResolver resolver;

    @Test
    void resolve() throws IOException {
        //given
        Board board = loadObjectFromJson("board-to-resolve-5x5.json", Board.class);
        PreparedLines preparedLines = loadObjectFromJson("prepared-lines-5x5.json", PreparedLines.class);
        //when(linePreparationService.prepareLines(board)).thenReturn(lines);


        //when
        Solution solution = resolver.resolve(board);

        //then
    }

    private <T> T loadObjectFromJson(String filename, Class<T> clazz) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/service/%s", filename)), clazz);
    }
}