package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common;

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
