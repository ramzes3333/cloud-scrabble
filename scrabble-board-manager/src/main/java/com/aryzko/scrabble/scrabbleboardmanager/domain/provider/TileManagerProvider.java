package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import com.aryzko.scrabble.scrabbleboardmanager.domain.TileConfiguration;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.external.TileManagerClient;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface TileManagerProvider {

    List<Character> getCharset(String uuid);

    TileConfiguration getTileConfiguration(String uuid);
}
