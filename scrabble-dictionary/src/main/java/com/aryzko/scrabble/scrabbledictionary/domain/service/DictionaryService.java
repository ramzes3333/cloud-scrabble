package com.aryzko.scrabble.scrabbledictionary.domain.service;

import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;
import com.aryzko.scrabble.scrabbledictionary.domain.ports.DictionaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    @Transactional(readOnly = true)
    public Dictionary getDefault() {
        return dictionaryRepository.getDefault();
    }

    @Transactional(readOnly = true)
    public boolean lookup(String entry) {
        return dictionaryRepository.lookupInDefaultDictionary(entry);
    }

    @Transactional(readOnly = true)
    public boolean lookup(String language, String entry) {
        return dictionaryRepository.lookup(language, entry);
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> lookup(Set<String> entries) {
        List<String> entriesInDictionary = dictionaryRepository.lookupInDefaultDictionary(entries);

        return entries.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> entriesInDictionary.contains(s.toUpperCase()),
                        (v1, v2) -> v1 || v2
                ));
    }
}
