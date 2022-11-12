package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultDictionaryProvider implements DictionaryProvider {

    private final DictionaryClient dictionaryClient;

    @Override
    public Map<String, Boolean> lookupEntries(List<String> values) {
        return dictionaryClient.lookupEntries(values);
    }
}
