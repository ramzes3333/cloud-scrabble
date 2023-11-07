package com.aryzko.scrabblegame.domain.service;

import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameUpdater {
    private final GameRepository gameRepository;

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
}
