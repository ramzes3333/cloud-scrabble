package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.db.mongo;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoDbBoardRepository implements BoardRepository {
    private final SpringDataMongoBoardRepository boardRepository;

    @Override
    public Board create(final Board board) {
        return boardRepository.save(board);
    }

    @Override
    @CacheEvict(value = "boards", key = "#board.id")
    public Board update(final Board board) {
        return boardRepository.save(board);
    }

    @Override
    @Cacheable(value = "boards", key = "#uuid", unless = "#result == null", cacheManager = "caffeineCacheManager")
    public Optional<Board> get(final UUID uuid) {
        log.info("Fetching board from repository with uuid: {}", uuid);
        return boardRepository.findById(uuid);
    }

    @Override
    public Collection<Board> findAll() {
        return boardRepository.findAll();
    }
}
