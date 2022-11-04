package com.aryzko.scrabble.scrabbletilemanager.application.mapper;

import com.aryzko.scrabble.scrabbletilemanager.application.response.TileResponse;
import com.aryzko.scrabble.scrabbletilemanager.domain.Tile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TileMapper {
    TileResponse tileToTileResponse(Tile tile);
}
