package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.aryzko.scrabble:scrabble-dictionary:+:stubs")
class DictionaryClientIntegrationTest {

    private static final String CORRECT_ENTRY_1 = "polski";
    private static final String CORRECT_ENTRY_2 = "gol";
    private static final String INCORRECT_ENTRY = "dg";

    @Autowired
    private DictionaryClient dictionaryClient;

    @Test
    void lookupValues_requestWithCorrectAndIncorrectWords_returnsAppropriateStatus() throws Exception {

        //when
        Map<String, Boolean> result = dictionaryClient.lookupEntries(List.of(CORRECT_ENTRY_1, CORRECT_ENTRY_2, INCORRECT_ENTRY));

        //then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(true, result.get(CORRECT_ENTRY_1));
        assertEquals(true, result.get(CORRECT_ENTRY_2));
        assertEquals(false, result.get(INCORRECT_ENTRY));
    }
}