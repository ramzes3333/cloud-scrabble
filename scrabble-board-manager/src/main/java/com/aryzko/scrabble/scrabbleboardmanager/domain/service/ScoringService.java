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

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Service
public class ScoringService {

    private static final int BINGO_BONUS = 50;

    private final TileManagerProvider tileManagerProvider;

    public Solution score(final Board board, final Solution solution) {
        Map<Character, Integer> pointsMap = tileManagerProvider.getTileConfiguration(board.getId().toString()).getPointsMap();
        Map<Position, Bonus> bonusMap = board.getBonusMap();

        solution.getWords().stream()
                .map(Solution.Word::getRelatedWords)
                .flatMap(Collection::stream)
                .forEach(relatedWord -> relatedWord.setPoints(
                        score(board.getBoardParameters().getRackSize(), pointsMap, bonusMap, relatedWord)));

        solution.getWords()
                .forEach(word -> word.setPoints(score(board.getBoardParameters().getRackSize(), pointsMap, bonusMap, word)));

        solution.getWords().stream()
                .map(Solution.Word::getElements)
                .flatMap(Collection::stream)
                .forEach(el -> el.setPoints(getLetterPoints(pointsMap, el)));

        solution.getWords().stream()
                .map(Solution.Word::getRelatedWords)
                .flatMap(Collection::stream)
                .map(Solution.Word::getElements)
                .flatMap(Collection::stream)
                .forEach(el -> el.setPoints(getLetterPoints(pointsMap, el)));

        return solution;
    }

    private int score(Integer rackSize, Map<Character, Integer> pointsMap, Map<Position, Bonus> bonusMap, Solution.Word word) {
        List<Bonus> wordBonus = word.getElements().stream()
                .map(e -> getBonus(bonusMap, e))
                .filter(this::isWordBonus)
                .toList();

        int lettersPoints = word.getElements().stream()
                .mapToInt(e -> getLetterBonus(bonusMap, e).getMultiply() * getLetterPoints(pointsMap, e))
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

    private static Integer getLetterPoints(Map<Character, Integer> pointsMap, Solution.Word.Element e) {
        /*if(e.isOnBoard()) {
            return 0;
        }*/
        return ofNullable(pointsMap.get(Character.toLowerCase(e.getLetter())))
                .orElseThrow(() -> new IllegalStateException("No tile configuration for letter"));
    }

    private static Bonus getBonus(Map<Position, Bonus> bonusMap, Solution.Word.Element e) {
        return ofNullable(bonusMap.get(Position.builder()
                .x(e.getX())
                .y(e.getY())
                .build()))
                .orElse(Bonus.None);
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
