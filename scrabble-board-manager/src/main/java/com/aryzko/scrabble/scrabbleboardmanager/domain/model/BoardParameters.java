package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BoardParameters {
    private Integer horizontalSize;
    private Integer verticalSize;
    private Integer rackSize;
}
