package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Field;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.TransposeType;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WordFinder {

    private final Collator collator;

    public WordFinder() {
        collator = Collator.getInstance(new Locale("pl", "PL"));
    }

    public Word findWord(Board board, Tiles tiles) {
        Set<Word> foundWords = findWordHorizontally(board, tiles);
        foundWords.addAll(findWordVertically(board, tiles));

        return chooseWord(foundWords, tiles);
    }

    private Set<Word> findWordVertically(Board board, Tiles tiles) {
        return findWordHorizontally(board.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT),
                tiles.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT)).stream()
                .map(w -> w.transpose(TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_RIGHT))
                .collect(Collectors.toSet());
    }

    private Set<Word> findWordHorizontally(Board board, Tiles tiles) {
        final Map<Position, Field> fieldMap = board.geFieldMap();

        Set<Word> foundWords = new HashSet<>();

        for (Tile tile : tiles.getTiles()) {
            Position tilePosition = Position.builder().x(tile.getX()).y(tile.getY()).build();
            List<Word.Element> elements = new ArrayList<>();

            elements.add(Word.Element.builder()
                    .x(tile.getX())
                    .y(tile.getY())
                    .letter(tile.getLetter())
                    .blank(tile.isBlank())
                    .onBoard(false)
                    .build());

            extendWord(fieldMap, tiles, tilePosition, elements, -1);
            extendWord(fieldMap, tiles, tilePosition, elements, 1);

            if(elements.size() > 1) {
                foundWords.add(Word.builder().elements(elements).build());
            }
        }
        return foundWords;
    }

    private Word chooseWord(Set<Word> foundWords, Tiles tiles) {
        log.info("Found words: " + foundWords.stream().map(Word::getWordAsString).collect(Collectors.joining(", ")));
        return foundWords.stream()
                .filter(word -> matchesAllTiles(word, tiles))
                .peek(word -> log.info("Matched word: " + word.getWordAsString()))
                .min(Comparator.comparing(Word::getWordAsString, collator))
                .orElseThrow(() -> new IllegalStateException("No matching word found"));
    }

    private boolean matchesAllTiles(Word word, Tiles tiles) {
        List<Word.Element> elements = word.getElements();

        return tiles.getTiles().stream()
                .allMatch(tile -> elements.stream()
                        .anyMatch(element -> isMatchingElement(element, tile)));
    }

    private boolean isMatchingElement(Word.Element element, Tile tile) {
        return element.getX() == tile.getX() && element.getY() == tile.getY()
                && Character.toLowerCase(element.getLetter()) == Character.toLowerCase(tile.getLetter())
                && element.isBlank() == tile.isBlank();
    }

    private void extendWord(Map<Position, Field> fieldMap, Tiles tiles, Position start, List<Word.Element> elements, int direction) {
        int x = start.getX();
        int y = start.getY();

        while (true) {

            if (direction < 0) {
                x--;
            } else {
                x++;
            }

            Optional<TileWithSource> tileWithSource = getTileFromAnySource(fieldMap, tiles, x, y);

            if (tileWithSource.isPresent()) {
                Word.Element element = Word.Element.builder()
                        .x(tileWithSource.get().getX())
                        .y(tileWithSource.get().getY())
                        .letter(tileWithSource.get().getLetter())
                        .blank(tileWithSource.get().isBlank())
                        .onBoard(tileWithSource.get().isOnBoard())
                        .build();

                if (direction < 0) elements.add(0, element);
                else elements.add(element);
            } else {
                break;
            }
        }
    }

    private Optional<TileWithSource> getTileFromAnySource(Map<Position, Field> fieldMap, Tiles tiles, int x, int y) {
        Position position = Position.builder().x(x).y(y).build();

        Field field = fieldMap.get(position);
        if(field != null && field.getLetter() != null) {
            return Optional.of(new TileWithSource(x, y,
                    field.getLetter().getLetter(),
                    field.getLetter().isBlank(), TileWithSource.Source.BOARD));
        } else {
            Tile tile = tiles.getTile(x, y);
            if(tile != null) {
                return Optional.of(new TileWithSource(x, y,
                        tile.getLetter(),
                        tile.isBlank(), TileWithSource.Source.USER_TILE));
            }
        }
        return Optional.empty();
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TileWithSource extends Tile {

        private Source source;

        public TileWithSource(int x, int y, char letter, boolean blank, Source source) {
            super(x, y, letter, blank);
            this.source = source;
        }

        public boolean isOnBoard() {
            return Source.BOARD.equals(this.source);
        }

        public enum Source {
            BOARD,
            USER_TILE
        }
    }
}
