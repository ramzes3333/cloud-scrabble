package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Bonus;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Service
public class ScoringService {

    private static final int BINGO_BONUS = 50;

    private final TileManagerProvider tileManagerProvider;

    public Solution score(final Board board, final Solution solution) {
        final Map<Character, Integer> pointsMap = tileManagerProvider.getTileConfiguration(board.getId().toString()).getPointsMap();
        final Map<Position, Bonus> bonusMap = board.getBonusMap();
        final List<Position> positionsWithBlank = board.getPositionsWithBlank();

        processWords(board, solution, pointsMap, bonusMap, positionsWithBlank);
        processRelatedWords(board, solution, pointsMap, bonusMap, positionsWithBlank);
        processWordElements(solution, pointsMap, positionsWithBlank);
        processRelatedWordElements(solution, pointsMap, positionsWithBlank);

        return solution;
    }

    private void processRelatedWords(Board board, Solution solution, Map<Character, Integer> pointsMap, Map<Position, Bonus> bonusMap, List<Position> positionsWithBlank) {
        solution.getWords()
                .forEach(word -> {
                    word.setPoints(score(board.getBoardParameters().getRackSize(),
                            pointsMap,
                            bonusMap,
                            positionsWithBlank,
                            word));
                    word.setBonuses(
                            getBonuses(word, bonusMap)
                    );
                });
    }

    private void processWords(Board board, Solution solution, Map<Character, Integer> pointsMap, Map<Position, Bonus> bonusMap, List<Position> positionsWithBlank) {
        solution.getWords().stream()
                .map(Solution.Word::getRelatedWords)
                .flatMap(Collection::stream)
                .forEach(relatedWord -> {
                            relatedWord.setPoints(
                                    score(board.getBoardParameters().getRackSize(),
                                            pointsMap,
                                            bonusMap,
                                            positionsWithBlank,
                                            relatedWord));
                            relatedWord.setBonuses(
                                    getBonuses(relatedWord, bonusMap)
                            );
                        }
                );
    }

    private static void processRelatedWordElements(Solution solution, Map<Character, Integer> pointsMap, List<Position> positionsWithBlank) {
        solution.getWords().stream()
                .map(Solution.Word::getRelatedWords)
                .flatMap(Collection::stream)
                .map(Solution.Word::getElements)
                .flatMap(Collection::stream)
                .forEach(el -> el.setPoints(getLetterPoints(pointsMap, positionsWithBlank, el)));
    }

    private static void processWordElements(Solution solution, Map<Character, Integer> pointsMap, List<Position> positionsWithBlank) {
        solution.getWords().stream()
                .map(Solution.Word::getElements)
                .flatMap(Collection::stream)
                .forEach(el -> el.setPoints(getLetterPoints(pointsMap, positionsWithBlank, el)));
    }

    private static List<Bonus> getBonuses(Solution.Word word, Map<Position, Bonus> bonusMap) {
        return word.getElements().stream()
                .filter(not(Solution.Word.Element::isOnBoard))
                .map(e -> getBonus(bonusMap, e))
                .filter(b -> !b.equals(Bonus.None))
                .collect(Collectors.toList());
    }

    private int score(final Integer rackSize,
                      final Map<Character, Integer> pointsMap,
                      final Map<Position, Bonus> bonusMap,
                      final List<Position> positionsWithBlank,
                      final Solution.Word word) {

        List<Bonus> wordBonus = word.getElements().stream()
                .map(e -> getBonus(bonusMap, e))
                .filter(this::isWordBonus)
                .toList();

        int lettersPoints = word.getElements().stream()
                .mapToInt(e -> getLetterBonus(bonusMap, e).getMultiply() * getLetterPoints(pointsMap, positionsWithBlank, e))
                .sum();

        boolean bingoBonus = word.getElements().stream()
                .filter(not(Solution.Word.Element::isOnBoard))
                .count() == rackSize;

        int wordPoints = wordBonus.stream()
                .mapToInt(Bonus::getMultiply)
                .reduce((bonus, bonus2) -> bonus * bonus2)
                .orElse(1) * lettersPoints + (bingoBonus ? BINGO_BONUS : 0);

        return wordPoints + word.getRelatedWords().stream()
                .mapToInt(Solution.Word::getPoints)
                .sum();
    }

    private static Integer getLetterPoints(
            final Map<Character, Integer> pointsMap,
            final List<Position> positionsWithBlank,
            final Solution.Word.Element e) {

        if (e.isBlank() || positionsWithBlank.contains(Position.builder().x(e.getX()).y(e.getY()).build())) {
            return 0;
        }
        return ofNullable(pointsMap.get(Character.toLowerCase(e.getLetter())))
                .orElseThrow(() -> new IllegalStateException("No tile configuration for letter"));
    }

    private static Bonus getBonus(Map<Position, Bonus> bonusMap, Solution.Word.Element e) {
        return e.isOnBoard() ? Bonus.None : ofNullable(bonusMap.get(getPosition(e))).orElse(Bonus.None);
    }

    private static Position getPosition(Solution.Word.Element e) {
        return Position.builder()
                .x(e.getX())
                .y(e.getY())
                .build();
    }

    private Bonus getLetterBonus(Map<Position, Bonus> bonusMap, Solution.Word.Element e) {
        Bonus bonus = getBonus(bonusMap, e);
        return isLetterBonus(bonus) ? bonus : Bonus.None;
    }

    private Bonus getWordBonus(Map<Position, Bonus> bonusMap, Solution.Word.Element e) {
        Bonus bonus = getBonus(bonusMap, e);
        return isWordBonus(bonus) ? bonus : Bonus.None;
    }

    private boolean isWordBonus(Bonus bonus) {
        return switch (bonus) {
            case DoubleWordScore, TripleWordScore -> true;
            case DoubleLetterScore, TripleLetterScore, None -> false;
        };
    }

    private boolean isLetterBonus(Bonus bonus) {
        return switch (bonus) {
            case DoubleLetterScore, TripleLetterScore -> true;
            case DoubleWordScore, TripleWordScore, None -> false;
        };
    }
}
