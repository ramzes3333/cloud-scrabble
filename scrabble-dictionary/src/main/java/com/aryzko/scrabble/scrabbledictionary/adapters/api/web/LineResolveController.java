package com.aryzko.scrabble.scrabbledictionary.adapters.api.web;

import com.aryzko.scrabble.scrabbledictionary.adapters.api.mapper.LineResolveMapper;
import com.aryzko.scrabble.scrabbledictionary.adapters.api.request.ResolveRequest;
import com.aryzko.scrabble.scrabbledictionary.adapters.api.web.error.RestErrorResponse;
import com.aryzko.scrabble.scrabbledictionary.domain.aspect.PerformanceLog;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.AvailableLetters;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import com.aryzko.scrabble.scrabbledictionary.domain.service.LineResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping(value = "/api/line")
@RequiredArgsConstructor
public class LineResolveController {

    private static final String DAWG_IS_NOT_READY = "E0000";

    private final LineResolver boardLineResolver;
    private final LineResolveMapper lineResolveMapper;

    @PerformanceLog
    @PostMapping("resolve")
    public Solution resolve(@RequestBody ResolveRequest resolveRequest) throws DawgIsNotReady {
        return lineResolveMapper.convert(
                boardLineResolver.resolve(
                        lineResolveMapper.convert(resolveRequest.getLine()),
                        AvailableLetters.builder().characters(resolveRequest.getAvailableLetters()).build()));
    }

    @ExceptionHandler(DawgIsNotReady.class)
    public ResponseEntity<RestErrorResponse> handleException() {
        log.error("Error during processing request: dawg is not ready");

        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(APPLICATION_JSON)
                .body(RestErrorResponse.builder()
                        .error(RestErrorResponse.RestError.of(DAWG_IS_NOT_READY, "Dawg is not ready"))
                        .build());
    }
}
