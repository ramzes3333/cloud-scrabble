package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultTileManagerProvider implements TileManagerProvider {

    private final TileManagerClient tileManagerClient;

    @Override
    public List<Character> getCharset(String uuid) {
        return tileManagerClient.getCharset(uuid);
    }
}
