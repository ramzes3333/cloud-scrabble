package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import java.util.List;
import java.util.Map;

public interface DictionaryProvider {

    Map<String, Boolean> lookupValues(List<String> values);
}
