package com.aryzko.scrabble.scrabbledictionary.domain.ports;

import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;

import java.util.List;
import java.util.Map;

public interface DictionaryRepository {
    Dictionary getDefault();

    boolean lookupInDefaultDictionary(String value);

    List<String> lookupInDefaultDictionary(List<String> values);

    boolean lookup(String language, String value);
}
