package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.BoardParameters;
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
    public static final int BOARD_VERTICAL_SIZE = 15;
    public static final int BOARD_RACK_SIZE = 7;

    public Board prepareEmptyBoard() {
        Board board = new Board();
        board.setId(generateUUID());
        board.setFields(
                IntStream.range(0, BOARD_VERTICAL_SIZE)
                        .mapToObj(y -> IntStream.range(0, BOARD_HORIZONTAL_SIZE)
                                .mapToObj(x -> Field.builder()
                                        .x(x)
                                        .y(y)
                                        .bonus(BonusPosition.getByPosition(x, y))
                                        .build())
                                .collect(Collectors.toList()))
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        );
        board.setRacks(new ArrayList<>());
        board.setBoardParameters(
                BoardParameters.builder()
                        .horizontalSize(BOARD_HORIZONTAL_SIZE)
                        .verticalSize(BOARD_VERTICAL_SIZE)
                        .rackSize(BOARD_RACK_SIZE)
                        .build());
        return board;
    }

    private static UUID generateUUID() {
        return UUID.randomUUID();
    }
}

class BonusPosition {
    private static Map<Position, Bonus> map;

    static {
        map = new HashMap<>();
        map.put(Position.builder().x(0).y(0).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(0).y(7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(0).y(14).build(), Bonus.TripleWordScore);

        map.put(Position.builder().x(7).y(0).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(7).y(14).build(), Bonus.TripleWordScore);

        map.put(Position.builder().x(14).y(0).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(14).y(7).build(), Bonus.TripleWordScore);
        map.put(Position.builder().x(14).y(14).build(), Bonus.TripleWordScore);


        map.put(Position.builder().x(1).y(1).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(2).y(2).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(3).y(3).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(4).y(4).build(), Bonus.DoubleWordScore);

        map.put(Position.builder().x(1).y(13).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(2).y(12).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(3).y(11).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(4).y(10).build(), Bonus.DoubleWordScore);

        map.put(Position.builder().x(10).y(4).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(11).y(3).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(12).y(2).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(13).y(1).build(), Bonus.DoubleWordScore);

        map.put(Position.builder().x(10).y(10).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(11).y(11).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(12).y(12).build(), Bonus.DoubleWordScore);
        map.put(Position.builder().x(13).y(13).build(), Bonus.DoubleWordScore);


        map.put(Position.builder().x(0).y(3).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(0).y(11).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(2).y(6).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(2).y(8).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(3).y(0).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(3).y(7).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(3).y(14).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(6).y(2).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(6).y(6).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(6).y(8).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(6).y(12).build(), Bonus.DoubleLetterScore);

        map.put(Position.builder().x(7).y(3).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(7).y(11).build(), Bonus.DoubleLetterScore);

        map.put(Position.builder().x(14).y(3).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(14).y(11).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(12).y(6).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(12).y(8).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(11).y(0).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(11).y(7).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(11).y(14).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(8).y(2).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(8).y(6).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(8).y(8).build(), Bonus.DoubleLetterScore);
        map.put(Position.builder().x(8).y(12).build(), Bonus.DoubleLetterScore);


        map.put(Position.builder().x(1).y(5).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(1).y(9).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(5).y(1).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(5).y(5).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(5).y(9).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(5).y(13).build(), Bonus.TripleLetterScore);

        map.put(Position.builder().x(13).y(5).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(13).y(9).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(9).y(1).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(9).y(5).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(9).y(9).build(), Bonus.TripleLetterScore);
        map.put(Position.builder().x(9).y(13).build(), Bonus.TripleLetterScore);
    }

    public static Bonus getByPosition(Integer x, Integer y) {
        Position position = Position.builder().x(x).y(y).build();
        return map.get(position);
    }

    @Value
    @Builder
    private static class Position {
        private Integer x;
        private Integer y;
    }
}