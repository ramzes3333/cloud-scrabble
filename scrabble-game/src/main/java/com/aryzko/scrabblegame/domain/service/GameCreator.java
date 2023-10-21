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
        game.setCreationDate(OffsetDateTime.now());
        game.setState(State.NOT_STARTED);
        game.setBoardId(UUID.fromString(command.getBoardId()));
        game.getPlayers().addAll(prepareBotPlayers(command.getBotPlayers()));
        game.getPlayers().addAll(prepareHumanPlayers(command.getHumanPlayers()));
        game.setActualPlayerId(randomPlayer(game.getPlayers()));

        setPlayersOrder(game);

        return gameRepository.create(game);
    }

    private static void setPlayersOrder(Game game) {
        for(int i = 0; i < game.getPlayers().size(); i++) {
            game.getPlayers().get(i).setOrder(i);
        }
    }

    private static String randomPlayer(List<Player> players) {
        return players.get((new Random()).nextInt(players.size())).getId();
    }

    private List<Player> prepareHumanPlayers(List<CreateGameCommand.HumanPlayer> humanPlayers) {
        return IntStream
                .range(0, humanPlayers.size())
                .mapToObj(i -> {
                    CreateGameCommand.HumanPlayer humanPlayer = humanPlayers.get(i);
                    return HumanPlayer.builder()
                            .id(format("H%d", i))
                            .points(0)
                            .login(humanPlayer.getLogin())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<Player> prepareBotPlayers(List<CreateGameCommand.BotPlayer> botPlayers) {
        return IntStream
                .range(0, botPlayers.size())
                .mapToObj(i -> {
                    CreateGameCommand.BotPlayer botPlayer = botPlayers.get(i);
                    return BotPlayer.builder()
                            .id(format("B%d", i))
                            .points(0)
                            .level(Level.valueOf(botPlayer.getLevel().name()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Value
    @Builder
    public static class CreateGameCommand {
        private String boardId;
        @Singular
        private List<BotPlayer> botPlayers;
        @Singular
        private List<HumanPlayer> humanPlayers;

        @Value
        @Builder
        public static class BotPlayer {
            private Level level;
        }

        @Value
        @Builder
        public static class HumanPlayer {
            private String login;
        }

        public enum Level {
            NEWBIE,
            BEGINNER,
            ADVANCED,
            EXPERT,
            LEGEND;
        }
    }
}
