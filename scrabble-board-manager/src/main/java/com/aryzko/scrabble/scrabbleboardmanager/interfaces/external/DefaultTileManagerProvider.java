package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.TileConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultTileManagerProvider implements TileManagerProvider {

    private final TileManagerClient tileManagerClient;
    private final TileMapper tileMapper;

    @Override
    public List<Character> getCharset(String uuid) {
        return tileManagerClient.getCharset(uuid);
    }

    @Override
    public List<Tile> getTiles(String uuid, Integer numberOfItems) {
        return tileMapper.convert(tileManagerClient.getTiles(uuid, numberOfItems));
    }

    @Override
    public TileConfiguration getTileConfiguration(String uuid) {
        return tileMapper.convert(tileManagerClient.getTileConfiguration(uuid));
    }
}
