package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model.TileConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TileMapper {

    TileConfiguration.Tile convert(TileManagerClient.TileConfigurationResponse.Tile tile);

    TileConfiguration convert(TileManagerClient.TileConfigurationResponse response);

    List<Tile> convert(List<TileManagerClient.TileResponse> responses);
    Tile convert(TileManagerClient.TileResponse response);
}
