package com.aryzko.scrabblegame.interfaces.web;

import com.aryzko.scrabblegame.application.request.GameStartRequest;
import com.aryzko.scrabblegame.application.response.GameStartResponse;
import com.aryzko.scrabblegame.application.service.GameStarter;
import com.aryzko.scrabblegame.interfaces.web.error.RestErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping(value = "/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameStarter gameStarter;
    @PostMapping("start")
    public ResponseEntity<?> start(@RequestBody GameStartRequest gameStartRequest) {
        return gameStarter.start(gameStartRequest).fold(
                success -> handleSuccess(success),
                failure -> handleFailure(failure));
    }

    private ResponseEntity<RestErrorResponse> handleFailure(GameStarter.GameStartFailure failure) {
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(APPLICATION_JSON)
                .body(RestErrorResponse.builder()
                        .errors(failure.getErrors().stream()
                                .map(error -> RestErrorResponse.RestError.of(error.code(), error.message()))
                                .collect(Collectors.toList()))
                        .build());
    }

    private ResponseEntity<GameStartResponse> handleSuccess(GameStarter.GameStartSuccess success) {
        return ResponseEntity.ok(GameStartResponse.builder()
                .id(success.getId())
                .boardId(success.getBoardId()).build());
    }
}
