package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Field {
    private Integer x;
    private Integer y;
    private Bonus bonus;
    private Letter letter;
}
