package com.aryzko.scrabble.scrabbledictionary.domain.ports;

import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;

import java.util.List;
import java.util.Map;

public interface DictionaryRepository {
    Dictionary getDefault();

    boolean lookupInDefaultDictionary(String entry);

    List<String> lookupInDefaultDictionary(List<String> entries);

    boolean lookup(String language, String entry);
}
