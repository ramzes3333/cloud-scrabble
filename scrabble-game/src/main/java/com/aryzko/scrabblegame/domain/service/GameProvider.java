package com.aryzko.scrabblegame.domain.service;

import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameProvider {
    private final GameRepository gameRepository;

    public Game getGame(String id) {
        return gameRepository.get(id).orElse(null);
    }

    public List<Game> getAllGames() {
        return gameRepository.getAll();
    }
}
