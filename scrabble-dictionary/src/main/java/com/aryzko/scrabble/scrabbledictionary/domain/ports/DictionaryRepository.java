package com.aryzko.scrabble.scrabbledictionary.domain.ports;

import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;

public interface DictionaryRepository {
    Dictionary getDefault();

    boolean lookupInDefaultDictionary(String value);

    boolean lookup(String language, String value);
}
