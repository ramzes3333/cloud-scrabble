package com.aryzko.scrabblegame.infrastructure.db;

import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.repository.GameRepository;
import com.aryzko.scrabblegame.infrastructure.db.model.GameEntryDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class DbGameRepository implements GameRepository {
    private final SpringDataGameRepository gameRepository;

    @Override
    public Game create(Game game) {

        GameEntryDb gameEntryDb = new GameEntryDb();
        gameEntryDb.setData(GameConverter.toData(game));
        return convert(gameRepository.save(gameEntryDb));
    }

    @Override
    public Optional<Game> get(String id) {
        return convert(gameRepository.getGameById(id));
    }

    public Game save(Game game) {
        Optional<GameEntryDb> gameEntryDbOpt = gameRepository.getGameById(game.getId().toString());
        if(gameEntryDbOpt.isEmpty()) {
            throw new IllegalStateException(format("No game with id: %s", game.getId().toString()));
        } else {
            GameEntryDb gameEntryDb = gameEntryDbOpt.get();
            gameEntryDb.setData(GameConverter.toData(game));
            return convert(gameRepository.save(gameEntryDb));
        }
    }

    public List<Game> getAll() {
        return gameRepository.findAll().stream()
                .map(this::convert)
                .toList();
    }

    private Optional<Game> convert(Optional<GameEntryDb> gameEntryDb) {
        return gameEntryDb
                .map(this::convert);
    }

    private Game convert(GameEntryDb gameEntryDb) {
        return GameConverter.fromData(gameEntryDb.getData());
    }
}
