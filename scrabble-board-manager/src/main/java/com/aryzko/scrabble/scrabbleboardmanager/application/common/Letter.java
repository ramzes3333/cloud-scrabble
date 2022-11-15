package com.aryzko.scrabble.scrabbleboardmanager.application.common;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Letter {
    @NotBlank
    private Character letter;
    @NotNull
    private Integer points;
    private boolean blank;
}
