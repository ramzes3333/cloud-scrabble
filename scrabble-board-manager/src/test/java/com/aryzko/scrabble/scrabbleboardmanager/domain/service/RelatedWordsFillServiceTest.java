package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RelatedWordsFillServiceTest {

    private RelatedWordsFillService relatedWordsFillService = new RelatedWordsFillService();

    @Test
    void fill_solution() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-3x3.json", Board.class);
        Solution solution = prepareSolution();

        //when
        relatedWordsFillService.fill(board, solution);

        //then
        assertEquals(1, solution.getWords().size());
        Word word = solution.getWords().get(0);
        assertEquals(3, word.getRelatedWords().size());

        Word relatedWord1 = getRelatedWord(word, "on");
        assertNotNull(relatedWord1);
        assertEquals(0, relatedWord1.getElements().get(0).getX());
        assertEquals(0, relatedWord1.getElements().get(1).getX());
        assertEquals(1, relatedWord1.getElements().get(0).getY());
        assertEquals(2, relatedWord1.getElements().get(1).getY());

        Word relatedWord2 = getRelatedWord(word, "no");
        assertNotNull(relatedWord2);
        assertEquals(1, relatedWord2.getElements().get(0).getX());
        assertEquals(1, relatedWord2.getElements().get(1).getX());
        assertEquals(1, relatedWord2.getElements().get(0).getY());
        assertEquals(2, relatedWord2.getElements().get(1).getY());

        Word relatedWord3 = getRelatedWord(word, "as");
        assertNotNull(relatedWord2);
        assertEquals(2, relatedWord3.getElements().get(0).getX());
        assertEquals(2, relatedWord3.getElements().get(1).getX());
        assertEquals(1, relatedWord3.getElements().get(0).getY());
        assertEquals(2, relatedWord3.getElements().get(1).getY());
    }

    @Test
    void fill_wordAdjacentToEndOfExistingHorizontalWord() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-15x15-word-adjacent-to-end-of-existing-word.json", Board.class);
        Word word = prepareWord();

        //when
        List<Word> relatedWords = relatedWordsFillService.getRelatedWords(board, word);

        //then
        assertEquals(0, relatedWords.size());
    }

    private static Word getRelatedWord(Word word, String value) {
        return word.getRelatedWords().stream()
                .filter(w -> w.getWordAsString().equals(value))
                .findFirst().orElse(null);
    }

    private Solution prepareSolution() {
        Solution.SolutionBuilder solutionBuilder = Solution.builder();
        solutionBuilder.words(List.of(
                prepareWord(List.of(
                        prepareElement(0, 2, 'n', false),
                        prepareElement(1, 2, 'o', false),
                        prepareElement(2, 2, 's', false)))));

        return solutionBuilder.build();
    }

    private Word prepareWord() {
        return prepareWord(List.of(
                        prepareElement(9, 5, 'ź', false),
                        prepareElement(9, 6, 'l', false),
                        prepareElement(9, 7, 'i', false)));
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