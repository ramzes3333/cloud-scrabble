package com.aryzko.scrabblegame.domain.repository;

import com.aryzko.scrabblegame.domain.model.Game;

import java.util.Optional;

public interface GameRepository {
    Game create(Game game);
    Optional<Game> get(Long id);
}
