package com.aryzko.scrabblegame.interfaces.web;

import com.aryzko.scrabblegame.application.request.GameMoveRequest;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.application.service.GameService;
import com.aryzko.scrabblegame.application.service.moveperformer.MovePerformer;
import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.HumanPlayer;
import com.aryzko.scrabblegame.domain.model.Player;
import com.aryzko.scrabblegame.domain.service.GameProvider;
import com.aryzko.scrabblegame.interfaces.web.error.RestErrorResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping(value = "/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameProvider gameProvider;
    private final MovePerformer movePerformer;

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateGameRequest request) {
        return gameService.create(request.toCommand()).fold(
                //TODO fix response
                success -> ResponseEntity.ok(GameResponse.builder()
                        .id(success.getId())
                        .boardId(success.getBoardId()).build()),
                failure -> handleFailure(failure));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") String gameId) {
        Game game = gameProvider.getGame(gameId);
        return ResponseEntity.ok(GameResponse.of(game));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Game> games = gameProvider.getAllGames();
        return ResponseEntity.ok(games.stream().map(GameResponse::of).toList());
    }

    @PostMapping("move")
    public ResponseEntity<?> move(@RequestBody @Valid GameMoveRequest gameMoveRequest) {
        return movePerformer.move(gameMoveRequest).fold(
                success -> ResponseEntity.ok(success),
                failure -> handleFailure(failure));
    }

    private ResponseEntity<RestErrorResponse> handleFailure(GameFailure failure) {
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(APPLICATION_JSON)
                .body(RestErrorResponse.builder()
                        .errors(failure.getErrors().stream()
                                .map(error -> RestErrorResponse.RestError.of(error.code(), error.message()))
                                .collect(Collectors.toList()))
                        .build());
    }

    @Value
    public static class CreateGameRequest {

        @Valid List<BotPlayer> botPlayers;
        @Valid List<HumanPlayer> humanPlayers;

        public GameService.CreateGameCommand toCommand() {
            return GameService.CreateGameCommand.builder()
                    .botPlayers(botPlayers.stream()
                            .map(p -> GameService.CreateGameCommand.BotPlayer.builder()
                                    .level(GameService.CreateGameCommand.Level.valueOf(p.level().toString()))
                                    .build()).toList())
                    .humanPlayers(humanPlayers.stream()
                            .map(p -> GameService.CreateGameCommand.HumanPlayer.builder().build())
                            .toList())
                    .build();
        }

        public record BotPlayer(@NotNull Level level) {}

        public record HumanPlayer() {}

        public enum Level {
            NEWBIE,
            BEGINNER,
            ADVANCED,
            EXPERT,
            LEGEND
        }
    }

    @Value
    @Builder
    public static class GameResponse {
        String id;
        String boardId;
        OffsetDateTime creationDate;
        String actualPlayerId;
        List<Player> players;

        public static GameResponse of(Game game) {
            return GameResponse.builder()
                    .id(game.getId().toString())
                    .boardId(game.getBoardId().toString())
                    .creationDate(game.getCreationDate())
                    .actualPlayerId(game.getActualPlayerId())
                    .players(game.getPlayers().stream()
                            .map(p -> Player.builder()
                                    .id(p.getId())
                                    .order(p.getOrder())
                                    .type(Type.valueOf(p.getType().toString()))
                                    .parameters(getParameters(p))
                                    .build())
                            .toList())
                    .build();
        }

        private static Map<String, String> getParameters(com.aryzko.scrabblegame.domain.model.Player player) {
            Map<String, String> parameters = new HashMap<>();

            switch (player.getType()) {
                case HUMAN -> {
                    parameters.put("login", ((HumanPlayer) player).getLogin());
                }
                case BOT -> {
                    parameters.put("level", ((BotPlayer) player).getLevel().toString());
                }
            }
            return parameters;
        }

        @Value
        @Builder
        static class Player {
            String id;
            Type type;
            Integer order;
            Map<String, String> parameters;
        }

        public enum Type {
            HUMAN,
            BOT
        }
    }
}
