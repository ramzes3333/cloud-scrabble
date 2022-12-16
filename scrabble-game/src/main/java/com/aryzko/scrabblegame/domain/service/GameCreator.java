package com.aryzko.scrabblegame.domain.service;

import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Level;
import com.aryzko.scrabblegame.domain.model.Player;
import com.aryzko.scrabblegame.domain.model.State;
import com.aryzko.scrabblegame.domain.model.Type;
import com.aryzko.scrabblegame.domain.repository.GameRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GameCreator {

    private final GameRepository gameRepository;

    public Game create(CreateGameCommand command) {
        Game game = new Game();
        game.setCreationDate(OffsetDateTime.now());
        game.setState(State.NOT_STARTED);
        game.setBoardId(UUID.fromString(command.getBoardId()));
        game.setPlayers(prepareBotPlayers(command.getBotPlayers()));

        return gameRepository.create(game);
    }

    private List<Player> prepareBotPlayers(List<CreateGameCommand.BotPlayer> botPlayers) {
        return IntStream
                .range(0, botPlayers.size())
                .mapToObj(i -> {
                    CreateGameCommand.BotPlayer botPlayer = botPlayers.get(i);
                    return BotPlayer.builder()
                            .id(i)
                            .points(0)
                            .type(Type.BOT)
                            .level(Level.valueOf(botPlayer.level.name()))
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

        @Value
        @Builder
        public static class BotPlayer {
            private Level level;
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
