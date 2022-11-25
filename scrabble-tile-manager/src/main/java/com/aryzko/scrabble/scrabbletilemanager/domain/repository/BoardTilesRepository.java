package com.aryzko.scrabble.scrabbletilemanager.domain.repository;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;

import java.util.Optional;
import java.util.UUID;

public interface BoardTilesRepository {
    Optional<BoardTiles> get(UUID boardId);
    Optional<TileSet> getBoardTileSet(UUID boardId);
    BoardTiles create(BoardTiles boardTiles);
    BoardTiles update(BoardTiles boardTiles);
}
