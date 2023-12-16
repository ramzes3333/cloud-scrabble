package com.aryzko.scrabble.scrabbletilemanager.domain;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@Document(collection = TileSet.TILE_SET_COLLECTION_NAME)
public class TileSet {

    public static final String TILE_SET_COLLECTION_NAME = "tileSetCollection";

    @Id
    private ObjectId id;

    private String name;
    private Integer version;
    private Boolean defaultSet;
    private List<TileConfiguration> tileConfigurations;
}