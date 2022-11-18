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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@NonFinal
@Service
public class DawgService implements ApplicationListener<ApplicationReadyEvent> {

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

    public boolean isDawgReady() {
        return root != null;
    }

    public Node getDawg() throws DawgIsNotReady {
        return Optional.ofNullable(root).orElseThrow(() -> new DawgIsNotReady());
    }

    public boolean lookup(String word) throws DawgIsNotReady {
        Node node = getDawg();
        for (Character character : word.toCharArray()){
            node = node.getTransitions().get(character);
            if (node == null) {
                return false;
            }
        }
        return node.isTerminal();
    }
}