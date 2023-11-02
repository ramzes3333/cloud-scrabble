package com.aryzko.scrabblegame.application.service;

import com.aryzko.scrabblegame.application.common.AuthenticationFacade;
import com.aryzko.scrabblegame.application.model.Player;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.HumanPlayer;
import com.aryzko.scrabblegame.domain.service.GameCreator;
import com.aryzko.scrabblegame.domain.service.PlayerFactory;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GameService {

    private final BoardProvider boardProvider;
    private final GameCreator gameCreator;
    private final PlayerFactory playerFactory;
    private final AuthenticationFacade authenticationFacade;

    public Either<CreateGameSuccess, GameFailure> create(CreateGameCommand request) {
        List<BotPlayer> botPlayers = playerFactory.prepareBotPlayers(convertBotPlayers(request.getBotPlayers()));
        List<HumanPlayer> humanPlayers = playerFactory.prepareHumanPlayers(convertHumanPlayers(request.getHumanPlayers()));

        List<String> playerIds = Stream.concat(botPlayers.stream(), humanPlayers.stream()).map(com.aryzko.scrabblegame.domain.model.Player::getId).toList();

        String boardId = boardProvider.createBoard(playerIds);
        Game game = gameCreator.create(GameCreator.CreateGameCommand.builder()
                .boardId(boardId)
                .botPlayers(botPlayers)
                .humanPlayers(humanPlayers)
                .build());

        return Either.left(CreateGameSuccess.builder()
                .id(game.getId().toString())
                .boardId(boardId)
                .players(game.getPlayers().stream().map(Player::of).collect(Collectors.toList()))
                .build());
    }

    private List<PlayerFactory.BotPlayerCommand> convertBotPlayers(List<CreateGameCommand.BotPlayer> botPlayers) {
        return botPlayers.stream()
                .map(p -> PlayerFactory.BotPlayerCommand.builder()
                        .level(PlayerFactory.BotPlayerCommand.Level.valueOf(p.level.name()))
                        .build())
                .toList();
    }

    private List<PlayerFactory.HumanPlayerCommand> convertHumanPlayers(List<CreateGameCommand.HumanPlayer> humanPlayers) {
        return humanPlayers.stream()
                .map(p -> PlayerFactory.HumanPlayerCommand.builder()
                        .login(authenticationFacade.getName())
                        .build())
                .toList();
    }

    @Value
    @Builder
    public static class CreateGameSuccess {
        String id;
        String boardId;
        List<Player> players;
    }

    @Value
    @Builder
    public static class CreateGameCommand {

        @Valid List<BotPlayer> botPlayers;
        @Valid List<HumanPlayer> humanPlayers;

        @Builder
        public record BotPlayer(@NotNull Level level) {}

        @Builder
        public record HumanPlayer() {}

        public enum Level {
            NEWBIE,
            BEGINNER,
            ADVANCED,
            EXPERT,
            LEGEND
        }
    }
}
