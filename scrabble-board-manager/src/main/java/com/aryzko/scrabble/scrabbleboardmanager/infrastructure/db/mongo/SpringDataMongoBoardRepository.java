package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.db.mongo;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataMongoBoardRepository extends MongoRepository<Board, UUID> {
}
