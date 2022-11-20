package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.domain.aspect.PerformanceLog;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.DictionaryEntry;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.DawgBuilder;
import com.aryzko.scrabble.scrabbledictionary.domain.model.dawg.Node;
import com.aryzko.scrabble.scrabbledictionary.domain.ports.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
@NonFinal
@Service
public class DawgService implements ApplicationListener<ApplicationReadyEvent> {

    private static final Character WILD_CARD = '*';
    private static final int MINIMUM_PATTERN_LENGTH = 2;

    private Node root;
    private final DictionaryRepository dictionaryRepository;

    @Transactional
    @PerformanceLog
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        DawgBuilder builder = new DawgBuilder();
        try(Stream<DictionaryEntry> dictionaryEntryStream = dictionaryRepository.findAllInDefaultDictionary()) {
            dictionaryEntryStream.forEach(dictionaryEntry -> builder.insert(dictionaryEntry.getEntry()));
        }
        root = builder.build();
    }

    private boolean isDawgReady() {
        return root != null;
    }

    private Node getDawg() throws DawgIsNotReady {
        return Optional.ofNullable(root).orElseThrow(() -> new DawgIsNotReady());
    }

    public boolean lookup(String word) throws DawgIsNotReady {
        return lookup(getDawg(), word);
    }

    private boolean lookup(Node node, String word) throws DawgIsNotReady {
        for (Character character : word.toCharArray()){
            node = node.getTransitions().get(character);
            if (node == null) {
                return false;
            }
        }
        return node.isTerminal();
    }

    public List<Character> fillGapInPattern(String pattern) throws DawgIsNotReady {
        if (!isPatternCorrect(pattern)) {
            throw new IllegalArgumentException(
                    format("Pattern should contain exactly one occurrence of %c sign and length greater than 1", WILD_CARD));
        }
        List<Character> matchedCharacters = new ArrayList<>();
        Node node = getDawg();
        for (int i = 0; i < pattern.length(); i++) {
            Character character = pattern.charAt(i);
            if (character == WILD_CARD) {
                for (Map.Entry<Character, Node> transition : node.getTransitions().entrySet()) {
                    if (lookup(transition.getValue(), pattern.substring(i+1))) {
                        matchedCharacters.add(transition.getKey());
                    }
                }
            } else {
                node = node.getTransitions().get(character);
                if (node == null) {
                    break;
                }
            }
        }
        return matchedCharacters;
    }

    private static boolean isPatternCorrect(String pattern) {
        return pattern.length() >= MINIMUM_PATTERN_LENGTH && pattern.chars()
                .map(v -> (char) v)
                .filter(character -> character == WILD_CARD)
                .count() == 1;
    }
}