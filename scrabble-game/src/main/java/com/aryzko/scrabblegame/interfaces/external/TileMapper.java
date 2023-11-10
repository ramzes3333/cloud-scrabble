package com.aryzko.scrabblegame.interfaces.external;

import com.aryzko.scrabblegame.application.provider.model.Tile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TileMapper {

    List<Tile> convert(List<TileManagerClient.TileResponse> responses);
    Tile convert(TileManagerClient.TileResponse response);
}
