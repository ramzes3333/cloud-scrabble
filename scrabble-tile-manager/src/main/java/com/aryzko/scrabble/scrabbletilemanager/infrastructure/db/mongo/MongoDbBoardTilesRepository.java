package com.aryzko.scrabble.scrabbletilemanager.infrastructure.db.mongo;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.BoardTilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoDbBoardTilesRepository implements BoardTilesRepository {
    private final SpringDataMongoBoardTilesRepository boardTilesRepository;

    @Override
    public Optional<BoardTiles> get(UUID boardId) {
        return boardTilesRepository.findByBoardId(boardId);
    }

    @Override
    public Optional<TileSet> getBoardTileSet(UUID boardId) {
        return boardTilesRepository.findByBoardId(boardId)
                .map(BoardTiles::getTileSet);
    }

    @Override
    public BoardTiles create(BoardTiles boardTiles) {
        return boardTilesRepository.insert(boardTiles);
    }

    @Override
    public BoardTiles update(BoardTiles boardTiles) {
        return boardTilesRepository.save(boardTiles);
    }
}
