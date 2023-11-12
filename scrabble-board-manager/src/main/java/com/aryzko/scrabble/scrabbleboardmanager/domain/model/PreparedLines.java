package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PreparedLines {
    List<PreparedLine> lines;

    public PreparedLines(List<PreparedLine> lines) {
        this.lines = lines;
    }
}
