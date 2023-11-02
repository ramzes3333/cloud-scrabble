package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
