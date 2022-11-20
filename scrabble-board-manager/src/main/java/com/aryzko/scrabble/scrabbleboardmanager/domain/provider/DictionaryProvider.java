package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public interface DictionaryProvider {

    Map<String, Boolean> lookupEntries(List<String> values);

    List<Character> fillGap(String pattern);
}
