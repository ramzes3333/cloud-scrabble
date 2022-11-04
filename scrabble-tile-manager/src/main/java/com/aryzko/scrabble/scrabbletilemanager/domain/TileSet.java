package com.aryzko.scrabble.scrabbletilemanager.domain;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Builder
public class TileSet {
    @Id
    private ObjectId id;

    private String name;
    private Integer version;
    private boolean defaultSet;
    private List<TileConfiguration> tileConfigurations;
}