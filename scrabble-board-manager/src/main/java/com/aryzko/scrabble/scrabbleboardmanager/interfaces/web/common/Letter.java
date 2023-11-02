package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Letter {
    @NotBlank
    private Character letter;
    @NotNull
    private Integer points;
    private boolean blank;
}
