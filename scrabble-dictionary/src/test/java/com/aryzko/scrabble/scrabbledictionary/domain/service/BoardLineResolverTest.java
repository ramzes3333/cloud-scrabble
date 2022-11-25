package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.common.JsonUtils;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.Node;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.AvailableLetters;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardLineResolverTest {

    @Mock
    private DawgService dawgService;
    @InjectMocks
    private LineResolver resolver;

    @Test
    void resolve() throws IOException, DawgIsNotReady {
        //given
        Node root = JsonUtils.loadObjectFromJson("/domain/service/dawg-for-resolver.json", Node.class);
        Line preparedLine = JsonUtils.loadObjectFromJson("/domain/service/prepared-lines-5x5.json", Line.class);
        AvailableLetters rack = AvailableLetters.builder().character('f').character('k').character('c').build();

        when(dawgService.getDawg()).thenReturn(root);

        //when
        Solution solution = resolver.resolve(preparedLine, rack);

        //then
        assertNotNull(solution);
        assertNotNull(solution.getWords());
        assertEquals(5, solution.getWords().size()); //fok, koc, ok, ko, co

        //fok
        Solution.Word word1 = getWord(solution, "fok", 2, 0);
        assertEquals('f', word1.getElements().get(0).getLetter());
        assertEquals('o', word1.getElements().get(1).getLetter());
        assertEquals('k', word1.getElements().get(2).getLetter());

        assertFalse(word1.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(2).isUnmodifiableLetter());

        assertEquals(2, word1.getElements().get(0).getX());
        assertEquals(3, word1.getElements().get(1).getX());
        assertEquals(4, word1.getElements().get(2).getX());

        assertEquals(0, word1.getElements().get(0).getY());
        assertEquals(0, word1.getElements().get(1).getY());
        assertEquals(0, word1.getElements().get(2).getY());

        //koc
        Solution.Word word2 = getWord(solution, "koc", 2, 0);
        assertEquals('k', word2.getElements().get(0).getLetter());
        assertEquals('o', word2.getElements().get(1).getLetter());
        assertEquals('c', word2.getElements().get(2).getLetter());

        assertFalse(word2.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word2.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word2.getElements().get(2).isUnmodifiableLetter());

        assertEquals(2, word2.getElements().get(0).getX());
        assertEquals(3, word2.getElements().get(1).getX());
        assertEquals(4, word2.getElements().get(2).getX());

        assertEquals(0, word2.getElements().get(0).getY());
        assertEquals(0, word2.getElements().get(1).getY());
        assertEquals(0, word2.getElements().get(2).getY());

        //ok
        Solution.Word word3 = getWord(solution, "ok", 3, 0);
        assertEquals('o', word3.getElements().get(0).getLetter());
        assertEquals('k', word3.getElements().get(1).getLetter());

        assertTrue(word3.getElements().get(0).isUnmodifiableLetter());
        assertFalse(word3.getElements().get(1).isUnmodifiableLetter());

        assertEquals(3, word3.getElements().get(0).getX());
        assertEquals(4, word3.getElements().get(1).getX());

        assertEquals(0, word3.getElements().get(0).getY());
        assertEquals(0, word3.getElements().get(1).getY());

        //ko
        Solution.Word word4 = getWord(solution, "ko", 2, 0);
        assertEquals('k', word4.getElements().get(0).getLetter());
        assertEquals('o', word4.getElements().get(1).getLetter());

        assertFalse(word4.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word4.getElements().get(1).isUnmodifiableLetter());

        assertEquals(2, word4.getElements().get(0).getX());
        assertEquals(3, word4.getElements().get(1).getX());

        assertEquals(0, word4.getElements().get(0).getY());
        assertEquals(0, word4.getElements().get(1).getY());

        //co
        Solution.Word word5 = getWord(solution, "co", 2, 0);
        assertEquals('c', word5.getElements().get(0).getLetter());
        assertEquals('o', word5.getElements().get(1).getLetter());

        assertFalse(word5.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word5.getElements().get(1).isUnmodifiableLetter());

        assertEquals(2, word5.getElements().get(0).getX());
        assertEquals(3, word5.getElements().get(1).getX());

        assertEquals(0, word5.getElements().get(0).getY());
        assertEquals(0, word5.getElements().get(1).getY());
    }

    private Solution.Word getWord(Solution solution, String word, Integer x, Integer y) {
        List<Solution.Word> words = solution.getWords().stream()
                .filter(w -> w.getWordAsString().equals(word)
                        && w.getElements().get(0).getX() == x
                        && w.getElements().get(0).getY() == y)
                .collect(Collectors.toList());

        assertEquals(1, words.size());
        return words.get(0);
    }
}