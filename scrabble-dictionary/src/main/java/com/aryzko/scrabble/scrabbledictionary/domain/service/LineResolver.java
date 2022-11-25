package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.Node;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.AvailableLetters;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LineResolver {

    private final DawgService dawgService;

    public Solution resolve(final Line line, final AvailableLetters availableLetters) throws DawgIsNotReady {
        DynamicAvailableLetters letters = DynamicAvailableLetters.of(availableLetters);
        Solution.SolutionBuilder solutionBuilder = Solution.builder();
        Node root = dawgService.getDawg();

        return solutionBuilder.words(
                        line.getFields().stream()
                                .filter(Line.LineField::isAnchor)
                                .mapMulti(resolve(root, letters))
                                .collect(Collectors.toList()))
                .build();
    }

    private static BiConsumer<Line.LineField, Consumer<Solution.Word>> resolve(Node root, DynamicAvailableLetters letters) {
        return (anchor, wordConsumer) -> leftPart(root, anchor, letters, wordConsumer);
    }

    private static void leftPart(Node root, Line.LineField field,
                                 DynamicAvailableLetters letters, Consumer<Solution.Word> wordConsumer) {
        int leftLimit = field.getLeftLimit();
        while(leftLimit > 0) {
            for(Map.Entry<Character, Node> entry : root.getTransitions().entrySet()) {
                if(letters.isAvailable(entry.getKey())) {
                    letters.useLetter(entry.getKey());

                    Solution.Word.WordBuilder wordBuilder = Solution.Word.builder().element(
                            prepareElement(field, entry.getKey(), false));

                    extendRight(wordBuilder, entry.getValue(), field, letters, wordConsumer);
                    letters.returnLetter(entry.getKey());
                }
            }
            leftLimit--;
        }
        if(field.getPrev() != null && field.getPrev().isLetter()) {
            Solution.Word.WordBuilder wordBuilder = buildLeftPrefix(field.getPrev());
            Node node = getNode(root, wordBuilder.build().getWordAsString());
            if(node != null) {
                extendRight(wordBuilder, getNode(root, wordBuilder.build().getWordAsString()), field, letters, wordConsumer);
            }
        } else {
            extendRight(Solution.Word.builder(), root, field, letters, wordConsumer);
        }
    }

    private static void extendRight(Solution.Word.WordBuilder wordBuilder, Node node, Line.LineField field,
                                    DynamicAvailableLetters letters, Consumer<Solution.Word> wordConsumer) {

        if(wordBuilder.build().getElements().size() > 1 && node.isTerminal()) {
            wordConsumer.accept(wordBuilder.build());
        }
        if(field == null) {
            return;
        }
        if(field.getLetter() != null) {
            for(Map.Entry<Character, Node> entry : node.getTransitions().entrySet()) {
                if(field.getLetter().equals(entry.getKey())) {
                    Solution.Word.WordBuilder wordBuilderCopy = Solution.Word.builder()
                            .elements(wordBuilder.build().getElements());
                    wordBuilderCopy.element(prepareElement(field, entry.getKey(), true));
                    extendRight(wordBuilderCopy, entry.getValue(), field.getNext(), letters, wordConsumer);
                }
            }
        } else {
            List<Character> intersection = intersection(letters, node.getTransitions(), field);
            for(Character character : intersection) {
                letters.useLetter(character);
                Solution.Word.WordBuilder wordBuilderCopy = Solution.Word.builder()
                        .elements(wordBuilder.build().getElements());
                wordBuilderCopy.element(prepareElement(field, character, false));
                extendRight(wordBuilderCopy,
                        node.getTransitions().get(character),
                        field.getNext(),
                        letters,
                        wordConsumer);
                letters.returnLetter(character);
            }
        }
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
            wordBuilder.element(prepareElement(field, field.getLetter(), true));
            field = field.getNext();
        } while (field != null && field.isLetter());
        return wordBuilder;
    }

    private static Solution.Word.Element prepareElement(Line.LineField field, Character character, boolean unmodifiableLetter) {
        return Solution.Word.Element.builder()
                .x(field.getX())
                .y(field.getY())
                .letter(character)
                .unmodifiableLetter(unmodifiableLetter)
                .build();
    }

    private static List<Character> intersection(DynamicAvailableLetters letters,
                                                SortedMap<Character, Node> transitions,
                                                Line.LineField field) {
        return letters.getLetters().stream()
                .filter(DynamicAvailableLetters.CharacterWithAvailability::isAvailable)
                .distinct()
                .map(DynamicAvailableLetters.CharacterWithAvailability::getLetter)
                .filter(transitions::containsKey)
                .filter(character -> field.isAnyLetter() || field.getAvailableLetters().contains(character))
                .map(Character::toLowerCase)
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DynamicAvailableLetters {
        private List<CharacterWithAvailability> letters;

        public static DynamicAvailableLetters of(AvailableLetters availableLetters) {
            List<CharacterWithAvailability> letters = availableLetters.getCharacters().stream()
                    .map(l -> new CharacterWithAvailability(Character.toLowerCase(l), true))
                    .collect(Collectors.toList());

            return new DynamicAvailableLetters(letters);
        }

        @Getter
        @ToString
        @AllArgsConstructor
        public static class CharacterWithAvailability {
            private Character letter;
            @Setter
            private boolean isAvailable;
        }

        public boolean isAvailable(Character character) {
            return letters.stream()
                    .anyMatch(ch -> ch.getLetter().equals(character) && ch.isAvailable());
        }

        public void useLetter(Character character) {
            CharacterWithAvailability characterWithAvailability = letters.stream()
                    .filter(ch -> ch.getLetter().equals(character) && ch.isAvailable())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("There is no available letter"));

            characterWithAvailability.setAvailable(false);
        }

        public void returnLetter(Character character) {
            CharacterWithAvailability characterWithAvailability = letters.stream()
                    .filter(ch -> ch.getLetter().equals(character) && !ch.isAvailable())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Letter is not used"));

            characterWithAvailability.setAvailable(true);
        }
    }
}