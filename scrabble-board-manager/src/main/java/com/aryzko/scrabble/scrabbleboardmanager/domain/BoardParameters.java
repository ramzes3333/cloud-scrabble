package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BoardParameters {
    private Integer horizontalSize;
    private Integer verticalSize;
}
