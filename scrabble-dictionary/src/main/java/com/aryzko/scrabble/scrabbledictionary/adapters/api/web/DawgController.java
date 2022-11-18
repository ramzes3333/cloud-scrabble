package com.aryzko.scrabble.scrabbledictionary.adapters.api.web;

import com.aryzko.scrabble.scrabbledictionary.adapters.api.web.error.RestErrorResponse;
import com.aryzko.scrabble.scrabbledictionary.domain.aspect.PerformanceLog;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.service.DawgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping(value = "/api/dawg")
@RequiredArgsConstructor
public class DawgController {

    public static final String DAWG_IS_NOT_READY = "E0000";
    private final DawgService dawgService;

    @PerformanceLog
    @GetMapping("lookup/{entry}")
    public Boolean lookupEntry(@PathVariable @NotBlank String entry) throws DawgIsNotReady {
        return dawgService.lookup(entry);
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
