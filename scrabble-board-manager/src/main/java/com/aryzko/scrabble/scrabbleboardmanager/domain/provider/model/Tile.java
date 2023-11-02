package com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model;

import lombok.Data;

@Data
public class Tile {
    private Character letter;
    private Integer points;
}
