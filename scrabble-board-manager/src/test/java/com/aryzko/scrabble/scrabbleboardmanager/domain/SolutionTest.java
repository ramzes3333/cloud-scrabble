package com.aryzko.scrabble.scrabbleboardmanager.domain;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.TransposeType;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SolutionTest {

    @Test
    void transpose() {
        //given
        Solution solution = prepareSolution();

        //when
        Solution result = solution.transpose();

        //then
        assertNotNull(result);
        Word word1 = getWord(result, "oto");
        assertNotNull(word1);
        assertEquals(3, word1.getElements().get(0).getX());
        assertEquals(3, word1.getElements().get(1).getX());
        assertEquals(3, word1.getElements().get(2).getX());
        assertEquals(0, word1.getElements().get(0).getY());
        assertEquals(1, word1.getElements().get(1).getY());
        assertEquals(2, word1.getElements().get(2).getY());

        Word word2 = getWord(result, "koc");
        assertNotNull(word2);
        assertEquals(0, word2.getElements().get(0).getX());
        assertEquals(1, word2.getElements().get(1).getX());
        assertEquals(2, word2.getElements().get(2).getX());
        assertEquals(3, word2.getElements().get(0).getY());
        assertEquals(3, word2.getElements().get(1).getY());
        assertEquals(3, word2.getElements().get(2).getY());
    }

    private static Word getWord(Solution solution, String word) {
        return solution.getWords().stream()
                .filter(w -> w.getWordAsString().equals(word))
                .findFirst().orElse(null);
    }

    private Solution prepareSolution() {
        Solution.SolutionBuilder solutionBuilder = Solution.builder();
        solutionBuilder.words(List.of(
                prepareWord(List.of(
                        prepareElement(0, 3, 'o', false),
                        prepareElement(1, 3, 't', false),
                        prepareElement(2, 3, 'o', false))),
                prepareWord(List.of(
                        prepareElement(3, 0, 'k', false),
                        prepareElement(3, 1, 'o', false),
                        prepareElement(3, 2, 'c', false)))));

        return solutionBuilder.build();
    }

    private static Word prepareWord(List<Word.Element> elements) {
        return Word.builder()
                .elements(elements)
                .build();
    }

    private static Word.Element prepareElement(Integer x, Integer y, Character character, boolean onBoard) {
        return Word.Element.builder()
                .x(x)
                .y(y)
                .letter(character)
                .onBoard(onBoard)
                .build();
    }
}