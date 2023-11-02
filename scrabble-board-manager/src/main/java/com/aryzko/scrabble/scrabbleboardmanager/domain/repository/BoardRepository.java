package com.aryzko.scrabble.scrabbleboardmanager.domain.repository;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository {
    Board create(Board board);
    Board update(Board board);
    Optional<Board> get(UUID uuid);
    Collection<Board> findAll();
}
