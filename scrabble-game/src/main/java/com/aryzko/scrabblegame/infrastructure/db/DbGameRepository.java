package com.aryzko.scrabblegame.infrastructure.db;

import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.repository.GameRepository;
import com.aryzko.scrabblegame.infrastructure.db.model.GameEntryDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbGameRepository implements GameRepository {
    private final SpringDataGameRepository gameRepository;

    @Override
    public Game create(Game game) {

        GameEntryDb gameEntryDb = new GameEntryDb();
        gameEntryDb.setId(game.getId());
        gameEntryDb.setVersion(1);
        gameEntryDb.setData(GameConverter.toData(game));
        return convert(gameRepository.save(gameEntryDb));
    }

    private Game convert(GameEntryDb gameEntryDb) {
        Game game = GameConverter.fromData(gameEntryDb.getData());
        game.setId(gameEntryDb.getId());
        return game;
    }
}
