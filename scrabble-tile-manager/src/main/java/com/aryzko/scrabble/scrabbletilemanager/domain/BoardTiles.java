package com.aryzko.scrabble.scrabbletilemanager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.UUID;

import static com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles.BOARD_TILES_COLLECTION_NAME;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = BOARD_TILES_COLLECTION_NAME)
public class BoardTiles {

    public static final String BOARD_TILES_COLLECTION_NAME = "boardTilesCollection";

    @Id
    private ObjectId id;

    private UUID boardId;
    @DocumentReference(lazy=true)
    private TileSet tileSet;
    private List<Tile> tiles;
}
