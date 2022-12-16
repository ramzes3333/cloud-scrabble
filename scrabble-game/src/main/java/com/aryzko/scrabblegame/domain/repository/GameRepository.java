package com.aryzko.scrabblegame.domain.repository;

import com.aryzko.scrabblegame.domain.model.Game;

public interface GameRepository {
    Game create(Game game);
}
