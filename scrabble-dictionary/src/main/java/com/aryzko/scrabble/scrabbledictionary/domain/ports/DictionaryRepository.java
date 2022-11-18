package com.aryzko.scrabble.scrabbledictionary.domain.ports;

import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;
import com.aryzko.scrabble.scrabbledictionary.domain.model.DictionaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface DictionaryRepository {
    Dictionary getDefault();

    boolean lookupInDefaultDictionary(String entry);

    List<String> lookupInDefaultDictionary(Set<String> entries);

    boolean lookup(String language, String entry);

    Stream<DictionaryEntry> findAllInDefaultDictionary();

    long count();
}
