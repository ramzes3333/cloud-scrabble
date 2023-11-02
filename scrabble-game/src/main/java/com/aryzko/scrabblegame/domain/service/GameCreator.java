package com.aryzko.scrabblegame.domain.service;

import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.HumanPlayer;
import com.aryzko.scrabblegame.domain.model.Level;
import com.aryzko.scrabblegame.domain.model.Player;
import com.aryzko.scrabblegame.domain.model.State;
import com.aryzko.scrabblegame.domain.repository.GameRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class GameCreator {

    private final GameRepository gameRepository;

    public Game create(CreateGameCommand command) {
        Game game = new Game();
        game.setId(generateUUID());
        game.setCreationDate(OffsetDateTime.now());
        game.setState(State.NOT_STARTED);
        game.setBoardId(UUID.fromString(command.getBoardId()));
        game.getPlayers().addAll(command.getBotPlayers());
        game.getPlayers().addAll(command.getHumanPlayers());
        game.setActualPlayerId(randomPlayer(game.getPlayers()));

        setPlayersOrder(game);

        return gameRepository.create(game);
    }

    private static UUID generateUUID() {
        return UUID.randomUUID();
    }

    private static void setPlayersOrder(Game game) {
        for(int i = 0; i < game.getPlayers().size(); i++) {
            game.getPlayers().get(i).setOrder(i);
        }
    }

    private static String randomPlayer(List<Player> players) {
        return players.get((new Random()).nextInt(players.size())).getId();
    }

    @Value
    @Builder
    public static class CreateGameCommand {
        String boardId;
        @Singular
        List<BotPlayer> botPlayers;
        @Singular
        List<HumanPlayer> humanPlayers;
    }
}
