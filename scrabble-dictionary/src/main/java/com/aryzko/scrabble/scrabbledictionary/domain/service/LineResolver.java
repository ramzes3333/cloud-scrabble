package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.Node;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.AvailableLetters;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineResolver {

    private final DawgService dawgService;

    public Solution resolve(final Line line, final AvailableLetters availableLetters) throws DawgIsNotReady {
        Solution.SolutionBuilder solutionBuilder = Solution.builder();
        Node root = dawgService.getDawg();

        return solutionBuilder.words(
                        line.getFields().stream()
                                .filter(Line.LineField::isAnchor)
                                .mapMulti(resolve(root, DynamicAvailableLetters.of(availableLetters)))
                                .collect(Collectors.toList()))
                .build();
    }

    private static BiConsumer<Line.LineField, Consumer<Solution.Word>> resolve(Node root, DynamicAvailableLetters letters) {
        return (anchor, wordConsumer) -> leftPart(LeftPart.builder(), root, anchor, anchor.getLeftLimit(), letters, wordConsumer);
    }

    private static void leftPart(LeftPart.LeftPartBuilder leftPartBuilder, Node root, Line.LineField field, Integer leftLimit,
                                 DynamicAvailableLetters letters, Consumer<Solution.Word> wordConsumer) {
        if (leftLimit > 0) {
            for (Map.Entry<Character, Node> entry : root.getTransitions().entrySet()) {
                if (letters.isAvailable(entry.getKey())) {
                    boolean blankUsed = letters.useLetter(entry.getKey());
                    leftPart(LeftPart.builder()
                                    .letters(leftPartBuilder.build().getLetters())
                                    .letter(new LeftPart.Letter(entry.getKey(), blankUsed)),
                            entry.getValue(), field, leftLimit - 1, letters, wordConsumer);
                    letters.returnLetter(entry.getKey());
                }
            }
        }
        if(field.getPrev() != null && field.getPrev().isLetter()) {
            Solution.Word.WordBuilder wordBuilder = buildLeftPrefix(field.getPrev());
            Node node = getNode(root, wordBuilder.build().getWordAsString());
            if(node != null) {
                extendRight(wordBuilder, getNode(root, wordBuilder.build().getWordAsString()), field, letters, wordConsumer, false);
            }
        } else {
            extendRight(initWordBuilder(leftPartBuilder, field), root, field, letters, wordConsumer, false);
        }
    }

    private static void extendRight(Solution.Word.WordBuilder wordBuilder, Node node, Line.LineField field,
                                    DynamicAvailableLetters letters, Consumer<Solution.Word> wordConsumer,
                                    boolean extendedRight) {
        if(field != null && field.getLetter() != null) {
            for(Map.Entry<Character, Node> entry : node.getTransitions().entrySet()) {
                if(field.getLetter().equals(entry.getKey())) {
                    Solution.Word.WordBuilder wordBuilderCopy = Solution.Word.builder()
                            .elements(wordBuilder.build().getElements());
                    wordBuilderCopy.element(prepareElement(field, entry.getKey(), false, true));
                    extendRight(wordBuilderCopy, entry.getValue(), field.getNext(), letters, wordConsumer, extendedRight);
                }
            }
        } else if(field != null) {
            List<Character> intersection = intersection(letters, node.getTransitions(), field);
            for(Character character : intersection) {
                boolean blankUsed = letters.useLetter(character);
                Solution.Word.WordBuilder wordBuilderCopy = Solution.Word.builder()
                        .elements(wordBuilder.build().getElements());
                wordBuilderCopy.element(prepareElement(field, character, blankUsed, false));
                extendRight(wordBuilderCopy,
                        node.getTransitions().get(character),
                        field.getNext(),
                        letters,
                        wordConsumer,
                        true);
                letters.returnLetter(character);
            }
        }
        if(extendedRight
                && !isFieldHasLetter(field)
                && isWordHasLengthGreaterThan1(wordBuilder)
                && !isWordWithLettersOnlyFromBoard(wordBuilder)
                && node.isTerminal()) {
            wordConsumer.accept(wordBuilder.build());
        }
    }

    private static boolean isFieldHasLetter(Line.LineField field) {
        return field != null && field.isLetter();
    }

    private static Solution.Word.WordBuilder initWordBuilder(LeftPart.LeftPartBuilder leftPartBuilder, Line.LineField field) {
        LeftPart leftPart = leftPartBuilder.build();
        if(leftPart.getLetters().size() == 0) {
            return Solution.Word.builder();
        } else {
            Solution.Word.WordBuilder wordBuilder = Solution.Word.builder();
            for(int x=0; x < leftPart.getLetters().size(); x++) {
                wordBuilder.element(
                        prepareElement(
                                field.getX()-leftPart.getLetters().size()+x,
                                field.getY(),
                                leftPart.getLetters().get(x).letter(),
                                leftPart.getLetters().get(x).blank(),
                                false));
            }
            return wordBuilder;
        }
    }

    private static boolean isWordWithLettersOnlyFromBoard(Solution.Word.WordBuilder wordBuilder) {
        return wordBuilder.build().getElements().stream()
                .allMatch(Solution.Word.Element::isUnmodifiableLetter);
    }

    private static boolean isWordHasLengthGreaterThan1(Solution.Word.WordBuilder wordBuilder) {
        return wordBuilder.build().getElements().size() > 1;
    }

    private static boolean isNextFieldHasLetter(Line.LineField field) {
        return ofNullable(field)
                .map(Line.LineField::getNext)
                .map(Line.LineField::isLetter)
                .orElse(false);
    }

    private static Node getNode(Node node, String wordAsString) {
        for(char character : wordAsString.toCharArray()) {
            node = node.getTransitions().get(character);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    private static Solution.Word.WordBuilder buildLeftPrefix(Line.LineField field) {
        Solution.Word.WordBuilder wordBuilder = Solution.Word.builder();
        while (field != null && field.getPrev().isLetter()) {
            field = field.getPrev();
        }

        do {
            wordBuilder.element(prepareElement(field, field.getLetter(), false, true));
            field = field.getNext();
        } while (field != null && field.isLetter());
        return wordBuilder;
    }

    private static Solution.Word.Element prepareElement(Line.LineField field, Character character, boolean blank, boolean unmodifiableLetter) {
        return prepareElement(field.getX(), field.getY(), character, blank, unmodifiableLetter);
    }

    private static Solution.Word.Element prepareElement(Integer x, Integer y, Character character, boolean blank, boolean unmodifiableLetter) {
        return Solution.Word.Element.builder()
                .x(x)
                .y(y)
                .letter(character)
                .blank(blank)
                .unmodifiableLetter(unmodifiableLetter)
                .build();
    }

    private static List<Character> intersection(final DynamicAvailableLetters letters,
                                                final SortedMap<Character, Node> transitions,
                                                final Line.LineField field) {
        return getBase(letters, transitions)
                .filter(character -> field.isAnyLetter() || field.getAvailableLetters().contains(character))
                .map(Character::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }

    private static Stream<Character> getBase(final DynamicAvailableLetters letters,
                                             final SortedMap<Character, Node> transitions) {
        if(letters.isBlankAvailable()) {
            return transitions.keySet().stream();
        } else {
            return letters.getLetters().stream()
                    .filter(DynamicAvailableLetters.CharacterWithAvailability::isAvailable)
                    .distinct()
                    .map(DynamicAvailableLetters.CharacterWithAvailability::getLetter)
                    .filter(transitions::containsKey);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DynamicAvailableLetters {
        private List<CharacterWithAvailability> letters;

        public static DynamicAvailableLetters of(AvailableLetters availableLetters) {
            List<CharacterWithAvailability> letters = availableLetters.getCharacters().stream()
                    .map(l -> new CharacterWithAvailability(Character.toLowerCase(l), l.equals(' '), true))
                    .collect(Collectors.toList());

            return new DynamicAvailableLetters(letters);
        }

        public boolean isBlankAvailable() {
            return letters.stream()
                    .anyMatch(ch -> ch.isBlank() && ch.isAvailable());
        }

        public boolean isAvailable(Character character) {
            return letters.stream()
                    .anyMatch(ch -> (ch.getLetter().equals(character) || ch.isBlank) && ch.isAvailable());
        }

        /**
         * Use letter from available letters
         * @param character letter to use
         * @return true if blank is used, false otherwise
         */
        public boolean useLetter(Character character) {
            CharacterWithAvailability foundLetter = letters.stream()
                    .filter(ch -> (ch.getLetter().equals(character) || ch.isBlank) && ch.isAvailable())
                    .sorted(Comparator.comparing(CharacterWithAvailability::isBlank))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("There is no available letter"));

            foundLetter.isAvailable = false;
            if(foundLetter.isBlank()) {
                foundLetter.letter = character;
            }

            return foundLetter.isBlank;
        }

        public void returnLetter(Character character) {
            CharacterWithAvailability foundLetter = letters.stream()
                    .filter(ch -> ch.getLetter().equals(character) && !ch.isAvailable())
                    .sorted(Comparator.comparing(CharacterWithAvailability::isBlank).reversed())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Letter is not used"));

            foundLetter.isAvailable = true;
            if(foundLetter.isBlank()) {
                foundLetter.letter = ' ';
            }
        }

        @Getter
        @ToString
        @AllArgsConstructor
        public static class CharacterWithAvailability {
            private Character letter;
            private boolean isBlank;
            private boolean isAvailable;
        }
    }

    @Getter
    @Builder
    public static class LeftPart {
        @Singular
        private List<Letter> letters;

        public record Letter(Character letter, boolean blank) { }
    }
}
