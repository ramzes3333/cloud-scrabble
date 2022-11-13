package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.BoardParameters;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BoardOrphanSearcher {

    public List<CharacterWithPosition> searchOrphans(final Board board) {
        if(!hasOddDimensions(board.getBoardParameters())) {
            throw new IllegalStateException("Board size is not odd");
        }
        Map<Position, Optional<Character>> fieldMap = board.getCharacterMap();
        Map<Position, Boolean> correctCharactersMap = new HashMap<>();

        Integer x = board.getBoardParameters().getHorizontalSize() / 2;
        Integer y = board.getBoardParameters().getVerticalSize() / 2;

        check(x, y, board.getBoardParameters(), fieldMap, correctCharactersMap);

        return fieldMap.entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .filter(e -> !correctCharactersMap.containsKey(e.getKey()))
                .map(e -> CharacterWithPosition.builder().x(e.getKey().getX()).y(e.getKey().getY()).character(e.getValue()).build())
                .collect(Collectors.toList());
    }

    private void check(Integer x, Integer y, BoardParameters boardParameters,
                       Map<Position, Optional<Character>> fieldMap, Map<Position, Boolean> correctCharactersMap) {

        Position position = Position.builder().x(x).y(y).build();
        if(fieldMap.get(position).isPresent() && !correctCharactersMap.containsKey(position)) {
            correctCharactersMap.put(position, Boolean.TRUE);
            if(x > 0) {
                check(x-1, y, boardParameters, fieldMap, correctCharactersMap);
            }
            if(y > 0) {
                check(x, y-1, boardParameters, fieldMap, correctCharactersMap);
            }
            if(x < boardParameters.getHorizontalSize()) {
                check(x+1, y, boardParameters, fieldMap, correctCharactersMap);
            }
            if(y < boardParameters.getVerticalSize()) {
                check(x, y+1, boardParameters, fieldMap, correctCharactersMap);
            }
        }
    }

    private boolean hasOddDimensions(BoardParameters boardParameters) {
        return boardParameters.getHorizontalSize() % 2 != 0 &&
                boardParameters.getVerticalSize() % 2 != 0;
    }
}
