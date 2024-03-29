package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.TileConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Mock
    private TileManagerProvider tileManagerProvider;

    @InjectMocks
    private ScoringService scoringService;

    @Test
    void score_singleWords() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-5x5.json", Board.class);
        TileConfiguration tileConfiguration =
                loadObjectFromJson("/domain/service/tile-configuration.json", TileConfiguration.class);
        Solution solution = prepareSolution();

        when(tileManagerProvider.getTileConfiguration(board.getId().toString())).thenReturn(tileConfiguration);

        //when
        Solution result = scoringService.score(board, solution);

        //then
        assertEquals(solution.getWords().size(), result.getWords().size());
        Word word1 = getWord(solution, "FOK"); //(5+1+(2*2))*3 + 50 (bonus)
        assertNotNull(word1);
        assertEquals(80, word1.getPoints());

        Word word2 = getWord(solution, "KOC"); //(2+1+(2*3))*2 + 50 (bonus)
        assertNotNull(word2);
        assertEquals(68, word2.getPoints());

        Word word3 = getWord(solution, "OK"); //1+2*2
        assertNotNull(word3);
        assertEquals(5, word3.getPoints());
    }

    @Test
    void score_withRelatedWords() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-3x3-related-words.json", Board.class);
        TileConfiguration tileConfiguration =
                loadObjectFromJson("/domain/service/tile-configuration.json", TileConfiguration.class);
        Solution solution = prepareSolutionRelatedWords();

        when(tileManagerProvider.getTileConfiguration(board.getId().toString())).thenReturn(tileConfiguration);

        //when
        Solution result = scoringService.score(board, solution);

        //then
        assertEquals(solution.getWords().size(), result.getWords().size());
        Word word1 = getWord(solution, "MĄ"); //MĄ (2+5)*2 + IM (1+2)*2
        assertNotNull(word1);
        assertEquals(20, word1.getPoints());
    }

    @Test
    void score_wordAdjacentToEndOfExistingWord() throws IOException {
        //given
        Board board = loadObjectFromJson("/domain/service/board-to-resolve-15x15-word-adjacent-to-end-of-existing-word.json", Board.class);
        TileConfiguration tileConfiguration =
                loadObjectFromJson("/domain/service/tile-configuration.json", TileConfiguration.class);
        Word solution = prepareWord();

        when(tileManagerProvider.getTileConfiguration(board.getId().toString())).thenReturn(tileConfiguration);

        //when
        Word result = scoringService.score(board, solution);

        //then
        assertNotNull(result);
        assertEquals(35, result.getPoints()); //9*3+2+1 + 3+1+1
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
                        prepareElement(2, 0, 'F', false),
                        prepareElement(3, 0, 'O', true),
                        prepareElement(4, 0, 'K', false)), emptyList()),

                prepareWord(List.of(
                        prepareElement(1, 1, 'K', false),
                        prepareElement(1, 2, 'O', true),
                        prepareElement(1, 3, 'C', false)), emptyList()),

                prepareWord(List.of(
                        prepareElement(3, 0, 'O', true),
                        prepareElement(4, 0, 'K', false)), emptyList())));

        return solutionBuilder.build();
    }

    private Solution prepareSolutionRelatedWords() {
        Solution.SolutionBuilder solutionBuilder = Solution.builder();
        solutionBuilder.words(List.of(
                prepareWord(List.of(
                        prepareElement(1, 1, 'M', false),
                        prepareElement(2, 1, 'Ą', true)),
                        List.of(
                                prepareWord(List.of(
                                        prepareElement(1, 0, 'I', true),
                                        prepareElement(1, 1, 'M', false)), emptyList())))));

        return solutionBuilder.build();
    }

    private Word prepareWord() {
        return prepareWord(List.of(
                                prepareElement(9, 5, 'Ź', false),
                                prepareElement(9, 6, 'L', false),
                                prepareElement(9, 7, 'I', false)),
                        List.of(
                                prepareWord(List.of(
                                        prepareElement(7, 7, 'B', true),
                                        prepareElement(8, 7, 'O', true),
                                        prepareElement(9, 7, 'I', false)), emptyList())));
    }

    private static Word prepareWord(List<Word.Element> elements, List<Word> relatedWords) {
        return Word.builder()
                .elements(elements)
                .relatedWords(relatedWords)
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