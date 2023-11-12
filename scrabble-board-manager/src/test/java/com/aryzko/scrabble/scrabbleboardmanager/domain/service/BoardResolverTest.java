package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.PreparedLines;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;

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
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-5x5.json", Board.class);
        PreparedLines lines = loadObjectFromJson("/domain/service/prepared-lines-5x5.json", PreparedLines.class);

        //when

        //then
    }
}