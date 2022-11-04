package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Bonus;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Field;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DefaultBoardCreator implements BoardCreator {

    public static final int BOARD_HORIZONTAL_SIZE = 15;
    public static final int BOARD_HORIZONTAL_HALF = BOARD_HORIZONTAL_SIZE/2;
    public static final int BOARD_VERTICAL_SIZE = 15;
    public static final int BOARD_VERTICAL_HALF = BOARD_VERTICAL_SIZE/2;

    public Board prepareEmptyBoard() {
        Board board = new Board();
        board.setId(generateUUID());
        board.setRacks(new ArrayList<>());
        board.setFields(
                IntStream.range(0, BOARD_HORIZONTAL_SIZE)
                        .mapToObj(x -> IntStream.range(0, BOARD_VERTICAL_SIZE)
                                .mapToObj(y -> Field.builder()
                                        .x(getValueMinusHalfDimension(x, BOARD_HORIZONTAL_HALF))
                                        .y(getValueMinusHalfDimension(y, BOARD_VERTICAL_HALF))
                                        .bonus(BonusPosition.getByPosition(
                                                getValueMinusHalfDimension(x, BOARD_HORIZONTAL_HALF),
                                                getValueMinusHalfDimension(y, BOARD_VERTICAL_HALF)))
                                        .build())
                                .collect(Collectors.toList()))
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        );
        return board;
    }

    private static int getValueMinusHalfDimension(int value, int boardHalfDimension) {
        return value - boardHalfDimension;
    }

    private static UUID generateUUID() {
        return UUID.randomUUID();
    }
}

class BonusPosition {
    private static Map<Position, Bonus> map;

    static {
        map = new HashMap<>();
        map.put(Position.builder().x(-7).y(7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(-7).y(0).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(-7).y(-7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(0).y(7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(0).y(-7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(7).y(7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(7).y(0).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(7).y(-7).build(), Bonus.TripleWordScore);

        map.put(Position.builder().x(-7).y(4).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-7).y(-4).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-5).y(1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-5).y(-1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-4).y(7).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-4).y(0).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-4).y(-7).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-1).y(5).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-1).y(1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-1).y(-1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(-1).y(-5).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(0).y(4).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(0).y(-4).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(7).y(4).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(7).y(-4).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(5).y(1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(5).y(-1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(4).y(7).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(4).y(0).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(4).y(-7).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(1).y(5).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(1).y(1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(1).y(-1).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(1).y(-5).build(), Bonus.DoubleLetterScore);

        map.put(Position.builder().x(-6).y(2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(-6).y(-2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(-2).y(6).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(-2).y(2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(-2).y(-2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(-2).y(-6).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(6).y(2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(6).y(-2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(2).y(6).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(2).y(2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(2).y(-2).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(2).y(-6).build(), Bonus.TripleLetterScore);

        map.put(Position.builder().x(-6).y(6).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-5).y(5).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-4).y(4).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-3).y(3).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-6).y(-6).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-5).y(-5).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-4).y(-4).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(-3).y(-3).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(6).y(6).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(5).y(5).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(4).y(4).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(3).y(3).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(6).y(-6).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(5).y(-5).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(4).y(-4).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(3).y(-3).build(), Bonus.DoubleWordScore);
    }

    public static Bonus getByPosition(Integer x, Integer y) {
        Position position = Position.builder().x(x).y(y).build();
        return map.get(position);
    }
}

@Value
@Builder
class Position {
    private Integer x;
    private Integer y;
}