package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLines;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static java.lang.String.format;

@ExtendWith(MockitoExtension.class)
class BoardResolverTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DictionaryProvider dictionaryProvider;
    @Mock
    private LinePreparationService linePreparationService;

    @InjectMocks
    private BoardResolver resolver;

    @Test
    void resolve() throws IOException {
        //given
        Board board = loadObjectFromJson("board-to-resolve-5x5.json", Board.class);
        PreparedLines lines = loadObjectFromJson("prepared-lines-5x5.json", PreparedLines.class);

        //when

        //then
    }

    private <T> T loadObjectFromJson(String filename, Class<T> clazz) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/service/%s", filename)), clazz);
    }
}