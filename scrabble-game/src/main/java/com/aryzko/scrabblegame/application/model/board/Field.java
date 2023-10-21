package com.aryzko.scrabblegame.application.model.board;

import lombok.Data;

@Data
public class Field {
    private Integer x;
    private Integer y;
    private Bonus bonus;
    private Letter letter;
}