package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.TileConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TileMapper {

    TileConfiguration.Tile convert(TileManagerClient.TileConfigurationResponse.Tile tile);

    TileConfiguration convert(TileManagerClient.TileConfigurationResponse response);
}
