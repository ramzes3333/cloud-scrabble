package com.aryzko.scrabble.scrabbletilemanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardTiles {
    @Id
    private ObjectId id;

    private UUID boardId;
    @DocumentReference(lazy=true)
    private TileSet tileSet;
    private List<Tile> tiles;
}
