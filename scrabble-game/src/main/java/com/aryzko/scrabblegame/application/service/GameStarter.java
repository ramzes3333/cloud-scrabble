package com.aryzko.scrabblegame.application.service;

import com.aryzko.scrabblegame.application.common.AuthenticationFacade;
import com.aryzko.scrabblegame.application.request.GameStartRequest;
import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Level;
import com.aryzko.scrabblegame.domain.model.Player;
import com.aryzko.scrabblegame.domain.provider.BoardProvider;
import com.aryzko.scrabblegame.domain.service.GameCreator;
import io.vavr.control.Either;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameStarter {

    private final BoardProvider boardProvider;
    private final GameCreator gameCreator;
    private final AuthenticationFacade authenticationFacade;

    public Either<GameStartSuccess, GameStartFailure> start(GameStartRequest request) {
        String boardId = boardProvider.createBoard();
        Game game = gameCreator.create(GameCreator.CreateGameCommand.builder()
                .boardId(boardId)
                .botPlayers(prepareBotPlayers(request.getBotPlayers()))
                .humanPlayers(preparePlayerForCurrentUser(request.getHumanPlayers()))
                .build());

        return Either.left(GameStartSuccess.builder()
                .id(game.getId())
                .boardId(boardId)
                .build());
    }

    private List<GameCreator.CreateGameCommand.HumanPlayer> preparePlayerForCurrentUser(
            List<GameStartRequest.HumanPlayer> humanPlayers) {

        return humanPlayers.stream()
                .map(botPlayer -> GameCreator.CreateGameCommand.HumanPlayer.builder()
                        .login(authenticationFacade.getName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<GameCreator.CreateGameCommand.BotPlayer> prepareBotPlayers(
            List<GameStartRequest.BotPlayer> botPlayers) {

        return botPlayers.stream()
                .map(botPlayer -> GameCreator.CreateGameCommand.BotPlayer.builder()
                        .level(GameCreator.CreateGameCommand.Level.valueOf(botPlayer.level().toString()))
                        .build())
                .collect(Collectors.toList());
    }

    @Value
    @Builder
    public static class GameStartSuccess {
        private Long id;
        private String boardId;
    }

    @Value
    @Builder
    public static class GameStartFailure {
        private List<Error> errors;

        public record Error(String code, String message) { }
    }
}
