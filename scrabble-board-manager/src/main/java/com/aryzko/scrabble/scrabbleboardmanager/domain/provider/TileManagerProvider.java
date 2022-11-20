package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import java.util.List;
import java.util.Map;

public interface TileManagerProvider {

    List<Character> getCharset(String uuid);
}
