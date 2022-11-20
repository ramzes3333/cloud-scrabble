package com.aryzko.scrabble.scrabbleboardmanager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PreparedLines {
    private final List<PreparedLine> lines;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PreparedLines(@JsonProperty("lines") List<PreparedLine> lines) {
        this.lines = lines;
    }
}
