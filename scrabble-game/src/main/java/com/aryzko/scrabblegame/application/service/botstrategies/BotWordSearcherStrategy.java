package com.aryzko.scrabblegame.application.service.botstrategies;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.domain.model.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public abstract class BotWordSearcherStrategy {

    private static final String WORD_LOG_FORMAT = "%s:%d";
    private final BoardProvider boardProvider;

    public abstract BoardProvider.Word search(Board board, String playerId);

    public abstract Level level();

    protected BoardProvider.Word resolve(Board board, String playerId) {
        Random random = new Random();

        BoardProvider.Solution solution = boardProvider.resolve(board, playerId);
        List<BoardProvider.Word> bucket = distributeIntoBuckets(solution.words()).get(level().getLevel()-1);

        if(bucket.isEmpty()) {
            return null;
        } else {
            return bucket.get(random.nextInt(bucket.size()));
        }
    }

    public static List<List<BoardProvider.Word>> distributeIntoBuckets(List<BoardProvider.Word> words) {
        if (words.isEmpty()) {
            return emptyList();
        }

        List<Integer> sortedUniquePoints = words.stream()
                .map(BoardProvider.Word::points)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        double rangeSize = (double) sortedUniquePoints.size() / Level.values().length;
        List<List<BoardProvider.Word>> buckets = new ArrayList<>();
        for (int i = 0; i < Level.values().length; i++) {
            int start = (int) (i * rangeSize);
            int end = (int) Math.min((i + 1) * rangeSize, sortedUniquePoints.size());
            List<Integer> range = sortedUniquePoints.subList(start, end);
            buckets.add(new ArrayList<>());

            for (BoardProvider.Word word : words) {
                if (range.contains(word.points())) {
                    buckets.get(i).add(word);
                }
            }
        }

        printBuckets(buckets);
        return buckets;
    }

    private static void printBuckets(List<List<BoardProvider.Word>> buckets) {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println("Bucket " + (i + 1) + ": " + buckets.get(i).stream()
                    .map(BotWordSearcherStrategy::wordAsString)
                    .collect(Collectors.joining(", ")));
        }
    }

    private static String wordAsString(BoardProvider.Word word) {
        return WORD_LOG_FORMAT.formatted(ofNullable(word.elements()).orElse(emptyList()).stream()
                .map(BoardProvider.Element::letter)
                .map(String::valueOf)
                .collect(Collectors.joining()), word.points());
    }
}
