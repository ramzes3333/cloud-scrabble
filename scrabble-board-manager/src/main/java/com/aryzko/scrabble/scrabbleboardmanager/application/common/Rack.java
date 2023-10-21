package com.aryzko.scrabble.scrabbleboardmanager.application.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class Rack {
    @NotBlank
    private String playerId;
    @Valid
    private List<Letter> letters;
}
