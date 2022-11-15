package com.aryzko.scrabble.scrabbleboardmanager.application.common;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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