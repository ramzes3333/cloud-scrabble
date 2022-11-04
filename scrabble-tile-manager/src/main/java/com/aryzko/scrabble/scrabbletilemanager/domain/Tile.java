package com.aryzko.scrabble.scrabbletilemanager.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class Tile {
    private Character letter;
    private Integer points;
}
