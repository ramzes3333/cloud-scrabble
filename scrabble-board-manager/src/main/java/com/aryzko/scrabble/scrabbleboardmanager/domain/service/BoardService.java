package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Letter;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Rack;
import com.aryzko.scrabble.scrabbleboardmanager.domain.command.CreateBoardCommand;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.preview.BoardPreview;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.TileManagerProvider;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.repository.BoardRepository;
import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidationResult;
import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final TileManagerProvider tileManagerProvider;
    private final BoardRepository boardRepository;
    private final BoardCreator boardCreator;
    private final BoardValidator boardValidator;

    public BoardPreview prepareEmptyBoardPreview() {
        return boardCreator.prepareEmptyBoardPreview();
    }

    public Board createEmptyBoard(CreateBoardCommand command) {
        Board board = boardCreator.prepareEmptyBoard();

        int rackSize = board.getBoardParameters().getRackSize();
        int playersNumber = command.getPlayerIds().size();

        List<Tile> initialTilesForRacks = getInitialTilesForRacks(board.getId().toString(), rackSize, playersNumber);

        if (initialTilesForRacks.size() < playersNumber * rackSize) {
            throw new IllegalStateException("There are not enough tiles for all players.");
        }

        Iterator<Tile> tileIterator = initialTilesForRacks.iterator();

        board.setRacks(command.getPlayerIds().stream().map(playerId -> {
            List<Letter> letters = IntStream.range(0, rackSize)
                    .mapToObj(i -> tileIterator.next())
                    .map(tile -> new Letter(
                            tile.getLetter(),
                            tile.getPoints(),
                            tile.getLetter().equals(' ')
                    ))
                    .collect(Collectors.toList());

            return new Rack(playerId, letters);
        }).collect(Collectors.toList()));

        return boardRepository.create(board);
    }

    private List<Tile> getInitialTilesForRacks(String boardId, int rackSize, int playersNumber) {
        return tileManagerProvider.getTiles(boardId, rackSize * playersNumber);
    }

    public Board getBoard(final UUID uuid) {
        log.info("Fetching board with uuid: {}", uuid);
        return boardRepository.get(uuid).orElse(null);
    }

    public Collection<Board> getBoards() {
        return boardRepository.findAll();
    }

    public Board updateBoard(Board board) {
        Board boardFromDb = getBoard(board.getId());
        boardFromDb.setFields(board.getFields());
        boardFromDb.setRacks(board.getRacks());
        return boardRepository.update(boardFromDb);
    }

    public BoardValidationResult validate(Board board) {
        Board boardFromDb = getBoard(board.getId());
        board.setBoardParameters(boardFromDb.getBoardParameters());
        return boardValidator.validate(board);
    }
}
