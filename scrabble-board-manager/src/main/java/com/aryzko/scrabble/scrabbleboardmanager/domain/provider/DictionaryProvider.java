package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public interface DictionaryProvider {

    Map<String, Boolean> lookupEntries(List<String> values);

    List<Character> fillGap(String pattern);

    Solution resolve(PreparedLine preparedLine, List<Character> availableLetters);
}
