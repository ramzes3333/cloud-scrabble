package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultDictionaryProvider implements DictionaryProvider {

    private final DictionaryClient dictionaryClient;
    private final DictionaryMapper dictionaryMapper;

    @Override
    public Map<String, Boolean> lookupEntries(List<String> values) {
        return dictionaryClient.lookupEntries(values);
    }

    @Override
    public List<Character> fillGap(String pattern) {
        return dictionaryClient.fillGap(pattern);
    }

    @Override
    public Solution resolve(PreparedLine preparedLine, List<Character> availableLetters) {
        return dictionaryMapper.convert(
                dictionaryClient.resolve(
                        new DictionaryClient.ResolveRequest(preparedLine, availableLetters)));
    }
}
