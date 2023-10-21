package com.aryzko.scrabblegame.application.model.board;

import lombok.Data;

import java.util.List;

@Data
public class Rack {

    private String playerId;
    private List<Letter> letters;
}
