package com.aryzko.scrabblegame.domain.repository;

import com.aryzko.scrabblegame.domain.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    Game create(Game game);
    Optional<Game> get(String id);
    Game save(Game game);
    Page<Game> findAllGamesWithSorting(Pageable pageable);
}
