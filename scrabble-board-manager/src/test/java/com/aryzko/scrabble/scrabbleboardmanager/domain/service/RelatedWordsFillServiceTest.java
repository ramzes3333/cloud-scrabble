package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.*;

class RelatedWordsFillServiceTest {

    private RelatedWordsFillService relatedWordsFillService = new RelatedWordsFillService();

    @Test
    void fill() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-3x3.json", Board.class);
        Solution solution = prepareSolution();

        //when
        relatedWordsFillService.fill(board, solution);

        //then
        assertEquals(1, solution.getWords().size());
        Solution.Word word = solution.getWords().get(0);
        assertEquals(3, word.getRelatedWords().size());

        Solution.Word relatedWord1 = getWord(word, "on");
        assertNotNull(relatedWord1);
        assertEquals(0, relatedWord1.getElements().get(0).getX());
        assertEquals(0, relatedWord1.getElements().get(1).getX());
        assertEquals(1, relatedWord1.getElements().get(0).getY());
        assertEquals(2, relatedWord1.getElements().get(1).getY());

        Solution.Word relatedWord2 = getWord(word, "no");
        assertNotNull(relatedWord2);
        assertEquals(1, relatedWord2.getElements().get(0).getX());
        assertEquals(1, relatedWord2.getElements().get(1).getX());
        assertEquals(1, relatedWord2.getElements().get(0).getY());
        assertEquals(2, relatedWord2.getElements().get(1).getY());

        Solution.Word relatedWord3 = getWord(word, "as");
        assertNotNull(relatedWord2);
        assertEquals(2, relatedWord3.getElements().get(0).getX());
        assertEquals(2, relatedWord3.getElements().get(1).getX());
        assertEquals(1, relatedWord3.getElements().get(0).getY());
        assertEquals(2, relatedWord3.getElements().get(1).getY());
    }

    private static Solution.Word getWord(Solution.Word word, String value) {
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

    private static Solution.Word prepareWord(List<Solution.Word.Element> elements) {
        return Solution.Word.builder()
                .elements(elements)
                .build();
    }

    private static Solution.Word.Element prepareElement(Integer x, Integer y, Character character, boolean onBoard) {
        return Solution.Word.Element.builder()
                .x(x)
                .y(y)
                .letter(character)
                .onBoard(onBoard)
                .build();
    }
}