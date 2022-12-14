package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.TileConfiguration;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.aryzko.scrabble.scrabbleboardmanager.common.JsonUtils.loadObjectFromJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Mock
    private TileManagerProvider tileManagerProvider;

    @InjectMocks
    private ScoringService scoringService;

    @Test
    void score() throws IOException {
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
        Solution.Word word1 = getWord(solution, "fok"); //(5+(2*2))*3 + 50 (bonus)
        assertNotNull(word1);
        assertEquals(77, word1.getPoints());

        Solution.Word word2 = getWord(solution, "koc"); //(2+(2*3))*2 + 50 (bonus)
        assertNotNull(word2);
        assertEquals(66, word2.getPoints());

        Solution.Word word3 = getWord(solution, "ok"); //2*2
        assertNotNull(word3);
        assertEquals(4, word3.getPoints());
    }

    private static Solution.Word getWord(Solution solution, String word) {
        return solution.getWords().stream()
                .filter(w -> w.getWordAsString().equals(word))
                .findFirst().orElse(null);
    }

    private Solution prepareSolution() {
        Solution.SolutionBuilder solutionBuilder = Solution.builder();
        solutionBuilder.words(List.of(
                prepareWord(List.of(
                        prepareElement(2, 0, 'f', false),
                        prepareElement(3, 0, 'o', true),
                        prepareElement(4, 0, 'k', false))),

                prepareWord(List.of(
                        prepareElement(1, 1, 'k', false),
                        prepareElement(1, 2, 'o', true),
                        prepareElement(1, 3, 'c', false))),

                prepareWord(List.of(
                        prepareElement(3, 0, 'o', true),
                        prepareElement(4, 0, 'k', false)))));

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