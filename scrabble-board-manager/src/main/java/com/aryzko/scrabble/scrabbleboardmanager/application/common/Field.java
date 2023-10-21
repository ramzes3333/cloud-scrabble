package com.aryzko.scrabble.scrabbleboardmanager.application.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Field {
    @NotNull
    private Integer x;
    @NotNull
    private Integer y;
    private Bonus bonus;
    @Valid
    private Letter letter;
}