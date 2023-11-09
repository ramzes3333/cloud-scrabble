package com.aryzko.scrabble.scrabbletilemanager.domain.service;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import com.aryzko.scrabble.scrabbletilemanager.domain.Tile;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileConfiguration;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.BoardTilesRepository;
import com.aryzko.scrabble.scrabbletilemanager.domain.repository.TileSetRepository;
import com.aryzko.scrabble.scrabbletilemanager.domain.service.exception.NoBoardWithUUIDException;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TileService {

    private final BoardTilesRepository boardTilesRepository;
    private final TileSetRepository tileSetRepository;
    private final RandomTileService randomTileService;
    private final Collator collator;

    public TileService(BoardTilesRepository boardTilesRepository, TileSetRepository tileSetRepository, RandomTileService randomTileService) {
        this.boardTilesRepository = boardTilesRepository;
        this.tileSetRepository = tileSetRepository;
        this.randomTileService = randomTileService;
        this.collator = Collator.getInstance(new Locale("pl", "PL"));
        this.collator.setStrength(Collator.PRIMARY);
    }

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
                .filter(ch -> !withoutBlank || !ch.equals(' '))
                .distinct()
                .map(String::valueOf)
                .sorted(collator)
                .map(str -> str.charAt(0))
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

    public List<TileConfiguration> getTileConfigurations(UUID boardId) {
        return boardTilesRepository.getBoardTileSet(boardId)
                .orElseThrow(() -> new NoBoardWithUUIDException(boardId))
                .getTileConfigurations();
    }
}
