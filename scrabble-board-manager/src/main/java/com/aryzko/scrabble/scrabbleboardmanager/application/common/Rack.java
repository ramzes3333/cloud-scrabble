package com.aryzko.scrabble.scrabbleboardmanager.application.common;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class Rack {
    @NotBlank
    private String playerId;
    @Valid
    private List<Letter> letters;
}
