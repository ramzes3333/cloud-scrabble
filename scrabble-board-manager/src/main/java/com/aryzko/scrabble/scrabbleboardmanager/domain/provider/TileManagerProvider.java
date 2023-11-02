package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.TileConfiguration;

import java.util.List;

public interface TileManagerProvider {

    List<Character> getCharset(String uuid);
    List<Tile> getTiles(String uuid, Integer numberOfItems);

    TileConfiguration getTileConfiguration(String uuid);
}
