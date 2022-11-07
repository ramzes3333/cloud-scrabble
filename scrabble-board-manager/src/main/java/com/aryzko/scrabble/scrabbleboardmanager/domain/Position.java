package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode
@ToString
@SuperBuilder
public class Position {
    private Integer x;
    private Integer y;
}
