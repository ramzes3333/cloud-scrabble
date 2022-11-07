package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.repository.BoardRepository;
import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidationResult;
import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCreator boardCreator;
    private final BoardValidator boardValidator;

    public Board createEmptyBoard() {
        return boardRepository.create(boardCreator.prepareEmptyBoard());
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
