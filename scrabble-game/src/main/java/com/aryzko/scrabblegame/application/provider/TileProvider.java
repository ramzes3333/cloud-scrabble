package com.aryzko.scrabblegame.application.provider;

import com.aryzko.scrabblegame.application.provider.model.Tile;

import java.util.List;

public interface TileProvider {

    List<Tile> getTiles(String uuid, Integer numberOfItems);
}
