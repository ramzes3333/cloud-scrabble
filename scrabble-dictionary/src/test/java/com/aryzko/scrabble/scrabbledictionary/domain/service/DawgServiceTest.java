package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.common.JsonUtils;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.Node;
import com.aryzko.scrabble.scrabbledictionary.domain.ports.DictionaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DawgServiceTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private DictionaryRepository dictionaryRepository;
    private DawgService dawgService;

    @BeforeEach
    void beforeAll() throws IOException {
        Node root = prepareDawg("sample-dawg.json");
        this.dawgService = new DawgService(dictionaryRepository);
        ReflectionTestUtils.setField(dawgService, "root", root);
    }

    @Test
    void lookup() {
    }

    @Test
    void fillGapInPattern() throws DawgIsNotReady {
        //given
        String pattern = "c*t";

        //when
        List<Character> characters = dawgService.fillGapInPattern(pattern);

        //then
        assertNotNull(characters);
        assertEquals(4, characters.size());
        assertTrue(characters.contains('a'));
        assertTrue(characters.contains('o'));
        assertTrue(characters.contains('u'));
        assertTrue(characters.contains('w'));
    }

    @ParameterizedTest
    @ValueSource(strings = {"c**t", "c*t*", "*c*t", "cats", "***", "a", "*"})
    void fillGapInPattern_twoGaps_throwException(String pattern) {
        //given

        //when
        Assertions.assertThrows(IllegalArgumentException.class, () -> dawgService.fillGapInPattern(pattern));
    }

    private Node prepareDawg(String filename) throws IOException {
        return objectMapper.readValue(
                JsonUtils.loadJsonFromClasspath(format("/domain/service/%s", filename)), Node.class);
    }
}