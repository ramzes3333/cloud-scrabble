package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.common.JsonUtils;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.Node;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.AvailableLetters;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineResolverTest {

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

    @Test
    void resolve_joiningWords() throws IOException, DawgIsNotReady {
        //given
        Node root = JsonUtils.loadObjectFromJson("/domain/service/dawg-for-resolver-joining-words.json", Node.class);
        Line preparedLine = JsonUtils.loadObjectFromJson("/domain/service/prepared-lines-15x15-joining-words.json", Line.class);
        AvailableLetters rack = AvailableLetters.builder()
                .character('n').character('o').character('w').character('i').character('e').character('ó').build();

        when(dawgService.getDawg()).thenReturn(root);

        //when
        Solution solution = resolver.resolve(preparedLine, rack);

        //then
        assertNotNull(solution);
        assertNotNull(solution.getWords());
        assertEquals(2, solution.getWords().size()); //ponów, ponowieni, !ponowie!

        //fok
        Solution.Word word1 = getWord(solution, "ponów", 6, 7);
        assertEquals('p', word1.getElements().get(0).getLetter());
        assertEquals('o', word1.getElements().get(1).getLetter());
        assertEquals('n', word1.getElements().get(2).getLetter());
        assertEquals('ó', word1.getElements().get(3).getLetter());
        assertEquals('w', word1.getElements().get(4).getLetter());

        assertTrue(word1.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(2).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(3).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(4).isUnmodifiableLetter());

        assertEquals(6, word1.getElements().get(0).getX());
        assertEquals(7, word1.getElements().get(1).getX());
        assertEquals(8, word1.getElements().get(2).getX());
        assertEquals(9, word1.getElements().get(3).getX());
        assertEquals(10, word1.getElements().get(4).getX());

        assertEquals(7, word1.getElements().get(0).getY());
        assertEquals(7, word1.getElements().get(1).getY());
        assertEquals(7, word1.getElements().get(2).getY());
        assertEquals(7, word1.getElements().get(3).getY());
        assertEquals(7, word1.getElements().get(4).getY());

        //koc
        Solution.Word word2 = getWord(solution, "ponowieni", 6, 7);
        assertEquals('p', word2.getElements().get(0).getLetter());
        assertEquals('o', word2.getElements().get(1).getLetter());
        assertEquals('n', word2.getElements().get(2).getLetter());
        assertEquals('o', word2.getElements().get(3).getLetter());
        assertEquals('w', word2.getElements().get(4).getLetter());
        assertEquals('i', word2.getElements().get(5).getLetter());
        assertEquals('e', word2.getElements().get(6).getLetter());
        assertEquals('n', word2.getElements().get(7).getLetter());
        assertEquals('i', word2.getElements().get(8).getLetter());

        assertTrue(word2.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word2.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word2.getElements().get(2).isUnmodifiableLetter());
        assertFalse(word2.getElements().get(3).isUnmodifiableLetter());
        assertFalse(word2.getElements().get(4).isUnmodifiableLetter());
        assertFalse(word2.getElements().get(5).isUnmodifiableLetter());
        assertFalse(word2.getElements().get(6).isUnmodifiableLetter());
        assertTrue(word2.getElements().get(7).isUnmodifiableLetter());
        assertTrue(word2.getElements().get(8).isUnmodifiableLetter());

        assertEquals(6, word2.getElements().get(0).getX());
        assertEquals(7, word2.getElements().get(1).getX());
        assertEquals(8, word2.getElements().get(2).getX());
        assertEquals(9, word2.getElements().get(3).getX());
        assertEquals(10, word2.getElements().get(4).getX());
        assertEquals(11, word2.getElements().get(5).getX());
        assertEquals(12, word2.getElements().get(6).getX());
        assertEquals(13, word2.getElements().get(7).getX());
        assertEquals(14, word2.getElements().get(8).getX());

        assertEquals(7, word2.getElements().get(0).getY());
        assertEquals(7, word2.getElements().get(1).getY());
        assertEquals(7, word2.getElements().get(2).getY());
        assertEquals(7, word2.getElements().get(3).getY());
        assertEquals(7, word2.getElements().get(4).getY());
        assertEquals(7, word2.getElements().get(5).getY());
        assertEquals(7, word2.getElements().get(6).getY());
        assertEquals(7, word2.getElements().get(7).getY());
        assertEquals(7, word2.getElements().get(8).getY());

        noWord(solution, "ponowie", 6, 7);
    }

    @Test
    void resolve_duplicatesInAvailableLetters() throws IOException, DawgIsNotReady {
        //given
        Node root = JsonUtils.loadObjectFromJson("/domain/service/dawg-for-resolver-joining-words.json", Node.class);
        Line preparedLine = JsonUtils.loadObjectFromJson("/domain/service/prepared-lines-15x15-joining-words.json", Line.class);
        AvailableLetters rack = AvailableLetters.builder()
                .character('n').character('w').character('ó').character('ó').build();

        when(dawgService.getDawg()).thenReturn(root);

        //when
        Solution solution = resolver.resolve(preparedLine, rack);

        //then
        assertNotNull(solution);
        assertNotNull(solution.getWords());
        assertEquals(1, solution.getWords().size()); //ponów, ponowieni, !ponowie!

        //fok
        Solution.Word word1 = getWord(solution, "ponów", 6, 7);
        assertEquals('p', word1.getElements().get(0).getLetter());
        assertEquals('o', word1.getElements().get(1).getLetter());
        assertEquals('n', word1.getElements().get(2).getLetter());
        assertEquals('ó', word1.getElements().get(3).getLetter());
        assertEquals('w', word1.getElements().get(4).getLetter());

        assertTrue(word1.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(2).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(3).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(4).isUnmodifiableLetter());

        assertEquals(6, word1.getElements().get(0).getX());
        assertEquals(7, word1.getElements().get(1).getX());
        assertEquals(8, word1.getElements().get(2).getX());
        assertEquals(9, word1.getElements().get(3).getX());
        assertEquals(10, word1.getElements().get(4).getX());

        assertEquals(7, word1.getElements().get(0).getY());
        assertEquals(7, word1.getElements().get(1).getY());
        assertEquals(7, word1.getElements().get(2).getY());
        assertEquals(7, word1.getElements().get(3).getY());
        assertEquals(7, word1.getElements().get(4).getY());
    }

    @Test
    void resolve_anchorFilled() throws IOException, DawgIsNotReady {
        //given
        Node root = JsonUtils.loadObjectFromJson("/domain/service/dawg-for-resolver-anchor-filling.json", Node.class);
        Line preparedLine = JsonUtils.loadObjectFromJson("/domain/service/prepared-lines-5x5-anchor-filling.json", Line.class);
        AvailableLetters rack = AvailableLetters.builder()
                .character('u').character('l').character('a').build();

        when(dawgService.getDawg()).thenReturn(root);

        //when
        Solution solution = resolver.resolve(preparedLine, rack);

        //then
        assertNotNull(solution);
        assertNotNull(solution.getWords());
        assertEquals(1, solution.getWords().size()); //ponów, ponowieni, !ponowie!

        //fok
        Solution.Word word1 = getWord(solution, "ula", 0, 0);
        assertEquals('u', word1.getElements().get(0).getLetter());
        assertEquals('l', word1.getElements().get(1).getLetter());
        assertEquals('a', word1.getElements().get(2).getLetter());

        assertFalse(word1.getElements().get(0).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(2).isUnmodifiableLetter());

        assertEquals(0, word1.getElements().get(0).getX());
        assertEquals(1, word1.getElements().get(1).getX());
        assertEquals(2, word1.getElements().get(2).getX());

        assertEquals(0, word1.getElements().get(0).getY());
        assertEquals(0, word1.getElements().get(1).getY());
        assertEquals(0, word1.getElements().get(2).getY());
    }

    @Test
    void resolve_blank() throws IOException, DawgIsNotReady {
        //given
        Node root = JsonUtils.loadObjectFromJson("/domain/service/dawg-for-resolver-anchor-filling.json", Node.class);
        Line preparedLine = JsonUtils.loadObjectFromJson("/domain/service/prepared-lines-5x5-anchor-filling.json", Line.class);
        AvailableLetters rack = AvailableLetters.builder()
                .character('u').character('l').character(' ').build();

        when(dawgService.getDawg()).thenReturn(root);

        //when
        Solution solution = resolver.resolve(preparedLine, rack);

        //then
        assertNotNull(solution);
        assertNotNull(solution.getWords());
        assertEquals(1, solution.getWords().size()); //ponów, ponowieni, !ponowie!

        //fok
        Solution.Word word1 = getWord(solution, "ula", 0, 0);
        assertEquals('u', word1.getElements().get(0).getLetter());
        assertEquals('l', word1.getElements().get(1).getLetter());
        assertEquals('a', word1.getElements().get(2).getLetter());

        assertFalse(word1.getElements().get(0).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(1).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(2).isUnmodifiableLetter());

        assertEquals(0, word1.getElements().get(0).getX());
        assertEquals(1, word1.getElements().get(1).getX());
        assertEquals(2, word1.getElements().get(2).getX());

        assertEquals(0, word1.getElements().get(0).getY());
        assertEquals(0, word1.getElements().get(1).getY());
        assertEquals(0, word1.getElements().get(2).getY());

        assertFalse(word1.getElements().get(0).isBlank());
        assertFalse(word1.getElements().get(1).isBlank());
        assertTrue(word1.getElements().get(2).isBlank());
    }

    @Test
    void resolve_blankInResponse() throws IOException, DawgIsNotReady {
        //given
        Node root = JsonUtils.loadObjectFromJson("/domain/service/dawg-for-resolver-blank-in-response.json", Node.class);
        Line preparedLine = JsonUtils.loadObjectFromJson("/domain/service/prepared-lines-15x15-blank-in-response.json", Line.class);
        AvailableLetters rack = AvailableLetters.builder()
                .character('o').character('a').character('y')
                .character(' ').character('m').character('n').character('z').build();

        when(dawgService.getDawg()).thenReturn(root);

        //when
        Solution solution = resolver.resolve(preparedLine, rack);

        //then
        assertNotNull(solution);
        assertNotNull(solution.getWords());
        assertEquals(1, solution.getWords().size());

        //fok
        Solution.Word word1 = getWord(solution, "aśćkami", 3, 9);
        assertEquals('a', word1.getElements().get(0).getLetter());
        assertEquals('ś', word1.getElements().get(1).getLetter());
        assertEquals('ć', word1.getElements().get(2).getLetter());
        assertEquals('k', word1.getElements().get(3).getLetter());
        assertEquals('a', word1.getElements().get(4).getLetter());
        assertEquals('m', word1.getElements().get(5).getLetter());
        assertEquals('i', word1.getElements().get(6).getLetter());

        assertTrue(word1.getElements().get(0).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(1).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(2).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(3).isUnmodifiableLetter());
        assertTrue(word1.getElements().get(4).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(5).isUnmodifiableLetter());
        assertFalse(word1.getElements().get(6).isUnmodifiableLetter());

        assertEquals(3, word1.getElements().get(0).getX());
        assertEquals(4, word1.getElements().get(1).getX());
        assertEquals(5, word1.getElements().get(2).getX());
        assertEquals(6, word1.getElements().get(3).getX());
        assertEquals(7, word1.getElements().get(4).getX());
        assertEquals(8, word1.getElements().get(5).getX());
        assertEquals(9, word1.getElements().get(6).getX());

        assertEquals(9, word1.getElements().get(0).getY());
        assertEquals(9, word1.getElements().get(1).getY());
        assertEquals(9, word1.getElements().get(2).getY());
        assertEquals(9, word1.getElements().get(3).getY());
        assertEquals(9, word1.getElements().get(4).getY());
        assertEquals(9, word1.getElements().get(5).getY());
        assertEquals(9, word1.getElements().get(6).getY());

        assertFalse(word1.getElements().get(0).isBlank());
        assertFalse(word1.getElements().get(1).isBlank());
        assertFalse(word1.getElements().get(2).isBlank());
        assertFalse(word1.getElements().get(3).isBlank());
        assertFalse(word1.getElements().get(4).isBlank());
        assertFalse(word1.getElements().get(5).isBlank());
        assertTrue(word1.getElements().get(6).isBlank());
    }

    private Solution.Word getWord(Solution solution, String word, Integer x, Integer y) {
        List<Solution.Word> words = solution.getWords().stream()
                .filter(w -> w.getWordAsString().equals(word)
                        && w.getElements().get(0).getX() == x
                        && w.getElements().get(0).getY() == y)
                .toList();

        assertEquals(1, words.size());
        return words.get(0);
    }

    private void noWord(Solution solution, String word, Integer x, Integer y) {
        List<Solution.Word> words = solution.getWords().stream()
                .filter(w -> w.getWordAsString().equals(word)
                        && w.getElements().get(0).getX() == x
                        && w.getElements().get(0).getY() == y)
                .toList();

        assertEquals(0, words.size());
    }
}