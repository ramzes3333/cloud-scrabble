package com.aryzko.scrabble.scrabbletilemanager.infrastracture.db.mongo;

import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMongoTileSetRepository extends MongoRepository<TileSet, ObjectId> {

    @Query("{defaultSet:true}")
    TileSet getDefault();
}
