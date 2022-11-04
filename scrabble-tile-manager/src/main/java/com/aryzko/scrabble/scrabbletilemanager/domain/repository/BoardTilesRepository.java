package com.aryzko.scrabble.scrabbletilemanager.domain.repository;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;

import java.util.Optional;
import java.util.UUID;

public interface BoardTilesRepository {
    Optional<BoardTiles> get(UUID boardId);
    BoardTiles create(BoardTiles boardTiles);
    BoardTiles update(BoardTiles boardTiles);
}
