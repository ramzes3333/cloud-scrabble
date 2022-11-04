package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCreator boardCreator;

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
        return boardRepository.update(board);
    }
}
