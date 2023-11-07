package com.aryzko.scrabblegame.domain.repository;

import com.aryzko.scrabblegame.domain.model.Game;
import io.vavr.control.Either;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    Game create(Game game);
    Optional<Game> get(String id);
    Game save(Game game);
    List<Game> getAll();
}
