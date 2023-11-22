package com.aryzko.scrabblegame.domain.model;

import lombok.Getter;

@Getter
public enum Level {
    NEWBIE(1),
    BEGINNER(2),
    ADVANCED(3),
    EXPERT(4),
    LEGEND(5);

    private Integer level;
    Level(Integer level) {
        this.level = level;
    }
}
