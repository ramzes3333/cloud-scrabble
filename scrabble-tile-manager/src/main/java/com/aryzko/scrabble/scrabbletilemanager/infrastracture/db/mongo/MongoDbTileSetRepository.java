package com.aryzko.scrabble.scrabbletilemanager.infrastracture.db.mongo;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.BoardTilesRepository;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.TileSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoDbTileSetRepository implements TileSetRepository {
    private final SpringDataMongoTileSetRepository tileSetRepository;

    @Override
    public TileSet getDefault() {
        return tileSetRepository.getDefault();
    }
}
