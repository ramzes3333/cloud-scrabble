package com.aryzko.scrabble.scrabbletilemanager.infrastructure.db.mongo;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataMongoBoardTilesRepository extends MongoRepository<BoardTiles, ObjectId> {

    @Query("{boardId:?0}")
    Optional<BoardTiles> findByBoardId(UUID id);
}
