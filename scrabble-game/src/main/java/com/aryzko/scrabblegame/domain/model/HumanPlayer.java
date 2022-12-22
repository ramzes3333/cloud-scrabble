package com.aryzko.scrabblegame.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class HumanPlayer extends Player {
    private String login;

    @Override
    public Type getType() {
        return Type.HUMAN;
    }
}
