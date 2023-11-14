package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tile {

    private int x;
    private int y;
    private char letter;
    private boolean blank;
}
