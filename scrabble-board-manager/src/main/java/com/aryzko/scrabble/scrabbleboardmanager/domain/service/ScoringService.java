package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Bonus;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {

    private static final int BINGO_BONUS = 50;

    private final TileManagerProvider tileManagerProvider;

    public Word score(final Board board, final Word word) {
        final Map<Character, Integer> pointsMap = tileManagerProvider.getTileConfiguration(board.getId().toString()).getPointsMap();
        final Map<Position, Bonus> bonusMap = board.getBonusMap();
        final List<Position> positionsWithBlank = board.getPositionsWithBlank();

        processRelatedWords(word, board, pointsMap, bonusMap, positionsWithBlank);
        processWord(word, board, pointsMap, bonusMap, positionsWithBlank);
        processRelatedWordElements(word, pointsMap, positionsWithBlank);
        processWordElements(word, pointsMap, positionsWithBlank);
        return word;
    }

    public Solution score(final Board board, final Solution solution) {
        final Map<Character, Integer> pointsMap = tileManagerProvider.getTileConfiguration(board.getId().toString()).getPointsMap();
        final Map<Position, Bonus> bonusMap = board.getBonusMap();
        final List<Position> positionsWithBlank = board.getPositionsWithBlank();

        solution.getWords().forEach(word -> {
            processRelatedWords(word, board, pointsMap, bonusMap, positionsWithBlank);
            processWord(word, board, pointsMap, bonusMap, positionsWithBlank);
            processRelatedWordElements(word, pointsMap, positionsWithBlank);
            processWordElements(word, pointsMap, positionsWithBlank);
        });


        return solution;
    }

    private void processWord(Word word, Board board, Map<Character, Integer> pointsMap, Map<Position, Bonus> bonusMap, List<Position> positionsWithBlank) {
        word.setPoints(score(board.getBoardParameters().getRackSize(), pointsMap, bonusMap, positionsWithBlank, word));
        word.setBonuses(getBonuses(word, bonusMap));
    }

    private void processRelatedWords(Word word, Board board, Map<Character, Integer> pointsMap, Map<Position, Bonus> bonusMap, List<Position> positionsWithBlank) {
        ofNullable(word.getRelatedWords()).orElse(Collections.emptyList())
                .forEach(relatedWord -> processWord(relatedWord, board, pointsMap, bonusMap, positionsWithBlank));
    }

    private static void processRelatedWordElements(Word word, Map<Character, Integer> pointsMap, List<Position> positionsWithBlank) {
        ofNullable(word.getRelatedWords()).orElse(Collections.emptyList()).stream()
                .map(Word::getElements)
                .flatMap(Collection::stream)
                .forEach(el -> el.setPoints(getLetterPoints(pointsMap, positionsWithBlank, el)));
    }

    private static void processWordElements(Word word, Map<Character, Integer> pointsMap, List<Position> positionsWithBlank) {
        word.getElements()
                .forEach(el -> el.setPoints(getLetterPoints(pointsMap, positionsWithBlank, el)));
    }

    private static List<Bonus> getBonuses(Word word, Map<Position, Bonus> bonusMap) {
        return word.getElements().stream()
                .filter(not(Word.Element::isOnBoard))
                .map(e -> getBonus(bonusMap, e))
                .filter(b -> !b.equals(Bonus.None))
                .collect(Collectors.toList());
    }

    private int score(final Integer rackSize,
                      final Map<Character, Integer> pointsMap,
                      final Map<Position, Bonus> bonusMap,
                      final List<Position> positionsWithBlank,
                      final Word word) {

        List<Bonus> wordBonus = word.getElements().stream()
                .map(e -> getBonus(bonusMap, e))
                .filter(this::isWordBonus)
                .toList();

        int lettersPoints = word.getElements().stream()
                .mapToInt(e -> getLetterBonus(bonusMap, e).getMultiply() * getLetterPoints(pointsMap, positionsWithBlank, e))
                .sum();

        boolean bingoBonus = word.getElements().stream()
                .filter(not(Word.Element::isOnBoard))
                .count() == rackSize;

        int wordPoints = wordBonus.stream()
                .mapToInt(Bonus::getMultiply)
                .reduce((bonus, bonus2) -> bonus * bonus2)
                .orElse(1) * lettersPoints + (bingoBonus ? BINGO_BONUS : 0);

        int points = wordPoints + ofNullable(word.getRelatedWords()).orElse(Collections.emptyList()).stream()
                .mapToInt(Word::getPoints)
                .sum();

        log.debug("Word (%s) score: %d".formatted(word.getWordAsString(), points));
        return points;
    }

    private static Integer getLetterPoints(
            final Map<Character, Integer> pointsMap,
            final List<Position> positionsWithBlank,
            final Word.Element e) {

        if (e.isBlank() || positionsWithBlank.contains(Position.builder().x(e.getX()).y(e.getY()).build())) {
            return 0;
        }
        return ofNullable(pointsMap.get(e.getLetter()))
                .orElseThrow(() -> new IllegalStateException("No tile configuration for letter"));
    }

    private static Bonus getBonus(Map<Position, Bonus> bonusMap, Word.Element e) {
        return e.isOnBoard() ? Bonus.None : ofNullable(bonusMap.get(getPosition(e))).orElse(Bonus.None);
    }

    private static Position getPosition(Word.Element e) {
        return Position.builder()
                .x(e.getX())
                .y(e.getY())
                .build();
    }

    private Bonus getLetterBonus(Map<Position, Bonus> bonusMap, Word.Element e) {
        Bonus bonus = getBonus(bonusMap, e);
        return isLetterBonus(bonus) ? bonus : Bonus.None;
    }

    private Bonus getWordBonus(Map<Position, Bonus> bonusMap, Word.Element e) {
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
