package com.aryzko.scrabble.scrabbleboardmanager.infrastracture.db.mongo;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoDbBoardRepository implements BoardRepository {
    private final SpringDataMongoBoardRepository boardRepository;

    @Override
    public Board create(final Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board update(final Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Optional<Board> get(final UUID uuid) {
        return boardRepository.findById(uuid);
    }

    @Override
    public Collection<Board> findAll() {
        return boardRepository.findAll();
    }
}
