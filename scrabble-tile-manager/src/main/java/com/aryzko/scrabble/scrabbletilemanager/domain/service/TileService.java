package com.aryzko.scrabble.scrabbletilemanager.domain.service;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import com.aryzko.scrabble.scrabbletilemanager.domain.Tile;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileConfiguration;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.BoardTilesRepository;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.TileSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TileService {

    private final BoardTilesRepository boardTilesRepository;
    private final TileSetRepository tileSetRepository;
    private final RandomTileService randomTileService;

    public List<Tile> get(UUID boardId, Integer numberOfItems) {
        BoardTiles boardTiles = boardTilesRepository.get(boardId).orElseGet(() -> createBoardTiles(boardId));
        List<Tile> randomTiles = randomTileService.getRandomTiles(boardTiles, numberOfItems);
        boardTiles.getTiles().addAll(randomTiles);
        boardTilesRepository.update(boardTiles);
        return randomTiles;
    }

    public List<Character> getCharset(UUID boardId, Boolean withoutBlank) {
        BoardTiles boardTiles = boardTilesRepository.get(boardId).orElseGet(() -> createBoardTiles(boardId));
        boardTilesRepository.update(boardTiles);
        return boardTiles.getTileSet().getTileConfigurations().stream()
                .map(TileConfiguration::getTile)
                .map(Tile::getLetter)
                .filter(ch -> withoutBlank ? !ch.equals(' ') : true)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private BoardTiles createBoardTiles(UUID boardId) {
        TileSet defaultTileSet = tileSetRepository.getDefault();
        BoardTiles boardTiles = initialBoardTiles(boardId, defaultTileSet);
        return boardTilesRepository.create(boardTiles);
    }

    private static BoardTiles initialBoardTiles(UUID boardId, TileSet defaultTileSet) {
        BoardTiles boardTiles = new BoardTiles();
        boardTiles.setBoardId(boardId);
        boardTiles.setTileSet(defaultTileSet);
        boardTiles.setTiles(new ArrayList<>());
        return boardTiles;
    }
}
