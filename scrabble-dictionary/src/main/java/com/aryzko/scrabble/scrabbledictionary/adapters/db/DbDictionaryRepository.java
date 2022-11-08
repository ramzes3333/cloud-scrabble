package com.aryzko.scrabble.scrabbledictionary.adapters.db;

import com.aryzko.scrabble.scrabbledictionary.adapters.api.mapper.DictionaryMapper;
import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryEntryDb;
import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;
import com.aryzko.scrabble.scrabbledictionary.domain.ports.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DbDictionaryRepository implements DictionaryRepository {

    private final SpringDataDictionaryRepository dictionaryRepository;
    private final SpringDataDictionaryEntryRepository dictionaryEntryRepository;
    private final DictionaryMapper dictionaryMapper;

    @Override
    public Dictionary getDefault() {
        return dictionaryMapper.dictionaryDbToDictionary(
                dictionaryRepository.findDefaultDictionary()
                        .orElseThrow(() -> new IllegalStateException("There is no default dictionary")));
    }

    @Override
    public boolean lookupInDefaultDictionary(String entry) {
        return dictionaryEntryRepository.findInDefaultDictionary(entry);
    }

    @Override
    public boolean lookup(String language, String entry) {
        return dictionaryEntryRepository.findInDictionary(language, entry);
    }

    @Override
    public List<String> lookupInDefaultDictionary(List<String> entries) {
        return dictionaryEntryRepository.findInDefaultDictionary(
                        entries.stream()
                                .map(String::toLowerCase)
                                .collect(Collectors.toList())).stream()
                .map(DictionaryEntryDb::getEntry)
                .collect(Collectors.toList());
    }
}
