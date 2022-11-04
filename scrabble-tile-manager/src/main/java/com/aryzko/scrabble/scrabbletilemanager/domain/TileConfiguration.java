package com.aryzko.scrabble.scrabbletilemanager.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TileConfiguration {
    private Tile tile;
    private Integer number;
}
