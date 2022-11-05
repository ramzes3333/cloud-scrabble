package com.aryzko.scrabble.scrabbledictionary.adapters.db;

import com.aryzko.scrabble.scrabbledictionary.adapters.api.mapper.DictionaryMapper;
import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;
import com.aryzko.scrabble.scrabbledictionary.domain.ports.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbDictionaryRepository implements DictionaryRepository {

    private final SpringDataDictionaryRepository dictionaryRepository;
    private final DictionaryMapper dictionaryMapper;

    @Override
    public Dictionary getDefault() {
        return dictionaryMapper.dictionaryDbToDictionary(
                dictionaryRepository.findDefaultDictionary()
                        .orElseThrow(() -> new IllegalStateException("There is no default dictionary")));
    }

    @Override
    public boolean lookupInDefaultDictionary(String value) {
        return dictionaryRepository.findInDefaultDictionary(value);
    }

    @Override
    public boolean lookup(String language, String value) {
        return dictionaryRepository.findInDictionary(language, value);
    }
}
